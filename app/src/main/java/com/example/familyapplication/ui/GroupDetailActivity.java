package com.example.familyapplication.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyapplication.Contacted;
import com.example.familyapplication.list.GridViewInScroll;
import com.example.familyapplication.R;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends AppCompatActivity {

    private static final String TAG = "zjz--group detail";

    private TextView groupName,board,id;
    private Button exit,addMember;
    private GridViewInScroll owner,members;

    List<String> memberIdList = new ArrayList<String>();
    List<Contacted> memberList = new ArrayList<Contacted>();
    List<Contacted> ownerList = new ArrayList<>();


    private MemberAdapter memberAdapter,ownerAdapter;

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
        //点击组名进入更改组名页面
        groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        GroupDetailActivity.this, ModifyGroupNameActivity.class)
                        .putExtra("groupId",groupId));
            }
        });

        //群加人
        addMember = findViewById(R.id.group_detail_add);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult((new Intent(
                        GroupDetailActivity.this, GroupPickContactsActivity.class)
                        .putExtra("groupId", groupId)),0);


            }
        });

        id = findViewById(R.id.group_detail_id);
        id.setText(group.getGroupId());


        board = findViewById(R.id.group_detail_board);


        updateGroup();

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupDetailActivity.this, ModifyBoardActivity.class);
                i.putExtra("groupId",group.getGroupId());
                startActivityForResult(i,1);

            }
        });

        exit = findViewById(R.id.group_detail_exit);
        if(group.getOwner().equals(EMClient.getInstance().getCurrentUser())){
            exit.setText("解散该群");
        }else {
            exit.setText("退出该群");
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group.getOwner() .equals(EMClient.getInstance().getCurrentUser())){
                    //解散群
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                    builder.setTitle("解散该群");
                    builder.setMessage("确认解散该群？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismissGroup();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();

//                    Toast.makeText(GroupDetailActivity.this,"解散",Toast.LENGTH_SHORT).show();
                }else{
                    //退出群

                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                    builder.setTitle("退出该群");
                    builder.setMessage("确认退出该群？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exitGroup();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();

//                    Toast.makeText(GroupDetailActivity.this,"退群",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser()) != null){
            initOwner();
        }

        owner = findViewById(R.id.group_detail_owner);
        ownerAdapter = new MemberAdapter(this,R.layout.item_grid,ownerList);
        owner.setAdapter(ownerAdapter);

        members = findViewById(R.id.group_detail_members);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null){

//            if (data.getStringArrayListExtra("newmembers") != null){
            final String[] newmembers = data.getStringArrayExtra("newmembers");

            addMembersToGroup(newmembers);
        }
        updateGroup();

//        if(requestCode == 1){
//
//        }
    }

    private void addMembersToGroup(final String[] newmembers) {
//        final String st6 = getResources().getString(R.string.Add_group_members_fail);
        new Thread(new Runnable() {

            public void run() {
                try {
                    // 创建者调用add方法
                    if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
                        EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);
                    }
                    updateGroup();
//                    refreshMembersAdapter();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            ((TextView) findViewById(R.id.group_detail_group_name))
//                                    .setText(group.getGroupName() + "(" + group.getMemberCount() + ")");
////                            progressDialog.dismiss();
//                        }
//                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            progressDialog.dismiss();
                            Toast.makeText(GroupDetailActivity.this, "添加成员失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {


                    try {
                        group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);

                        Log.e(TAG, "------------139---------- " );
//                        adminList.clear();
//                        adminList.addAll(group.getAdminList());
                        memberIdList.clear();
                        EMCursorResult<String> result = null;
                        do {
                            Log.e(TAG, "------------145---------- " );
                            // page size set to 20 is convenient for testing, should be applied to big value
                            result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
                                    result != null ? result.getCursor() : "",
                                    20);
                            Log.e(TAG, "fetchGroupMembers result.size:" + result.getData().size());
                            memberIdList.addAll(result.getData());
                        } while (result.getCursor() != null && !result.getCursor().isEmpty());
//
//                        muteList.clear();
//                        muteList.addAll(EMClient.getInstance().groupManager().fetchGroupMuteList(groupId, 0, 200).keySet());
//                        blackList.clear();
//                        blackList.addAll(EMClient.getInstance().groupManager().fetchGroupBlackList(groupId, 0, 200));

                    } catch (Exception e) {
                        //e.printStackTrace();  // User may have no permission for fetch mute, fetch black list operation
                    } finally {
                        memberIdList.remove(group.getOwner());
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

                            Log.e(TAG, "------------175----------" );
                            Log.e(TAG, "member id list size -> "+memberIdList.size() );
                            Log.e(TAG, "-----------177----------- " );

                            if (UsersBaseDao.searchByUserId(EMClient.getInstance().getCurrentUser()) != null){
                                initMembers();
                            }

                            memberAdapter = new MemberAdapter(
                                    GroupDetailActivity.this,R.layout.item_grid,memberList);
                            members = findViewById(R.id.group_detail_members);
                            members.setAdapter(memberAdapter);
//							refreshUIVisibility();
                            ((TextView) findViewById(R.id.group_detail_group_name))
                                    .setText(group.getGroupName() + "(" + group.getMemberCount() + ")");
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
                    e.printStackTrace();
                    Log.e(TAG, "---189---"+e.toString(),e );
                }
            }
        }).start();

    }



    private void exitGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "退出群聊成功！", Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();
                            if(ChatActivity.activityInstance != null)
                                ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(getApplicationContext(),
                                    "退出群聊失败 ： "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }


    private void dismissGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(GroupDetailActivity.this,
                                    "解散群聊成功！",Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();
                            if(ChatActivity.activityInstance != null)
                                ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(GroupDetailActivity.this,
                                    "解散群聊失败 ： "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void initMembers(){
        Log.e(TAG, "initMembers: " );

        memberList.clear();
        for(String id :memberIdList){

            int image;
            String name = id;//默认name为userId
            Log.e(TAG, "------------------302`````member id --->"+id);
            image = UsersBaseDao.searchByUserId(id).getHead();
            Log.e(TAG, "---------304----------" );
            if(!id.equals(EMClient.getInstance().getCurrentUser())
                    && ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),id).getName() != null
                    && !TextUtils.isEmpty(ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),id).getName())){
                //该id不是当前用户id 且 当前用户给该联系人设置了name时
                name = ContactsBaseDao.searchByUserIdAndContactedId
                        (EMClient.getInstance().getCurrentUser(),id).getName();
            }else if(UsersBaseDao.searchByUserId(id).getNickname() != null){
                //该联系人给自己设置了昵称
                name = UsersBaseDao.searchByUserId(id).getNickname();
            }

            Contacted contacted = new Contacted(id,name,image);
            //id是该联系人的userId,name是这条item将要显示的名字，image是头像

            memberList.add(contacted);

        }


        //从Contacts表获取当前用户的联系人
        //根据联系人的id找到联系人的头像（User表）和联系人要显示的name（name -> nick -> userId）
        //并整理成一个name和头像的list（String类型），保存在contactsList中
    }

    private void initOwner(){
        Log.e(TAG, "initOwner: " );

        String id = group.getOwner();

        int image;
        String name = id;//默认name为当前联系人的userId
        Log.e(TAG, "------------------302`````member id --->"+id);
        image = UsersBaseDao.searchByUserId(id).getHead();
        Log.e(TAG, "---------304----------" );
        if(!id.equals(EMClient.getInstance().getCurrentUser())
                && ContactsBaseDao.searchByUserIdAndContactedId
                (EMClient.getInstance().getCurrentUser(),id).getName() != null){
            //该id不是当前用户id 且 当前用户给该联系人设置了name时
            name = ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),id).getName();
        }else if(UsersBaseDao.searchByUserId(id).getNickname() != null){
            //该联系人给自己设置了昵称
            name = UsersBaseDao.searchByUserId(id).getNickname();
        }

        Contacted contacted = new Contacted(id,name,image);
        //id是该联系人的userId,name是这条item将要显示的名字，image是头像

        ownerList.add(contacted);

    }

    private class MemberAdapter extends ArrayAdapter {

        private int res;

        public MemberAdapter(Context context, int textViewResourceId, List<Contacted> objects) {
            super(context, textViewResourceId, objects);
            res = textViewResourceId;
            Log.e(TAG, "MemberAdapter: 336" );
        }

        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {
            Log.e(TAG, "---------341--------- position"+position );

            Contacted contacted = (Contacted) getItem(position);
            Log.e(TAG, "--------344----------contacted"+contacted.getName() );
            View view = LayoutInflater.from(getContext()).inflate(res, null);//实例化一个对象
            ImageView head = view.findViewById(R.id.iv_avatar);//获取该布局内的图片视图
            TextView name = view.findViewById(R.id.tv_name);//获取该布局内的文本视图
            head.setImageResource(contacted.getImage());
            name.setText(contacted.getName());
            return view;


        }

//        @Override
//        public int getCount() {
//            return super.getCount() + 1;
//        }
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
