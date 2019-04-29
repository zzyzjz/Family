package com.example.familyapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;

public class CreateGroupActivity extends AppCompatActivity {

    private static final String TAG = "zzzzzjz--create group";
    private Button create;
    private EditText groupNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupNameEditText = findViewById(R.id.create_group_name);

        create = findViewById(R.id.create_group_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupNameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateGroupActivity.this,"群名称不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // select from contact list
                    Intent i = new Intent(CreateGroupActivity.this,GroupPickContactsActivity.class);
                    i.putExtra("groupName",name);
                    startActivityForResult(i,0);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);
        if (resultCode == RESULT_OK) {
            //new group
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage(st1);
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String groupName = groupNameEditText.getText().toString().trim();
//                    String desc = introductionEditText.getText().toString();
                    String[] members = data.getStringArrayExtra("newmembers");
                    try {
                        EMGroupOptions option = new EMGroupOptions();
                        option.maxUsers = 200;
                        option.inviteNeedConfirm = true;

//                        String reason = CreateGroupActivity.this.getString(R.string.invite_join_group);
                        String reason = " ";
                        reason  = EMClient.getInstance().getCurrentUser() + reason + groupName;

//                        if(publibCheckBox.isChecked()){
//                            option.style = memberCheckbox.isChecked() ? EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval : EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
//                        }else{
//                            option.style = memberCheckbox.isChecked()? EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite: EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
//                        }
                        EMClient.getInstance().groupManager().createGroup(groupName, "", members, reason, option);
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressDialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressDialog.dismiss();
                                Toast.makeText(CreateGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }



    public void back(View view) {
        finish();
    }
}
