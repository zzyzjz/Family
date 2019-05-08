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

public class ModifyContactedNameActivity extends AppCompatActivity {

    private static final String TAG = "zzzzzjz---modify contacts name";

    private Contacts contacts;
    private String userId,contactedId;

    private EditText etName;
    private Button modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contacted_name);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        etName = findViewById(R.id.modify_contacted_name_et_name);
        if(!TextUtils.isEmpty (contacts.getName())){
            etName.setText(contacts.getName());
        }

        modify = findViewById(R.id.modify_contacted_name_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();

                contacts.setName(name);
                ContactsBaseDao.update(contacts);
                Toast.makeText(ModifyContactedNameActivity.this,
                        "备注名称修改成功！",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    public void back(View view) {
        finish();
    }
}
