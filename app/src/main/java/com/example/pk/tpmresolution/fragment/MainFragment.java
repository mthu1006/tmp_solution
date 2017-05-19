package com.example.pk.tpmresolution.fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
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
import com.example.pk.tpmresolution.adapter.CommonAdapter;
import com.example.pk.tpmresolution.model.ProductItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.AppUltils;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;

import org.json.JSONException;
import org.json.JSONObject;

public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ProductItem mItem = null;
    private CustomFontTextView txtID, txtName, txtModel, txtFunction, txtSeriesNumber, txtPuchaseDate, txtValidDate, txtCorporation, txtFactory, txtLine, txtTitleLine;
    LinearLayout layoutLine;
    private AppCompatSpinner spn_status;
    private MaterialLetterIcon imgAvatar;
    private CustomFontButton btnSave;
    MaterialDialog mDialogLoading;
    CustomFontButton mBtn_dialog;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getString(R.string.machine_information));
        ((MainActivity) getActivity()).frag = this;
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());
        mItem = ((MainActivity) getActivity()).mItem;
        txtID = (CustomFontTextView) root.findViewById(R.id.txt_product_id);
        txtName = (CustomFontTextView) root.findViewById(R.id.txt_product_name);
        txtModel = (CustomFontTextView) root.findViewById(R.id.txt_product_model);
        txtFunction = (CustomFontTextView) root.findViewById(R.id.txt_function);
        txtSeriesNumber= (CustomFontTextView) root.findViewById(R.id.txt_series_number);
        txtPuchaseDate = (CustomFontTextView) root.findViewById(R.id.txt_purchase_date);
        txtValidDate = (CustomFontTextView) root.findViewById(R.id.txt_valid_date);
        txtCorporation = (CustomFontTextView) root.findViewById(R.id.txt_corporation);
        txtFactory = (CustomFontTextView) root.findViewById(R.id.txt_factory);
        txtLine = (CustomFontTextView) root.findViewById(R.id.txt_line);
        txtTitleLine = (CustomFontTextView) root.findViewById(R.id.txt_title_line);
        layoutLine = (LinearLayout) root.findViewById(R.id.layout_line);
        spn_status = (AppCompatSpinner) root.findViewById(R.id.spn_status);
        imgAvatar = (MaterialLetterIcon) root.findViewById(R.id.img_avatar);
        if(((MainActivity)getActivity()).stt_list.size()>0) {

            CommonAdapter adapter = new CommonAdapter(((MainActivity)getActivity()).stt_list, getActivity());
            spn_status.setAdapter(adapter);
        }

        btnSave = (CustomFontButton) root.findViewById(R.id.btn_save_status);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });
            setupProduct();
        return root;
    }

    private void changeStatus(){
        mDialogLoading.show();
        JSONObject obj = new JSONObject();
        try{
            obj.put("Token", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""));
            obj.put("MachineId", mItem.getMachineID());
            obj.put("MachineStatus", ((MainActivity)getActivity()).stt_list.get(spn_status.getSelectedItemPosition()).getId());

        }catch (Exception e){
            Log.d("Kien", "Loi parse json "+e.toString());
        }
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if (output != null) {
                    mDialogLoading.show();
                    try {
                        JSONObject obj = new JSONObject(output);
                        if(obj.getString("Status").equals("Y")){
                            mDialogLoading.dismiss();
                            ((MainActivity) getActivity()).mItem.setStatus(((MainActivity)getActivity()).stt_list.get(spn_status.getSelectedItemPosition()).getName());
                             AppTransaction.Toast(getActivity(),"Change status successfully");

                        }else {
                            ShowDialogError(obj.getString("Message"));
                            mDialogLoading.dismiss();
                            if (obj.getString("Type").equals("Login")) {
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
                        ShowDialogError("Server error, please try again!");
                        mDialogLoading.dismiss();
                        Log.d("Kien", "Loi parse json "+e.toString());
                    }
                }
            }
        }, getActivity()).execute(AppConstants.URL_CHANGE_STATUS, obj.toString());
    }

    private void setupProduct(){
        if(mItem!=null){
            if(mItem.getAvatar()!=null)
            imgAvatar.setImageBitmap(mItem.getAvatar());
            validationText(txtID, mItem.getMachineID());
            validationText(txtName, mItem.getProduct_name());
            validationText(txtModel, mItem.getProduct_model());
            validationText(txtFunction, mItem.getFunction());
            validationText(txtSeriesNumber, mItem.getSeries_number());
            validationText(txtPuchaseDate, AppUltils.fomatDateFromString(mItem.getPurchase_date()));
            validationText(txtValidDate, AppUltils.fomatDateFromString(mItem.getValid_date()));
            validationText(txtCorporation, mItem.getCorporation());
            validationText(txtFactory, mItem.getFactory());

            if(Validation.checkNullOrEmpty(mItem.getLine())){
                if(!Validation.checkNullOrEmpty(mItem.getWarehouse())) {
                    txtTitleLine.setText("Warehouse");
                    txtLine.setText(mItem.getWarehouse());
                }else layoutLine.setVisibility(View.GONE);
            }else {
                txtTitleLine.setText("Line");
                txtLine.setText(mItem.getLine());
            }
            if(((MainActivity)getActivity()).stt_list.size()>0){
                for (int i = 0; i < ((MainActivity)getActivity()).stt_list.size(); i++) {
                    if (((MainActivity)getActivity()).stt_list.get(i).getName().trim().equalsIgnoreCase(mItem.getStatus().trim())) {
                        spn_status.setSelection(i);
                        return;
                    }
                }
            }else spn_status.setVisibility(View.GONE);

        }
    }

    public void validationText(TextView tv, String s){
        if(!Validation.checkNullOrEmpty(s))
            tv.setText(s);
        else tv.setText(AppConstants.DEFAULT_NULL_TEXT);
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

    void ShowDialogError(String message) {
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
        txt1.setText("Change status failed");
        txt2.setText(message);
        mDialog.show();
    }



}
