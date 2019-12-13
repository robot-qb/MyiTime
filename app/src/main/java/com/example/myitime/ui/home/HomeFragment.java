package com.example.myitime.ui.home;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myitime.CreateActivity;
import com.example.myitime.EditActivity;
import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.MyRecord;
import com.example.myitime.model.NestedListView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static com.example.myitime.MainActivity.CLICK;
import static com.example.myitime.MainActivity.myRecords;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private TimeThread timeThread;
    private static final int COMPLETED = 0;
    private Handler handler;

    private MainActivity.MyRecordAdapter myRecordAdapter;
    private NestedListView list_view;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<View> mList;
    private int pos;


    public HomeFragment(){this.myRecordAdapter=MainActivity.getMyRecordAdapter();}


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        /*homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);

        /*homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        list_view=root.findViewById(R.id.list_view);
        viewPager=root.findViewById(R.id.viewPager);
        list_view.setAdapter(myRecordAdapter);


        init();
        myViewPagerAdapter=new MyViewPagerAdapter(mList);
        viewPager.setAdapter(myViewPagerAdapter);


        timeThread=new TimeThread();
        timeThread.start();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==COMPLETED){
                    myRecordAdapter.notifyDataSetChanged();
                    if(myRecords.size()!=myViewPagerAdapter.getCount()){
                        if(myRecords.size()>myViewPagerAdapter.getCount()){
                            myViewPagerAdapter.addList();
                            myViewPagerAdapter.notifyDataSetChanged();
                        }else{
                            myViewPagerAdapter.delList(pos);
                            myViewPagerAdapter.notifyDataSetChanged();
                        }
                    }
                    for(int i=0;i<myRecords.size();i++){
                        TextView textView=myViewPagerAdapter.getmLists().get(i).findViewById(R.id.text_remaining);
                        textView.setText(myRecords.get(i).getTimeRemainFull());
                    }
                }
            }
        };


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(getActivity(),EditActivity.class);
                MyRecord myRecord=(MyRecord)myRecordAdapter.getItem(position);
                intent.putExtra("record",myRecord);
                intent.putExtra("position",position);
                startActivityForResult(intent,CLICK);


            }
        });



        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        mList=new ArrayList<>();
        for(int i=0;i<myRecords.size();i++){
            View view=View.inflate(this.getContext(),R.layout.picture_round_layout,null);
            ConstraintLayout constraint_picture=view.findViewById(R.id.constraint_picture);
            TextView text_title=view.findViewById(R.id.text_title);
            TextView text_time=view.findViewById(R.id.text_time);
            TextView text_remaining=view.findViewById(R.id.text_remaining);

            constraint_picture.setBackground(new BitmapDrawable(myRecords.get(i).getBitmap()));
            text_title.setText(myRecords.get(i).getTitle());
            text_time.setText(myRecords.get(i).getTime());
            text_remaining.setText(myRecords.get(i).getTimeRemainFull());

            mList.add(view);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CLICK:

                if(resultCode==RESULT_CANCELED){
                    pos=data.getIntExtra("position",0);
                    MainActivity.myRecords.remove(pos);
                    myRecordAdapter.notifyDataSetChanged();

                }
                if(resultCode==RESULT_OK){
                    int position=data.getIntExtra("position",0);
                    MyRecord record=(MyRecord)data.getSerializableExtra("record");
                    MyRecord myRecord=(MyRecord)myRecordAdapter.getItem(position);
                    myRecord.setTitle(record.getTitle());
                    myRecord.setNote(record.getNote());
                    myRecord.setTime(record.getTime());
                    myRecord.setRepeat(record.getRepeat());
                    myRecord.setBitmap(record.getBitmap());
                    myRecord.setLabel(record.getLabel());
                    myRecordAdapter.notifyDataSetChanged();


                }
        }
    }


    private class MyViewPagerAdapter extends PagerAdapter {

        private ArrayList<View> mLists;
        public MyViewPagerAdapter(ArrayList<View> mList){
            this.mLists=mList;
        }
        public ArrayList<View> getmLists(){
            return mLists;
        }
        @Override
        public int getCount() {
            return mLists!=null?mLists.size():0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            container.addView(mLists.get(position));
            return mLists.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mLists.get(position));
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void addList(){
            View view = View.inflate(getContext(),R.layout.picture_round_layout,null);
            ConstraintLayout constraint_picture=view.findViewById(R.id.constraint_picture);
            TextView text_title=view.findViewById(R.id.text_title);
            TextView text_time=view.findViewById(R.id.text_time);
            TextView text_remaining=view.findViewById(R.id.text_remaining);

            constraint_picture.setBackground(new BitmapDrawable(myRecords.get(myRecords.size()-1).getBitmap()));
            text_title.setText(myRecords.get(myRecords.size()-1).getTitle());
            text_time.setText(myRecords.get(myRecords.size()-1).getTime());
            text_remaining.setText(myRecords.get(myRecords.size()-1).getTimeRemainFull());
            mLists.add(view);
        }
        public void delList(int pos){
            mLists.remove(pos);
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