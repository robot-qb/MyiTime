package com.example.myitime.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class MyRecord implements Serializable {
    private String title;
    private String note;
    private String time;
    private String repeat;
    private byte[] bitmap;
    private String label;

    public MyRecord() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        this.bitmap=baos.toByteArray();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getTime() {
        return time;
    }

    public String getRepeat() {
        return repeat;
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(this.bitmap,0,this.bitmap.length);
    }

    public String getLabel() {
        return label;
    }
}
