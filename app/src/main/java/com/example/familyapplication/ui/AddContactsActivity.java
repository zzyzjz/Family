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
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class AddContactsActivity extends AppCompatActivity {
    private String TAG = "zzzzzjz-addContacts";
    private EditText contactedName;
    private Button addContact,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        back = findViewById(R.id.add_contacts_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AddContactsActivity.this,MainActivity.class));
                finish();
            }
        });

        contactedName = findViewById(R.id.add_friend_et_contacted_name);
        addContact = findViewById(R.id.add_contacts_btn_add);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = contactedName.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(AddContactsActivity.this,R.string.User_name_cannot_be_empty,Toast.LENGTH_SHORT).show();
                    return;
                }else if(ContactsBaseDao.searchByUserIdAndContactedId(
                        EMClient.getInstance().getCurrentUser(),name) != null){
                    //该用户已是联系人
                    Toast.makeText(AddContactsActivity.this,"该用户已是联系人！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(UsersBaseDao.searchByUserId(name) == null){
                    //该用户不存在
                    Toast.makeText(AddContactsActivity.this,"该用户不存在！",Toast.LENGTH_SHORT).show();
                    return;
                }else if(name.equals(EMClient.getInstance().getCurrentUser())){
                    //该用户名为当前用户
                    Log.e(TAG, "----------------47 " );
                    Toast.makeText(AddContactsActivity.this,"不可以加自己为联系人！",Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e(TAG, "addContacts.."+name );

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        //参数为要添加的好友的username和添加理由
                        try {
                            EMClient.getInstance().contactManager().addContact(name,"");
                            Contacts c = new Contacts(null,EMClient.getInstance().getCurrentUser(),name,
                                    null,null,null,null,null,null);
                            ContactsBaseDao.insert(c);
                            //当前用户添加id=name的用户为联系人的同时，当前用户也是name的新增联系人
                            c = new Contacts(null,name,EMClient.getInstance().getCurrentUser(),
                                    null,null,null,null,null,null);
                            ContactsBaseDao.insert(c);
                            Log.e(TAG, "添加联系人·····"+name );
                            List<Contacts> contacts = ContactsBaseDao.searchAll();
                            Log.e(TAG, "---------------70" );
                            for(Contacts con : contacts){
                                Log.e(TAG, "id ---> "+con.getId() );
                                Log.e(TAG, "user ---> "+con.getUserId() );
                                Log.e(TAG, "contact ---> "+con.getContactedId() );
                                Log.e(TAG, "name ---> "+con.getName() );
                                Log.e(TAG, "birth ---> "+con.getBirthday() );
                                Log.e(TAG, "tel ---> "+con.getTel() );
                                Log.e(TAG, "remark ---> "+con.getRemarks() );
                            }
                        } catch (HyphenateException e) {
                            e.printStackTrace();

                        }
                    }
                }.start();
            }
        });
    }
}
