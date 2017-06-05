package com.example.pk.tpmresolution.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.LoginActivity;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.adapter.CommonAdapter;
import com.example.pk.tpmresolution.adapter.MovingMachineAdapter;
import com.example.pk.tpmresolution.adapter.NavClickAdapter;
import com.example.pk.tpmresolution.model.CommonClass;
import com.example.pk.tpmresolution.model.MachineForMoving;
import com.example.pk.tpmresolution.model.ProductItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.DialogAcceptClickListener;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.pk.tpmresolution.R.id.spn_from;
import static com.example.pk.tpmresolution.R.id.spn_to;

public class ChangeLoctionFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private OnFragmentInteractionListener mListener;
    private LinearLayout imgAdd;
    private CustomFontEditText edtDate, edtRemark;
    private CustomFontTextView  edtMachine;
    private CustomFontButton btnSave, btnCancel, btnAccept, mBtn_dialog;
    private AutoCompleteTextView edtReveiceUser;
    DatePickerDialog datePickerDialog;
    MaterialDialog mDialogLoading;
    private RecyclerView recyclerMovingMachine;
    ArrayList<CommonClass> fromCorpList, toCorpList, warehouseList, factoryList, lineList;
    ArrayList<MachineForMoving> listMachine;
    SharedPreferences prefs;
    //dialog
    private Dialog mDialog;
    CommonAdapter factoryAdapter, lineAdapter, warehouseAdapter;
    private AppCompatSpinner spnFrom, spnTo, spnFactory, spnLine, spnWarehouse;
    private CheckBox radio_line, radio_warehouse;
    LinearLayout layout_line, layout_warehouse;
    String[] arrReceiveUser;
    ProductItem machine_for_moving;
    MovingMachineAdapter machineAdapter;

    public ChangeLoctionFragment() {
    }

    public static ChangeLoctionFragment newInstance() {
        ChangeLoctionFragment fragment = new ChangeLoctionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_change_loction, container, false);
        ((MainActivity)getActivity()).fab.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getTextArray(R.array.navigation_array_tile)[0]);
        imgAdd = (LinearLayout) root.findViewById(R.id.img_add_location);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        recyclerMovingMachine = (RecyclerView) root.findViewById(R.id.recycler_moving_machine);
        LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerMovingMachine.setLayoutManager(layoutManager);

        spnFrom= (AppCompatSpinner) root.findViewById(spn_from);
        spnTo= (AppCompatSpinner) root.findViewById(spn_to);
        edtDate = (CustomFontEditText) root.findViewById(R.id.edt_date);
        edtReveiceUser = (AutoCompleteTextView) root.findViewById(R.id.edt_reveice_user);
        edtRemark = (CustomFontEditText) root.findViewById(R.id.edt_remark);

        fromCorpList = new ArrayList<>();
        toCorpList = new ArrayList<>();
        factoryList = new ArrayList<>();
        lineList = new ArrayList<>();
        warehouseList = new ArrayList<>();
        listMachine = new ArrayList<>();

        machineAdapter = new MovingMachineAdapter(getActivity(), listMachine, new NavClickAdapter() {
            @Override
            public void onItemClick(View v, int position) {
                showDialogUpdate(listMachine.get(position), position);
            }

            @Override
            public void onLongItemClick(View v, final int position) {
                AppDialogManager.onCreateDialogConfirm(getActivity(), "Do you want to delete?", new DialogAcceptClickListener() {
                    @Override
                    public void onAcceptClick(View v) {
                        listMachine.remove(position);
                        machineAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCloseClick(View v) {

                    }
                });
            }
        });
        recyclerMovingMachine.setAdapter(machineAdapter);

        btnCancel = (CustomFontButton)root.findViewById(R.id.btn_cancel_location);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mNavAdapter.notifyItemChanged(MainActivity.mNavAdapter.getFocusedItem());
                MainActivity.mNavAdapter.setFocusedItem(-1);
                Fragment frag = MainFragment.newInstance();
                AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
            }
        });

        btnSave = (CustomFontButton)root.findViewById(R.id.btn_save_location);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String date = edtDate.getText().toString();
                final String user = edtReveiceUser.getText().toString();
                final String remark = edtRemark.getText().toString();
                if(Validation.checkNullOrEmpty(date))
                    edtDate.setError("Please enter date");
                if(Validation.checkNullOrEmpty(user))
                    edtReveiceUser.setError("Please enter id or name of user");
                if(Validation.checkNullOrEmpty(remark))
                    edtRemark.setError("Please enter remark");
                else if(listMachine.isEmpty())
                    ShowDialogError("Machine list can not be empty", "Please add machine and try again");
                else
                movingMachine();
            }
        });

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        getFromCorparation();
        getToCorparation();
        getReceiveUser();
        initAddDialog();

        spnTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getFactory(toCorpList.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrFrom);
