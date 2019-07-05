package com.mycode.appforenp.models;

import android.graphics.Bitmap;

public class MyModel {

    private String headder,dis;
    private Bitmap bitmap;

    public MyModel(String headder, String dis, Bitmap bitmap) {
        this.headder = headder;
        this.dis = dis;
        this.bitmap = bitmap;
    }

    public String getHeadder() {
        return headder;
    }

    public void setHeadder(String headder) {
        this.headder = headder;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
