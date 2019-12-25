package com.example.myitime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import cn.lankton.flowlayout.FlowLayout;

public class CreateActivity extends AppCompatActivity {
    private String text;
    private MyRecord myRecord;

    public static final int TAKE_PHOTO = 300;
    public static final int CHOOSE_PHOTO = 301;
    private Uri imageUri;
    private Bitmap bitmap;

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

        return_button=findViewById(R.id.return_button);
        yes_button=findViewById(R.id.yes_button);

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
                View view=photoDialog();
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
                    if(time_text.getText().toString().equals("")){

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                        Date date=new Date(System.currentTimeMillis());
                        myRecord.setTime(simpleDateFormat.format(date));

                    }else {
                        myRecord.setTime(time_text.getText().toString()+":00");
                    }
                    if(repeat_text.getText().toString().equals("0天"))
                        myRecord.setRepeat("无");
                    else
                        myRecord.setRepeat(repeat_text.getText().toString());
                    myRecord.setLabel(label_text.getText().toString());

                    if(bitmap!=null){
                        myRecord.setBitmap(bitmap);
                    }else {
                        Resources res = getResources();
                        Random random = new Random();
                        int a = random.nextInt(4)+1;
                        if(a==1)
                            bitmap = BitmapFactory.decodeResource(res, R.drawable.random1);
                        else if(a==2)
                            bitmap = BitmapFactory.decodeResource(res, R.drawable.random2);
                        else if(a==3)
                            bitmap = BitmapFactory.decodeResource(res, R.drawable.random3);
                        else
                            bitmap = BitmapFactory.decodeResource(res, R.drawable.random4);
                        myRecord.setBitmap(bitmap);
                    }



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
            bitmap=myRecord.getBitmap();
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

    //以下是选择图片的代码
    public View photoDialog(){
        View view=View.inflate(CreateActivity.this,R.layout.create_select_photo_layout,null);
        ImageView select_photo,take_photo;
        select_photo=view.findViewById(R.id.select_photo);
        take_photo=view.findViewById(R.id.take_photo);
        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else{
                    openAlbum();
                }
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //android 7.0版本以下的系统，直接Uri.fromFile取得真实文件路径；7.0及以上版本的系统，使用fileprovider封装过的Uri再提供出去。
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(CreateActivity.this, "com.example.myitime.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);   //启动Intent活动，拍完照会有结果返回到onActivityResult()方法中。

            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this, "You denied the permision.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        constraintLayout.setBackground(new BitmapDrawable(bitmap));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handeleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
//    Toast.makeText(this,"到了handleImageOnKitKat(Intent data)方法了", Toast.LENGTH_LONG).show();
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri)){
            //如果是 document 类型的 Uri，则通过 document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的 id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是 content 类型的 uri ， 则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是 file 类型的 Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//显示选中的图片
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void handeleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过 Uri 和 selection 来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayImage(String imagePath) {
        if(imagePath != null){
            bitmap = BitmapFactory.decodeFile(imagePath);
            constraintLayout.setBackground(new BitmapDrawable(bitmap));
        }else{
            Toast.makeText(this,"failed to get image", Toast.LENGTH_LONG).show();
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
