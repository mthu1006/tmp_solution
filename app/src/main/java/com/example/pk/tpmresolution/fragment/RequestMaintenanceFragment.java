package com.example.pk.tpmresolution.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.LoginActivity;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.adapter.CommonAdapter;
import com.example.pk.tpmresolution.adapter.MaintenaceAdapter;
import com.example.pk.tpmresolution.adapter.NavClickAdapter;
import com.example.pk.tpmresolution.model.CommonClass;
import com.example.pk.tpmresolution.model.HistoryItem;
import com.example.pk.tpmresolution.model.PartBookItem;
import com.example.pk.tpmresolution.model.ProductItem;
import com.example.pk.tpmresolution.model.RequestMaintenace;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.DialogAcceptClickListener;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import butterknife.ButterKnife;

public class RequestMaintenanceFragment extends Fragment {
    ImageView mImgAdd;
    RecyclerView recycler_accessories;
    private OnFragmentInteractionListener mListener;
    private MaintenaceAdapter accessoriesAdapter;
    private ArrayList<PartBookItem> list;
    private ArrayList<PartBookItem> listPartBook;
    private ArrayList<CommonClass> listUnit;
    String[] arrUnit, arrAcceptUsers, arrPartBook ;
    private Dialog mDialog, mDialogError;
    ProductItem mItem = null;
    private RequestMaintenace mMaitainItem = null;
   // LinearLayout layoutContent;
    private ArrayAdapter<String> adapter;
    private AppCompatSpinner spnUnit, spnName, spnStatus;
    private CustomFontEditText edtQantity, edtPrice, edtReason;
    private CustomFontButton btnAccept, btnCancel, btnOK, mBtn_dialog;
    CustomFontTextView txtModel, txtRequestDate, txtMachineID, txtName, txtTitle;
    LinearLayout layoutStatus;
    AppCompatAutoCompleteTextView edtAcceptUser;
    MaterialDialog mDialogLoading;
    SharedPreferences prefs;
    HistoryItem item;

    public RequestMaintenanceFragment(HistoryItem item) {
        this.item = item;
    }

    public static RequestMaintenanceFragment newInstance(HistoryItem item) {
        RequestMaintenanceFragment fragment = new RequestMaintenanceFragment(item);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    void initAddDialog() {
        mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_add_new_accessories);
         spnUnit = (AppCompatSpinner)mDialog.findViewById(R.id.spn_unit);
         spnName = (AppCompatSpinner)mDialog.findViewById(R.id.spn_name_pb);
         edtQantity = (CustomFontEditText)mDialog.findViewById(R.id.edt_qty);
         edtPrice = (CustomFontEditText)mDialog.findViewById(R.id.edt_price);
         btnAccept = (CustomFontButton)mDialog.findViewById(R.id.btn_accept);
        spnName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edtQantity.setText(String.valueOf(listPartBook.get(i).getPartbook_qty()));
                edtPrice.setText(String.valueOf(listPartBook.get(i).getPartbook_price()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void validationText(TextView tv, String s){
        if(!Validation.checkNullOrEmpty(s))
            tv.setText(s);
        else tv.setText(AppConstants.DEFAULT_NULL_TEXT);
    }

    private void showDialogAdd(){
        btnAccept.setText("Add");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit = "", name = "", quantity, price;
                if (arrUnit.length > 0)
                    unit = arrUnit[spnUnit.getSelectedItemPosition()];
                if (spnName.getSelectedItemPosition()>-1)
                    name = arrPartBook[spnName.getSelectedItemPosition()];

                    quantity = edtQantity.getText().toString();
                    price = edtPrice.getText().toString();

                    if (Validation.checkNullOrEmpty(name)) {
                        ShowDialogError("Add partbook failed", "Please place name from spiner name", true);
                    } else if (Validation.checkNullOrEmpty(price)) {
                        edtPrice.setError("Please enter price!");
                    } else if (Validation.checkNullOrEmpty(quantity)) {
                        edtQantity.setError("Please enter quantity!");
                    } else {
                        String id = listPartBook.get(spnName.getSelectedItemPosition()).getPartbook_id();
                        PartBookItem item = new PartBookItem();
                        item.setPartbook_unit_name(unit);
                        item.setPartbook_unit_id(listUnit.get(spnUnit.getSelectedItemPosition()).getId());
                        item.setPartbook_name(name);
                        item.setPartbook_price(Integer.parseInt(price));
                        item.setPartbook_qty(Integer.parseInt(quantity));
                        item.setPartbook_id(id);
                        int pos = checkExist(id);
                        if (pos > -1) {
                            item.setPartbook_qty(list.get(pos).getPartbook_qty() + item.getPartbook_qty());
                            list.set(pos, item);
                        } else
                            list.add(item);

                        accessoriesAdapter.notifyDataSetChanged();
                        Log.d("kien", "Size: " + String.valueOf(accessoriesAdapter.getItemCount()));
                        mDialog.dismiss();
                    }
                }


        });

        mDialog.show();

    }

