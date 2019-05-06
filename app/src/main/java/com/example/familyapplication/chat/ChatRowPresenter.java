package com.example.familyapplication.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.familyapplication.list.ChatMessageList;
import com.example.familyapplication.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.widget.EaseAlertDialog;

public abstract class ChatRowPresenter implements ChatRow.ChatRowActionCallback{

    private ChatRow chatRow;

    private Context context;
    private BaseAdapter adapter;
    private EMMessage message;
    private int position;

    private ChatMessageList.MessageListItemClickListener itemClickListener;

    @Override
    public void onResendClick(final EMMessage message) {
        new EaseAlertDialog(getContext(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (!confirmed) {
                    return;
                }
                message.setStatus(EMMessage.Status.CREATE);
                handleSendMessage(message);
            }
        }, true).show();
    }

    @Override
    public void onBubbleClick(EMMessage message) {
    }

    @Override
    public void onDetachedFromWindow() {
    }

    public ChatRow createChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        this.context = cxt;
        this.adapter = adapter;
        chatRow = onCreateChatRow(cxt, message, position, adapter);
        return chatRow;
    }

    public void setup(EMMessage msg, int position,
                      ChatMessageList.MessageListItemClickListener itemClickListener,
                      EaseMessageListItemStyle itemStyle) {
        this.message = msg;
        this.position = position;
        this.itemClickListener = itemClickListener;

        chatRow.setUpView(message, position, itemClickListener, this, itemStyle);

        handleMessage();
    }

    protected void handleSendMessage(final EMMessage message) {
        // Update the view according to the message current status.
        getChatRow().updateView(message);

        if (message.status() == EMMessage.Status.INPROGRESS) {
            Log.i("handleSendMessage", "Message is INPROGRESS");
            if (this.itemClickListener != null) {
                this.itemClickListener.onMessageInProgress(message);
            }
        }
    }

    protected void handleReceiveMessage(EMMessage message) {
    }

    protected abstract ChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter);

    protected ChatRow getChatRow() {
        return chatRow;
    }

    protected Context getContext() {
        return context;
    }

    protected BaseAdapter getAdapter() {
        return adapter;
    }

    protected EMMessage getMessage() {
        return message;
    }

    protected int getPosition() {
        return position;
    }

    private void handleMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            handleSendMessage(message);
        } else if (message.direct() == EMMessage.Direct.RECEIVE) {
            handleReceiveMessage(message);
        }
    }
}
