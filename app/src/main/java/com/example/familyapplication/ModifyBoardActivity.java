package com.example.familyapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.familyapplication.ui.GroupDetailActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

public class ModifyBoardActivity extends AppCompatActivity {

    private static final String TAG="zjz--modify board";

    private EditText board;
    private ImageView back;
    private Button modify;

    private String groupId;
    private EMGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_board);

        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        board = findViewById(R.id.modify_board_board);
        board.setText(group.getAnnouncement());

        back = findViewById(R.id.modify_board_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        ModifyBoardActivity.this, GroupDetailActivity.class));
                finish();
            }
        });
        modify = findViewById(R.id.modify_board_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            EMClient.getInstance().groupManager()
                                    .updateGroupAnnouncement(groupId,board.getText().toString());
                            Looper.prepare();
                            Toast.makeText(ModifyBoardActivity.this,
                                    "留言板更新成功！",Toast.LENGTH_SHORT).show();
                            Looper.loop();

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(ModifyBoardActivity.this,
                                    "留言板更新失败！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Log.e(TAG, "留言板更新失败 -> e:"+e.toString() );
                            return;
                        }
                    }
                }.start();


//                try {
//
//                    EMClient.getInstance().groupManager()
//                            .updateGroupAnnouncement(groupId,board.getText().toString());
//
//
//                    startActivity(new Intent(
//                            ModifyBoardActivity.this, GroupDetailActivity.class));
//                    finish();
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                    Toast.makeText(ModifyBoardActivity.this,
//                            "留言板更新失败！",Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "留言板更新失败 -> e:"+e.toString() );
//                }

//                startActivity(new Intent(
//                        ModifyBoardActivity.this, GroupDetailActivity.class)
//                        .putExtra("groupId", group.getGroupId()));
                finish();
            }
        });
    }


}
