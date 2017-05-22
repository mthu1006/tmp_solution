package com.example.pk.tpmresolution.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.LoginActivity;
import com.example.pk.tpmresolution.MainActivity;
import com.example.pk.tpmresolution.R;
import com.example.pk.tpmresolution.adapter.InfomationAdapter;
import com.example.pk.tpmresolution.model.InformationItem;
import com.example.pk.tpmresolution.utils.AppConstants;
import com.example.pk.tpmresolution.utils.AppDialogManager;
import com.example.pk.tpmresolution.utils.AppTransaction;
import com.example.pk.tpmresolution.utils.DownloadFileAsync;
import com.example.pk.tpmresolution.utils.HTTPRequest;
import com.example.pk.tpmresolution.utils.Validation;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.android.views.llm.DividerItemDecoration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class RefereneInfomationFragment extends Fragment {
    private LinearLayout layoutVideo, layoutPDF;
    private ExpandableRelativeLayout expPDF, expVideo;
    private RecyclerView recyclePDF, recycleVideo;
    private OnFragmentInteractionListener mListener;
    private MaterialDialog mDialogLoading;
    private AppCompatImageView imgPDF, imgVideo;
    ArrayList<InformationItem> list_pdf;
    InfomationAdapter adapter;
    String machine_id ="";
    String strDir;
    String user_id = "";
    String login_token = "";
    CustomFontButton mBtn_dialog;

    public RefereneInfomationFragment() {
    }

    public static RefereneInfomationFragment newInstance() {
        RefereneInfomationFragment fragment = new RefereneInfomationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_reference_infomation, container, false);
        ((MainActivity) getActivity()).toolbar.setTitle(getActivity().getResources().getTextArray(R.array.navigation_array_tile)[1]);
        mDialogLoading = AppDialogManager.onCreateDialogLoading(getActivity());

        imgPDF = (AppCompatImageView)root.findViewById(R.id.img_pdf);
        imgPDF.setImageResource(R.drawable.ic_show_close);
        imgVideo = (AppCompatImageView)root.findViewById(R.id.img_video);
        imgVideo.setImageResource(R.drawable.ic_show_close);

        recyclePDF = (RecyclerView)root.findViewById(R.id.recycler_pdf);
        recycleVideo = (RecyclerView)root.findViewById(R.id.recycler_video);

        expVideo = (ExpandableRelativeLayout) root.findViewById(R.id.exp_video);
        expPDF = (ExpandableRelativeLayout) root.findViewById(R.id.exp_pdf);
        expVideo.collapse();
        expPDF.collapse();

        layoutPDF = (LinearLayout) root.findViewById(R.id.layout_pdf);
        list_pdf = new ArrayList<>();
        machine_id = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREF_KEY_MACHINE_ID,"");
        user_id = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREF_KEY_LOGIN_USERNAME,"");
        login_token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREF_KEY_LOGIN_TOKEN,"");
        if(Validation.checkNullOrEmpty(machine_id)) machine_id = "M000000000039";
        strDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + "Pdfs" + File.separator + "guide.pdf";
        if(((MainActivity)getActivity()).mItem!=null)  LoadPDFList();
        layoutPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expPDF.toggle();
                if(expPDF.isExpanded()) {
                    imgPDF.setImageResource(R.drawable.ic_show_close);
                }else {
                    imgPDF.setImageResource(R.drawable.ic_down);
                }

            }
        });

        layoutVideo = (LinearLayout) root.findViewById(R.id.layout_video);
        layoutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*expVideo.toggle();
                if(expVideo.isExpanded()) {
                    imgVideo.setImageResource(R.drawable.ic_show_close);
                }else {
                    imgVideo.setImageResource(R.drawable.ic_down);
                }*/

            }
        });
        return root;
    }

    private void setupRecyclerview(){
        LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclePDF.setLayoutManager(layoutManager);
        recyclePDF.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        adapter = new InfomationAdapter(getActivity(), list_pdf, new InfomationAdapter.NavigationClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mDialogLoading.show();
                String url = list_pdf.get(position).getFile_dir();
            try {
                    new DownloadFileAsync(new DownloadFileAsync.AsyncResponse() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void processFinish(byte[] output) {
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(strDir);
                                fos.write(output);
                                fos.close();
                            } catch (Exception e) {
                               // Log.d("kien", "error while writing file " + e.toString() );
                            }

                            copyReadAssets();

                        }
                    }).execute(url);
                } catch (Exception e) {
                   // Log.d("kien", "Loi pdf " + e.toString() );
                    Fragment frag = LoadPDFFragment.newInstance();
                    AppTransaction.replaceFragmentWithAnimation(getActivity().getSupportFragmentManager(), frag);
                }
            }
        });
        recyclePDF.setAdapter(adapter);
    }

    private void LoadPDFList(){
        String url = AppConstants.URL_REFEREBCE_INFOMATION
        .replace(AppConstants.KEY_TOKEN, login_token)
        .replace(AppConstants.KEY_MODEL_ID, ((MainActivity)getActivity()).mItem.getModel_id());
        new HTTPRequest(new HTTPRequest.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject object = new JSONObject(output);
                    if(object.getString("Status").equals("Y")) {
                        JSONArray arr = object.getJSONArray("ReferenceInfo");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            InformationItem inf = new InformationItem();
                            inf.setName(obj.getString("FileName"));
                            inf.setDisplayName(obj.getString("DisplayFileName"));
                            inf.setFile_dir(obj.getString("FilePath"));
                            list_pdf.add(inf);
                           // Log.d("Kien", inf.getDisplayName());
                        }
                        setupRecyclerview();

                        //Log.d("Kien", "recyclePDF heihgt 1 :"+ String.valueOf(recyclePDF.getLayoutParams().height));

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
                        }

                    }

                } catch (JSONException e) {
                   // Log.d("Kien", "loi json inf "+e.toString());
                }
            }
        }, getActivity()).execute(url);
    }

    @Override
    public void onPause() {
        if(mDialogLoading.isShowing()) mDialogLoading.dismiss();
        super.onPause();
    }

    private void copyReadAssets()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" +strDir), "application/pdf");
        startActivity(intent);
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
        Dialog mDialog = AppDialogManager.onShowCustomDialog(getActivity(), R.layout.dialog_error);
        CustomFontTextView txt1 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content1);
        CustomFontTextView txt2 = (CustomFontTextView) mDialog.findViewById(R.id.txt_content2);
        mBtn_dialog = (CustomFontButton) mDialog.findViewById(R.id.btn_accept);
        txt1.setText("Change status failed");
        txt2.setText(message);
        mDialog.show();
    }
}
