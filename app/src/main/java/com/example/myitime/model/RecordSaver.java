package com.example.myitime.model;

import android.content.Context;

import com.example.myitime.data.MyRecord;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RecordSaver {
    private Context context;
    private ArrayList<MyRecord> myRecords=new ArrayList<>();

    public RecordSaver(Context context){this.context=context;}
    public void save(){
        try{

            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable_record.txt", Context.MODE_PRIVATE));
            outputStream.writeObject(myRecords);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }
    public ArrayList<MyRecord> load(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable_record.txt"));
            myRecords = (ArrayList<MyRecord>) inputStream.readObject();
            inputStream.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return myRecords;
    }

}
