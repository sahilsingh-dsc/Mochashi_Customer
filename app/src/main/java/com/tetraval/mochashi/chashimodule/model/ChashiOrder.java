package com.tetraval.mochashi.chashimodule.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class ChashiOrder {
    @ServerTimestamp
    private Date order_timestamp;
    private String order_id;
    private String order_product;
    private String order_product_image;
    private String order_quantity;
    private String order_rate;
    private String order_shipping;

    private String order_pickup_address;
    private String order_delivery_address;

    private String order_chashi_name;
    private String order_chashi_id;
    private String order_chashi_amount;

    private String order_customer_name;
    private String order_customer_id;
    private String order_customer_amount;

    private String order_status;

    public ChashiOrder() {
    }

    public ChashiOrder(Date order_timestamp, String order_id, String order_product, String order_product_image, String order_quantity, String order_rate, String order_shipping, String order_pickup_address, String order_delivery_address, String order_chashi_name, String order_chashi_id, String order_chashi_amount, String order_customer_name, String order_customer_id, String order_customer_amount, String order_status) {
        this.order_timestamp = order_timestamp;
        this.order_id = order_id;
        this.order_product = order_product;
        this.order_product_image = order_product_image;
        this.order_quantity = order_quantity;
        this.order_rate = order_rate;
        this.order_shipping = order_shipping;
        this.order_pickup_address = order_pickup_address;
        this.order_delivery_address = order_delivery_address;
        this.order_chashi_name = order_chashi_name;
        this.order_chashi_id = order_chashi_id;
        this.order_chashi_amount = order_chashi_amount;
        this.order_customer_name = order_customer_name;
        this.order_customer_id = order_customer_id;
        this.order_customer_amount = order_customer_amount;
        this.order_status = order_status;
    }

    public Date getOrder_timestamp() {
        return order_timestamp;
    }

    public void setOrder_timestamp(Date order_timestamp) {
        this.order_timestamp = order_timestamp;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_product() {
        return order_product;
    }

    public void setOrder_product(String order_product) {
        this.order_product = order_product;
    }

    public String getOrder_product_image() {
        return order_product_image;
    }

    public void setOrder_product_image(String order_product_image) {
        this.order_product_image = order_product_image;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(String order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getOrder_rate() {
        return order_rate;
    }

    public void setOrder_rate(String order_rate) {
        this.order_rate = order_rate;
    }

    public String getOrder_shipping() {
        return order_shipping;
    }

    public void setOrder_shipping(String order_shipping) {
        this.order_shipping = order_shipping;
    }

    public String getOrder_pickup_address() {
        return order_pickup_address;
    }

    public void setOrder_pickup_address(String order_pickup_address) {
        this.order_pickup_address = order_pickup_address;
    }

    public String getOrder_delivery_address() {
        return order_delivery_address;
    }

    public void setOrder_delivery_address(String order_delivery_address) {
        this.order_delivery_address = order_delivery_address;
    }

    public String getOrder_chashi_name() {
        return order_chashi_name;
    }

    public void setOrder_chashi_name(String order_chashi_name) {
        this.order_chashi_name = order_chashi_name;
    }

    public String getOrder_chashi_id() {
        return order_chashi_id;
    }

    public void setOrder_chashi_id(String order_chashi_id) {
        this.order_chashi_id = order_chashi_id;
    }

    public String getOrder_chashi_amount() {
        return order_chashi_amount;
    }

    public void setOrder_chashi_amount(String order_chashi_amount) {
        this.order_chashi_amount = order_chashi_amount;
    }

    public String getOrder_customer_name() {
        return order_customer_name;
    }

    public void setOrder_customer_name(String order_customer_name) {
        this.order_customer_name = order_customer_name;
    }

    public String getOrder_customer_id() {
        return order_customer_id;
    }

    public void setOrder_customer_id(String order_customer_id) {
        this.order_customer_id = order_customer_id;
    }

    public String getOrder_customer_amount() {
        return order_customer_amount;
    }

    public void setOrder_customer_amount(String order_customer_amount) {
        this.order_customer_amount = order_customer_amount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
