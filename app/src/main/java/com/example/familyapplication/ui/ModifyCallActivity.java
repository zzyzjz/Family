package com.example.familyapplication.ui;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyCallActivity extends AppCompatActivity {

    private String TAG = "zzzzzjz-modify call";

    private String pattern = "yyyy-MM-dd";

    private Contacts contacts;
    private String userId,contactedId;

    private TextView call;
    private EditText remarks;
    private Button modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_call);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        call = findViewById(R.id.modify_call_call);

        if (contacts.getLastCallTime() == null || TextUtils.isEmpty(contacts.getLastCallTime())){
            //
        }else {
            call.setText(contacts.getLastCallTime());
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //呈现一个日期选择器
                new DatePickerDialog(ModifyCallActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        call.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },2019,5-1,8).show();
                //mouth 0-11
            }
        });

        remarks = findViewById(R.id.modify_call_remarks);
        if (contacts.getRemarkForCall() == null || TextUtils.isEmpty(contacts.getRemarkForCall())){

        }else {
            remarks.setText(contacts.getRemarkForCall());
        }

        modify = findViewById(R.id.modify_call_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call_ = call.getText().toString();
                String remarks_ = remarks.getText().toString();
                if(call_.charAt(0)>='0' && call_.charAt(0) <='9'){
                    //当call内容是日期时，更新 call 和 remarks
                    Log.e(TAG, "是日期 ->"+call_ );
                    //判断日期是否小于当前日期
                    SimpleDateFormat df = new SimpleDateFormat(pattern);
                    Date now ,callTime;
                    //当前时间
                    now = new Date();
                    try {
                        callTime = df.parse(call_);
                        if(callTime.getTime() < now.getTime()){
                            //callTime 小于当前日期
                            contacts.setLastCallTime(call_);
                            contacts.setRemarkForCall(remarks_);

                            ContactsBaseDao.update(contacts);
                            Toast.makeText(ModifyCallActivity.this,"通话信息修改成功！",Toast.LENGTH_SHORT).show();
                            contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);
                            Log.e(TAG, "-------------------99---------------" );
                            Log.e(TAG, "user ->"+contacts.getUserId() );
                            Log.e(TAG, "contact ->"+contacts.getContactedId() );
                            Log.e(TAG, "name ->"+contacts.getName() );
                            Log.e(TAG, "callTime ->"+contacts.getLastCallTime() );
                            Log.e(TAG, "call remarks ->"+contacts.getRemarkForCall() );
                            Log.e(TAG, "-------------------99---------------" );
                            finish();
                        }else {
                            Toast.makeText(ModifyCallActivity.this,"通话日期不可大于当前日期！",Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    //当call内容不是日期时，只更新 remarks
                    contacts.setRemarkForCall(remarks_);

                    ContactsBaseDao.update(contacts);
                    Toast.makeText(ModifyCallActivity.this,"通话信息修改成功！",Toast.LENGTH_SHORT).show();
                    contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);
                    Log.e(TAG, "-------------------99---------------" );
                    Log.e(TAG, "user ->"+contacts.getUserId() );
                    Log.e(TAG, "contact ->"+contacts.getContactedId() );
                    Log.e(TAG, "name ->"+contacts.getName() );
                    Log.e(TAG, "callTime ->"+contacts.getLastCallTime() );
                    Log.e(TAG, "call remarks ->"+contacts.getRemarkForCall() );
                    Log.e(TAG, "-------------------99---------------" );
                    finish();
                }
            }
        });
    }


    public void back(View view) {
        finish();
    }
}
