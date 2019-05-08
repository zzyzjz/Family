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

public class ModifyInspectActivity extends AppCompatActivity {

    private String TAG = "zzzzzjz-modify inspect";

    private String pattern = "yyyy-MM-dd";

    private Contacts contacts;
    private String userId,contactedId;

    private TextView inspect;
    private EditText remarks;
    private Button modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_inspect);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        inspect = findViewById(R.id.modify_inspect_inspect);
        if(contacts.getLastInspectTime() == null || TextUtils.isEmpty(contacts.getLastInspectTime())){
            //
        }else {
            inspect.setText(contacts.getLastInspectTime());
        }
        inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //呈现一个日期选择器
                new DatePickerDialog(ModifyInspectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        inspect.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },2019,5-1,8).show();
                //mouth 0-11
            }
        });

        remarks = findViewById(R.id.modify_inspect_remarks);
        if (contacts.getRemarkForInspect() == null || TextUtils.isEmpty(contacts.getRemarkForInspect())){

        }else {
            remarks.setText(contacts.getRemarkForInspect());
        }

        modify = findViewById(R.id.modify_inspect_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inspect_ = inspect.getText().toString();
                String remarks_ = remarks.getText().toString();
                if(inspect_.charAt(0)>='0' && inspect_.charAt(0) <='9'){
                    //当inspect内容是日期时，更新 inspect 和remarks
                    Log.e(TAG, "是日期 ->"+inspect_ );
                    //判断日期是否小于当前日期
                    SimpleDateFormat df = new SimpleDateFormat(pattern);
                    Date now ,inspectTime;
                    //当前时间
                    now = new Date();
                    try {
                        inspectTime = df.parse(inspect_);
                        if(inspectTime.getTime() < now.getTime()){
                            //inspectTime 小于当前日期
                            contacts.setLastInspectTime(inspect_);
                            contacts.setRemarkForInspect(remarks_);

                            ContactsBaseDao.update(contacts);
                            Toast.makeText(ModifyInspectActivity.this,"体检信息修改成功！",Toast.LENGTH_SHORT).show();
                            contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);
                            Log.e(TAG, "-------------------99---------------" );
                            Log.e(TAG, "user ->"+contacts.getUserId() );
                            Log.e(TAG, "contact ->"+contacts.getContactedId() );
                            Log.e(TAG, "name ->"+contacts.getName() );
                            Log.e(TAG, "inspectTime ->"+contacts.getLastInspectTime() );
                            Log.e(TAG, "inspect remarks ->"+contacts.getRemarkForInspect() );
                            Log.e(TAG, "-------------------99---------------" );
                            finish();
                        }else {
                            Toast.makeText(ModifyInspectActivity.this,"体检日期不可大于当前日期！",Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    //inspect内容不是日期时，只保存remarks
                    contacts.setRemarkForInspect(remarks_);

                    ContactsBaseDao.update(contacts);
                    Toast.makeText(ModifyInspectActivity.this,"体检信息修改成功！",Toast.LENGTH_SHORT).show();
                    contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);
                    Log.e(TAG, "-------------------99---------------" );
                    Log.e(TAG, "user ->"+contacts.getUserId() );
                    Log.e(TAG, "contact ->"+contacts.getContactedId() );
                    Log.e(TAG, "name ->"+contacts.getName() );
                    Log.e(TAG, "inspectTime ->"+contacts.getLastInspectTime() );
                    Log.e(TAG, "inspect remarks ->"+contacts.getRemarkForInspect() );
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
