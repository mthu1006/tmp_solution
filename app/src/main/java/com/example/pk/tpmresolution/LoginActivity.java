package com.example.pk.tpmresolution;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.RelativeLayout;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.model.EmployeeItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edt_username) CustomFontEditText mEdtUser;
    @BindView(R.id.edt_password) CustomFontEditText mEdtPass;
    @BindView(R.id.btn_login) RelativeLayout mBtnLogin;
    @BindView(R.id.check_save_pass) AppCompatCheckBox checkSave;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    MaterialDialog mDialogLoading;
    SliderLayout sliderLayout;
    HashMap<String, Integer> imgslide ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        ButterKnife.bind(this);
        setupSlider();
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        mDialogLoading = AppDialogManager.onCreateDialogLoading(this);
        if (sharedPref.getBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false)){
            String tmp = sharedPref.getString(AppConstants.PREF_KEY_LOGIN_INFO, "");
                if(!Validation.checkNullOrEmpty(tmp)){
                    String[] arr = tmp.split(";");
                    EmployeeItem e = new EmployeeItem();
                    e.setEmployee_id(arr[0]);
                    e.setEmployee_name(arr[1]);
                    e.setEmployee_workbase(arr[2]);
                    EventBus.getDefault().postSticky(e);
                    AppTransaction.replaceActivityWithAnimation(this, MainActivity.class);
                    finish();
                }
        }
    }

    private void setupSlider(){
        imgslide = new HashMap<>();
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        imgslide.put("Pungkook building 1",R.drawable.pungkook1);
        imgslide.put("Pungkook building 2",R.drawable.pungkook2);
        imgslide.put("Pungkook building 3",R.drawable.pungkook3);
        imgslide.put("Pungkook building 4",R.drawable.pungkook4);
        imgslide.put("Pungkook building 5", R.drawable.pungkook5);

        for(String name : imgslide.keySet()){

            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(imgslide.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                        }
                    });
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    void ShowDialogError(String message) {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_error);
        CustomFontTextView txt = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        CustomFontButton mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        txt.setText(message);
        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    @OnClick(R.id.btn_login)
    public void login(View view) {
        final String usn = mEdtUser.getText().toString();
        final String pwd = mEdtPass.getText().toString();
        if(Validation.checkNullOrEmpty(usn)){
            mEdtUser.setError("You must enter username");
        }else if(Validation.checkNullOrEmpty(pwd)){
            mEdtPass.setError("You must enter password");
        }else {
                mDialogLoading.show();
                try {
                    final JSONObject object = new JSONObject();
                    object.put("UserId", usn);
                    object.put("Password", pwd);
                    new HTTPRequest(new HTTPRequest.AsyncResponse() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void processFinish(String output) {
//                            Log.d("kien", "res: " + output);
                            if (Validation.checkNullOrEmpty(output)) {
                                mDialogLoading.dismiss();
                                ShowDialogError("Invalid username or password");
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(output);

                                    if (obj.getString("Status").equals("Y")) {
                                        mDialogLoading.show();
                                        JSONObject object = obj.getJSONObject("UserData");
                                        EmployeeItem e = new EmployeeItem();
                                        e.setEmployee_id(object.getString("UserId"));
                                        e.setEmployee_name(object.getString("Username"));
                                        e.setEmployee_workbase(object.getString("Departement"));

                                        String tmp = e.getEmployee_id() + ";" + e.getEmployee_name() + ";" + e.getEmployee_workbase();
                                        EventBus.getDefault().postSticky(e);
                                        editor.putString(AppConstants.PREF_KEY_LOGIN_INFO, tmp);
                                        editor.putString(AppConstants.PREF_KEY_LOGIN_USERNAME, usn);
                                        editor.putString(AppConstants.PREF_KEY_LOGIN_PASSWORD, pwd);
                                        editor.putString(AppConstants.PREF_KEY_LOGIN_TOKEN, obj.getString("Token"));
                                        editor.putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, true);
                                        mDialogLoading.dismiss();
                                        AppTransaction.replaceActivityWithAnimation(LoginActivity.this, MainActivity.class);
                                        editor.commit();
                                        finish();
                                    } else {
                                        ShowDialogError(obj.getString("Message"));
                                        mDialogLoading.dismiss();
                                    }
                                } catch (JSONException e) {
                                    ShowDialogError("Server error. Please try again!");
                                    mDialogLoading.dismiss();
/*
                                    Log.d("Kien", "Error while parse json login" + e.toString());
*/
                                }
                            }

                        }
                    }, this).execute(AppConstants.URL_LOGIN, object.toString());
                } catch (JSONException e) {
                   // Log.d("Kien", "Error while parse json before login" + e.toString());
                }
        }

    }

    @Override
    public void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        sliderLayout.startAutoCycle();
    }
}
