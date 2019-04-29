package com.example.familyapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.familyapplication.CreateGroupActivity;
import com.example.familyapplication.FmlGroupAdapter;
import com.example.familyapplication.JoinGroupActivity;
import com.example.familyapplication.ListViewInScroll;
import com.example.familyapplication.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseBaseActivity;

import java.util.List;

public class GroupActivity extends FragmentActivity {
    private static final String TAG = "zzzzzjz--group";

    protected InputMethodManager inputMethodManager;

    private Button create,join;
    private ListViewInScroll groupList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        create = findViewById(R.id.group_btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, CreateGroupActivity.class));
            }
        });

        join = findViewById(R.id.group_btn_join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, JoinGroupActivity.class));
            }
        });

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
                    //异步方法
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

        //同步方法
        final List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();
        final FmlGroupAdapter adapter = new FmlGroupAdapter(GroupActivity.this,R.layout.item_group,grouplist);

        final ListView listView = findViewById(R.id.group_lv);
        listView.setAdapter(adapter);

        //群组的item点击事件，点击跳转到chat
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMGroup group = (EMGroup) grouplist.get(position);
                Intent i = new Intent(
                        GroupActivity.this,ChatActivity.class);
                //传group的id
                i.putExtra("userId",group.getGroupId());
                //传chat的类型为群聊
                i.putExtra("chatType",EaseConstant.CHATTYPE_GROUP);
                Log.e(TAG, "----------83-----group chat" );
                startActivity(i);

            }
        });


    }






    @Override
    protected void onResume() {

        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();
    }

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * back
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
