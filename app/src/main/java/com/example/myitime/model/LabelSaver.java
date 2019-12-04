package com.example.myitime.model;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LabelSaver {
    Context context;
    ArrayList<String> labels=new ArrayList<String>();

    public LabelSaver(Context context) {
        this.context = context;
    }
    public void save(){
        try{

            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt", Context.MODE_PRIVATE));
            outputStream.writeObject(labels);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }
    public ArrayList<String> load(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            labels = (ArrayList<String>) inputStream.readObject();
            inputStream.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return labels;
    }
}