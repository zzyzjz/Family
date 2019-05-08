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

public class ModifyBirthActivity extends AppCompatActivity {

    private String TAG = "zzzzzjz-modify birth";

    private String pattern = "yyyy-MM-dd";

    private Contacts contacts;
    private String userId,contactedId;

    private TextView birth;
    private EditText  remarks;
    private Button modify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_birth);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        birth = findViewById(R.id.modify_birth_birth);
        if (contacts.getBirthday() == null || TextUtils.isEmpty(contacts.getBirthday())){
            //
        }else {
            birth.setText(contacts.getBirthday());
        }
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //呈现一个日期选择器
                new DatePickerDialog(ModifyBirthActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birth.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },2019,5-1,8).show();
                //mouth 0-11
            }
        });


        remarks = findViewById(R.id.modify_birth_remarks);
        if (contacts.getRemarkForBirth() == null || TextUtils.isEmpty(contacts.getRemarkForBirth())){

        }else {
            remarks.setText(contacts.getRemarkForBirth());
        }

        modify = findViewById(R.id.modify_birth_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birth_ = birth.getText().toString();
                String remarks_ = remarks.getText().toString();
                if(birth_.charAt(0)>='0' && birth_.charAt(0) <='9'){
                    //当birth内容是日期时，更新 birth 和remarks
                    Log.e(TAG, "是日期 ->"+birth_ );
                    //判断日期是否小于当前日期
                    SimpleDateFormat df = new SimpleDateFormat(pattern);
                    Date now ,birthday;
                    //当前时间
                    now = new Date();
                    try {
                        birthday = df.parse(birth_);
                        if(birthday.getTime() < now.getTime()){
                            //birth 小于当前日期
                            contacts.setBirthday(birth_);
                            contacts.setRemarkForBirth(remarks_);

                            ContactsBaseDao.update(contacts);
                            Toast.makeText(ModifyBirthActivity.this,"生日信息修改成功！",Toast.LENGTH_SHORT).show();
                            contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);
                            Log.e(TAG, "-------------------99---------------" );
                            Log.e(TAG, "user ->"+contacts.getUserId() );
                            Log.e(TAG, "contact ->"+contacts.getContactedId() );
                            Log.e(TAG, "name ->"+contacts.getName() );
                            Log.e(TAG, "birth ->"+contacts.getBirthday() );
                            Log.e(TAG, "birth remarks ->"+contacts.getRemarkForBirth() );
                            Log.e(TAG, "-------------------99---------------" );
                            finish();
                        }else {
                            Toast.makeText(ModifyBirthActivity.this,"生日不可大于当前日期！",Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    //当birth内容不是日期时，只更新remarks
                    contacts.setRemarkForBirth(remarks_);

                    ContactsBaseDao.update(contacts);
                    Toast.makeText(ModifyBirthActivity.this,"生日信息修改成功！",Toast.LENGTH_SHORT).show();
                    contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);
                    Log.e(TAG, "-------------------99---------------" );
                    Log.e(TAG, "user ->"+contacts.getUserId() );
                    Log.e(TAG, "contact ->"+contacts.getContactedId() );
                    Log.e(TAG, "name ->"+contacts.getName() );
                    Log.e(TAG, "birth ->"+contacts.getBirthday() );
                    Log.e(TAG, "birth remarks ->"+contacts.getRemarkForBirth() );
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
