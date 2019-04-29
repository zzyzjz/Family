package com.example.familyapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.familyapplication.R;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class MessageFragment extends Fragment {
    private String TAG = "zzzzzjz_Message";

    private EaseConversationList conversationListView;

    private EaseConversationListFragment conversationListFragment;

    public MessageFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "MessageFragment....onCreate...." );


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//        Log.e(TAG, "conversation --->"+conversations.get("user1") );

//        for (EMConversation c:toList(conversations)){
//            Log.e(TAG, "Conversation "+c.toString() );
//        }


        //会话列表控件
        conversationListView = getActivity().findViewById(R.id.message_list);
        //初始化，参数为会话列表集合
        conversationListView.init(toList(conversations));
        //刷新列表
        conversationListView.refresh();

//        Log.e(TAG, "Refresh Click Click Click" );

        conversationListFragment = new EaseConversationListFragment();
//        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
//
//            @Override
//            public void onListItemClicked(EMConversation conversation) {
//                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
//            }
//        });
//通过getSupportFragmentManager启动fragment即可
//        Log.e(TAG, "81 Click Click Click" );

//        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                EMConversation conversation = conversationListView.getItem(position);
//                String username = conversation.conversationId();
//                Log.e(TAG, "onItemClick: 90" );
//                if (username.equals(EMClient.getInstance().getCurrentUser()))
//                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
//                else {
//                    // start chat acitivity
//                    Intent intent = new Intent(getActivity(), ChatActivity.class);
//                    Log.e(TAG, "onItemClick: 96" );
//                    if(conversation.isGroup()){
////                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
////                            // it's group chat
////                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
////                        }else{
//                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
////                        }
//
//                    }
//                    // it's single chat
//                    Log.e(TAG, "onItemClick: 107" );
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
//                    Log.e(TAG, "onItemClick: 109" );
//                    startActivity(intent);
//                    Log.e(TAG, "onItemClick: 111" );
//                }
//            }
//        });

        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                Log.e(TAG, "-------131----------group -----"+conversation.isGroup() );
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(UsersBaseDao.searchByUserId(username) == null){
                        Log.e(TAG, "-------138----------group -----"+conversation.isGroup() );
//                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
//                            // it's group chat
//                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
//                        }else{
                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
//                        }

                    }
                    // it's single chat
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });


//        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
//
//
//
//            @Override
//            public void onListItemClicked(EMConversation conversation) {
//
//                Log.e(TAG, "onListItemClicked Click Click Click" );
//                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
//            }
//        });



//        cf.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
//            @Override
//            public void onListItemClicked(EMConversation conversation) {
//                // 聊天需要的bundle对象
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(EaseConstant.EXTRA_USER_ID, conversation.getLastMessage().getUserName());
//                bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
//                intent.putExtras(bundle);
//                /**
//                                  * 从会话列表跳转到聊天页面
//                                  * 首先要判断最后一条信息是接收还是发送消息
//                                  * 然后分情况传递头像和昵称
//                                  *
//                                  */
//                EMMessage lastMessage = conversation.getLastMessage();
//                if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                    try {
//                        intent.putExtra(Utils.FROM_AVATER, lastMessage.getStringAttribute(Utils.FROM_AVATER));
//                        intent.putExtra(Utils.FROM_NICHENG, lastMessage.getStringAttribute(Utils.FROM_NICHENG));
//                    } catch (HyphenateException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        intent.putExtra(Utils.FROM_AVATER, lastMessage.getStringAttribute(Utils.TO_AVATER));
//                        intent.putExtra(Utils.FROM_NICHENG, lastMessage.getStringAttribute(Utils.TO_NICHENG));
//                    } catch (HyphenateException e) {
//                        e.printStackTrace();
//                    }
//                }
//                startActivity(intent);
//            }
//        });



    }

    public List<EMConversation> toList(Map<String, EMConversation> conversations){
        List<EMConversation> lc = new ArrayList<EMConversation>() ;

        for(EMConversation c : conversations.values()){
            lc.add(c);
        }
        return lc;
    }
}
