package com.tetraval.mochashi.genericmodule.model;

public class OnBoardModel {
    int ob_image;
    String ob_title;
    String ob_description;

    public OnBoardModel() {
    }

    public void setOb_image(int ob_image) {
        this.ob_image = ob_image;
    }

    public void setOb_title(String ob_title) {
        this.ob_title = ob_title;
    }

    public void setOb_description(String ob_description) {
        this.ob_description = ob_description;
    }

    public int getOb_image() {
        return ob_image;
    }

    public String getOb_title() {
        return ob_title;
    }

    public String getOb_description() {
        return ob_description;
    }
}
