package com.example.pk.tpmresolution;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontEditText;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.adapter.NavAdapter;
import com.example.pk.tpmresolution.fragment.ChangeLoctionFragment;
import com.example.pk.tpmresolution.fragment.CheckListFragment;
import com.example.pk.tpmresolution.fragment.MainFragment;
import com.example.pk.tpmresolution.fragment.RefereneInfomationFragment;
import com.example.pk.tpmresolution.fragment.RequestMaintenanceFragment;
import com.example.pk.tpmresolution.fragment.SettingFragment;
import com.example.pk.tpmresolution.fragment.ToolManagementFragment;
import com.example.pk.tpmresolution.model.CommonClass;
import com.example.pk.tpmresolution.model.EmployeeItem;
import com.example.pk.tpmresolution.model.NavigationItem;
import com.example.pk.tpmresolution.model.ProductItem;
import com.example.pk.tpmresolution.model.ToolManagementtItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static RecyclerView mRecycler_nav;
    public static NavAdapter mNavAdapter;
    FloatingActionButton fab;
    public Toolbar toolbar;
    private List<NavigationItem> mListNav;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public IntentIntegrator qrScan;
    Integer mPosition = 0;
    public ProductItem mItem = null;
    public static EmployeeItem mEmployee = null;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    MaterialDialog mDialogLoading;
    Dialog mDialog;
    public ArrayList<CommonClass> stt_list;
    public String[] arr_stt;
    CustomFontButton mBtn_dialog;
    public boolean isRequest, isToolManager;

    public Fragment frag;
    String main_name;
    ArrayList<Fragment> list_stack_fraggments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mRecycler_nav = (RecyclerView)findViewById(R.id.recycler_nav);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        qrScan = new IntentIntegrator(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mDialogLoading = AppDialogManager.onCreateDialogLoading(this);
        LinearLayout logo = (LinearLayout) toolbar.findViewById(R.id.img_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), MainFragment.newInstance());
            }
        });

        stt_list = new ArrayList<>();
        frag = MainFragment.newInstance();
        main_name = frag.getClass().getName();
        list_stack_fraggments = new ArrayList<>();
        isToolManager = false;

        LoadNav();
        getListStatus();
        qrScan.setOrientationLocked(true);
        qrScan.setTimeout(120*1000);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogChoice();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                if(isRequest) {
                    requestMachine(result.getContents());

                }else if(isToolManager){
                    isToolManager = false;
                    requestToolManager(result.getContents());
                }else ((ChangeLoctionFragment)frag).getMachine(result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestToolManager(String id){
        try {
            String url = AppConstants.URL_TOOL_MANGAGER
                    .replace(AppConstants.KEY_TOKEN, sharedPref.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""))
                    .replace(AppConstants.KEY_TOOL_ID, id);
            //  Log.d("kien", "url: " + url);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void processFinish(String output) {
                    //  Log.d("kien", "res: " + output);
                    try {
                        JSONObject  object = new JSONObject(output);
                        if(object.getString("Status").equals("Y")){
                            JSONObject oj = object.getJSONObject("ToolInfo");
                            ToolManagementtItem item = new ToolManagementtItem();

                            item.setTool_code(oj.getString("ToolCode"));
                            item.setJing_name(oj.getString("ToolName"));
                            item.setStyle_name(oj.getString("StyleName"));
                            item.setBuyer_name(oj.getString("BuyerName"));
                            item.setJing_name(oj.getString("ToolName"));
                            item.setJing_serial_no(oj.getString("ToolSerialNo"));
                            item.setJing_meterial(oj.getString("MaterialInfo"));
                            item.setPart(oj.getString("ToolPartName"));
                            item.setMachine_model(oj.getString("ModelName"));
                            item.setJing_type(oj.getString("ToolTypeName"));
                            item.setCorporation(oj.getString("CorporationName"));
                            item.setFactory(oj.getString("FactoryName"));
                            item.setLine(oj.getString("LineName"));
                            item.setWarehouse(oj.getString("WarehouseName"));
                            item.setStatus(oj.getString("ToolStatusName"));
                            item.setVideo_file(oj.getString("VideoClipFile"));
                            item.setPattem_file(oj.getString("PatternFile"));
                            item.setAttach_file(oj.getString("AttachFile"));
                            item.setProcess(oj.getString("Proccess"));

                            ToolManagementFragment tool = ToolManagementFragment.newInstance(item);
                            AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), tool);
                        }
                    } catch (JSONException e) {
                        Log.d(AppConstants.TAG, "Loi json "+ e.toString());
                    }

                }
            }, this).execute(url);
        } catch (Exception e) {
            Log.e(AppConstants.TAG, "error: " + e.toString());
        }
    }

    private void requestMachine(String id){
        mDialogLoading.show();
        try {
            String url = AppConstants.URL_GET_MACHINE_DETAIL
                    .replace(AppConstants.KEY_TOKEN, sharedPref.getString(AppConstants.PREF_KEY_LOGIN_TOKEN, ""))
                    .replace(AppConstants.KEY_MACHINE_ID, id);
            //  Log.d("kien", "url: " + url);
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void processFinish(String output) {
                    //  Log.d("kien", "res: " + output);
                    handlerResultFromQR(output);
                }
            }, this).execute(url);

        } catch (Exception e) {
            Log.e("kien", "error: " + e.toString());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handlerResultFromQR(String res){
        if(res!=null){
//            Log.d("kien", "res handler: "+res);
            try {
                JSONObject object = new JSONObject(res);
                if(object.get("Status").equals("Y")) {
                    JSONObject obj_product = object.getJSONObject("MachineInfo");
                    ProductItem item = new ProductItem();
                    item.setMachineID(obj_product.getString("MachineId"));
                    item.setModel_id(obj_product.getString("ModelId"));
                    item.setProduct_name(obj_product.getString("MachineName"));
                    item.setProduct_model(obj_product.getString("ModelName"));
                    item.setFunction(obj_product.getString("Category"));
                    item.setSeries_number(obj_product.getString("SeriesNo"));

                    item.setPurchase_date(obj_product.getString("PurchaseDate"));
                    item.setCorporation(obj_product.getString("CorporationName"));
                    item.setCorporation_id(obj_product.getString("CorporationId"));
                    item.setValid_date(obj_product.getString("ValidDate"));
                    item.setFactory(obj_product.getString("FactoryName"));
                    item.setFactory_id(obj_product.getString("FactoryId"));
                    item.setLine(obj_product.getString("LineName"));
                    item.setLine_id(obj_product.getString("LineId"));
                    item.setWarehouse(obj_product.getString("WarehouseName"));
                    item.setWarehouse_id(obj_product.getString("WarehouseId"));


                    item.setStatus(obj_product.getString("MachineStatus"));

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit().putString(AppConstants.PREF_KEY_MACHINE_ID, item.getMachineID()).commit();

                    String picture_data = obj_product.getString("PictureData");
                    if (!Validation.checkNullOrEmpty(picture_data)) {
                        byte[] decodedString = Base64.decode(picture_data, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        item.setAvatar(Bitmap.createScaledBitmap(decodedByte, 282, 200, false));
                    }
                    mItem = item;
                    mDialogLoading.dismiss();
                    mNavAdapter.notifyItemChanged(mNavAdapter.getFocusedItem());
                    mNavAdapter.setFocusedItem(-1);
                    AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), MainFragment.newInstance());
                }else{
                    mDialogLoading.dismiss();
                    ShowDialogError(object.getString("Message"));
                    if (object.getString("Type").equals("Login")) {
                        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                                AppTransaction.replaceActivityWithAnimation(MainActivity.this, LoginActivity.class);
                            }
                        });
                    }else {
                        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            mDialog.dismiss();
                            }
                        });

                    }
                }

            } catch (JSONException e) {
                mDialogLoading.dismiss();
              //  Log.d("kien", e.toString());
            }
        }else mDialogLoading.dismiss();
    }

    private void  getListStatus(){
            new HTTPRequest(new HTTPRequest.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    if (!Validation.checkNullOrEmpty(output)) {
//                        Log.d("Kien", output);
                        try {
                            JSONArray arr = new JSONArray(output);
                            arr_stt = new String[arr.length()];
                            for (int i = 0; i < arr.length(); i++) {
                                arr_stt[i] = arr.getJSONObject(i).getString("SatusName");
                                CommonClass c = new CommonClass();
                                c.setId(arr.getJSONObject(i).getString("SatusId"));
                                c.setName(arr.getJSONObject(i).getString("SatusName"));
                                stt_list.add(c);
                            }

                        } catch (JSONException e) {
                          //  Log.d("Kien", "Loi get status " + e.toString());
                        }

                    }
                }
            }, this).execute(AppConstants.URL_LIST_STATUS);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetData(EmployeeItem employee) {
      // Log.d("kien", "receive data on mainActivity " +employee.getEmployee_name());
        mEmployee = employee;
    }

    @Override
    protected void onPause(){
        if(mDialogLoading.isShowing()) mDialogLoading.dismiss();
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void LoadNav(){
        AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), MainFragment.newInstance());
        mRecycler_nav.setLayoutManager(new LinearLayoutManager(this));
        mListNav = new ArrayList<NavigationItem>();
        final List<String> titleitems = Arrays.asList(getResources().getStringArray(R.array.navigation_array_tile));
        TypedArray iconitems = getResources().obtainTypedArray(R.array.navigation_array_icon);
        for (int i = 0; i < titleitems.size(); i++) {
            Drawable drawable = iconitems.getDrawable(i);
            NavigationItem item = new NavigationItem(drawable, titleitems.get(i));
            mListNav.add(item);
        }

        mNavAdapter = new NavAdapter(getApplicationContext(), mListNav, new NavAdapter.NavigationClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                list_stack_fraggments.add(frag);
                frag = ChangeLoctionFragment.newInstance();
                mPosition = position;
                if (position == 0) {
                    frag = ChangeLoctionFragment.newInstance();
                } else if (position == 1) {
                    frag = RefereneInfomationFragment.newInstance();
                } else if (position == 2) {
                    frag = RequestMaintenanceFragment.newInstance();
                } else if (position == 3) {
                    isToolManager = true;
                    ShowDialogChoice();
                }else if (position == 4 ) {
                    frag = CheckListFragment.newInstance();
                } else if (position == 5) {
                    frag = SettingFragment.newInstance();
                }
//                toolbar.setTitle(titleitems.get(position));
                AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), frag);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        mRecycler_nav.setAdapter(mNavAdapter);
        onSetupHeader(navigationView);
        //onCheckSetting();
    }

    public void onSetupHeader(NavigationView navigationView) {
       // Log.d("kien", "setup header");
        final View hView = navigationView.getHeaderView(0);
        ImageView mIvHome = (ImageView) hView.findViewById(R.id.img_home);
        final MaterialLetterIcon mIvAvatar = (MaterialLetterIcon) hView.findViewById(R.id.img_avatar);
        final CustomFontTextView txtID = (CustomFontTextView) hView.findViewById(R.id.txt_employee_id);
        final CustomFontTextView txtName = (CustomFontTextView) hView.findViewById(R.id.txt_employee_name);
        final CustomFontTextView txtWorkbase = (CustomFontTextView) hView.findViewById(R.id.txt_work_base);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEmployee != null){
                  //  Log.d("kien", "mEmployee.getID "+mEmployee.getEmployee_id()+ "mEmployee.getName() " +mEmployee.getEmployee_name());

                    txtID.setText(mEmployee.getEmployee_id());
                    txtName.setText(mEmployee.getEmployee_name());
                    txtWorkbase.setText(mEmployee.getEmployee_workbase());
                    String[] arr = mEmployee.getEmployee_name().split(" ");
                    String letter = arr[arr.length-1];
                    mIvAvatar.setLetter(letter);
                    }
            }
        }, 100);

        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavAdapter.notifyItemChanged(mNavAdapter.getFocusedItem());
                mNavAdapter.setFocusedItem(-1);
                AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), MainFragment.newInstance());
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    public void validationText(TextView tv, String s){
        if(!Validation.checkNullOrEmpty(s)) {
           // Log.d("kien", "settext "+s);
            tv.setText(s);
        }else {
           // Log.d("kien", "settext default");
            tv.setText(AppConstants.DEFAULT_NULL_TEXT);
        }
    }

    void ShowDialogChoice() {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_choice);
        final AppCompatImageView choice1 = (AppCompatImageView) mDialog.findViewById(R.id.btn_choice1);
        LinearLayout choice2 = (LinearLayout) mDialog.findViewById(R.id.layout_scan);
        final CustomFontEditText txt = (CustomFontEditText) mDialog.findViewById(R.id.edt_content);
        choice1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if(!Validation.checkNullOrEmpty(txt.getText().toString())) {
                    if(!isToolManager)
                    requestMachine(txt.getText().toString().toUpperCase());
                    else requestToolManager(txt.getText().toString().toUpperCase());
                    mDialog.dismiss();
                }
                else txt.setError("Please enter machine id");
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mDialogLoading.show();
                if(!isToolManager)
                    isRequest = true;
                qrScan.initiateScan();
            }
        });
        AppCompatImageView img_close = (AppCompatImageView) mDialog.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    void ShowDialogCofirm() {
        final Dialog mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_confirm);
        CustomFontButton mBtAccept = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        CustomFontButton mBtDeny = (CustomFontButton) mDialog.findViewById(R.id.btn_denice);
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(AppConstants.PREF_KEY_LOGIN_REMEMBERLOGIN, false).commit();
                AppTransaction.replaceActivityWithAnimation(MainActivity.this, LoginActivity.class);
                mDialog.dismiss();
            }
        });
        mBtDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        AppCompatImageView img_close = (AppCompatImageView) mDialog.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    void ShowDialogError(String message) {
        mDialog = AppDialogManager.onShowCustomDialog(this, R.layout.dialog_error);
        CustomFontTextView txt = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        mBtn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        txt.setText(message);
        mDialog.show();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(frag.getClass().getName().equals(main_name)){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1000);
        } else {
            if (list_stack_fraggments.size() > 0) {
                AppTransaction.replaceFragmentWithAnimation(getSupportFragmentManager(), list_stack_fraggments.get(list_stack_fraggments.size() - 1));
                list_stack_fraggments.remove(list_stack_fraggments.size() - 1);
            }
        }

    }
}
