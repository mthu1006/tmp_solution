package com.example.pk.tpmresolution.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.example.pk.tpmresolution.R;

/**
 * Created by kien on 3/16/2017.
 */

public class AppTransaction {
    public static void replaceFragmentWithAnimation(FragmentManager fm, Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_to_top, 0);
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceActivityWithAnimation(Context c, Class cls)
    {
        Intent i = new Intent(c, cls);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(c, R.anim.bottom_to_top, 0).toBundle();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i, bndlanimation);
    }

    public static void replaceActivityWithAnimationWithBundle(Context c, Class cls,Bundle bundle) {
        Intent i = new Intent(c, cls);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(c, R.anim.bottom_to_top, 0).toBundle();
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i, bndlanimation);
    }

    public static void replaceActivityResultWithAnimation(Context c, Class cls,Bundle bundle,Integer code) {
        Intent i = new Intent(c, cls);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(c, R.anim.bottom_to_top, 0).toBundle();
        i.putExtras(bundle);
        ((AppCompatActivity)c).startActivityForResult(i, code,bndlanimation);
    }

    public static void replaceActivityWithAnimationElement(Context c, Class cls,View ivProfile) {
        Intent i = new Intent(c, cls);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) c, (View)ivProfile, "ivGVBM");
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(c, R.anim.bottom_to_top, 0).toBundle();
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i, options.toBundle());
    }

    public static void replaceActivityResultWithAnimation(Context c, Class cls,Integer code) {
        Intent i = new Intent(c, cls);
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(c, R.anim.bottom_to_top, 0).toBundle();
        ((AppCompatActivity)c).startActivityForResult(i, code,bndlanimation);
    }

    public static Drawable getScreenShot(Activity c) {
        View view = c.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap drawingCache = view.getDrawingCache();
        Rect frame = new Rect();
        c.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap b = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, drawingCache.getWidth(), drawingCache.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        return new BitmapDrawable(c.getResources(), b);
    }

    public static void Toast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