//        spnFrom.setAdapter(adapter);
//        spnTo.setAdapter(adapter);
        return root;
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

    void initAddDialog() {
        mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_add_new_location);
        spnFactory = (AppCompatSpinner)mDialog.findViewById(R.id.spn_factory);
        spnLine = (AppCompatSpinner)mDialog.findViewById(R.id.spn_line);
        spnWarehouse = (AppCompatSpinner)mDialog.findViewById(R.id.spn_warehouse);
        edtMachine = (CustomFontTextView) mDialog.findViewById(R.id.edt_seach_machine);
        LinearLayout layout_scan = (LinearLayout) mDialog.findViewById(R.id.layout_ask);

        radio_line = (CheckBox) mDialog.findViewById(R.id.check_line);
        radio_warehouse = (CheckBox) mDialog.findViewById(R.id.check_warehouse);

        layout_line = (LinearLayout) mDialog.findViewById(R.id.layout_line);
        layout_warehouse = (LinearLayout) mDialog.findViewById(R.id.layout_warehouse);

        btnAccept = (CustomFontButton)mDialog.findViewById(R.id.btn_accept);
        factoryAdapter = new CommonAdapter(factoryList, getActivity());
        lineAdapter = new CommonAdapter(lineList, getActivity());

        warehouseAdapter = new CommonAdapter(warehouseList, getActivity());
        spnFactory.setAdapter(factoryAdapter);
        spnLine.setAdapter(lineAdapter);
        spnWarehouse.setAdapter(warehouseAdapter);

        getWarehouse();

        radio_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(radio_line.isChecked()) {
                        layout_line.setVisibility(View.VISIBLE);
                    }else{
                        layout_line.setVisibility(View.GONE);
                    }
                radio_warehouse.setChecked(false);
                layout_warehouse.setVisibility(View.GONE);

            }
        });

        radio_warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_warehouse.isChecked()) {
                    layout_warehouse.setVisibility(View.VISIBLE);
                }else{
                    layout_warehouse.setVisibility(View.GONE);
                }
                radio_line.setChecked(false);
                layout_line.setVisibility(View.GONE);
            }
        });

        layout_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogChoice();
            }
        });
        spnFactory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getLine(factoryList.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    void ShowDialogChoice() {
        final Dialog dialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_choice);
        AppCompatImageView choice1 = (AppCompatImageView) dialog.findViewById(R.id.btn_choice1);
        LinearLayout choice2 = (LinearLayout) dialog.findViewById(R.id.layout_scan);
        final CustomFontEditText txt = (CustomFontEditText) dialog.findViewById(R.id.edt_content);
        choice1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if(!Validation.checkNullOrEmpty(txt.getText().toString())) {
                    getMachine(txt.getText().toString().toUpperCase());
                    dialog.dismiss();
                }else txt.setError("Please enter machine id");
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mDialogLoading.show();
                ((MainActivity)getActivity()).isRequest = false;
                ((MainActivity)getActivity()).qrScan.initiateScan();
            }
        });
        AppCompatImageView img_close = (AppCompatImageView) mDialog.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogAdd(){
        radio_line.setChecked(false);
        radio_line.performClick();
        edtMachine.setText("Please scan QR code");
        btnAccept.setText("Add");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMachine.getText().equals("Please scan qr code")) {
                    ShowDialogError("Save failed", "Please scan QR code");
                    return;
                }

                MachineForMoving mc = new MachineForMoving();
                if(machine_for_moving!=null) {
                    mc.setName(machine_for_moving.getProduct_name());
                    mc.setFrFactory(machine_for_moving.getFactory());
                    mc.setFrLine(machine_for_moving.getLine());
                    mc.setFrWh(machine_for_moving.getWarehouse());
                    mc.setMachineId(machine_for_moving.getMachineID());
                }else {
                    //ShowDialogError("Add machine failed", "Please scan QR code");
                    edtMachine.setError("Please scan QR code");
                    return;
                }
                if(!factoryList.isEmpty() && spnFactory.getSelectedItemPosition() >=0)
                    mc.setToFactory(factoryList.get(spnFactory.getSelectedItemPosition()).getId());
                else {
                    AppTransaction.Toast(getActivity(), "Factory can not null");
                    return;
                }
                if(!lineList.isEmpty() && radio_line.isChecked() && spnLine.getSelectedItemPosition() >=0)
                mc.setToLine(lineList.get(spnLine.getSelectedItemPosition()).getId());
                if(!warehouseList.isEmpty() && radio_warehouse.isChecked() && spnWarehouse.getSelectedItemPosition()>=0)
                mc.setToWh(warehouseList.get(spnWarehouse.getSelectedItemPosition()).getId());
                mDialog.dismiss();
                if(!checkDulicate(mc.getMachineId())) {
                    listMachine.add(mc);
                    machineAdapter.notifyDataSetChanged();
                    machine_for_moving = null;

                }else {
                    ShowDialogError("Machine has already added", "Please scan another machine!");
                    mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    });
                }
              //  Log.d("Kien", "listMachine.size " + String.valueOf(listMachine.size()));
            }
        });
        mDialog.show();
    }

    private void showDialogUpdate(final MachineForMoving machine, final int postion){
        radio_line.setChecked(false);
        radio_warehouse.setChecked(false);
        edtMachine.setText(machine.getName());
        btnAccept.setText("Update");

        for (int i = 0; i< factoryList.size(); i++){
            if(machine.getToFactory().equals(factoryList.get(i))){
                spnFactory.setSelection(i);
            }
        }
        if(machine.getToLine()!=null) {
            radio_line.performClick();
            for (int i = 0; i < lineList.size(); i++) {
                if (machine.getToLine().equals(lineList.get(i))) {
                    spnLine.setSelection(i);
                }
            }
        }
        if(machine.getToWh()!=null) {
            radio_warehouse.performClick();
            for (int i = 0; i < warehouseList.size(); i++) {
                if (machine.getToWh().equals(warehouseList.get(i))) {
                    spnWarehouse.setSelection(i);
                }
            }
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtMachine.getText().toString();
                if (Validation.checkNullOrEmpty(name)) {
                    ShowDialogError("Add partbook failed", "Please choose name from spiner name");
                } else {

                    MachineForMoving mc = new MachineForMoving();
                    if(machine_for_moving!=null) {
                        mc.setName(machine_for_moving.getProduct_name());
                        mc.setFrFactory(machine_for_moving.getFactory());
                        mc.setFrLine(machine_for_moving.getLine());
                        mc.setFrWh(machine_for_moving.getWarehouse());
                        mc.setMachineId(machine_for_moving.getMachineID());
                    }else {
                        mc.setName(machine.getName());
                        mc.setFrFactory(machine.getFrFactory());
                        mc.setFrLine(machine.getFrLine());
                        mc.setFrWh(machine.getFrWh());
                        mc.setMachineId(machine.getMachineId());
                    }
                    if(!factoryList.isEmpty() && spnFactory.getSelectedItemPosition() >=0)
                        mc.setToFactory(factoryList.get(spnFactory.getSelectedItemPosition()).getId());
                    else {
                        AppTransaction.Toast(getActivity(), "Factory can not null");
                        return;
                    }
                    if(!lineList.isEmpty() && radio_line.isChecked() && spnLine.getSelectedItemPosition() >=0)
                        mc.setToLine(lineList.get(spnLine.getSelectedItemPosition()).getId());
                    if(!warehouseList.isEmpty() && radio_warehouse.isChecked() && spnWarehouse.getSelectedItemPosition()>=0)
                        mc.setToWh(warehouseList.get(spnWarehouse.getSelectedItemPosition()).getId());

                    listMachine.set(postion, mc);
                    machineAdapter.notifyDataSetChanged();
                    machine_for_moving = null;
                    mDialog.dismiss();
                }

            }
        });
        mDialog.show();
    }

    private boolean checkDulicate(String id){
        for (int i=0; i<listMachine.size(); i++)
            if(listMachine.get(i).getMachineId().equals(id))
                return true;
        return false;
    }

    private void getFromCorparation(){

        String url = AppConstants.URL_GET_FROM_CORPARATION
                .replace(AppConstants.KEY_TOKEN, PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""));
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {

                    JSONArray arr = new JSONArray(output);
                    for(int i=0; i< arr.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setId(arr.getJSONObject(i).getString("CorpId"));
                        c.setName(arr.getJSONObject(i).getString("CorpName"));
                        fromCorpList.add(c);
                    }
                    CommonAdapter adapter = new CommonAdapter(fromCorpList, getActivity());
                    spnFrom.setAdapter(adapter);

                } catch (Exception e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(url);
    }

    private void getToCorparation(){
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {

                    JSONArray arr = new JSONArray(output);
                    for(int i=0; i< arr.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setId(arr.getJSONObject(i).getString("CorpId"));
                        c.setName(arr.getJSONObject(i).getString("CorpName"));
                        toCorpList.add(c);
                    }
                    CommonAdapter adapter = new CommonAdapter(toCorpList, getActivity());
                    spnTo.setAdapter(adapter);
                    getFactory(toCorpList.get(0).getId());
                } catch (JSONException e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(AppConstants.URL_GET_TO_CORPARATION);
    }

    private void getReceiveUser(){
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray array = new JSONArray(output);
                    arrReceiveUser = new String[array.length()];
                    for(int i=0; i<array.length(); i++){
                        arrReceiveUser[i] = array.getJSONObject(i).getString("UserId") +" - "+array.getJSONObject(i).getString("UserName");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrReceiveUser);
                    edtReveiceUser.setAdapter(adapter);

                } catch (JSONException e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(AppConstants.URL_LIST_USER);
    }

    private void getFactory(String corpId){
        factoryList.clear();
        String url = AppConstants.URL_GET_FACTORY.replace(AppConstants.KEY_ID, corpId);
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {

                    JSONArray arr = new JSONArray(output);
                    for(int i=0; i< arr.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setId(arr.getJSONObject(i).getString("FactoryId"));
                        c.setName(arr.getJSONObject(i).getString("FactoryName"));
                        factoryList.add(c);
                    }
                    factoryAdapter.notifyDataSetChanged();
                    if (lineList.isEmpty() && factoryList.size()>0) getLine(factoryList.get(0).getId());

                } catch (JSONException e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(url);
    }

    private void getLine(String factoryId){
        lineList.clear();
        String url = AppConstants.URL_GET_LINE.replace(AppConstants.KEY_ID, factoryId);
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray arr = new JSONArray(output);
                    for(int i=0; i< arr.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setId(arr.getJSONObject(i).getString("LineId"));
                        c.setName(arr.getJSONObject(i).getString("LineName"));
                        lineList.add(c);
                    }
                    lineAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(url);
    }

    private void getWarehouse(){
        warehouseList.clear();
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray arr = new JSONArray(output);
                    for(int i=0; i< arr.length(); i++){
                        CommonClass c = new CommonClass();
                        c.setId(arr.getJSONObject(i).getString("WareHouseId"));
                        c.setName(arr.getJSONObject(i).getString("WareHouseName"));
                        warehouseList.add(c);
                    }
                    warehouseAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }
            }
        }, getActivity()).execute(AppConstants.URL_GET_WAREHOUSE);
    }

    public void getMachine(String machine_id){
        String url = AppConstants.URL_GET_MACHINE_FOR_MOVING
                .replace("CORPID", fromCorpList.get(spnFrom.getSelectedItemPosition()).getId())
                .replace("KEY", machine_id);
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject object = new JSONObject(output);
                    if(object.getString("Status").equals("Y")){
                        JSONObject obj = object.getJSONObject("MachineInfo");
                        machine_for_moving = new ProductItem();
                        machine_for_moving.setMachineID(obj.getString("MachineId"));
                        machine_for_moving.setProduct_name(obj.getString("MachineName"));
                        machine_for_moving.setSeries_number(obj.getString("SerialNo"));
                        machine_for_moving.setFactory(obj.getString("FactoryId"));
                        machine_for_moving.setLine(obj.getString("LineNo"));
                        machine_for_moving.setWarehouse(obj.getString("WareHouseId"));
                        edtMachine.setText(machine_for_moving.getProduct_name());
                    }else {
                        ShowDialogError("Search machine failed", object.getString("Message"));
                        if (object.getString("Type").equals("Login")) {
                            mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                                    AppTransaction.replaceActivityWithAnimation(getActivity(), LoginActivity.class);
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                   // Log.d("Kien", "Loi json "+e.toString());
                }

            }
        }, getActivity()).execute(url);
    }

    private void movingMachine(){
        mDialogLoading.show();
        String accept_user = edtReveiceUser.getText().toString().split(" - ")[0];
        JSONObject object = new JSONObject();
        try {


            object.put("Token", prefs.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""));
            if(fromCorpList.get(spnFrom.getSelectedItemPosition()).getId().equals(toCorpList.get(spnTo.getSelectedItemPosition()).getId()))
                object.put("Type", "ApprovalN");
            else
                object.put("Type", "ApprovalY");

            object.put("FromCorp", fromCorpList.get(spnFrom.getSelectedItemPosition()).getId());
            object.put("ToCorp", toCorpList.get(spnTo.getSelectedItemPosition()).getId());
            object.put("MovingDate", edtDate.getText().toString());
            object.put("ReceiveUser", accept_user);
            object.put("Remarks", edtRemark.getText().toString());
            JSONArray array = new JSONArray();
            for (int i=0; i<listMachine.size();i++){
                JSONObject obj = new JSONObject();
                obj.put("MachineId", listMachine.get(i).getMachineId());
                obj.put("FrFactory", listMachine.get(i).getFrFactory());
                obj.put("FrLine", listMachine.get(i).getFrLine());
                obj.put("FrWh", listMachine.get(i).getFrWh());
                obj.put("ToFactory", listMachine.get(i).getToFactory());
                obj.put("ToLine", listMachine.get(i).getToLine());
                obj.put("ToWh", listMachine.get(i).getToWh());
                array.put(obj);
            }
            object.put("ListMachineMove", array);

            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    mDialogLoading.dismiss();
                    try {
                        JSONObject res = new JSONObject(output);
                        if(res.getString("Status").equals("Y")){
                            AppTransaction.Toast(getActivity(), res.getString("Message"));
                        }else {
                            ShowDialogError("Save request change location failed", res.getString("Message"));
                            if (res.getString("Type").equals("Login")) {
                                mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                                        AppTransaction.replaceActivityWithAnimation(getActivity(), LoginActivity.class);
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                       // Log.d("Kien", "Loi json "+e.toString());
                    }
                }
            }, getActivity()).execute(AppConstants.URL_MOVING_MACHINE, object.toString());

        } catch (JSONException e) {
           // Log.d("Kien", "Loi json "+e.toString());
        }
    }

    private void ShowDialogError(String title, String message) {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_error);
        CustomFontTextView txt1 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content1);
        CustomFontTextView txt2 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        txt1.setText(title);
        txt2.setText(message);
        mDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        if(mDialogLoading.isShowing()) mDialogLoading.dismiss();
        super.onPause();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        edtDate.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
