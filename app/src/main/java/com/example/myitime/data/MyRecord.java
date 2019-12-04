package com.example.myitime.data;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import java.io.Serializable;

public class MyRecord implements Serializable {
    private String title;
    private String note;
    private String time;
    private String repeat;
    private Bitmap bitmap;
    private String label;

    public MyRecord(String title) {
        this.title = title;
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
        this.bitmap = bitmap;
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
        return bitmap;
    }

    public String getLabel() {
        return label;
    }
}
