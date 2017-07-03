package com.example.pk.tpmresolution.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.LoginActivity;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingFragment extends Fragment {
    CustomFontButton btnSavePass;
    CustomFontEditText edtPass, edtNewPass, edtReNewPass, mTextTitle;
    private OnFragmentInteractionListener mListener;
    private CustomFontTextView txtID, txtName, txtDOB;
    LinearLayout btnlogOut, btnChangePass;
    ExpandableRelativeLayout expChangePass;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    MaterialDialog mDialogLoading;
    AppCompatImageView imgChangePss, imgLogout;
    Dialog mDialog;
    CustomFontButton mBtn_dialog;

    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        ((MainActivity)getActivity()).fab.setVisibility(View.VISIBLE);

        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getTextArray(R.array.navigation_array_tile)[4]);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPref.edit();
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());


        txtID = (CustomFontTextView)root.findViewById(R.id.txtName);
        txtName = (CustomFontTextView)root.findViewById(R.id.txtDOB);
        txtDOB = (CustomFontTextView)root.findViewById(R.id.txtLevel);

        edtPass = (CustomFontEditText)root.findViewById(R.id.edt_password);
        edtNewPass = (CustomFontEditText)root.findViewById(R.id.edt_new_password);
        edtReNewPass = (CustomFontEditText)root.findViewById(R.id.edt_new_repassword);

        txtID.setText(MainActivity.mEmployee.getEmployee_id());
        txtName.setText(MainActivity.mEmployee.getEmployee_name());
        txtDOB.setText(MainActivity.mEmployee.getEmployee_workbase());

        imgChangePss = (AppCompatImageView)root.findViewById(R.id.img_change_pass);
        imgLogout = (AppCompatImageView)root.findViewById(R.id.img_logout);

        expChangePass = (ExpandableRelativeLayout) root.findViewById(R.id.exp_change_pass);
        btnlogOut = (LinearLayout) root.findViewById(R.id.layout_logout);
        expChangePass.collapse();
        btnlogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgLogout.setImageResource(R.drawable.ic_add_bottom);
                ShowDialogCofirm();
            }
        });

        btnSavePass = (CustomFontButton)root.findViewById(R.id.btn_save_pass);
        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePassword();
            }
        });

        btnChangePass = (LinearLayout)root.findViewById(R.id.layout_change_pass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPass.setError(null);
                edtNewPass.setError(null);
                edtReNewPass.setError(null);

                expChangePass.toggle();

                if(expChangePass.isExpanded()) {
                    imgChangePss.setImageResource(R.drawable.ic_add_top);
                }else {
                    imgChangePss.setImageResource(R.drawable.ic_add_bottom);
                }

            }
        });
        return root;
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

    private void savePassword(){

        final String pwd = edtPass.getText().toString();
        final String new_pwd = edtNewPass.getText().toString();
        final String renew_pwd = edtReNewPass.getText().toString();
        if (Validation.checkNullOrEmpty(pwd)) {
            edtPass.setError("You must enter password");
        } else if (!pwd.equals(sharedPref.getString(AppConstants.PREF_KEY_LOGIN_PASSWORD, ""))) {
            edtPass.setError("Password is not correct!");
        } else if (Validation.checkNullOrEmpty(new_pwd)) {
            edtNewPass.setError("You must enter new password");
        } else if (Validation.checkNullOrEmpty(renew_pwd)) {
            edtReNewPass.setError("You must enter renew password");
        }else if (!new_pwd.equals(renew_pwd)) {
            edtReNewPass.setError("Retype password doesn't match with new password");
        } else {

            mDialogLoading.show();
            JSONObject object = new JSONObject();
            try {

                object.put("Token", sharedPref.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""));
                object.put("OldPassword", pwd);
                object.put("NewPassword", new_pwd);

            } catch (JSONException e) {
                //Log.d("kien", "Error parse json change password "+ e.toString());
            }
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void processFinish(String output) {
                   // Log.d("kien", "res: " + output);
                    if (Validation.checkNullOrEmpty(output)) {
                        mDialogLoading.dismiss();
                        ShowDialogError("Server error, please try again!");
                    } else {
                        mDialogLoading.dismiss();
                        try {
                            JSONObject obj = new JSONObject(output);
                            if(obj.getString("Status").equals("Y")){
                                editor.putString(AppConstants.PREF_KEY_LOGIN_PASSWORD, renew_pwd).commit();
                                AppTransaction.Toast(getActivity(), obj.getString("Message"));
                            }else {
                                ShowDialogError(obj.getString("Message"));
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
                           // Log.d("Kien", "Error while parse json login" + e.toString());
                        }
                    }

                }
            }, getActivity()).execute(AppConstants.URL_LOGIN_CHANGEPASS, object.toString());
        }
    }

    void ShowDialogCofirm() {
        mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_confirm);
        CustomFontButton mBtAccept = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        CustomFontButton mBtDeny = (CustomFontButton) mDialog.findViewById(R.id.btn_denice);

        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject obj =  new JSONObject();
                try {
                    obj.put("Token", sharedPref.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""));

                    new HTTPRequest(new HTTPRequest.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            try {
                                JSONObject object = new JSONObject(output);
                                if(object.getString("Status").equals("Y")){
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                                    AppTransaction.replaceActivityWithAnimation(getActivity(), LoginActivity.class);
                                    mDialog.dismiss();
                                }else {
                                    ShowDialogError(object.getString("Message"));
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
                                e.printStackTrace();
                            }

                        }
                    }, getActivity()).execute(AppConstants.URL_lOGOUT, obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        mBtDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgLogout.setImageResource(R.drawable.ic_add_top);
                mDialog.dismiss();
            }
        });
        AppCompatImageView img_close = (AppCompatImageView) mDialog.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgLogout.setImageResource(R.drawable.ic_add_top);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    void ShowDialogError(String message) {
        Dialog mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_error);
        CustomFontTextView txt = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        txt.setText(message);
        mDialog.show();
    }
}
