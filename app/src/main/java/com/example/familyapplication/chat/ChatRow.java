package com.example.familyapplication.chat;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.familyapplication.list.ChatMessageList;
import com.example.familyapplication.adapter.MessageAdapter;
import com.example.familyapplication.R;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.DateUtils;

import java.util.Date;

public abstract class ChatRow extends LinearLayout {


    public interface ChatRowActionCallback {
        void onResendClick(EMMessage message);

        void onBubbleClick(EMMessage message);

        void onDetachedFromWindow();
    }

    protected static final String TAG = "zjz Chat Row";

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected ChatMessageList.MessageListItemClickListener itemClickListener;
    protected EaseMessageListItemStyle itemStyle;

    private ChatRowActionCallback itemActionCallback;

    public ChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        this.activity = (Activity) context;
        inflater = LayoutInflater.from(context);

        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        itemActionCallback.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public void updateView(final EMMessage msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onViewUpdate(msg);
            }
        });
    }

    private void initView() {
        onInflateView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);

        onFindViewById();
    }

    /**
     * set property according message and postion
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position,
                          ChatMessageList.MessageListItemClickListener itemClickListener,
                          ChatRowActionCallback itemActionCallback,
                          EaseMessageListItemStyle itemStyle) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;
        this.itemActionCallback = itemActionCallback;
        this.itemStyle = itemStyle;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    private void setUpBaseView() {
        // set nickname, avatar and background of bubble
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        if(userAvatarView != null) {
            //set nickname and avatar
            if (message.direct() == EMMessage.Direct.SEND) {
                setUserAvatar(context, EMClient.getInstance().getCurrentUser(), userAvatarView);
            } else {
                setUserAvatar(context, message.getFrom(), userAvatarView);
                setUserNick(message.getFrom(), usernickView);
            }
        }
        if (EMClient.getInstance().getOptions().getRequireDeliveryAck()) {
            if(deliveredView != null){
                if (message.isDelivered()) {
                    deliveredView.setVisibility(View.VISIBLE);
                } else {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (EMClient.getInstance().getOptions().getRequireAck()) {
            if (ackedView != null) {
                if (message.isAcked()) {
                    if (deliveredView != null) {
                        deliveredView.setVisibility(View.INVISIBLE);
                    }
                    ackedView.setVisibility(View.VISIBLE);
                } else {
                    ackedView.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (itemStyle != null) {
            if (userAvatarView != null) {
                if (itemStyle.isShowAvatar()) {
                    userAvatarView.setVisibility(View.VISIBLE);
                    EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
                    if(avatarOptions != null && userAvatarView instanceof EaseImageView){
                        EaseImageView avatarView = ((EaseImageView)userAvatarView);
                        if(avatarOptions.getAvatarShape() != 0)
                            avatarView.setShapeType(avatarOptions.getAvatarShape());
                        if(avatarOptions.getAvatarBorderWidth() != 0)
                            avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
                        if(avatarOptions.getAvatarBorderColor() != 0)
                            avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
                        if(avatarOptions.getAvatarRadius() != 0)
                            avatarView.setRadius(avatarOptions.getAvatarRadius());
                    }
                } else {
                    userAvatarView.setVisibility(View.GONE);
                }
            }
            if (usernickView != null) {
                if (itemStyle.isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (bubbleLayout != null) {
                if (message.direct() == EMMessage.Direct.SEND) {
                    if (itemStyle.getMyBubbleBg() != null) {
                        bubbleLayout.setBackground(((MessageAdapter) adapter).getMyBubbleBg());
                    }
                } else if (message.direct() == EMMessage.Direct.RECEIVE) {
                    if (itemStyle.getOtherBubbleBg() != null) {
                        bubbleLayout.setBackground(((MessageAdapter) adapter).getOtherBubbleBg());
                    }
                }
            }
        }

    }




    public static String getUserNick(String userId){
        Users user = UsersBaseDao.searchByUserId(userId);
        String nick = userId;

        if(!userId.equals(EMClient.getInstance().getCurrentUser())
                && ContactsBaseDao.searchByUserIdAndContactedId
                (EMClient.getInstance().getCurrentUser(),userId).getName() != null
                && !TextUtils.isEmpty(ContactsBaseDao.searchByUserIdAndContactedId
                (EMClient.getInstance().getCurrentUser(),userId).getName())){
            //当前用户给该联系人设置了name时
            nick = ContactsBaseDao.searchByUserIdAndContactedId
                    (EMClient.getInstance().getCurrentUser(),userId).getName();
        }else if(UsersBaseDao.searchByUserId(userId).getNickname() != null){
            //该联系人给自己设置了昵称
            nick = UsersBaseDao.searchByUserId(userId).getNickname();
        }

        Log.e(TAG, "------213-----getUserNick: ->"+nick );
        return nick;
    }

    public static Users getUserInfo(String userId){
        Users user = UsersBaseDao.searchByUserId(userId);
        return user;
    }

    public static void setUserAvatar(Context context, String username, ImageView imageView){
        Users user = getUserInfo(username);
        if(user != null){ //&& user.getHead() != null){
            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
                int avatarResId = user.getHead();
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getHead()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.head).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.head).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
            Users user = getUserInfo(username);
            if(user != null && user.getNickname() != null){
//                textView.setText(user.getNickname());
                textView.setText(getUserNick(username));
            }else{
                textView.setText(username);
            }
        }
    }



    private void setClickListener() {
        if(bubbleLayout != null){
            bubbleLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && itemClickListener.onBubbleClick(message)){
                        return;
                    }
                    if (itemActionCallback != null) {
                        itemActionCallback.onBubbleClick(message);
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && itemClickListener.onResendClick(message)){
                        return;
                    }
                    if (itemActionCallback != null) {
                        itemActionCallback.onResendClick(message);
                    }
                }
            });
        }

        if(userAvatarView != null){
            userAvatarView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if(itemClickListener != null){
                        if (message.direct() == EMMessage.Direct.SEND) {
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh view when message status change
     */
    protected abstract void onViewUpdate(EMMessage msg);

    /**
     * setup view
     *
     */
    protected abstract void onSetUpView();


}
