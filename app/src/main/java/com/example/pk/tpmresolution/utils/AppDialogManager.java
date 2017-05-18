package com.example.pk.tpmresolution.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private static String buttonYes;
    private static String buttonNo;
    private static DialogAcceptClickListener mClickCancel;

    public static String getButton() {
        return button;
    }

    public static void setButton(String button) {
        AppDialogManager.button = button;
    }

    public DialogAcceptClickListener getmClick() {
        return mClick;
    }

    public void setmClick(DialogAcceptClickListener mClick) {
        this.mClick = mClick;
    }

    public static String getTitles() {
        return titles;
    }

    public static void setTitles(String titles) {
        AppDialogManager.titles = titles;
    }

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
        buttonYes=context.getString(R.string.txt_profile10);
        buttonNo=context.getString(R.string.txt_profile13);
    }

    /*public static void onCreateDialogESuccess(Context context, final DialogAcceptClickListener mclick) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.customView(R.layout.dialog_thanhcong, false);
        final MaterialDialog mDialog = mBuilder.show();
        mDialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = mDialog.getCustomView();

        ImageView mClose = (ImageView) view.findViewById(R.id.button_close);
        CustomFontTextView mTvContent = (CustomFontTextView) view.findViewById(R.id.textView_content);
        CustomFontTextView mTvTitles = (CustomFontTextView) view.findViewById(R.id.textView_titles);
        CustomFontButton mBtAccept = (CustomFontButton) view.findViewById(R.id.button_accept);
        mTvContent.setText(content);
        mTvTitles.setText(titles);
        mBtAccept.setText(button);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onAcceptClick(view);
                mDialog.dismiss();
            }
        });
        onCreate(context);
    }*/

    public static void onCreateDialogESuccess(final Context context, String titles, String content, final DialogAcceptClickListener mclick) {
        final Dialog mDialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_thanhcong, null);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);//chạm ở ngoài
        mDialog.setContentView(view);

        final ImageView mClose = (ImageView) view.findViewById(R.id.button_close);
        CustomFontTextView mTvContent = (CustomFontTextView) view.findViewById(R.id.textView_content);
        CustomFontButton btn_accept = (CustomFontButton) view.findViewById(R.id.btn_accept);

        mTvContent.setText(content);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onAcceptClick(view);
                mDialog.dismiss();
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onCloseClick(view);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /*public static void onCreateDialogConfirm(Context context, final DialogAcceptClickListener mclick) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.customView(R.layout.dialog_confirm, false);
        final MaterialDialog mDialog = mBuilder.show();
        View view = mDialog.getCustomView();

        ImageView mClose = (ImageView) view.findViewById(R.id.button_close);
        CustomFontTextView mTvContent = (CustomFontTextView) view.findViewById(R.id.textView_content);
        CustomFontTextView mTvTitles = (CustomFontTextView) view.findViewById(R.id.textView_titles);
        CustomFontButton mBtAccept = (CustomFontButton) view.findViewById(R.id.button_accept);
        //CustomFontButton mBtDenice = (CustomFontButton) view.findViewById(R.id.button_denice);
        mTvContent.setText(content);
        mTvTitles.setText(titles);
        mBtAccept.setText(buttonYes);
        //mBtDenice.setText(buttonNo);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onAcceptClick(view);
                mDialog.dismiss();
            }
        });
       *//* mBtDenice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });*//*
    }*/

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

        CustomFontButton btnDenice = (CustomFontButton) view.findViewById(R.id.btn_denice);
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
        return dialog;
    }

    public static Dialog onShowDialogWithBackground(final Context context, int layoutId) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final Dialog dialog = new Dialog(context, R.style.MyDialog);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        params.height = height - getHeightToolbar(context) - getHeightStatusbar(context);
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.MyDialog);
        dialog.setCanceledOnTouchOutside(true);
        final View click = (View) dialog.findViewById(R.id.bg_dialog);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        View noClick = (View) dialog.findViewById(R.id.bg_noclick);
        noClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
//                if(imm != null){
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
            }
        });
        return dialog;
    }

    private static int getHeightToolbar(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    private static int getHeightStatusbar(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*public static void onCreateDialogESuccessTest(Context context, final DialogAcceptClickListener mclick) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.customView(R.layout.dialog_thanhcong, false);
        final MaterialDialog mDialog = mBuilder.show();
        View view = mDialog.getCustomView();
        ImageView mClose = (ImageView) view.findViewById(R.id.button_close);
        CustomFontTextView mTvContent = (CustomFontTextView) view.findViewById(R.id.textView_content);
        CustomFontTextView mTvTitles = (CustomFontTextView) view.findViewById(R.id.textView_titles);
        CustomFontButton mBtAccept = (CustomFontButton) view.findViewById(R.id.button_accept);
        mTvContent.setText(content);
        mTvTitles.setText(titles);
        mBtAccept.setText(button);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onAcceptClick(view);
                mDialog.dismiss();
            }
        });
        onCreate(context);
    }*/

    public static Dialog onShowDialogWithBackgroundTest(final Context context, int layoutId) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        View view = LayoutInflater.from(context).inflate(layoutId, null);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        params.height = height - getHeightToolbar(context) - getHeightStatusbar(context);

        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.MyDialog);
        dialog.setCanceledOnTouchOutside(true);
        View click = (View) dialog.findViewById(R.id.bg_dialog);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        View noClick = (View) dialog.findViewById(R.id.bg_noclick);
        noClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return dialog;
    }


    /*public static void onCreateDialogConfirmTest(Context context,String content, final DialogAcceptClickListener mclick) {

        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(context);
        mBuilder.customView(R.layout.dialog_confirm, false);
        final MaterialDialog mDialog = mBuilder.show();
        View view = mDialog.getCustomView();
        ImageView mClose = (ImageView) view.findViewById(R.id.button_close);
        CustomFontTextView mTvContent = (CustomFontTextView) view.findViewById(R.id.textView_content);
        CustomFontTextView mTvTitles = (CustomFontTextView) view.findViewById(R.id.textView_titles);
        CustomFontButton mBtAccept = (CustomFontButton) view.findViewById(R.id.button_accept);
        //CustomFontButton mBtDenice = (CustomFontButton) view.findViewById(R.id.button_denice);
        mTvContent.setText(content);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mBtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.onAcceptClick(view);
                mDialog.dismiss();
            }
        });
       *//* mBtDenice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });*//*
    }*/

//    mDialog = AppDialogManager.onShowDialogWithBackground(getContext(), R.layout.dialog_thanhcong);
//    mDialog.show();
}
