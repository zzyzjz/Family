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

public class ModifyContactedRemarksActivity extends AppCompatActivity {

    private static final String TAG = "zzzzzjz---modify contacts remarks";

    private Contacts contacts;
    private String userId,contactedId;

    private EditText etRemarks;
    private Button modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_contacted_remarks);

        userId = getIntent().getStringExtra("userId");
        contactedId = getIntent().getStringExtra("contactedId");

        contacts = ContactsBaseDao.searchByUserIdAndContactedId(userId,contactedId);

        etRemarks = findViewById(R.id.modify_contacted_remarks_et_remarks);
        if(!TextUtils.isEmpty (contacts.getRemarks())){
            etRemarks.setText(contacts.getRemarks());
        }

        modify = findViewById(R.id.modify_contacted_remarks_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = etRemarks.getText().toString();

                contacts.setRemarks(remarks);
                ContactsBaseDao.update(contacts);
                Toast.makeText(ModifyContactedRemarksActivity.this,
                        "备注板修改成功！",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

    public void back(View view) {
        finish();
    }
}
