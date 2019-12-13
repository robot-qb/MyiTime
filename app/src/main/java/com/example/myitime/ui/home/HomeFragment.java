package com.example.myitime.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myitime.CreateActivity;
import com.example.myitime.EditActivity;
import com.example.myitime.MainActivity;
import com.example.myitime.R;
import com.example.myitime.data.MyRecord;
import com.example.myitime.model.NestedListView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static com.example.myitime.MainActivity.CLICK;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private MainActivity.MyRecordAdapter myRecordAdapter;
    private NestedListView list_view;


    public HomeFragment(){this.myRecordAdapter=MainActivity.getMyRecordAdapter();}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
        list_view.setAdapter(myRecordAdapter);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CLICK:

                if(resultCode==RESULT_CANCELED){
                    int position=data.getIntExtra("position",0);
                    MainActivity.myRecords.remove(position);
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


}