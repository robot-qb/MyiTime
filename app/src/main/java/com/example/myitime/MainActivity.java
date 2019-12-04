package com.example.myitime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.myitime.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ArrayList<MyRecord> myRecords;
    private static MyRecordAdapter myRecordAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myRecords= new ArrayList<>();
        MyRecord myRecord=new MyRecord("123");
        myRecord.setNote("note");
        myRecords.add(myRecord);
        myRecords.add(myRecord);
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

    public static MyRecordAdapter abc(){return myRecordAdapter;}
    public class MyRecordAdapter extends ArrayAdapter<MyRecord> {
        private int resourceId;
        public MyRecordAdapter(@NonNull Context context, int resource, @NonNull List<MyRecord> objects) {
            super(context, resource, objects);
            this.resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(this.getContext());
            View item=mInflater.inflate(this.resourceId,null);

            ImageView picture=item.findViewById(R.id.picture);
            TextView title_text_view=item.findViewById(R.id.title_text_view);
            TextView time_text_view=item.findViewById(R.id.time_text_view);
            TextView note_text_view=item.findViewById(R.id.note_text_view);

            MyRecord myRecord=(MyRecord)this.getItem(position);
            if(null==myRecord.getBitmap())
                picture.setImageResource(R.drawable.image);
            else
                picture.setImageBitmap(myRecord.getBitmap());
            title_text_view.setText(myRecord.getTitle());
            if(null==myRecord.getTime())
                time_text_view.setText("");
            else
                time_text_view.setText(myRecord.getTime());
            if(null==myRecord.getNote())
                note_text_view.setText("");
            else
                note_text_view.setText(myRecord.getNote());

            return item;
        }
    }
}
