package com.example.familyapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;

public class ModifyContactedTelActivity extends AppCompatActivity {


    private static final String TAG = "zzzzzjz---modify contacts tel";

    private Contacts contacts;
    private String userId,contactedId;

    private EditText etTel;
    private Button modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contacted_tel);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        etTel = findViewById(R.id.modify_contacted_tel_et_tel);
        if(!TextUtils.isEmpty (contacts.getTel())){
            etTel.setText(contacts.getTel());
        }

        modify = findViewById(R.id.modify_contacted_tel_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = etTel.getText().toString();

                contacts.setTel(tel);
                ContactsBaseDao.update(contacts);
                Toast.makeText(ModifyContactedTelActivity.this,
                        "电话号码修改成功！",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    public void back(View view) {
        finish();
    }
}
