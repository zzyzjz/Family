package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.UsersBaseDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemindActivity extends AppCompatActivity {

    private String TAG = "zzzzzjz-Remind";

    private String pattern = "yyyy-MM-dd";

    private Contacts contacts;
    private String userId,contactedId;

    private TextView title;
    private TextView birth,beforeBirth,birthRemarks;
    private TextView call,passCall,callRemarks;
    private TextView inspect,passInspect,inspectRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        title = findViewById(R.id.remind_title);
        title.setText(getName(userId,contactedId));
        //-------------------------birth---------------------
        birth = findViewById(R.id.remind_birth);
        if (contacts.getBirthday() == null || TextUtils.isEmpty(contacts.getBirthday())){
            birth.setText("点击设置生日");
        }else {
            birth.setText(contacts.getBirthday());

            int beforeBirth_ = getBeforeBirth(contacts.getBirthday());
            beforeBirth = findViewById(R.id.remind_before_birth);
            beforeBirth.setText("距离生日还有"+beforeBirth_+"天");
            if(beforeBirth_ <= 10){
                beforeBirth.setBackgroundColor(
                        ContextCompat.getColor(this,R.color.colorPrimary));
            }
        }
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改生日页面
                Intent i = new Intent(RemindActivity.this,ModifyBirthActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        birthRemarks = findViewById(R.id.remind_birth_remarks);
        if (contacts.getRemarkForBirth() == null || TextUtils.isEmpty(contacts.getRemarkForBirth())){
            birthRemarks.setText("点击设置生日备注");
        }else {
            birthRemarks.setText(contacts.getRemarkForBirth());
        }
        birthRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RemindActivity.this,ModifyBirthActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        //-----------------------call--------------------------
        call = findViewById(R.id.remind_call);
        if (contacts.getLastCallTime() == null || TextUtils.isEmpty(contacts.getLastCallTime())){
            call.setText("点击设置最近通话日期");
        }else {
            call.setText(contacts.getLastCallTime());

            int passCall_ = getPassCall(contacts.getLastCallTime());
            passCall = findViewById(R.id.remind_pass_call);
            passCall.setText("距离上次通话 "+passCall_+"天");
            if(passCall_ >= 10){
                passCall.setBackgroundColor(
                        ContextCompat.getColor(RemindActivity.this,R.color.colorPrimary));
            }
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改通话信息页面
                Intent i = new Intent(RemindActivity.this,ModifyCallActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        callRemarks = findViewById(R.id.remind_call_remarks);
        if (contacts.getRemarkForCall() == null || TextUtils.isEmpty(contacts.getRemarkForCall())){
            callRemarks.setText("点击设置通话备注");
        }else {
            callRemarks.setText(contacts.getRemarkForCall());
        }
        callRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改通话信息页面
                Intent i = new Intent(RemindActivity.this,ModifyCallActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        //-----------------------inspect-----------------------
        inspect = findViewById(R.id.remind_inspect);
        if(contacts.getLastInspectTime() == null || TextUtils.isEmpty(contacts.getLastInspectTime())){
            inspect.setText("点击设置最近体检日期");
        }else {
            inspect.setText(contacts.getLastInspectTime());

            int passInspect_ = getPassInspect(contacts.getLastInspectTime());

            passInspect = findViewById(R.id.remind_pass_inspect);
            passInspect.setText("距离上次体检 "+passInspect_+"天");
            if(passInspect_ > 365){
                passInspect.setBackgroundColor(
                        ContextCompat.getColor(RemindActivity.this,R.color.colorPrimary));
            }
        }
        inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改体检信息页面
                Intent i = new Intent(RemindActivity.this,ModifyInspectActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        inspectRemarks = findViewById(R.id.remind_inspect_remarks);
        if (contacts.getRemarkForInspect() == null || TextUtils.isEmpty(contacts.getRemarkForInspect())){
            inspectRemarks.setText("点击设置体检备注");
        }else {
            inspectRemarks.setText(contacts.getRemarkForInspect());
        }
        inspectRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改体检信息页面
                Intent i = new Intent(RemindActivity.this,ModifyInspectActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //-------------------------birth---------------------
        birth = findViewById(R.id.remind_birth);
        if (contacts.getBirthday() == null || TextUtils.isEmpty(contacts.getBirthday())){
            birth.setText("点击设置生日");
        }else {
            birth.setText(contacts.getBirthday());

            int beforeBirth_ = getBeforeBirth(contacts.getBirthday());
            beforeBirth = findViewById(R.id.remind_before_birth);
            beforeBirth.setText("距离生日还有"+beforeBirth_+"天");
            if(beforeBirth_ <= 10){
                beforeBirth.setBackgroundColor(
                        ContextCompat.getColor(this,R.color.colorPrimary));
            }
        }
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改生日页面
                Intent i = new Intent(RemindActivity.this,ModifyBirthActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        birthRemarks = findViewById(R.id.remind_birth_remarks);
        if (contacts.getRemarkForBirth() == null || TextUtils.isEmpty(contacts.getRemarkForBirth())){
            birthRemarks.setText("点击设置生日备注");
        }else {
            birthRemarks.setText(contacts.getRemarkForBirth());
        }
        birthRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RemindActivity.this,ModifyBirthActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        //-----------------------call--------------------------
        call = findViewById(R.id.remind_call);
        if (contacts.getLastCallTime() == null || TextUtils.isEmpty(contacts.getLastCallTime())){
            call.setText("点击设置最近通话日期");
        }else {
            call.setText(contacts.getLastCallTime());

            int passCall_ = getPassCall(contacts.getLastCallTime());
            passCall = findViewById(R.id.remind_pass_call);
            passCall.setText("距离上次通话 "+passCall_+"天");
            if(passCall_ >= 10){
                passCall.setBackgroundColor(
                        ContextCompat.getColor(RemindActivity.this,R.color.colorPrimary));
            }
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改通话信息页面
                Intent i = new Intent(RemindActivity.this,ModifyCallActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        callRemarks = findViewById(R.id.remind_call_remarks);
        if (contacts.getRemarkForCall() == null || TextUtils.isEmpty(contacts.getRemarkForCall())){
            callRemarks.setText("点击设置通话备注");
        }else {
            callRemarks.setText(contacts.getRemarkForCall());
        }
        callRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改通话信息页面
                Intent i = new Intent(RemindActivity.this,ModifyCallActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        //-----------------------inspect-----------------------
        inspect = findViewById(R.id.remind_inspect);
        if(contacts.getLastInspectTime() == null || TextUtils.isEmpty(contacts.getLastInspectTime())){
            inspect.setText("点击设置最近体检日期");
        }else {
            inspect.setText(contacts.getLastInspectTime());

            int passInspect_ = getPassInspect(contacts.getLastInspectTime());

            passInspect = findViewById(R.id.remind_pass_inspect);
            passInspect.setText("距离上次体检 "+passInspect_+"天");
            if(passInspect_ > 365){
                passInspect.setBackgroundColor(
                        ContextCompat.getColor(RemindActivity.this,R.color.colorPrimary));
            }
        }
        inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改体检信息页面
                Intent i = new Intent(RemindActivity.this,ModifyInspectActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });

        inspectRemarks = findViewById(R.id.remind_inspect_remarks);
        if (contacts.getRemarkForInspect() == null || TextUtils.isEmpty(contacts.getRemarkForInspect())){
            inspectRemarks.setText("点击设置体检备注");
        }else {
            inspectRemarks.setText(contacts.getRemarkForInspect());
        }
        inspectRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改体检信息页面
                Intent i = new Intent(RemindActivity.this,ModifyInspectActivity.class);
                i.putExtra("userId",contacts.getUserId());
                i.putExtra("contactedId",contacts.getContactedId());
                startActivityForResult(i,0);
            }
        });


    }

    /**
     * @param me 当前用户id
     * @param contacted 联系人id
     * @return 要显示的name
     */
    public String getName(String me,String contacted){

        String name = contacted;//默认name为当前联系人的userId
        Log.e(TAG, "------------------72`````contacted id --->"+contacted);

        if(ContactsBaseDao.searchByUserIdAndContactedId
                (me,contacted).getName() != null
                && !TextUtils.isEmpty(ContactsBaseDao.searchByUserIdAndContactedId
                (me,contacted).getName())){
            //当前用户给该联系人设置了name时
            name = ContactsBaseDao.searchByUserIdAndContactedId
                    (me,contacted).getName();
        }else if(UsersBaseDao.searchByUserId(contacted).getNickname() != null){
            //该联系人给自己设置了昵称
            name = UsersBaseDao.searchByUserId(contacted).getNickname();
        }

        return name;
    }

    /**
     *
     * @param birth 联系人生日
     * @return "距离生日还有xxx天"
     *          返回天数
     *
     * 先把生日年份设置为当前年，比较当前日期与生日
     * 生日大 则直接减算天数
     * 生日小 则设置生日年为下一年
     *       然后减 算天数
     */

    public int getBeforeBirth(String birth){

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date now ,birthday;
        //当前时间
        now = new Date();
        Long time ;
        try {
            birthday = df.parse(birth);
            Log.e(TAG, "now ->"+now );
            Log.e(TAG, "birthday ->"+birthday );
            Log.e(TAG, "birthday.getyear ->"+birthday.getYear() );
            //今年生日
            birthday.setYear(now.getYear());
            Log.e(TAG, "birthday ->"+birthday );
            if(birthday.getTime() < now.getTime()){
                //今年生日已过
                birthday.setYear(now.getYear()+1);
            }
            time = birthday.getTime()-now.getTime();
            time = time/1000/60/60/24;
            return time.intValue()+1;
            //+1是因为birthday的时间是00:00

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return 0;
    }

    /**
     *
     * @param call 上次通话时间
     * @return 返回 现在距离上次通话时间的天数
     *         "距离上次通话xxx天"
     */
    public int getPassCall(String call){
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        Date now,callTime;
        now = new Date();
        Long time;
        try {
            callTime = df.parse(call);
            if(now.getTime() > callTime.getTime()){
                time = now.getTime() - callTime.getTime();
                time = time/1000/60/60/24;

                return time.intValue();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     *
     * @param inspect 上次体检时间
     * @return "距离上次体检xxx天"
     *          返回天数
     */
    public int getPassInspect(String inspect){

        SimpleDateFormat df = new SimpleDateFormat(pattern);

        Date now,inspectTime;
        Long time;

        now = new Date();

        try {
            inspectTime = df.parse(inspect);
            if(now.getTime() > inspectTime.getTime()){
                time = now.getTime() - inspectTime.getTime();
                time = time/1000/60/60/24;

                return time.intValue();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public void back(View view) {
        finish();
    }
}
