package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;

public class ModifyInfoActivity extends AppCompatActivity {
    private static final String TAG = "zzzzzjz--modify info";

    private Contacts contact ;
    private Button back,modify;
    private EditText name,birth,tel,callTime,inspectTime,remarks;
    private String userId,contactedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        userId = getIntent().getExtras().getString("userId");
        contactedId = getIntent().getExtras().getString("contactedId");
        //当前用户对该联系人的备注信息
        contact = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        name = findViewById(R.id.modify_infor_et_name);
        name.setText(contact.getName());

        birth = findViewById(R.id.modify_infor_et_birth);
        birth.setText(contact.getBirthday());

        tel = findViewById(R.id.modify_infor_et_tel);
        tel.setText(contact.getTel());

        callTime = findViewById(R.id.modify_infor_et_timeofcall);
        callTime.setText(contact.getLastCallTime());

        inspectTime = findViewById(R.id.modify_infor_et_timeofinspect);
        inspectTime.setText(contact.getLastInspectTime());

        remarks = findViewById(R.id.modify_infor_et_remarks);
        remarks.setText(contact.getRemarks());

        back = findViewById(R.id.modify_info_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModifyInfoActivity.this, ContactedInfoActivity.class));
                finish();
            }
        });

        modify = findViewById(R.id.modify_info_btn_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString().trim())){
                    contact.setName(null);
                }else{
                    contact.setName(name.getText().toString().trim());
                }

                contact.setBirthday(birth.getText().toString().trim());
                contact.setTel(tel.getText().toString().trim());
                contact.setLastCallTime(callTime.getText().toString().trim());
                contact.setLastInspectTime(inspectTime.getText().toString().trim());
                contact.setRemarks(remarks.getText().toString().trim());

                ContactsBaseDao.update(contact);
                Log.e(TAG, "修改备注成功 " );
                Toast.makeText(ModifyInfoActivity.this,"备注修改成功！",Toast.LENGTH_SHORT).show();
                return;

            }
        });
    }
}
