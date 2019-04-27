package com.example.familyapplication.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapplication.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceholderFragment extends Fragment {

    private EaseConversationList conversationListView;

    public PlaceholderFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();


        //会话列表控件
//        conversationListView = getActivity().findViewById(R.id.message_list);
//        //初始化，参数为会话列表集合
//        conversationListView.init(toList(conversations));
//        //刷新列表
//        conversationListView.refresh();

        View rootView = inflater.inflate(R.layout.fragment_message,container,false);
        //inflate(R.layout.item,parent,false)
        //把item加入到parent视图中

        return rootView;
    }

    public List<EMConversation> toList(Map<String, EMConversation> conversations){
        List<EMConversation> lc = new ArrayList<EMConversation>() ;

        for(EMConversation c : conversations.values()){
            lc.add(c);
        }
        return lc;
    }
}
