package com.example.myitime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myitime.data.MyRecord;

import static com.example.myitime.MainActivity.EDIT;

public class EditActivity extends AppCompatActivity {

    private static final int COMPLETED = 0;
    private TimeThread timeThread;
    private Handler handler;

    private ConstraintLayout constraint_layout;
    private ImageButton fanhui_button,delete_button,edit_button;
    private TextView edit_title_text,edit_time_text,edit_time2_text;
    private MyRecord myRecord;
    private int position;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();

        }

        constraint_layout=this.findViewById(R.id.constraint_layout);
        fanhui_button=this.findViewById(R.id.fanhui_button);
        delete_button=this.findViewById(R.id.delete_button);
        edit_button=this.findViewById(R.id.edit_button);
        edit_title_text=this.findViewById(R.id.edit_title_text);
        edit_time_text=this.findViewById(R.id.edit_time_text);
        edit_time2_text=this.findViewById(R.id.edit_time2_text);

        myRecord=(MyRecord)getIntent().getSerializableExtra("record");
        position=getIntent().getIntExtra("position",0);
        Drawable drawable=new BitmapDrawable(myRecord.getBitmap());
        constraint_layout.setBackground(drawable);
        edit_title_text.setText(myRecord.getTitle());
        edit_time_text.setText(myRecord.getTime());

        fanhui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeThread.stopThread();
                Intent intent = new Intent();
                intent.putExtra("record", myRecord);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                EditActivity.this.finish();
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_CANCELED,intent);
                EditActivity.this.finish();
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditActivity.this,CreateActivity.class);
                intent.putExtra("record",myRecord);
                startActivityForResult(intent,EDIT);
            }
        });
        timeThread=new TimeThread();
        timeThread.start();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==COMPLETED){
                    edit_time2_text.setText(myRecord.getTimeRemaining());
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EDIT:
                if(resultCode==RESULT_OK) {
                    myRecord = (MyRecord)data.getSerializableExtra("record");
                    edit_title_text.setText(myRecord.getTitle());
                    edit_time_text.setText(myRecord.getTime());
                }
        }
    }

    private class TimeThread extends Thread{
        private Boolean beAlive=false;
        public void run(){
            beAlive=true;
            while (beAlive){
                try{
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler.sendMessage(msg);
                }catch(InterruptedException e){e.printStackTrace();}
            }
        }
        public void stopThread(){
            beAlive=false;
            while(true){
                try{
                    this.join();
                    break;
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
