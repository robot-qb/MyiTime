package com.example.myitime.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myitime.MainActivity;
import com.example.myitime.R;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    private MainActivity.MyRecordAdapter myRecordAdapter;
    private ListView list_view;

    public HomeFragment(){this.myRecordAdapter=MainActivity.abc();}
    public HomeFragment(MainActivity.MyRecordAdapter myRecordAdapter){
        this.myRecordAdapter=myRecordAdapter;

    }
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
        return root;
    }
}