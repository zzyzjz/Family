package com.example.familyapplication.chat;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;

public class ChatBigExpressionPresenter extends ChatTextPresenter {

    @Override
    protected ChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new ChatRowBigExpression(cxt, message, position, adapter);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
    }

}
