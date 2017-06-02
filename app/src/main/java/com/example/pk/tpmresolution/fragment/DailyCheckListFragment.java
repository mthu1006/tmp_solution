package com.example.pk.tpmresolution.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.LoginActivity;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.adapter.CheckListAdapter;
import com.example.pk.tpmresolution.model.CheckListItem;
import com.example.pk.tpmresolution.model.CommonClass;
import com.example.pk.tpmresolution.model.ProductItem;
import com.example.pk.tpmresolution.model.RequestMaintenace;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.id.message;
import static com.example.pk.tpmresolution.R.id.layout_daily;
import static com.example.pk.tpmresolution.R.id.layout_monthly;
import static com.example.pk.tpmresolution.R.id.layout_weekly;
import static com.example.pk.tpmresolution.R.id.view;

public class DailyCheckListFragment extends Fragment implements View.OnClickListener{
    private ExpandableRelativeLayout expDaily, expWeekly, expMonthly;
    private LinearLayout mLayoutDaily, mLayoutWeekly, mLayoutMonthly;
    private AppCompatImageView imgDaily, imgWeekly, imgMonthly;
    private RecyclerView recyclerDaily, recyclerWeekly, recyclerMonthly;
    private CustomFontTextView tv_mon, tv_tue, tv_wed, tv_thu, tv_fri, tv_sat, tv_sun;
    private int position = 0;
    private CustomFontButton btnCancel, btnSave, mBtn_dialog;
    MaterialDialog mDialogLoading;
    int newHeight = 0;
    ProductItem mItem = null;
    private RequestMaintenace mMaitainItem = null;
    ArrayList<CheckListItem> listDaily, listWeekly, listMonthly, listChanged;
    ArrayList<CommonClass> status_list;
    CustomFontTextView[] list_tv;
    AppCompatCheckBox cbDaily, cbWeekly, cbMonthly;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public Date choosedDate;
    int[] maxDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private boolean isFirstTime = true;
    Dialog mDialogError;

    public DailyCheckListFragment() {
    }


    public static DailyCheckListFragment newInstance() {
        return new DailyCheckListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daily_check_list, container, false);
        ((MainActivity)getActivity()).fab.setVisibility(View.VISIBLE);
        
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPref.edit();
//        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getString(R.string.checklist_daily));
        mItem = ((MainActivity) getActivity()).mItem;

