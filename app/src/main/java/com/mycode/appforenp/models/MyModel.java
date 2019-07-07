package com.mycode.appforenp.models;

import android.graphics.Bitmap;

public class MyModel {

    private String headder,dis;
    private byte[] bitmap;

    public MyModel(String headder, String dis, byte[] bitmap) {
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

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }
}
