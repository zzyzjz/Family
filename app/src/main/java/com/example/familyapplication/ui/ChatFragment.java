package com.example.familyapplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {


    private static final String TAG = "zzzzzjz--ChatFragment";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void setUpView() {
//        setChatFragmentHelper(this);

        super.setUpView();

        //设置聊天界面的title
//        titleBar.setTitle(toChatUsername);
//        titleBar.setBackgroundColor(R.color.colorPrimaryDark);


        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            Log.e(TAG, "setUpView ------36-----single" );
            if(UsersBaseDao.searchByUserId(toChatUsername) != null){
//                EaseUser user = EaseUserUtils.getUserInfo(toChatUsername);
//                if (user != null) {
                    titleBar.setTitle(getChatUserName(toChatUsername));
//                }

            }
//            titleBar.setRightImageResource(com.hyphenate.easeui.R.drawable.ease_mm_title_remove);
        } else {
            titleBar.setRightImageResource(com.hyphenate.easeui.R.drawable.ease_to_group_details_normal);
//            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                Log.e(TAG, "setUpView ------49-----group" );
                if (group != null)
                    titleBar.setTitle(group.getGroupName());
                // listen the event that user moved out group or group is dismissed
                groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);

//            }

        }
//        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
//                    emptyHistory();
//                } else {
//                    toGroupDetails();
//                }
//            }
//        });

//        titleBar.setBackgroundColor(R.color.colorPrimary);
//        setRefreshLayoutListener();
//
//        // show forward message if the message is not null
//        String forward_msg_id = getArguments().getString("forward_msg_id");
//        if (forward_msg_id != null) {
//            forwardMessage(forward_msg_id);
//        }

//        super.setUpView();

    }


    public String getChatUserName(String chatUserId){
        String name = chatUserId;//默认name为当前联系人的userId


        Log.e(TAG, "---------180----------" );
        if(ContactsBaseDao.searchByUserIdAndContactedId
                (EMClient.getInstance().getCurrentUser(),chatUserId).getName() != null){
            //当前用户给该联系人设置了name时
            name = ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),chatUserId).getName();
        }else if(UsersBaseDao.searchByUserId(chatUserId).getNickname() != null){
            //该联系人给自己设置了昵称
            name = UsersBaseDao.searchByUserId(chatUserId).getNickname();
        }
        return name;

    }



    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    public void onEnterToChatDetails() {
        Log.e(TAG, "------124------onEnterToChatDetails:  " );
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            Log.e(TAG, "------127------group name: "+group.getGroupName() );
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
//            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);

            startActivity(new Intent(getActivity(),GroupDetailActivity.class)
                    .putExtra("groupId", group.getGroupId()));
        }

    }

    @Override
    protected void toGroupDetails() {
        Log.e(TAG, "------144------toGroupDetails:  " );
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            Log.e(TAG, "------147------group name: "+group.getGroupName() );
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
//            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);

            startActivity(new Intent(getActivity(),GroupDetailActivity.class)
                    .putExtra("groupId", group.getGroupId()));
        }
    }

    @Override
    public void onAvatarClick(String username) {
        return ;
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }


}
