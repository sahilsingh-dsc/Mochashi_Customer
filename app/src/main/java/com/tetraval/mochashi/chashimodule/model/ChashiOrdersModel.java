package com.tetraval.mochashi.chashimodule.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ChashiOrdersModel {

    @ServerTimestamp
    private String o_uid;
    private String o_p_category;
    private String o_customer_uid;
    private String o_chashi_uid;
    private String o_chashi_name;
    private String o_chashi_photo;
    private String o_rate;
    private String o_quantity;
    private String o_total;
    private String o_status;


    public ChashiOrdersModel() {
    }

    public ChashiOrdersModel(String o_uid, String o_p_category, String o_customer_uid, String o_chashi_uid, String o_chashi_name, String o_chashi_photo, String o_rate, String o_quantity, String o_total, String o_status) {
        this.o_uid = o_uid;
        this.o_p_category = o_p_category;
        this.o_customer_uid = o_customer_uid;
        this.o_chashi_uid = o_chashi_uid;
        this.o_chashi_name = o_chashi_name;
        this.o_chashi_photo = o_chashi_photo;
        this.o_rate = o_rate;
        this.o_quantity = o_quantity;
        this.o_total = o_total;
        this.o_status = o_status;
    }

    public String getO_uid() {
        return o_uid;
    }

    public void setO_uid(String o_uid) {
        this.o_uid = o_uid;
    }

    public String getO_p_category() {
        return o_p_category;
    }

    public void setO_p_category(String o_p_category) {
        this.o_p_category = o_p_category;
    }

    public String getO_customer_uid() {
        return o_customer_uid;
    }

    public void setO_customer_uid(String o_customer_uid) {
        this.o_customer_uid = o_customer_uid;
    }

    public String getO_chashi_uid() {
        return o_chashi_uid;
    }

    public void setO_chashi_uid(String o_chashi_uid) {
        this.o_chashi_uid = o_chashi_uid;
    }

    public String getO_chashi_name() {
        return o_chashi_name;
    }

    public void setO_chashi_name(String o_chashi_name) {
        this.o_chashi_name = o_chashi_name;
    }

    public String getO_chashi_photo() {
        return o_chashi_photo;
    }

    public void setO_chashi_photo(String o_chashi_photo) {
        this.o_chashi_photo = o_chashi_photo;
    }

    public String getO_rate() {
        return o_rate;
    }

    public void setO_rate(String o_rate) {
        this.o_rate = o_rate;
    }

    public String getO_quantity() {
        return o_quantity;
    }

    public void setO_quantity(String o_quantity) {
        this.o_quantity = o_quantity;
    }

    public String getO_total() {
        return o_total;
    }

    public void setO_total(String o_total) {
        this.o_total = o_total;
    }

    public String getO_status() {
        return o_status;
    }

    public void setO_status(String o_status) {
        this.o_status = o_status;
    }
}
