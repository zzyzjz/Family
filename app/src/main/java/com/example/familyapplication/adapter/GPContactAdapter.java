package com.example.familyapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Users;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.EMLog;

import java.util.ArrayList;
import java.util.List;

public class GPContactAdapter extends ArrayAdapter implements SectionIndexer {

    private static final String TAG = "zzzzzjz--GPAdapter";
    List<String> list;
    List<Users> userList;
    List<Users> copyUserList;
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;


    public GPContactAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.res = resource;
        this.userList = objects;
        copyUserList = new ArrayList<Users>();
        copyUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);

    }
    private static class ViewHolder{
        ImageView avatar;
        TextView nameView;
        TextView headerView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            if (res == 0){
                convertView = layoutInflater.inflate(
                        R.layout.ease_row_contact,parent,false);
            }else {
                convertView = layoutInflater.inflate(res,null);
            }
            holder.avatar = convertView.findViewById(R.id.avatar);
            holder.nameView = convertView.findViewById(R.id.name);
            holder.headerView = convertView.findViewById(R.id.header);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Users user = (Users) getItem(position);
        if (user == null){
            Log.e(TAG, "getView: position "+position );
        }
        String userId = user.getUserId();
        String nick =  user.getNickname();
        String header = user.getNickname();//原为Nick的首字母

//        if (position == 0 || header != null && !header.equals(getItem(position - 1).getInitialLetter())) {
//            if (TextUtils.isEmpty(header)) {
//                holder.headerView.setVisibility(View.GONE);
//            } else {
//                holder.headerView.setVisibility(View.VISIBLE);
//                holder.headerView.setText(header);
//            }
//        } else {
//            holder.headerView.setVisibility(View.GONE);
//        }
        //头像属性设置
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

        //名称显示
        if(nick != null && !TextUtils.isEmpty(nick)){
            holder.nameView.setText(nick);
        }else {
            holder.nameView.setText(userId);
        }
        //头像显示
        holder.avatar.setImageResource(user.getHead());



        return convertView;


    }

    @Override
    public Object[] getSections() {

//        positionOfSection = new SparseIntArray();
//        sectionOfPosition = new SparseIntArray();
//        int count = getCount();
//        list = new ArrayList<String>();
//        list.add(getContext().getString(com.hyphenate.easeui.R.string.search_header));
//        positionOfSection.put(0, 0);
//        sectionOfPosition.put(0, 0);
//        for (int i = 1; i < count; i++) {
//
////            String letter = getItem(i).getInitialLetter();
//            int section = list.size() - 1;
//            if (list.get(section) != null && !list.get(section).equals(letter)) {
//                list.add(letter);
//                section++;
//                positionOfSection.put(section, i);
//            }
//            sectionOfPosition.put(i, section);
//        }
//        return list.toArray(new String[list.size()]);

        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return positionOfSection.get(sectionIndex);
//        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
//        return 0;
    }


    protected class  MyFilter extends Filter {
        List<Users> mOriginalList = null;

        public MyFilter(List<Users> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if(mOriginalList==null){
                mOriginalList = new ArrayList<Users>();
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
            EMLog.d(TAG, "contacts copy size: " + copyUserList.size());

            if(prefix==null || prefix.length()==0){
                results.values = copyUserList;
                results.count = copyUserList.size();
            }else{

                if (copyUserList.size() > mOriginalList.size()) {
                    mOriginalList = copyUserList;
                }
                String prefixString = prefix.toString();
                final int count = mOriginalList.size();
                final ArrayList<Users> newValues = new ArrayList<Users>();
                for(int i=0;i<count;i++){
                    final Users user = mOriginalList.get(i);
                    String username = user.getUserId();

                    if(username.startsWith(prefixString)){
                        newValues.add(user);
                    }
                    else{
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(user);
                                break;
                            }
                        }
                    }
                }
                results.values=newValues;
                results.count=newValues.size();
            }
            EMLog.d(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            userList.clear();
            userList.addAll((List<Users>)results.values);
            EMLog.d(TAG, "publish contacts filter results size: " + results.count);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

//    protected int primaryColor;
//    protected int primarySize;
//    protected Drawable initialLetterBg;
//    protected int initialLetterColor;
//
//    public EaseContactAdapter setPrimaryColor(int primaryColor) {
//        this.primaryColor = primaryColor;
//        return this;
//    }
//
//
//    public EaseContactAdapter setPrimarySize(int primarySize) {
//        this.primarySize = primarySize;
//        return this;
//    }
//
//    public EaseContactAdapter setInitialLetterBg(Drawable initialLetterBg) {
//        this.initialLetterBg = initialLetterBg;
//        return this;
//    }
//
//    public EaseContactAdapter setInitialLetterColor(int initialLetterColor) {
//        this.initialLetterColor = initialLetterColor;
//        return this;
//    }

}
