package com.example.pk.tpmresolution.model;

import android.graphics.drawable.Drawable;

/**
 * Created by kien on 07/28/2016.
 */
public class NavigationItem {
    public Drawable icon;
    public String name;

    public NavigationItem() {
    }

    public NavigationItem(Drawable icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
