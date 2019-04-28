package com.example.familyapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupActivity extends AppCompatActivity {
    private static final String TAG = "zzzzzjz--group";

    private Button create,join;
    private ListViewInScroll groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        create = findViewById(R.id.group_btn_creat);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        join = findViewById(R.id.group_btn_join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
//                    FmlGroupAdapter adapter = new FmlGroupAdapter(GroupActivity.this,R.layout.item_group,grouplist);
//
//                    ListView listView = findViewById(R.id.group_lv);
//                    listView.setAdapter(adapter);
//
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

        List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();
        FmlGroupAdapter adapter = new FmlGroupAdapter(GroupActivity.this,R.layout.item_group,grouplist);

        ListView listView = findViewById(R.id.group_lv);
        listView.setAdapter(adapter);
    }
}
