package com.example.pk.tpmresolution.model;

import android.graphics.Bitmap;

/**
 * Created by mthu1 on 3/21/2017.
 */

public class ProductItem {
    String machineID;
    String model_id;
    String product_name;
    String product_model;
    String function;
    String series_number;
    String purchase_date;
    String valid_date;
    String corporation;
    String master_code;
    String factory;
    String line;
    String warehouse;
    String status;
    Bitmap avatar;
    String[] listStatus;
    String[] listStatusCode;


    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String[] getListStatusCode() {
        return listStatusCode;
    }

    public void setListStatusCode(String[] listStatusCode) {
        this.listStatusCode = listStatusCode;
    }




    public String[] getListStatus() {
        return listStatus;
    }

    public void setListStatus(String[] listStatus) {
        this.listStatus = listStatus;
    }

    public String getMaster_code() {
        return master_code;
    }

    public void setMaster_code(String master_code) {
        this.master_code = master_code;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getProduct_model() {
        return product_model;
    }

    public void setProduct_model(String product_model) {
        this.product_model = product_model;
    }

    public String getSeries_number() {
        return series_number;
    }

    public void setSeries_number(String series_number) {
        this.series_number = series_number;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
