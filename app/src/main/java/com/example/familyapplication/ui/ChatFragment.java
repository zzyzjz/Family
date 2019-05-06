package com.example.familyapplication.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.familyapplication.list.ChatMessageList;
import com.example.familyapplication.chat.CustomChatRowProvider;
import com.example.familyapplication.R;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;

import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseCompat;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {


    private static final String TAG = "zzzzzjz--ChatFragment";


    private View kickedForOfflineLayout;
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected boolean isRoaming = false;
    protected Handler handler = new Handler();
    private Handler typingHandler = null;
    private ExecutorService fetchQueue;
    private boolean turnOnTyping = false;
    protected ChatFragmentHelper chatFragmentHelper;



    protected ChatMessageList messageList;


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
            titleBar.setRightImageResource(R.drawable.ease_to_group_details_normal);
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
        if(!name.equals(EMClient.getInstance().getCurrentUser())
                && ContactsBaseDao.searchByUserIdAndContactedId
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean roaming) {
        isRoaming = roaming;
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    protected void initView() {
        // hold to record voice
        //noinspection ConstantConditions
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);

        // message list layout
        messageList = (ChatMessageList) getView().findViewById(R.id.message_list);
        if(chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
//        messageList.setAvatarShape(1);
        listView = messageList.getListView();

        kickedForOfflineLayout = getView().findViewById(R.id.layout_alert_kicked_off);
        kickedForOfflineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChatRoomViewCreation();
            }
        });

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onTyping(CharSequence s, int start, int before, int count) {
                // send action:TypingBegin cmd msg.
                typingHandler.sendEmptyMessage(MSG_TYPING_BEGIN);
            }

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(com.hyphenate.easeui.R.color.holo_blue_bright, com.hyphenate.easeui.R.color.holo_green_light,
                com.hyphenate.easeui.R.color.holo_orange_light, com.hyphenate.easeui.R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (isRoaming) {
            fetchQueue = Executors.newSingleThreadExecutor();
        }

        // to handle during-typing actions.
        typingHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_TYPING_BEGIN: // Notify typing start

                        if (!turnOnTyping) return;

                        // Only support single-chat type conversation.
                        if (chatType != EaseConstant.CHATTYPE_SINGLE)
                            return;

                        if (hasMessages(MSG_TYPING_END)) {
                            // reset the MSG_TYPING_END handler msg.
                            removeMessages(MSG_TYPING_END);
                        } else {
                            // Send TYPING-BEGIN cmd msg
                            EMMessage beginMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
                            EMCmdMessageBody body = new EMCmdMessageBody(ACTION_TYPING_BEGIN);
                            // Only deliver this cmd msg to online users
                            body.deliverOnlineOnly(true);
                            beginMsg.addBody(body);
                            beginMsg.setTo(toChatUsername);
                            EMClient.getInstance().chatManager().sendMessage(beginMsg);
                        }

                        sendEmptyMessageDelayed(MSG_TYPING_END, TYPING_SHOW_TIME);
                        break;
                    case MSG_TYPING_END:

                        if (!turnOnTyping) return;

                        // Only support single-chat type conversation.
                        if (chatType != EaseConstant.CHATTYPE_SINGLE)
                            return;

                        // remove all pedding msgs to avoid memory leak.
                        removeCallbacksAndMessages(null);
                        // Send TYPING-END cmd msg
                        EMMessage endMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
                        EMCmdMessageBody body = new EMCmdMessageBody(ACTION_TYPING_END);
                        // Only deliver this cmd msg to online users
                        body.deliverOnlineOnly(true);
                        endMsg.addBody(body);
                        endMsg.setTo(toChatUsername);
                        EMClient.getInstance().chatManager().sendMessage(endMsg);
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };

    }

    protected void registerExtendMenuItem(){
        for(int i = 0; i < itemStrings.length; i++){
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }




    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener{

        @Override
        public void onClick(int itemId, View view) {
            if(chatFragmentHelper != null){
                if(chatFragmentHelper.onExtendMenuItemClick(itemId, view)){
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
//            case ITEM_LOCATION:
//                Log.e("zjz easechat ", "location " );
//                startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
//                break;

                default:
                    break;
            }
        }

    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("zjz easechat ", "----1016---- " );
        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, EaseCompat.getUriForFile(getContext(), cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        Log.e("zjz easechat ", "----1031---- " );
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }





    @Override
    protected void onConversationInit(){
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number

        if (!isRoaming) {
            final List<EMMessage> msgs = conversation.getAllMessages();
            int msgCount = msgs != null ? msgs.size() : 0;
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                if (msgs != null && msgs.size() > 0) {
                    msgId = msgs.get(0).getMsgId();
                }
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            }
        } else {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
                        final List<EMMessage> msgs = conversation.getAllMessages();
                        int msgCount = msgs != null ? msgs.size() : 0;
                        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                            String msgId = null;
                            if (msgs != null && msgs.size() > 0) {
                                msgId = msgs.get(0).getMsgId();
                            }
                            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                        }
                        messageList.refreshSelectLast();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    @Override
    protected void onMessageListInit(){
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        messageList.getListView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    public interface ChatFragmentHelper{
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         * @return
         */
        CustomChatRowProvider onSetCustomChatRowProvider();
    }




    @Override
    protected void setListItemClickListener() {
        messageList.setItemClickListener(new ChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if(chatFragmentHelper != null){
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public boolean onResendClick(final EMMessage message) {
                EMLog.i(TAG, "onResendClick");
                new EaseAlertDialog(getContext(), com.hyphenate.easeui.R.string.resend, com.hyphenate.easeui.R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        message.setStatus(EMMessage.Status.CREATE);
                        sendMessage(message);
                    }
                }, true).show();
                return true;
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if(chatFragmentHelper != null){
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if(chatFragmentHelper != null){
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if(chatFragmentHelper == null){
                    return false;
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

            @Override
            public void onMessageInProgress(EMMessage message) {
                message.setMessageStatusCallback(messageStatusCallback);
            }
        });
    }

    private void loadMoreLocalMessage() {
        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(),
                        pagesize);
            } catch (Exception e1) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (messages.size() > 0) {
                messageList.refreshSeekTo(messages.size() - 1);
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
            }

            isloading = false;
        } else {
            Toast.makeText(getActivity(), getResources().getString(com.hyphenate.easeui.R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadMoreRoamingMessages() {
        if (!haveMoreData) {
            Toast.makeText(getActivity(), getResources().getString(com.hyphenate.easeui.R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (fetchQueue != null) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EMMessage> messages = conversation.getAllMessages();
                        EMClient.getInstance().chatManager().fetchHistoryMessages(
                                toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize,
                                (messages != null && messages.size() > 0) ? messages.get(0).getMsgId() : "");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreLocalMessage();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if(chatType == EaseConstant.CHATTYPE_GROUP){
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());

        // Remove all padding actions in handler
        handler.removeCallbacksAndMessages(null);
//        typingHandler.sendEmptyMessage(MSG_TYPING_END);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                conversation.markMessageAsRead(message.getMsgId());
            }
            EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
        }
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }
    @Override
    protected void sendMessage(EMMessage message){
        if (message == null) {
            return;
        }
        if(chatFragmentHelper != null){
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP){
            message.setChatType(EMMessage.ChatType.GroupChat);
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }

        message.setMessageStatusCallback(messageStatusCallback);

        // Send message.
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if(isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }

    protected EMCallBack messageStatusCallback = new EMCallBack() {
        @Override
        public void onSuccess() {
            if(isMessageListInited) {
                messageList.refresh();
            }
        }

        @Override
        public void onError(int code, String error) {
            Log.i("EaseChatRowPresenter", "onError: " + code + ", error: " + error);
            if(isMessageListInited) {
                messageList.refresh();
            }
        }

        @Override
        public void onProgress(int progress, String status) {
            Log.i(TAG, "onProgress: " + progress);
            if(isMessageListInited) {
                messageList.refresh();
            }
        }
    };

    protected void emptyHistory() {
        String msg = getResources().getString(com.hyphenate.easeui.R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(),null, msg, null,new EaseAlertDialog.AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if(confirmed){
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    messageList.refresh();
                    haveMoreData = true;
                }
            }
        }, true).show();
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
    protected void inputAtUsername(String username, boolean autoAddAtSymbol){
        if(EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != EaseConstant.CHATTYPE_GROUP){
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
//        EaseUser user = EaseUserUtils.getUserInfo(username);
        Users user = UsersBaseDao.searchByUserId(username);
        if (user != null){
            username = user.getNickname();
        }
        if(autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }

//    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener{
//
//        @Override
//        public void onClick(int itemId, View view) {
////            if(chatFragmentHelper != null){
////                if(chatFragmentHelper.onExtendMenuItemClick(itemId, view)){
////                    return;
////                }
////            }
//            switch (itemId) {
//                case ITEM_TAKE_PICTURE:
////                    selectPicFromCamera();
//                    Toast.makeText(getActivity(), "take picture!", Toast.LENGTH_SHORT).show();
//                    break;
//                case ITEM_PICTURE:
////                    selectPicFromLocal();
//                    break;
//                case ITEM_LOCATION:
////                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//    }

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
