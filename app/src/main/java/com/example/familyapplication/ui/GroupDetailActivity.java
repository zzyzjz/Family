package com.example.familyapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.familyapplication.ModifyBoardActivity;
import com.example.familyapplication.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.List;

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

        try {
            group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        groupName = findViewById(R.id.group_detail_group_name);
        groupName.setText(group.getGroupName());

        id = findViewById(R.id.group_detail_id);
        id.setText(group.getGroupId());

//        try {
//            EMClient.getInstance().groupManager().fetchGroupAnnouncement(groupId);
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }

        board = findViewById(R.id.group_detail_board);
        board.setText(group.getAnnouncement());
        Log.e(TAG, "group.getAnnouncement()" +group.getAnnouncement());

        updateGroup();

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupDetailActivity.this, ModifyBoardActivity.class);
                i.putExtra("groupId",group.getGroupId());
                startActivity(i);
            }
        });
    }


    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {


                    try {
                        group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);

//                        adminList.clear();
//                        adminList.addAll(group.getAdminList());
//                        memberList.clear();
//                        EMCursorResult<String> result = null;
//                        do {
//                            // page size set to 20 is convenient for testing, should be applied to big value
//                            result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
//                                    result != null ? result.getCursor() : "",
//                                    20);
//                            EMLog.d(TAG, "fetchGroupMembers result.size:" + result.getData().size());
//                            memberList.addAll(result.getData());
//                        } while (result.getCursor() != null && !result.getCursor().isEmpty());
//
//                        muteList.clear();
//                        muteList.addAll(EMClient.getInstance().groupManager().fetchGroupMuteList(groupId, 0, 200).keySet());
//                        blackList.clear();
//                        blackList.addAll(EMClient.getInstance().groupManager().fetchGroupBlackList(groupId, 0, 200));

                    } catch (Exception e) {
                        //e.printStackTrace();  // User may have no permission for fetch mute, fetch black list operation
                    } finally {
//                        memberList.remove(group.getOwner());
//                        memberList.removeAll(adminList);
                    }

                    try {
                        EMClient.getInstance().groupManager().fetchGroupAnnouncement(groupId);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        public void run() {
//                            refreshOwnerAdminAdapter();
//                            refreshMembersAdapter();

//							refreshUIVisibility();
                            ((TextView) findViewById(R.id.group_detail_group_name)).setText(group.getGroupName() + "(" + group.getMemberCount()
                                    + ")");
//                            loadingPB.setVisibility(View.INVISIBLE);

//                            if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
//                                // 显示解散按钮
//                                exitBtn.setVisibility(View.GONE);
//                                deleteBtn.setVisibility(View.VISIBLE);
//                            } else {
//                                // 显示退出按钮
//                                exitBtn.setVisibility(View.VISIBLE);
//                                deleteBtn.setVisibility(View.GONE);
//                            }

                            // update block
//                            EMLog.d(TAG, "group msg is blocked:" + group.isMsgBlocked());
//                            if (group.isMsgBlocked()) {
//                                switchButton.openSwitch();
//                            } else {
//                                switchButton.closeSwitch();
//                            }
//                            List<String> disabledIds = EMClient.getInstance().pushManager().getNoPushGroups();
//                            if(disabledIds != null && disabledIds.contains(groupId)){
//                                offlinePushSwitch.openSwitch();
//                            }else{
//                                offlinePushSwitch.closeSwitch();
//                            }

                            board.setText(group.getAnnouncement());

//                            RelativeLayout changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
//                            RelativeLayout changeGroupDescriptionLayout = (RelativeLayout) findViewById(R.id.rl_change_group_description);
//                            boolean isOwner = isCurrentOwner(group);
//                            exitBtn.setVisibility(isOwner ? View.GONE : View.VISIBLE);
//                            deleteBtn.setVisibility(isOwner ? View.VISIBLE : View.GONE);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            loadingPB.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }).start();

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