        mMaitainItem = new RequestMaintenace();
        recyclerDaily = (RecyclerView) root.findViewById(R.id.recycler_daily);
        recyclerWeekly = (RecyclerView) root.findViewById(R.id.recycler_weekly);
        recyclerMonthly = (RecyclerView) root.findViewById(R.id.recycler_monthly);
        LinearLayoutManager layoutManager1 = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager2 = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager3 = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerDaily.setLayoutManager(layoutManager1);
        recyclerWeekly.setLayoutManager(layoutManager2);
        recyclerMonthly.setLayoutManager(layoutManager3);
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());
        status_list = new ArrayList<>();
        listDaily = new ArrayList<>();
        listWeekly = new ArrayList<>();
        listMonthly = new ArrayList<>();
        listChanged = new ArrayList<>();

        tv_mon = (CustomFontTextView) root.findViewById(R.id.ic_mon);
        tv_tue = (CustomFontTextView) root.findViewById(R.id.ic_tue);
        tv_wed = (CustomFontTextView) root.findViewById(R.id.ic_wed);
        tv_thu = (CustomFontTextView) root.findViewById(R.id.ic_thu);
        tv_fri = (CustomFontTextView) root.findViewById(R.id.ic_fri);
        tv_sat = (CustomFontTextView) root.findViewById(R.id.ic_sat);
        tv_sun = (CustomFontTextView) root.findViewById(R.id.ic_sun);

        cbDaily = (AppCompatCheckBox) root.findViewById(R.id.check_daily);
        cbWeekly = (AppCompatCheckBox) root.findViewById(R.id.check_weekly);
        cbMonthly = (AppCompatCheckBox) root.findViewById(R.id.check_monthly);

        if(mItem == null){
            cbDaily.setClickable(false);
            cbDaily.setBackgroundResource(R.drawable.checkbox_red);
            cbDaily.setTag(false);

            cbWeekly.setClickable(false);
            cbWeekly.setBackgroundResource(R.drawable.checkbox_red);
            cbWeekly.setTag(false);

            cbMonthly.setClickable(false);
            cbMonthly.setBackgroundResource(R.drawable.checkbox_red);
            cbMonthly.setTag(false);
            ShowDialogError("Can not create checklist", "Please scan machine and try again!", false);
            mDialogError.setCancelable(false);
            mDialogError.setCanceledOnTouchOutside(false);
            mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogError.dismiss();
                    Fragment frag = MainFragment.newInstance();
                    AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
                }
            });
        }


        if(mItem == null){
            cbDaily.setClickable(false);
            cbDaily.setBackgroundResource(R.drawable.checkbox_red);
            cbDaily.setTag(false);

            cbWeekly.setClickable(false);
            cbWeekly.setBackgroundResource(R.drawable.checkbox_red);
            cbWeekly.setTag(false);

            cbMonthly.setClickable(false);
            cbMonthly.setBackgroundResource(R.drawable.checkbox_red);
            cbMonthly.setTag(false);
            ShowDialogError("Can not create checklist", "Please scan machine and try again!", false);
            mDialogError.setCancelable(false);
            mDialogError.setCanceledOnTouchOutside(false);
            mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogError.dismiss();
                    MainActivity.mNavAdapter.notifyItemChanged(MainActivity.mNavAdapter.getFocusedItem());
                    MainActivity.mNavAdapter.setFocusedItem(-1);
                    Fragment frag = MainFragment.newInstance();
                    AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
                }
            });
        }

        list_tv = new CustomFontTextView[]{tv_mon, tv_tue, tv_wed, tv_thu, tv_fri, tv_sat, tv_sun};

        tv_mon.setOnClickListener(this);
        tv_tue.setOnClickListener(this);
        tv_wed.setOnClickListener(this);
        tv_thu.setOnClickListener(this);
        tv_fri.setOnClickListener(this);
        tv_sat.setOnClickListener(this);
        tv_sun.setOnClickListener(this);

        expDaily = (ExpandableRelativeLayout) root.findViewById(R.id.exp_daily);
        expWeekly = (ExpandableRelativeLayout) root.findViewById(R.id.exp_weekly);
        expMonthly = (ExpandableRelativeLayout) root.findViewById(R.id.exp_monthly);
        expDaily.collapse();
        expWeekly.collapse();
        expMonthly.collapse();

        mLayoutDaily = (LinearLayout) root.findViewById(layout_daily);
        mLayoutWeekly = (LinearLayout) root.findViewById(layout_weekly);
        mLayoutMonthly = (LinearLayout) root.findViewById(layout_monthly);
        mLayoutDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listDaily.size()>0) {
                    expDaily.toggle();
                    expWeekly.collapse();
                    expMonthly.collapse();
                }
            }
        });

        mLayoutWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listWeekly.size()>0) {
                    expWeekly.toggle();
                    expDaily.collapse();
                    expMonthly.collapse();
                }
            }
        });

        mLayoutMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listMonthly.size()>0) {
                    expMonthly.toggle();
                    expDaily.collapse();
                    expWeekly.collapse();
                }
            }
        });

        btnCancel = (CustomFontButton) root.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mNavAdapter.notifyItemChanged(MainActivity.mNavAdapter.getFocusedItem());
                MainActivity.mNavAdapter.setFocusedItem(-1);
                Fragment frag = MainFragment.newInstance();
                AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
            }
        });

        btnSave = (CustomFontButton) root.findViewById(R.id.btn_save_status_checklist);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatusChecklists();
            }
        });
        getListStatus();
        setupWeek(Calendar.getInstance());
        choosedDate = Calendar.getInstance().getTime();


        return root;
    }

    private void getListStatus(){
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                JSONArray arrStatus = null;
                try {
                    arrStatus = new JSONArray(output);
                    for(int i=0; i< arrStatus.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setName(arrStatus.getJSONObject(i).getString("StatusCheckListName"));
                        c.setId(arrStatus.getJSONObject(i).getString("StatusChecklistId"));
                        status_list.add(c);
                    }
                    if(mItem!=null)
                    getChecklist(Calendar.getInstance().getTime());
                } catch (JSONException e) {
                    Log.d("Kien", "Loi json "+e.toString());
                }

            }
        }, getActivity()).execute(AppConstants.URL_LIST_STATUS_CHECKLIST);
    }

    private  void updateStatusChecklists(){
        if(cbDaily.isChecked() || cbWeekly.isChecked() || cbMonthly.isChecked()) {
            mDialogLoading.show();
            mergeCheckList();
            String token = sharedPref.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, "");
            JSONObject object = new JSONObject();
            try {
                object.put("Token", token);
                JSONArray arr = new JSONArray();
                for (int i = 0; i < listChanged.size(); i++) {
                    Log.d("Kien", "Status id: " + listChanged.get(i).getChecklist_status_id());
                    JSONObject objStatus = new JSONObject();
                    objStatus.put("ChklItemCode", listChanged.get(i).getChecklist_id());
                    objStatus.put("ModelId", mItem.getModel_id());
                    objStatus.put("MachineId", mItem.getMachineID());
                    if(!Validation.checkNullOrEmpty(listChanged.get(i).getChecklist_status_id()))
                        objStatus.put("StatusCode", listChanged.get(i).getChecklist_status_id());
                    else objStatus.put("StatusCode", status_list.get(0).getId());
                    objStatus.put("Date", new SimpleDateFormat("yyyy-MM-dd").format(choosedDate));
                    arr.put(objStatus);
                }
                object.put("UpdateCheckList", arr);

            } catch (JSONException e) {
                mDialogLoading.dismiss();
                Log.d("Kien", "Loi json " + e.toString());
            }

            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    try {
                        JSONObject obj = new JSONObject(output);
                        if (obj.getString("Status").equals("Y")) {
                            getChecklist(choosedDate);
                            mDialogLoading.dismiss();
                            AppTransaction.Toast(getActivity(), obj.getString("Message"));
                        } else {
                            mDialogLoading.dismiss();
                            ShowDialogError("Message from server:",obj.getString("Message"), true);
                        }
                    } catch (JSONException e) {
                        mDialogLoading.dismiss();
                        Log.d("Kien", "Loi json " + e.toString());
                    }
                }
            }, getActivity()).execute(AppConstants.URL_CHANGE_STATUS_CHECKLIST, object.toString());
        }else ShowDialogError("Can not send to server","Please check one of 3 checkboxs above!", true);
    }

    private void getChecklist(Date date) {
        mDialogLoading.show();
        listDaily.clear();
        listWeekly.clear();
        listMonthly.clear();
        cbDaily.setChecked(false);
        cbWeekly.setChecked(false);
        cbMonthly.setChecked(false);
        expDaily.collapse();
        expWeekly.collapse();
        expMonthly.collapse();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String url = AppConstants.URL_GET_CHECKLIST
                .replace(AppConstants.KEY_TOKEN, sharedPref.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""))
                .replace(AppConstants.KEY_MACHINE_ID, ((MainActivity)getActivity()).mItem.getMachineID())
                .replace(AppConstants.KEY_MODEL_ID, ((MainActivity)getActivity()).mItem.getModel_id())
                .replace(AppConstants.KEY_DATE, sdf.format(date));
        new HTTPRequest(new HTTPRequest.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                mDialogLoading.dismiss();
                try {
                    Log.d("Kien", "checklist "+output);
                    JSONObject object = new JSONObject(output);

                    if(object.getString("Status").equals("Y")) {
                        JSONArray arrDaily = object.getJSONArray("ListItemDay");

                        for (int i = 0; i < arrDaily.length(); i++) {
                            JSONObject obj = arrDaily.getJSONObject(i);
                            CheckListItem inf = new CheckListItem();
                            inf.setChecklist_id(obj.getString("ChklItemCode"));
                            inf.setChecklist_name(obj.getString("ChklItemName"));
                            inf.setChecklist_action_name(obj.getString("ActionName"));
                            inf.setChecklist_date(obj.getString("DateCheck"));
                            if(!Validation.checkNullOrEmpty(obj.getString("StatusCode")))
                                inf.setChecklist_status_id(obj.getString("StatusCode"));
                            else inf.setChecklist_status_id(status_list.get(0).getId());

                            if(!Validation.checkNullOrEmpty(obj.getString("StatusName")))
                                inf.setChecklist_status_name(obj.getString("StatusName"));
                            else inf.setChecklist_status_id(status_list.get(0).getId());

                            inf.setChecklist_user_name(obj.getString("UserCheckName"));
                            listDaily.add(inf);
                        }

                        JSONArray arrWeekly = object.getJSONArray("ListItemWeek");
                        for (int i = 0; i < arrWeekly.length(); i++) {
                            JSONObject obj = arrWeekly.getJSONObject(i);
                            CheckListItem inf = new CheckListItem();
                            inf.setChecklist_id(obj.getString("ChklItemCode"));
                            inf.setChecklist_name(obj.getString("ChklItemName"));
                            inf.setChecklist_action_name(obj.getString("ActionName"));
                            inf.setChecklist_date(obj.getString("DateCheck"));
                            if(!Validation.checkNullOrEmpty(obj.getString("StatusCode")))
                                inf.setChecklist_status_id(obj.getString("StatusCode"));
                            else inf.setChecklist_status_id(status_list.get(0).getId());

                            if(!Validation.checkNullOrEmpty(obj.getString("StatusName")))
                                inf.setChecklist_status_name(obj.getString("StatusName"));
                            else inf.setChecklist_status_id(status_list.get(0).getId());
                            inf.setChecklist_user_name(obj.getString("UserCheckName"));
                            listWeekly.add(inf);
                        }

                        JSONArray arrMonthly = object.getJSONArray("ListItemMonth");
                        for (int i = 0; i < arrMonthly.length(); i++) {
                            JSONObject obj = arrMonthly.getJSONObject(i);
                            CheckListItem inf = new CheckListItem();
                            inf.setChecklist_id(obj.getString("ChklItemCode"));
                            inf.setChecklist_name(obj.getString("ChklItemName"));
                            inf.setChecklist_action_name(obj.getString("ActionName"));
                            inf.setChecklist_date(obj.getString("DateCheck"));
                            if(!Validation.checkNullOrEmpty(obj.getString("StatusCode")))
                                inf.setChecklist_status_id(obj.getString("StatusCode"));
                            else inf.setChecklist_status_id(status_list.get(0).getId());

                            if(!Validation.checkNullOrEmpty(obj.getString("StatusName")))
                                inf.setChecklist_status_name(obj.getString("StatusName"));
                            else inf.setChecklist_status_id(status_list.get(0).getId());
                            inf.setChecklist_user_name(obj.getString("UserCheckName"));
                            listMonthly.add(inf);
                        }

                        boolean isNewDaily = object.getString("TypeDay").equals("New");
                        boolean isNewWeekly = object.getString("TypeWeek").equals("New");
                        boolean isNewMonthly = object.getString("TypeMonth").equals("New");

                        if(!isNewDaily || listDaily.isEmpty()){
                            cbDaily.setClickable(false);
                            cbDaily.setBackgroundResource(R.drawable.checkbox_red);
                            cbDaily.setTag(false);
                        }else {
                            cbDaily.setClickable(true);
                            cbDaily.setBackgroundResource(R.drawable.checkbox_selector);
                            cbDaily.setTag(null);
                        }
                        if(!isNewWeekly || listWeekly.isEmpty()){
                            cbWeekly.setClickable(false);
                            cbWeekly.setBackgroundResource(R.drawable.checkbox_red);
                            cbWeekly.setTag(false);
                        }else {
                            cbWeekly.setClickable(true);
                            cbWeekly.setBackgroundResource(R.drawable.checkbox_selector);

                            cbWeekly.setTag(null);
                        }
                        if(!isNewMonthly || listMonthly.isEmpty()){
                            cbMonthly.setClickable(false);
                            cbMonthly.setBackgroundResource(R.drawable.checkbox_red);
                            cbMonthly.setTag(false);
                        }else {
                            cbMonthly.setClickable(true);
                            cbMonthly.setBackgroundResource(R.drawable.checkbox_selector);
                            cbMonthly.setTag(null);
                        }

                        CheckListAdapter adapterDaily = new CheckListAdapter(getActivity(), listDaily, status_list, isNewDaily, 1, DailyCheckListFragment.this, new CheckListAdapter.SpinnerItemClickListener() {
                            @Override
                            public void onItemClick(int position, AppCompatSpinner spn) {
                                updateStatusDaily(position, spn);
                            }
                        });
                        recyclerDaily.setAdapter(adapterDaily);

                        CheckListAdapter adapterWeekly = new CheckListAdapter(getActivity(), listWeekly, status_list, isNewWeekly, 2, DailyCheckListFragment.this, new CheckListAdapter.SpinnerItemClickListener() {
                            @Override
                            public void onItemClick(int position, AppCompatSpinner spn) {
                                updateStatusWeekly(position, spn);
                            }
                        });
                        recyclerWeekly.setAdapter(adapterWeekly);
                        CheckListAdapter adapterMonthly = new CheckListAdapter(getActivity(), listMonthly, status_list, isNewMonthly, 3, DailyCheckListFragment.this, new CheckListAdapter.SpinnerItemClickListener() {
                            @Override
                            public void onItemClick(int position, AppCompatSpinner spn) {
                                updateStatusMonthly(position, spn);
                            }
                        });
                        recyclerMonthly.setAdapter(adapterMonthly);

                    }else{
                        ShowDialogError("Message from server:", object.getString("Message"), true);
                        if (object.getString("Type").equals("Login")) {
                            mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                                    AppTransaction.replaceActivityWithAnimation(getActivity(), LoginActivity.class);
                                }
                            });
                        }else {
                            mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDialogError.dismiss();
                                    AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), MainFragment.newInstance());
                                }
                            });
                        }

                    }

                } catch (JSONException e) {
                    mDialogLoading.dismiss();
                    Log.d("Kien", "loi json inf "+e.toString());
                }
            }
        }, getActivity()).execute(url);
    }

    private void mergeCheckList(){
        listChanged.clear();
        if(cbDaily.isChecked() && cbDaily.getTag()==null)
            for (int i=0; i< listDaily.size(); i++)
                listChanged.add(listDaily.get(i));

        if(cbWeekly.isChecked() && cbWeekly.getTag()==null)
            for (int i=0; i< listWeekly.size(); i++)
                listChanged.add(listWeekly.get(i));

        if(cbMonthly.isChecked() && cbMonthly.getTag()==null)
            for (int i=0; i< listMonthly.size(); i++)
                listChanged.add(listMonthly.get(i));
    }

    public void updateStatusDaily(int position, AppCompatSpinner spn){
        Log.d("Kien","Update status daily name: "+status_list.get(spn.getSelectedItemPosition()).getName()+" code: "+status_list.get(spn.getSelectedItemPosition()).getId());
        listDaily.get(position).setChecklist_status_id(status_list.get(spn.getSelectedItemPosition()).getId());
        listDaily.get(position).setChecklist_status_name(status_list.get(spn.getSelectedItemPosition()).getName());
    }

    public void updateStatusWeekly(int position, AppCompatSpinner spn){
        Log.d("Kien","Update status weekly name: "+status_list.get(spn.getSelectedItemPosition()).getName()+" code: "+status_list.get(spn.getSelectedItemPosition()).getId());
        listWeekly.get(position).setChecklist_status_id(status_list.get(spn.getSelectedItemPosition()).getId());
        listWeekly.get(position).setChecklist_status_name(status_list.get(spn.getSelectedItemPosition()).getName());
    }

    public void updateStatusMonthly(int position, AppCompatSpinner spn){
        Log.d("Kien","Update status monthly name: "+status_list.get(spn.getSelectedItemPosition()).getName()+" code: "+status_list.get(spn.getSelectedItemPosition()).getId());
        listMonthly.get(position).setChecklist_status_id(status_list.get(spn.getSelectedItemPosition()).getId());
        listMonthly.get(position).setChecklist_status_name(status_list.get(spn.getSelectedItemPosition()).getName());
    }


   /* private void checkRestoreStatus(){
        // to make sure that all status not restore
        for(CheckListItem item: listDaily)
            for(int i=0;i<listChanged.size(); i++){
                if(listChanged.get(i).getChecklist_id().equals(item.getChecklist_id()) && listChanged.get(i).getChecklist_status_name().equals(item.getChecklist_status_name()))
                    listChanged.remove(i);
            }
        for(CheckListItem item: listWeekly)
            for(int i=0;i<listChanged.size(); i++){
                if(listChanged.get(i).getChecklist_id().equals(item.getChecklist_id()) && listChanged.get(i).getChecklist_status_name().equals(item.getChecklist_status_name()))
                    listChanged.remove(i);
            }

        for(CheckListItem item: listMonthly)
            for(int i=0;i<listChanged.size(); i++){
                if(listChanged.get(i).getChecklist_id().equals(item.getChecklist_id()) && listChanged.get(i).getChecklist_status_name().equals(item.getChecklist_status_name()))
                    listChanged.remove(i);
            }
    }*/

    private int CheckExist(String id){
        for (int i=0; i<listChanged.size(); i++){
            if(listChanged.get(i).getChecklist_id().equals(id)){
                return i;
            }
        }
        return -1;
    }


    public void setupWeek(Calendar calendar){

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        if(day==1) day+=7;
        int d = date - (day-2);
        if(d<1) {
            if(month>1)
                month--;
            int tmp = date - (day-2);
            d = maxDay[month] + tmp;

        }
        // 0Log.d("kien", "day after calculate  " + String.valueOf(d) +" maxDay[month] "+ maxDay[month] +" month "+month+" day "+day);
        for(int i = 0; i< list_tv.length; i++){
            list_tv[i].setText( String.valueOf(d));
            list_tv[i].setTag(new Date(calendar.getTime().getYear(), month, d));
            if(d == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                if(isFirstTime) {
                    isFirstTime = false;
                    list_tv[i].setBackgroundResource(R.drawable.ring_fill_blue);
                }else list_tv[i].setBackgroundResource(R.drawable.ring_fill);
                list_tv[i].setTextColor(getActivity().getResources().getColor(R.color.white));
            }
            if(d<maxDay[month])
                d++;
            else{
                d=1;
                month++;
            }
        }
    }

    private void resetWeek(){
        for(int i = 0; i< list_tv.length; i++){
            list_tv[i].setBackgroundResource(R.drawable.ring);
            list_tv[i].setTextColor(getActivity().getResources().getColor(R.color.colorTab_blue));

        }
    }

    void ShowDialogError(String title, String message, boolean isClose) {
        mDialogError = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_error);
        CustomFontTextView txt1 = (CustomFontTextView) mDialogError.findViewById(R.id.txt_content1);
        CustomFontTextView txt2 = (CustomFontTextView) mDialogError.findViewById(R.id.txt_content2);
        AppCompatImageView img_close = (AppCompatImageView) mDialogError.findViewById(R.id.button_close);
        if(!isClose) img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBtn_dialog = (CustomFontButton) mDialogError.findViewById(R.id.btn_accept);
        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogError.dismiss();
            }
        });
        txt1.setText(title);
        txt2.setText(message);
        mDialogError.show();
    }

    @Override
    public void onClick(View v) {
        TextView tv = (TextView)v;
        Date currentDate = Calendar.getInstance().getTime();
        if(((Date) tv.getTag()).compareTo(currentDate)<=0) {
            resetWeek();
            setupWeek(CheckListFragment.calendar);
            v.setBackgroundResource(R.drawable.ring_fill_blue);
            tv.setTextColor(getActivity().getResources().getColor(R.color.white));
            CheckListFragment.setDate((Date) tv.getTag());
            if(mItem!=null)
            getChecklist((Date) tv.getTag());
            choosedDate = (Date) tv.getTag();
        }else {
            AppTransaction.Toast(getActivity(), "Can not choose future date!");
        }
    }

    public void SetSelectedDate(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        resetWeek();
        setupWeek(calendar);

        switch(day){
            case Calendar.MONDAY:
                tv_mon.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_mon.setTextColor(getActivity().getResources().getColor(R.color.white));
                break;
            case Calendar.TUESDAY:
                tv_tue.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_tue.setTextColor(getActivity().getResources().getColor(R.color.white));
                position = 1;
                break;


            case Calendar.WEDNESDAY:
                tv_wed.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_wed.setTextColor(getActivity().getResources().getColor(R.color.white));

                break;
            case Calendar.THURSDAY:
                tv_thu.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_thu.setTextColor(getActivity().getResources().getColor(R.color.white));

                break;
            case Calendar.FRIDAY:
                tv_fri.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_fri.setTextColor(getActivity().getResources().getColor(R.color.white));

                break;
            case Calendar.SATURDAY:
                tv_sat.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_sat.setTextColor(getActivity().getResources().getColor(R.color.white));

                break;
            case Calendar.SUNDAY:
                tv_sun.setBackgroundResource(R.drawable.ring_fill_blue);
                tv_sun.setTextColor(getActivity().getResources().getColor(R.color.white));
                break;
        }

    }

}
