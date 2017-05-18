package com.example.pk.tpmresolution.model;

/**
 * Created by kien on 4/1/2017.
 */

public class PartBookItem {
    String partbook_id;
    String partbook_name;
    int partbook_qty;
    int partbook_price;
    int partbook_total_money;
    String partbook_unit_id;
    String partbook_unit_name;

    public String getPartbook_id() {
        return partbook_id;
    }

    public void setPartbook_id(String partbook_id) {
        this.partbook_id = partbook_id;
    }

    public String getPartbook_name() {
        return partbook_name;
    }

    public void setPartbook_name(String partbook_name) {
        this.partbook_name = partbook_name;
    }

    public int getPartbook_qty() {
        return partbook_qty;
    }

    public void setPartbook_qty(int partbook_qty) {
        this.partbook_qty = partbook_qty;
    }

    public int getPartbook_price() {
        return partbook_price;
    }

    public void setPartbook_price(int partbook_price) {
        this.partbook_price = partbook_price;
    }

    public int getPartbook_total_money() {
        return partbook_total_money;
    }

    public void setPartbook_total_money(int partbook_total_money) {
        this.partbook_total_money = partbook_total_money;
    }

    public String getPartbook_unit_id() {
        return partbook_unit_id;
    }

    public void setPartbook_unit_id(String partbook_unit_id) {
        this.partbook_unit_id = partbook_unit_id;
    }

    public String getPartbook_unit_name() {
        return partbook_unit_name;
    }

    public void setPartbook_unit_name(String partbook_unit_name) {
        this.partbook_unit_name = partbook_unit_name;
    }
}