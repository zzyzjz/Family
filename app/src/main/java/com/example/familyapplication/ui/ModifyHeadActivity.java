package com.example.familyapplication.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.example.familyapplication.list.GridViewInScroll;
import com.example.familyapplication.R;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class ModifyHeadActivity extends AppCompatActivity {

    private static final String TAG = "zzzzzjz---modify head";

    private Users user;

    private GridViewInScroll headGrid;
    private Button modify;
    List<Integer> headList = new ArrayList<Integer>();



    HeadAdapter adapter;

    public int checked = -1;
//    Drawable border = getResources().getDrawable(R.drawable.border_dark);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_head);

        user = UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser());

        modify = findViewById(R.id.modify_head_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //TODO:改头像 用GridViewInScroll
        initHead();
        adapter = new HeadAdapter(ModifyHeadActivity.this,R.layout.item_head,headList);
        headGrid = findViewById(R.id.modify_head_grid_image);
        headGrid.setAdapter(adapter);

//        initMembers();
//        memberAdapter = new MemberAdapter(
//                GroupDetailActivity.this,R.layout.item_grid,memberList);
//        members = findViewById(R.id.group_detail_members);
//        members.setAdapter(memberAdapter);
    }

    private class HeadAdapter extends ArrayAdapter {
        private int res;

        public HeadAdapter(Context context, int resource, List<Integer> headList) {
            super(context, resource,headList);
            res = resource;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Integer h = (Integer) getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(res,null);
            final ImageView i = view.findViewById(R.id.iv_head);
            i.setImageResource(h);
            if(position == checked){
//                i.setBackground(border);
                i.setBackgroundResource(R.drawable.border_dark);
            }

            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checked = position;
//                    i.setBackgroundResource(R.drawable.border_dark);
                    user.setHead(h);
                    UsersBaseDao.update(user);

                }
            });

            return view;
        }

//        Contacted contacted = (Contacted) getItem(position);
//            Log.e(TAG, "--------344----------contacted"+contacted.getName() );
//        View view = LayoutInflater.from(getContext()).inflate(res, null);//实例化一个对象
//        ImageView head = view.findViewById(R.id.iv_avatar);//获取该布局内的图片视图
//        TextView name = view.findViewById(R.id.tv_name);//获取该布局内的文本视图
//            head.setImageResource(contacted.getImage());
//            name.setText(contacted.getName());
//            return view;

    }

    public void initHead(){

        headList.add(R.drawable.h34);
        headList.add(R.drawable.h21);
        headList.add(R.drawable.h24);
        headList.add(R.drawable.h25);
        headList.add(R.drawable.h23);
        headList.add(R.drawable.h15);

        headList.add(R.drawable.h1);
        headList.add(R.drawable.h2);
        headList.add(R.drawable.h7);
        headList.add(R.drawable.h13);
        headList.add(R.drawable.h16);
        headList.add(R.drawable.h29);
        headList.add(R.drawable.h30);

//
//        //----------------------------
//        headList.add(R.drawable.head11);
//        headList.add(R.drawable.head12);
//        headList.add(R.drawable.head21);
//        headList.add(R.drawable.head22);
//        headList.add(R.drawable.head31);
//        headList.add(R.drawable.head32);
//        headList.add(R.drawable.head41);
//        headList.add(R.drawable.head42);
        headList.add(R.drawable.head51);
//        headList.add(R.drawable.head52);
        headList.add(R.drawable.head61);
//        headList.add(R.drawable.head62);
//        headList.add(R.drawable.head71);
//        headList.add(R.drawable.head72);
//        headList.add(R.drawable.head81);
//        headList.add(R.drawable.head82);
        headList.add(R.drawable.head91);
//        headList.add(R.drawable.head92);

        headList.add(R.drawable.head);

    }


}
