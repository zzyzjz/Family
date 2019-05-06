package com.example.familyapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.familyapplication.list.ConversationList;
import com.example.familyapplication.R;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
//import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationAdapter extends ArrayAdapter<EMConversation> {

    private static final String TAG = "zjz conversationAdapter";
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationAdapter.ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;


//    public ConversationAdapter(Context context, int resource,
//                                   List objects) {
//        super(context, resource, objects);
//        conversationList = objects;
//        copyConversationList = new ArrayList<EMConversation>();
//        copyConversationList.addAll(objects);
//    }
    public ConversationAdapter(Context context, int resource,
                                   List<EMConversation> objects) {
        super(context, resource, objects);
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
    }


    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            holder.motioned = (TextView) convertView.findViewById(R.id.mentioned);
            convertView.setTag(holder);
        }
        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.conversationId();
        Log.e(TAG, "----104----"+username );
        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            String groupId = conversation.conversationId();
            if(EaseAtMessageHelper.get().hasAtMeMsg(groupId)){
                holder.motioned.setVisibility(View.VISIBLE);
            }else{
                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar
//            holder.avatar.setImageResource(R.drawable.ease_group_icon);

            holder.avatar.setImageResource(R.drawable.group);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            Log.e(TAG, "------117----- group  ->"+group.getGroupName() );
            holder.name.setText(group != null ? group.getGroupName() : username);
        } else {
            Users user = UsersBaseDao.searchByUserId(username);
            Log.e(TAG, "-------119----- "+user.getUserId() );
//            holder.avatar.setImageResource(user.getHead());
//            setUserAvatar( username, holder.avatar);
            setUserAvatar(getContext(), username, holder.avatar);
//            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
//            EaseUserUtils.setUserNick(username, holder.name);
            setUserNick(username, holder.name);
//            setUserNick(username, holder.name);
//            holder.name.setText(getUserNick(username));
            holder.motioned.setVisibility(View.GONE);
        }

        EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
        if(avatarOptions != null && holder.avatar instanceof EaseImageView) {
            EaseImageView avatarView = ((EaseImageView) holder.avatar);
            if (avatarOptions.getAvatarShape() != 0)
                avatarView.setShapeType(avatarOptions.getAvatarShape());
            if (avatarOptions.getAvatarBorderWidth() != 0)
                avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
            if (avatarOptions.getAvatarBorderColor() != 0)
                avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
            if (avatarOptions.getAvatarRadius() != 0)
                avatarView.setRadius(avatarOptions.getAvatarRadius());
        }
        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            if(cvsListHelper != null){
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
            holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                    TextView.BufferType.SPANNABLE);
            if(content != null){
                holder.message.setText(content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        //set property
        holder.name.setTextColor(primaryColor);
        holder.message.setTextColor(secondaryColor);
        holder.time.setTextColor(timeColor);
        if(primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if(secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if(timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }

//    @Override   原
    public View getView1(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.hyphenate.easeui.R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.message);
            holder.time = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(com.hyphenate.easeui.R.id.avatar);
            holder.msgState = convertView.findViewById(com.hyphenate.easeui.R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(com.hyphenate.easeui.R.id.list_itease_layout);
            holder.motioned = (TextView) convertView.findViewById(com.hyphenate.easeui.R.id.mentioned);
            convertView.setTag(holder);
        }
        holder.list_itease_layout.setBackgroundResource(com.hyphenate.easeui.R.drawable.ease_mm_listitem);

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.conversationId();

        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            String groupId = conversation.conversationId();
            if(EaseAtMessageHelper.get().hasAtMeMsg(groupId)){
                holder.motioned.setVisibility(View.VISIBLE);
            }else{
                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar
//            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.group);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);
        } else if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
            holder.motioned.setVisibility(View.GONE);
        }else {
            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
            EaseUserUtils.setUserNick(username, holder.name);
            holder.motioned.setVisibility(View.GONE);
        }

        EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
        if(avatarOptions != null && holder.avatar instanceof EaseImageView) {
            EaseImageView avatarView = ((EaseImageView) holder.avatar);
            if (avatarOptions.getAvatarShape() != 0)
                avatarView.setShapeType(avatarOptions.getAvatarShape());
            if (avatarOptions.getAvatarBorderWidth() != 0)
                avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
            if (avatarOptions.getAvatarBorderColor() != 0)
                avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
            if (avatarOptions.getAvatarRadius() != 0)
                avatarView.setRadius(avatarOptions.getAvatarRadius());
        }
        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            if(cvsListHelper != null){
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
            holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                    TextView.BufferType.SPANNABLE);
            if(content != null){
                holder.message.setText(content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        //set property
        holder.name.setTextColor(primaryColor);
        holder.message.setTextColor(secondaryColor);
        holder.time.setTextColor(timeColor);
        if(primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if(secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if(timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }





    //TODO setUserAvatar  setUserNick getUserInfo

    public void setUserAvatar(String userId ,ImageView imageView){
        Users user = UsersBaseDao.searchByUserId(userId);
        imageView.setImageResource(user.getHead());
    }

    public static String getUserNick(String userId){
        Users user = UsersBaseDao.searchByUserId(userId);
        String nick = userId;

        if(!userId.equals(EMClient.getInstance().getCurrentUser())
                && ContactsBaseDao.searchByUserIdAndContactedId
                (EMClient.getInstance().getCurrentUser(),userId).getName() != null){
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




    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(!notiyfyByFilter){
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }


    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }


    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                if (copyConversationList.size() > mOriginalValues.size()) {
                    mOriginalValues = copyConversationList;
                }
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.conversationId();

                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    if(group != null){
                        username = group.getGroupName();
                    }else{
                        Users user = getUserInfo(username);
                        // TODO: not support Nick anymore
                        if(user != null && user.getNickname() != null)
                            username = user.getNickname();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else{
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            if (results.values != null) {
                conversationList.addAll((List<EMConversation>) results.values);
            }
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private ConversationList.ConversationListHelper cvsListHelper;

    public void setCvsListHelper(ConversationList.ConversationListHelper cvsListHelper){
        this.cvsListHelper = cvsListHelper;
    }

    private static class ViewHolder {
        /** who you chat with */
        TextView name;
        /** unread message count */
        TextView unreadLabel;
        /** content of last message */
        TextView message;
        /** time of last message */
        TextView time;
        /** avatar */
        ImageView avatar;
        /** status of last message */
        View msgState;
        /** layout */
        RelativeLayout list_itease_layout;
        TextView motioned;
    }





}
