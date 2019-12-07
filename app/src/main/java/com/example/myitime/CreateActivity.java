package com.example.myitime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myitime.data.MyRecord;
import com.example.myitime.model.LabelSaver;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.lib.settingview.LSettingItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import cn.lankton.flowlayout.FlowLayout;

public class CreateActivity extends AppCompatActivity {
    private String text;
    private MyRecord myRecord;

    public static final int IMAGE_REQUEST_CODE=900;
    public static final int RESIZE_REQUEST_CODE=901;
    private ImageButton yes_button,return_button;
    private EditText title_edit,note_edit;
    private LSettingItem item_one,item_two,item_three,item_four;
    private TextView time_text,repeat_text,label_text;
    private ConstraintLayout constraintLayout;

    //标签
    private ArrayList<String> labels;
    private FlowLayout flowLayout;
    private LabelSaver labelSaver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        labelSaver.save();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();

        }

        //设置背景图片的linearLayout
        constraintLayout=findViewById(R.id.constraintLayout);

        //添加标签内容
        labelSaver=new LabelSaver(this);
        labels=labelSaver.load();
        if(labels.size()==0) {
            labels.add("生日");
            labels.add("学习");
            labels.add("工作");
            labels.add("节假日");
            labels.add("吐槽");
            labels.add("一二三四五六七八");
        }


        item_one=findViewById(R.id.item_one);
        item_two=findViewById(R.id.item_two);
        item_three=findViewById(R.id.item_three);
        item_four=findViewById(R.id.item_four);

        title_edit=findViewById(R.id.title_edit);
        note_edit=findViewById(R.id.note_edit);

        time_text=findViewById(R.id.time_text);
        repeat_text=findViewById(R.id.repeat_text);
        label_text=findViewById(R.id.label_text);
        item_one.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void click() {
                showDateDialog();

            }
        });
        item_two.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                final String[] items3 = new String[]{"每周", "每月", "每年", "自定义","无"};//创建item
                AlertDialog alertDialog3 = new AlertDialog.Builder(CreateActivity.this)
                        .setTitle("周期")
                        .setItems(items3, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i!=3)
                                    repeat_text.setText(items3[i]);
                                else
                                {
                                    final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(CreateActivity.this);
                                    View view1 = View.inflate(CreateActivity.this, R.layout.create_alter_dialog_view_layout, null);
                                    final EditText define_by_self = view1.findViewById(R.id.define_by_self);
                                    alertDialog7
                                            .setTitle("周期")
                                            .setView(view1)
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            })
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    repeat_text.setText(define_by_self.getText()+"天");
                                                }
                                            })
                                            .create();
                                    alertDialog7.show();


                                }
                            }
                        })
                        .create();
                alertDialog3.show();


            }
        });
        item_three.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void click() {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(CreateActivity.this);
                View view=View.inflate(CreateActivity.this,R.layout.create_select_photo_layout,null);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,IMAGE_REQUEST_CODE);

                    }
                });
                alertDialog
                        .setTitle("选择图片")
                        .setView(view)
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });
        item_four.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                View view = View.inflate(CreateActivity.this,R.layout.label_flow_layout,null);
                flowLayout=view.findViewById(R.id.flow_layout);
                for (int i = 0; i < labels.size(); i++) {
                    View view1 = View.inflate(CreateActivity.this, R.layout.label_son_layout, null);
                    TextView tv = (TextView) view1.findViewById(R.id.son_label);
                    tv.setText(labels.get(i));
                    tv.setTag(i);
                    view1.setTag(false);
                    // 设置view的点击事件，与onClick中的View一致
                    //否则需要在onClick中，去findViewById，找出设置点击事件的控件进行操作
                    //若不如此，则无法触发点击事件
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            TextView tv = (TextView) v.findViewById(R.id.son_label);

                            if ((Boolean) v.getTag()) {
                                v.setTag(false);
                                tv.setEnabled(false);

                            } else {
                                v.setTag(true);
                                tv.setEnabled(true);
                                if(label_text.getText()!="")
                                    text=text+","+tv.getText().toString();
                                    //label_text.setText(label_text.getText()+","+tv.getText().toString());
                                else
                                    text="已选："+tv.getText().toString();
                                //label_text.setText("已选："+tv.getText().toString());
                            }
                        }
                    });
                    flowLayout.addView(view1);
                }
                AlertDialog.Builder alterDialog=new AlertDialog.Builder(CreateActivity.this);
                alterDialog
                        .setTitle("标签")
                        .setView(view)
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                label_text.setText("");

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                label_text.setText(text);

                            }
                        })
                        .setNeutralButton("添加新标签", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder alterDialog2=new AlertDialog.Builder(CreateActivity.this);
                                View view1 = View.inflate(CreateActivity.this, R.layout.create_add_new_label_layout, null);
                                final EditText new_label = view1.findViewById(R.id.new_label);
                                alterDialog2
                                        .setTitle("添加标签")
                                        .setView(view1)
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                labels.add(new_label.getText().toString());
                                            }
                                        })
                                        .create();
                                alterDialog2.show();


                            }
                        })
                        .create()
                        .show();


            }
        });

        return_button=findViewById(R.id.return_button);
        yes_button=findViewById(R.id.yes_button);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivity.this.finish();
            }
        });
        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(title_edit.getText().toString().trim()))
                    Toast.makeText(CreateActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                else{
                    myRecord=new MyRecord();
                    myRecord.setTitle(title_edit.getText().toString());
                    myRecord.setNote(note_edit.getText().toString());
                    myRecord.setTime(time_text.getText().toString());
                    myRecord.setRepeat(repeat_text.getText().toString());
                    myRecord.setLabel(label_text.getText().toString());

                    Resources res=getResources();
                    Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.label);
                    myRecord.setBitmap(bmp);

                    Intent intent=new Intent();
                    intent.putExtra("record",myRecord);
                    setResult(RESULT_OK,intent);
                    CreateActivity.this.finish();
                }


            }
        });



        if((myRecord=(MyRecord) getIntent().getSerializableExtra("record"))!=null){
            title_edit.setText(myRecord.getTitle());
            note_edit.setText(myRecord.getNote());
            time_text.setText(myRecord.getTime());
            repeat_text.setText(myRecord.getRepeat());
            label_text.setText(myRecord.getLabel());
            Drawable drawable=new BitmapDrawable(myRecord.getBitmap());
            constraintLayout.setBackground(drawable);
        }

    }
    public void showDateDialog(){
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time_text.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                showTimeDialog();

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
    public void showTimeDialog(){
        Calendar calendar=Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time_text.setText(time_text.getText().toString()+"  "+hourOfDay+":"+minute);
            }
        },calendar.get(Calendar.HOUR)+8,calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    //获取图片路径
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;

                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //这里增加裁剪
    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪的大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        //设置返回码
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //裁剪之后设置保存图片的路径



            Drawable drawable = new BitmapDrawable(photo);
            constraintLayout.setBackground(drawable);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);

    }



}
