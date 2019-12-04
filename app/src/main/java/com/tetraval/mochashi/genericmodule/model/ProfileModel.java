package com.tetraval.mochashi.genericmodule.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ProfileModel {

    @ServerTimestamp
    Date p_timestamp;
    String p_uid;
    String p_nickname;
    String p_email;
    String p_address;
    String p_lat;
    String p_long;
    String p_credits;
    String p_active;


    public ProfileModel() {
    }

    public ProfileModel(Date p_timestamp, String p_uid, String p_nickname, String p_email, String p_address, String p_lat, String p_long, String p_credits, String p_active) {
        this.p_timestamp = p_timestamp;
        this.p_uid = p_uid;
        this.p_nickname = p_nickname;
        this.p_email = p_email;
        this.p_address = p_address;
        this.p_lat = p_lat;
        this.p_long = p_long;
        this.p_credits = p_credits;
        this.p_active = p_active;
    }

    public Date getP_timestamp() {
        return p_timestamp;
    }

    public void setP_timestamp(Date p_timestamp) {
        this.p_timestamp = p_timestamp;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public String getP_nickname() {
        return p_nickname;
    }

    public void setP_nickname(String p_nickname) {
        this.p_nickname = p_nickname;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public String getP_lat() {
        return p_lat;
    }

    public void setP_lat(String p_lat) {
        this.p_lat = p_lat;
    }

    public String getP_long() {
        return p_long;
    }

    public void setP_long(String p_long) {
        this.p_long = p_long;
    }

    public String getP_credits() {
        return p_credits;
    }

    public void setP_credits(String p_credits) {
        this.p_credits = p_credits;
    }

    public String getP_active() {
        return p_active;
    }

    public void setP_active(String p_active) {
        this.p_active = p_active;
    }
}
