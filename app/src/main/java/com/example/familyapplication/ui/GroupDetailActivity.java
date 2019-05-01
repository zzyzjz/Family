package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.familyapplication.ModifyBoardActivity;
import com.example.familyapplication.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

public class GroupDetailActivity extends AppCompatActivity {

    private static final String TAG = "zjz--group detail";

    private TextView groupName,board,id;

    private String groupId;
    private EMGroup group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        groupName = findViewById(R.id.group_detail_group_name);
        groupName.setText(group.getGroupName());

        id = findViewById(R.id.group_detail_id);
        id.setText(group.getGroupId());

        board = findViewById(R.id.group_detail_board);
        board.setText(group.getAnnouncement());
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupDetailActivity.this, ModifyBoardActivity.class);
                i.putExtra("groupId",group.getGroupId());
                startActivity(i);
            }
        });
    }

    /**
     * back
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
