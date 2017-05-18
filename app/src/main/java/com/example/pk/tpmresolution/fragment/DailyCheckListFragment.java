package com.example.pk.tpmresolution.fragment;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
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
import com.example.pk.tpmresolution.model.InformationItem;
import com.example.pk.tpmresolution.model.ProductItem;
import com.example.pk.tpmresolution.model.RequestMaintenace;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.AppUltils;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.android.views.llm.DividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.pk.tpmresolution.R.color.color_1;
import static com.example.pk.tpmresolution.R.color.color_5;
import static com.example.pk.tpmresolution.R.color.color_checkbox_login;
import static com.example.pk.tpmresolution.R.id.layout_daily;
import static com.example.pk.tpmresolution.R.id.layout_monthly;
import static com.example.pk.tpmresolution.R.id.layout_weekly;

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
    String[] status_names;
    String[] status_codes;
    CustomFontTextView[] list_tv;
    AppCompatCheckBox cbDaily, cbWeekly, cbMonthly;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public Date choosedDate;
    int[] maxDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private boolean isFirstTime = true;
    Dialog mDialog;

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
                    status_names = new String[arrStatus.length()];
                    status_codes = new String[arrStatus.length()];
                    for(int i=0; i< arrStatus.length(); i++){
                        status_names[i] = arrStatus.getJSONObject(i).getString("StatusCheckListName");
                        status_codes[i] = arrStatus.getJSONObject(i).getString("StatusChecklistId");
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
                    objStatus.put("StatusCode", listChanged.get(i).getChecklist_status_id());
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
                            mDialogLoading.dismiss();
                            AppTransaction.Toast(getActivity(), obj.getString("Message"));
                        } else {
                            mDialogLoading.dismiss();
                            ShowDialogError(obj.getString("Message"));
                        }
                    } catch (JSONException e) {
                        mDialogLoading.dismiss();
                        Log.d("Kien", "Loi json " + e.toString());
                    }
                }
            }, getActivity()).execute(AppConstants.URL_CHANGE_STATUS_CHECKLIST, object.toString());
        }else AppTransaction.Toast(getActivity(), "Please check one of 3 checkboxs above!");
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
                            inf.setChecklist_status_id(obj.getString("StatusCode"));
                            inf.setChecklist_status_name(obj.getString("StatusName"));
                            inf.setChecklist_user_name(obj.getString("UserCheckName"));
                            //Log.d("Kien", inf.getDisplayName());
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
                            inf.setChecklist_status_id(obj.getString("StatusCode"));
                            inf.setChecklist_status_name(obj.getString("StatusName"));
                            inf.setChecklist_user_name(obj.getString("UserCheckName"));
                            //Log.d("Kien", inf.getDisplayName());
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
                            inf.setChecklist_status_id(obj.getString("StatusCode"));
                            inf.setChecklist_status_name(obj.getString("StatusName"));
                            inf.setChecklist_user_name(obj.getString("UserCheckName"));
                            //Log.d("Kien", inf.getDisplayName());
                            listMonthly.add(inf);
                        }

                        boolean isNewDaily = object.getString("TypeDay").equals("New");
                        boolean isNewWeekly = object.getString("TypeWeek").equals("New");
                        boolean isNewMonthly = object.getString("TypeMonth").equals("New");

                        if(!isNewDaily || listDaily.isEmpty()){
                            cbDaily.setClickable(false);
                            cbDaily.setBackgroundResource(R.drawable.checkbox_red);
                            Log.d("Kien", "cbDaily.getButtonDrawable() "+cbDaily.getBackground().toString());
                            cbDaily.setTag(false);
                        }else {
                            cbDaily.setClickable(true);
                            cbDaily.setBackgroundResource(R.drawable.checkbox_selector);
                            cbDaily.setTag(null);
                        }
                        if(!isNewWeekly || listWeekly.isEmpty()){
                            cbWeekly.setClickable(false);
                            cbWeekly.setBackgroundResource(R.drawable.checkbox_red);
                            Log.d("Kien", "cbWeekly.getButtonDrawable() "+cbWeekly.getBackground().toString());
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

                        CheckListAdapter adapterDaily = new CheckListAdapter(getActivity(), listDaily, status_names, status_codes, isNewDaily, 1, DailyCheckListFragment.this, new CheckListAdapter.SpinnerItemClickListener() {
                            @Override
                            public void onItemClick(String name, String id, int position) {
                                updateStatusDaily(name, id, position);
                            }
                        });
                        recyclerDaily.setAdapter(adapterDaily);

                        CheckListAdapter adapterWeekly = new CheckListAdapter(getActivity(), listWeekly, status_names, status_codes, isNewWeekly, 2, DailyCheckListFragment.this, new CheckListAdapter.SpinnerItemClickListener() {
                            @Override
                            public void onItemClick(String name, String id, int position) {
                                updateStatusWeekly(name, id, position);
                            }
                        });
                        recyclerWeekly.setAdapter(adapterWeekly);
                        CheckListAdapter adapterMonthly = new CheckListAdapter(getActivity(), listMonthly, status_names, status_codes, isNewMonthly, 3, DailyCheckListFragment.this, new CheckListAdapter.SpinnerItemClickListener() {
                            @Override
                            public void onItemClick(String name, String id, int position) {
                                updateStatusMonthly(name, id, position);
                            }
                        });
                        recyclerMonthly.setAdapter(adapterMonthly);

                    }else{
                        ShowDialogError(object.getString("Message"));
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
                                    mDialog.dismiss();
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

    public void updateStatusDaily(String name, String code, int position){
        Log.d("Kien","Update status daily name: "+name+" code: "+code);
        listDaily.get(position).setChecklist_status_id(code);
        listDaily.get(position).setChecklist_status_name(name);
    }

    public void updateStatusWeekly(String name, String code, int position){
        Log.d("Kien","Update status daily name: "+name+" code: "+code);
        listWeekly.get(position).setChecklist_status_id(code);
        listWeekly.get(position).setChecklist_status_name(name);
    }

    public void updateStatusMonthly(String name, String code, int position){
        Log.d("Kien","Update status daily name: "+name+" code: "+code);
        listMonthly.get(position).setChecklist_status_id(code);
        listMonthly.get(position).setChecklist_status_name(name);
    }


    private void checkRestoreStatus(){
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
    }

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
        Log.d("kien", "day after calculate  " + String.valueOf(d) +" maxDay[month] "+ maxDay[month] +" month "+month+" day "+day);
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

    void ShowDialogError(String message) {
        mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_error);
        CustomFontTextView txt1 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content1);
        CustomFontTextView txt2 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        txt1.setText("Failed");
        txt2.setText(message);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        TextView tv = (TextView)v;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = Calendar.getInstance().getTime();
        Date choosedDate = null;
        try {
            choosedDate = sdf.parse(CheckListFragment.txtDateMonthYear.getText().toString());
        } catch (ParseException e) {
            Log.d("Kien", "Loi parse date "+e.toString());
        }
        Log.d("Kien", "Compare date "+String.valueOf(choosedDate.compareTo(currentDate)));
        if(choosedDate.compareTo(currentDate)<=0) {
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
