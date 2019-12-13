package com.example.myitime.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        if(this.repeat.equals("无"))
            return this.time;
        else if(this.repeat.equals("每年")){
            Calendar calendar=Calendar.getInstance();
            int year=calendar.get(Calendar.YEAR);
            time=year+time.substring(4);
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                Date now = new Date(System.currentTimeMillis());
                Date pre = df.parse(time);
                if(now.getTime()>pre.getTime())
                    return (year+1)+time.substring(4);
                else
                    return time;
            }catch (Exception e){}
        }else if(this.repeat.equals("每月")){
            Calendar calendar=Calendar.getInstance();
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            if(time.substring(6,7).equals("月"))
                time=year+"年"+month+time.substring(6);
            else
                time=year+"年"+month+time.substring(7);
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                Date now = new Date(System.currentTimeMillis());
                Date pre = df.parse(time);
                if(now.getTime()>pre.getTime()){
                    if(time.substring(6,7).equals("月"))
                        return year+"年"+(month+1)+time.substring(6);
                    else{
                        if(month<12)
                            return year+"年"+(month+1)+time.substring(7);
                        else
                            return (year+1)+"年"+1+time.substring(7);
                    }
                } else
                    return time;
            }catch (Exception e){}
        }else{
            int days;
            if(this.repeat.equals("每周"))
                days=7;
            else
                days=Integer.parseInt(repeat.substring(0,repeat.length()-1));
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                Date now = new Date(System.currentTimeMillis());
                Date pre = df.parse(time);
                if(now.getTime()>pre.getTime()){
                    while(now.getTime()>pre.getTime()){
                        pre=new Date(pre.getTime()+(long)days*24*60*60*1000);
                    }
                    time=df.format(pre);
                    return time;
                }
                else{
                    while(now.getTime()<pre.getTime()){
                        pre=new Date(pre.getTime()-(long)days*24*60*60*1000);
                    }
                    pre=new Date(pre.getTime()+(long)days*24*60*60*1000);
                    time=df.format(pre);
                    return time;
                }
            }catch (Exception e){}
        }
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

    public String getTimeRemaining(){
        String text="";
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            Date now = new Date(System.currentTimeMillis());
            Date pre = df.parse(time);
            long diff;
            if(this.repeat.equals("无"))
                diff = now.getTime()-pre.getTime();
            else
                diff = pre.getTime()-now.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
            if(this.repeat.equals("无")){
                if(diff>0) {
                    if (days > 0) {
                        text = "已经" + days + "天";
                    } else if (hours > 0) {
                        text = "已经" + hours + "小时";
                    } else if (minutes > 0) {
                        text = "已经" + minutes + "分";
                    } else {
                        text = "已经" + seconds + "秒";
                    }
                }else {
                    if(days<0){
                        text = "还有" + (0-days) + "天";
                    }else if (hours < 0) {
                        text = "还有" + (0-hours) + "小时";
                    } else if (minutes < 0) {
                        text = "还有" + (0-minutes) + "分";
                    } else {
                        text = "还有" + (0-seconds) + "秒";
                    }
                }
            }
            else {
                if (days > 0) {
                    text = "还有" + days + "天";
                } else if (hours > 0) {
                    text = "还有" + hours + "小时";
                } else if (minutes > 0) {
                    text = "还有" + minutes + "分";
                } else {
                    text = "还有" + seconds + "秒";
                }
            }
        }catch (Exception e)
        {

        }
        return text;
    }
}
