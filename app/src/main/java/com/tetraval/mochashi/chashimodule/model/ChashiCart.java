package com.tetraval.mochashi.chashimodule.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ChashiCart {
    @ServerTimestamp
    private Date cart_timestamp;
    private String cart_id;
    private String cart_product_id;
    private String cart_product_image;
    private String cart_item_name;
    private String cart_item_qty;
    private String cart_item_rate;
    private String cart_item_sub_total;
    private String cart_item_user_id;
    private String cart_chashi_user_id;

    public ChashiCart() {
    }

    public ChashiCart(Date cart_timestamp, String cart_id, String cart_product_id, String cart_product_image, String cart_item_name, String cart_item_qty, String cart_item_rate, String cart_item_sub_total, String cart_item_user_id, String cart_chashi_user_id) {
        this.cart_timestamp = cart_timestamp;
        this.cart_id = cart_id;
        this.cart_product_id = cart_product_id;
        this.cart_product_image = cart_product_image;
        this.cart_item_name = cart_item_name;
        this.cart_item_qty = cart_item_qty;
        this.cart_item_rate = cart_item_rate;
        this.cart_item_sub_total = cart_item_sub_total;
        this.cart_item_user_id = cart_item_user_id;
        this.cart_chashi_user_id = cart_chashi_user_id;
    }

    public Date getCart_timestamp() {
        return cart_timestamp;
    }

    public void setCart_timestamp(Date cart_timestamp) {
        this.cart_timestamp = cart_timestamp;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCart_product_id() {
        return cart_product_id;
    }

    public void setCart_product_id(String cart_product_id) {
        this.cart_product_id = cart_product_id;
    }

    public String getCart_product_image() {
        return cart_product_image;
    }

    public void setCart_product_image(String cart_product_image) {
        this.cart_product_image = cart_product_image;
    }

    public String getCart_item_name() {
        return cart_item_name;
    }

    public void setCart_item_name(String cart_item_name) {
        this.cart_item_name = cart_item_name;
    }

    public String getCart_item_qty() {
        return cart_item_qty;
    }

    public void setCart_item_qty(String cart_item_qty) {
        this.cart_item_qty = cart_item_qty;
    }

    public String getCart_item_rate() {
        return cart_item_rate;
    }

    public void setCart_item_rate(String cart_item_rate) {
        this.cart_item_rate = cart_item_rate;
    }

    public String getCart_item_sub_total() {
        return cart_item_sub_total;
    }

    public void setCart_item_sub_total(String cart_item_sub_total) {
        this.cart_item_sub_total = cart_item_sub_total;
    }

    public String getCart_item_user_id() {
        return cart_item_user_id;
    }

    public void setCart_item_user_id(String cart_item_user_id) {
        this.cart_item_user_id = cart_item_user_id;
    }

    public String getCart_chashi_user_id() {
        return cart_chashi_user_id;
    }

    public void setCart_chashi_user_id(String cart_chashi_user_id) {
        this.cart_chashi_user_id = cart_chashi_user_id;
    }
}