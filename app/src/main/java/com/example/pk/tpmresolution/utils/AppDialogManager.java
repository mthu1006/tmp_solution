package com.example.pk.tpmresolution.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daniribalbert.customfontlib.views.CustomFontButton;
import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.pk.tpmresolution.R;

/**
 * Created by kien on 29-Aug-16.
 */
public class AppDialogManager {
    private DialogAcceptClickListener mClick;
    private static String titles;
    private static String content;
    private static String button;

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        AppDialogManager.content = content;
    }

    public static void onCreate(Context context) {
        content = "";
        titles = context.getString(R.string.txt_thongbao1);
        button = context.getString(R.string.txt_profile10);
    }


    public static void onCreateDialogConfirm(Context context,String content, final DialogAcceptClickListener mclick) {
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        dialog.setContentView(view);

        ImageView mClose = (ImageView) view.findViewById(R.id.button_close);
        CustomFontTextView mTvContent = (CustomFontTextView) view.findViewById(R.id.textView_content);
        CustomFontTextView mTvTitles = (CustomFontTextView) view.findViewById(R.id.textView_titles);
        CustomFontButton mBtAccept = (CustomFontButton) view.findViewById(R.id.btn_accept);
        CustomFontButton mBtDenice = (CustomFontButton) view.findViewById(R.id.btn_denice);
        mTvContent.setText(content);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onAcceptClick(view);
                dialog.dismiss();
            }
        });
        mBtDenice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static MaterialDialog onCreateDialogLoading(Context context) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.content(R.string.txt_loading).progress(true, 0).cancelable(false);
        MaterialDialog mDialog = mBuilder.build();
        return mDialog;
    }

    public static Dialog onShowCustomDialog(final Context context, int layoutId){
        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        dialog.setContentView(view);
        CustomFontButton btn = (CustomFontButton) view.findViewById(R.id.btn_accept);
        CustomFontButton btnDenice = (CustomFontButton) view.findViewById(R.id.btn_cancel);
        if(btnDenice!=null) {
            btnDenice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        AppCompatImageView img_close = (AppCompatImageView) view.findViewById(R.id.button_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if(btn!=null)
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

}