    private void showDialogUpdate(int price, int Quantity, String name, String unit, final int position){
        edtPrice.setText(String.valueOf(price));
        edtQantity.setText(String.valueOf(Quantity));
        btnAccept.setText("Update");

        for (int i = 0; i< spnUnit.getAdapter().getCount(); i++){
            if(unit.equals(spnUnit.getAdapter().getItem(i).toString())){
                spnUnit.setSelection(i);
            }
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit = arrUnit[spnUnit.getSelectedItemPosition()];
                String name = "";
                if(spnName.getSelectedItemPosition()>-1)
                name = arrPartBook[spnName.getSelectedItemPosition()];
                String quantity  = edtQantity.getText().toString();
                String price = edtPrice.getText().toString();
                if (Validation.checkNullOrEmpty(name)) {
                    ShowDialogError("Add partbook failed", "Please choose name from spiner name", true);
                } else if (Validation.checkNullOrEmpty(price)) {
                    edtPrice.setError("Please enter price!");
                }else if (Validation.checkNullOrEmpty(quantity)) {
                    edtQantity.setError("Please enter quantity!");
                } else {
                    PartBookItem item = new PartBookItem();
                    item.setPartbook_unit_name(unit);
                    item.setPartbook_name(name);
                    item.setPartbook_price(Integer.parseInt(price));
                    item.setPartbook_qty(Integer.parseInt(quantity));

                    list.set(position, item);
                    accessoriesAdapter.notifyDataSetChanged();
                    Log.d("kien", "Size: " + String.valueOf(accessoriesAdapter.getItemCount()));
                    mDialog.dismiss();
                }

            }
        });
        mDialog.show();
    }

    private void loadList(){

        recycler_accessories.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_accessories.setLayoutManager(linearLayoutManager);
        accessoriesAdapter = new MaintenaceAdapter(getActivity(), list, new NavClickAdapter() {
            @Override
            public void onItemClick(View v, int position) {
                showDialogUpdate(list.get(position).getPartbook_price(), list.get(position).getPartbook_qty(), list.get(position).getPartbook_name(), list.get(position).getPartbook_unit_name(), position);
            }

            @Override
            public void onLongItemClick(View v, final int position) {
                AppDialogManager.onCreateDialogConfirm(getActivity(), "Do you want to delete?", new DialogAcceptClickListener() {
                    @Override
                    public void onAcceptClick(View v) {
                        list.remove(position);
                        accessoriesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCloseClick(View v) {

                    }
                });
            }

        });
        recycler_accessories.setAdapter(accessoriesAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request_maintenance, container, false);
        ((MainActivity)getActivity()).fab.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getTextArray(R.array.navigation_array_tile)[2]);
        ButterKnife.bind(getActivity());
        mItem = ((MainActivity) getActivity()).mItem;

        txtTitle = (CustomFontTextView) root.findViewById(R.id.title_model);
        layoutStatus = (LinearLayout) root.findViewById(R.id.layout_status);
        spnStatus = (AppCompatSpinner)root.findViewById(R.id.spn_status);
        if(mItem!=null) {
            if (!mItem.getStatus().equalsIgnoreCase("Broken")) {
                ShowDialogError("Machine is not broken", "Can not request maintenance", false);
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
        }else {
            ShowDialogError("Can not request maintain", "Please scan machine and try again!", false);
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
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        txtName = (CustomFontTextView) root.findViewById(R.id.txt_name);
        txtModel = (CustomFontTextView) root.findViewById(R.id.txt_model);
        txtRequestDate = (CustomFontTextView) root.findViewById(R.id.txt_request_date);
        edtAcceptUser = (AppCompatAutoCompleteTextView) root.findViewById(R.id.edt_accept_user);
        edtReason = (CustomFontEditText) root.findViewById(R.id.edt_reason);
        txtMachineID = (CustomFontTextView) root.findViewById(R.id.txt_machine_id);
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());
        LinearLayout img_add_product = (LinearLayout) root.findViewById(R.id.img_add_product);
        mMaitainItem = new RequestMaintenace();
        //layoutContent = (LinearLayout) root.findViewById(R.id.img_add_product);
        recycler_accessories = (RecyclerView) root.findViewById(R.id.recycler_accessories);
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

        btnOK = (CustomFontButton) root.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataMaitenance();
            }
        });

        list = new ArrayList<>();
        initAddDialog();
        if(mItem!=null){
            validationText(txtName, mItem.getProduct_name());
            validationText(txtModel, mItem.getProduct_model());
            validationText(txtMachineID, mItem.getMachineID());
            getListPartBook();
        }


        getListPartBookUnit();
        getListAcceptUsers();


        loadList();
        InitData();
        img_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });
        return root;
    }

    private void setDataMaitenance(){
        if(Validation.checkNullOrEmpty(edtAcceptUser.getText().toString())){
            edtAcceptUser.setError("You must choose accept user");
            return;
        }else if(Validation.checkNullOrEmpty(edtReason.getText().toString())){
            edtReason.setError("You must choose reason");
            return;
        }
        mDialogLoading.show();
        try{
            String accept_user = edtAcceptUser.getText().toString().split(" - ")[0];
            String token = prefs.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, "");
            String userID = prefs.getString(AppConstants.PREF_KEY_LOGIN_USERNAME, "");
            JSONObject obj = new JSONObject();
            obj.put("Token", token);
            obj.put("ModelId", mItem.getModel_id());
            obj.put("MachineId", mItem.getMachineID());
            obj.put("CreateUserId", userID);
            obj.put("AcceptUserId", accept_user);
            obj.put("TotalMoney", null);
            obj.put("Reason", edtReason.getText().toString());
            JSONArray arr = new JSONArray();
            for(int i=0; i<list.size(); i++){
                JSONObject ob = new JSONObject();
                ob.put("PartBookId", list.get(i).getPartbook_id());
                ob.put("Quantity", list.get(i).getPartbook_qty());
                ob.put("Price", list.get(i).getPartbook_price());
                ob.put("PriceUnitId", list.get(i).getPartbook_unit_id());
                arr.put(ob);
            }
            obj.put("PartBook", arr);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d("Kien", output);
                    try {
                        mDialogLoading.dismiss();
                        JSONObject obj = new JSONObject(output);
                        if (obj.getString("Status").equals("Y")) Toast.makeText(getActivity(), "Save request maintenance success", Toast.LENGTH_LONG).show();
                        else {
                            ShowDialogError("Save request maintenance failed", obj.getString("Message"), true);
                            if (obj.getString("Type").equals("Login")) {
                                mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                                        AppTransaction.replaceActivityWithAnimation(getActivity(), LoginActivity.class);
                                    }
                                });
                            }else mDialog.dismiss();

                        }
                    } catch (JSONException e) {
                        Log.d("Kien", "Loi json "+e.toString());
                        mDialogLoading.dismiss();
                    }


                }
            }, getActivity()).execute(AppConstants.URL_SEND_REQUEST_MAITENACE, obj.toString());
        }catch (Exception e){
            Log.d("kien", "Loi json gui "+e.toString());
            mDialogLoading.dismiss();
        }
    }

    private void getListPartBookUnit(){
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    listUnit = new ArrayList<CommonClass>();
                    JSONArray array = new JSONArray(output);
                    arrUnit = new String[array.length()];
                    for(int i=0; i<array.length(); i++){
                        arrUnit[i] = array.getJSONObject(i).getString("PartBookPriceUnitName");
                        CommonClass c = new CommonClass();
                        c.setId(array.getJSONObject(i).getString("PartBookPriceUnitId"));
                        c.setName(array.getJSONObject(i).getString("PartBookPriceUnitName"));
                        listUnit.add(c);
                    }
                    ArrayAdapter<String> adapterUnit = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrUnit);
                    spnUnit.setAdapter(adapterUnit);
                } catch (Exception e) {
                    Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(AppConstants.URL_LIST_UNIT);
    }

    private void getListAcceptUsers(){
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray array = new JSONArray(output);
                    arrAcceptUsers = new String[array.length()];
                    for(int i=0; i<array.length(); i++){
                        arrAcceptUsers[i] = array.getJSONObject(i).getString("UserId") +" - "+array.getJSONObject(i).getString("UserName");
                    }
                    if(arrAcceptUsers.length > 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrAcceptUsers);
                        edtAcceptUser.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(AppConstants.URL_LIST_USER);
    }

    private void getListPartBook(){
        String url = AppConstants.URL_LIST_PARTBOOK.replace(AppConstants.KEY_MODEL_ID, mItem.getModel_id());
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    listPartBook = new ArrayList<>();
                    JSONArray array = new JSONArray(output);
                    arrPartBook = new String[array.length()];
                    for(int i=0; i<array.length(); i++){
                        arrPartBook[i] = array.getJSONObject(i).getString("PartBookName");
                        PartBookItem c = new PartBookItem();
                        c.setPartbook_id(array.getJSONObject(i).getString("PartBookId"));
                        c.setPartbook_name(array.getJSONObject(i).getString("PartBookName"));
                        c.setPartbook_qty(array.getJSONObject(i).getInt("Quantity"));
                        c.setPartbook_price(array.getJSONObject(i).getInt("Price"));
                        c.setPartbook_total_money(array.getJSONObject(i).getInt("TotalMoney"));
                        c.setPartbook_unit_id(array.getJSONObject(i).getString("PriceUnitId"));
                        c.setPartbook_unit_name(array.getJSONObject(i).getString("PriceUnitName"));
                        listPartBook.add(c);
                    }
                    ArrayAdapter<String> adapterPartBook = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrPartBook);
                    spnName.setAdapter(adapterPartBook);
                } catch (JSONException e) {
                    Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(url);
    }

    private  void InitData(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtRequestDate.setText(sdf.format(Calendar.getInstance().getTime()));
        accessoriesAdapter.notifyDataSetChanged();

        if(item!=null){
            layoutStatus.setVisibility(View.VISIBLE);
            txtTitle.setText(R.string.txt_created_date);
            CommonAdapter adapter = new CommonAdapter(((MainActivity)getActivity()).stt_list,getActivity());
            spnStatus.setAdapter(adapter);
            for (int i = 0; i< ((MainActivity)getActivity()).stt_list.size(); i++ ){
                if(((MainActivity)getActivity()).stt_list.get(i).getName().equals(item.getStatus())){
                    spnStatus.setSelection(i);
                }
            }

            edtAcceptUser.setText(item.getRequest_user());
            edtAcceptUser.setEnabled(false);
            txtModel.setText(item.getCreated_date());
            txtRequestDate.setText(item.getRequest_date());
            edtReason.setText(item.getReason());
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private int checkExist(String id){
        for (int i=0; i<list.size(); i++){
            if(list.get(i).getPartbook_id().equals(id)){
                return i;
            }
        }
        return -1;
    }
}
