package com.example.myitime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.example.myitime.data.MyRecord;
import com.example.myitime.model.RecordSaver;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_RECORD=900;
    public static final int CLICK=901;
    public static final int EDIT=902;


    private AppBarConfiguration mAppBarConfiguration;
    public static ArrayList<MyRecord> myRecords;
    private RecordSaver recordSaver;
    private static MyRecordAdapter myRecordAdapter;




    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordSaver.save();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        recordSaver=new RecordSaver(this);
        myRecords=recordSaver.load();
        myRecordAdapter=new MyRecordAdapter(this,R.layout.linearlayout,myRecords);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

       // FragmentManager fragmentManager=getSupportFragmentManager();
       // fragmentManager.beginTransaction().replace(R.id.nav_home, new HomeFragment(myRecordAdapter)).commit();



        FloatingActionButton add_button=findViewById(R.id.fab);
        add_button.bringToFront();
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CreateActivity.class);
                startActivityForResult(intent,ADD_RECORD);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_RECORD:
                if(resultCode==RESULT_OK) {
                    MyRecord myRecord = (MyRecord) data.getSerializableExtra("record");
                    myRecords.add(myRecord);
                    myRecordAdapter.notifyDataSetChanged();
                }
                break;

        }
    }

    public static MyRecordAdapter getMyRecordAdapter(){return myRecordAdapter;}

    public class MyRecordAdapter extends ArrayAdapter<MyRecord> {
        private int resourceId;
        public MyRecordAdapter(@NonNull Context context, int resource, @NonNull List<MyRecord> objects) {
            super(context, resource, objects);
            this.resourceId=resource;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(this.getContext());
            View item=mInflater.inflate(this.resourceId,null);

            //ImageView picture=item.findViewById(R.id.picture);
            ConstraintLayout constraint_iamge=item.findViewById(R.id.constraint_image);
            TextView daojishi=item.findViewById(R.id.daojishi);
            TextView title_text_view=item.findViewById(R.id.title_text_view);
            TextView time_text_view=item.findViewById(R.id.time_text_view);
            TextView note_text_view=item.findViewById(R.id.note_text_view);

            MyRecord myRecord=(MyRecord)this.getItem(position);
            if(null==myRecord.getBitmap())
                constraint_iamge.setBackgroundResource(R.drawable.image);
            else
                constraint_iamge.setBackground(new BitmapDrawable(myRecord.getBitmap()));

            daojishi.setText(myRecord.getTimeRemaining());

            title_text_view.setText(myRecord.getTitle());

            time_text_view.setText(myRecord.getTime());

            note_text_view.setText(myRecord.getNote());

            return item;
        }
    }

    /*public class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            // TODO Auto-generated method stub
            return view == obj;
        }

        /**
         * 实例化视图内容（创建要显示的内容）
         */
       /* @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(imgList.get(position));
            return imgList.get(position);
        }

        /**
         * 销毁视图内容
         */
       /* @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }
    */
}

