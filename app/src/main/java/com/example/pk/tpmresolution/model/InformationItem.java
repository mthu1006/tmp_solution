package com.example.pk.tpmresolution.model;

/**
 * Created by kien on 3/27/2017.
 */

public class InformationItem {

    String Name;
    String displayName;
    String file_dir;


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFile_dir() {
        return file_dir;
    }

    public void setFile_dir(String file_dir) {
        this.file_dir = file_dir;
    }


}
