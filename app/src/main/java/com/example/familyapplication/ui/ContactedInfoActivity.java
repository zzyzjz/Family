package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familyapplication.Contacted;
import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;

public class ContactedInfoActivity extends AppCompatActivity {
    //备注展示页
    //在Uses表中得到该联系人的userId和head
    //在Contacts中得到当前用户对该联系人的备注
    //包括：name，birth，tel,callTime,inspectTime,remarks;

    private static final String TAG = "zzzzzjz-contacted info";

    private ImageView head;
    private TextView name,id,birth,tel,callTime,inspectTime,remarks;
    private Button modify,chat;
    private Contacts contact;
    private Contacted contacted;
    private String me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacted_info);
        //当前用户的userId
        me = EMClient.getInstance().getCurrentUser();
        //当前联系人
        contacted = new Contacted(getIntent().getExtras().getString("contactedId"),
                getIntent().getExtras().getString("name"),
                getIntent().getExtras().getInt("head"));

        //contact 为当前用户对该联系人的备注
        contact = ContactsBaseDao.searchByUserIdAndContactedId(me,contacted.getContactedId());
        //user为被选中联系人
//        user = UsersBaseDao.searchByUserId(contactedId);
        //设置头像
        head = findViewById(R.id.contacted_info_iv_head);
        head.setImageResource(contacted.getImage());

        //设置name,若修改，需显示修改后的，所以name要从数据库获取
        name = findViewById(R.id.contacted_info_tv_name);
        //初始化为当前联系人的userId
        String remarkName = contacted.getContactedId();

        if(ContactsBaseDao.searchByUserIdAndContactedId
                (EMClient.getInstance().getCurrentUser(),contacted.getContactedId()).getName() != null){
            //当前用户给该联系人设置了name时
            remarkName = ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),contacted.getContactedId()).getName();
        }else if(UsersBaseDao.searchByUserId(contacted.getContactedId()).getNickname() != null){
            //该联系人给自己设置了昵称
            remarkName = UsersBaseDao.searchByUserId(contacted.getContactedId()).getNickname();
        }
        name.setText(remarkName);

        //设置ID
        id = findViewById(R.id.contacted_info_tv_id);
        id.setText(contact.getContactedId());
        //设置生日
        birth = findViewById(R.id.contacted_info_tv_birth);
        birth.setText(contact.getBirthday());
        //设置TEL
        tel = findViewById(R.id.contacted_info_tv_tel);
        tel.setText(contact.getTel());
        //设置最近通话
        callTime = findViewById(R.id.contacted_info_tv_call);
        callTime.setText(contact.getLastCallTime());
        //设置最近体检
        inspectTime = findViewById(R.id.contacted_info_tv_inspect);
        inspectTime.setText(contact.getLastInspectTime());
        //设置备注板
        remarks = findViewById(R.id.contacted_info_tv_remarks);
        remarks.setText(contact.getRemarks());


        chat = findViewById(R.id.contacted_info_btn_chat);
        chat.setOnClickListener(new View.OnClickListener() {
            //跳转至聊天页面，所传值为当前联系人的userId
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ContactedInfoActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, contacted.getContactedId());
                Log.e(TAG, "----------82" );
                startActivity(intent);
                finish();
            }
        });

        modify = findViewById(R.id.contacted_info_btn_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            //跳转至修改备注页面
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ContactedInfoActivity.this, ModifyInfoActivity.class);
                //
                intent.putExtra("userId",contact.getUserId());
                intent.putExtra("contactedId",contact.getContactedId());
                startActivity(intent);
                Log.e(TAG, "-----------------101");
//                finish();
            }
        });



    }
}