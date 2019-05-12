package com.example.familyapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familyapplication.R;
import com.example.familyapplication.db.Contacts;
import com.example.familyapplication.db.ContactsBaseDao;
import com.example.familyapplication.db.Users;
import com.example.familyapplication.db.UsersBaseDao;
import com.hyphenate.chat.EMClient;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FmlRemindAdapter extends ArrayAdapter {

    private String TAG = "zzzzzjz-remind adapter";
    private final int resourceId;

    private String pattern = "yyyy-MM-dd";

    public FmlRemindAdapter( Context context, int resource,  List objects) {
        super(context, resource, objects);
        this.resourceId = resource;

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        Contacts c = (Contacts) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView head = view.findViewById(R.id.item_remind_head);
        TextView name = view.findViewById(R.id.item_remind_name);
        TextView beforeBirth = view.findViewById(R.id.item_remind_before_birth);
        TextView passCall = view.findViewById(R.id.item_remind_pass_call);
        TextView passInspect = view.findViewById(R.id.item_remind_pass_inspect);

        head.setImageResource(getHead(c.getContactedId()));
        name.setText(getName(c.getUserId(),c.getContactedId()));


        if(c.getBirthday() != null && !TextUtils.isEmpty(c.getBirthday())){
            int birth = getBeforeBirth(c.getBirthday());

            beforeBirth.setText("距离生日还有"+birth+"天");
            if(birth <= 10){
                beforeBirth.setBackgroundColor(
                        ContextCompat.getColor(getContext(),R.color.colorPrimary));
            }
        }else {
            beforeBirth.setVisibility(View.GONE);
        }

        if(c.getLastCallTime() != null && !TextUtils.isEmpty(c.getLastCallTime())){
            int call = getPassCall(c.getLastCallTime());

            passCall.setText("距离上次通话 "+call+"天");
            if(call >= 10){
                passCall.setBackgroundColor(
                        ContextCompat.getColor(getContext(),R.color.colorPrimary));
            }
        }else {
            passCall.setVisibility(View.GONE);
        }

        if(c.getLastInspectTime() != null && !TextUtils.isEmpty(c.getLastInspectTime())){
            int inspect = getPassInspect(c.getLastInspectTime());
            passInspect.setText("距离上次体检 "+inspect+"天");
            if(inspect > 365){
                passInspect.setBackgroundColor(
                        ContextCompat.getColor(getContext(),R.color.colorPrimary));
            }
        }else {
            passInspect.setVisibility(View.GONE);
        }



        return view;


    }
    /**
     *获取联系人头像
     * @param id 联系人id
     * @return 联系人头像
     */
    public int getHead(String id){

        Users u = UsersBaseDao.searchByUserId(id);
        int head = u.getHead();

        return head;
    }

    /**
     * @param me 当前用户id
     * @param contacted 联系人id
     * @return 要显示的name
     */
    public String getName(String me,String contacted){

        String name = contacted;//默认name为当前联系人的userId
        Log.e(TAG, "------------------72`````contacted id --->"+contacted);

        if(ContactsBaseDao.searchByUserIdAndContactedId
                (me,contacted).getName() != null
                && !TextUtils.isEmpty(ContactsBaseDao.searchByUserIdAndContactedId
                (me,contacted).getName())){
            //当前用户给该联系人设置了name时
            name = ContactsBaseDao.searchByUserIdAndContactedId
                    (me,contacted).getName();
        }else if(UsersBaseDao.searchByUserId(contacted).getNickname() != null){
            //该联系人给自己设置了昵称
            name = UsersBaseDao.searchByUserId(contacted).getNickname();
        }

        return name;
    }

    /**
     *
     * @param birth 联系人生日
     * @return "距离生日还有xxx天"
     *          返回天数
     *
     * 先把生日年份设置为当前年，比较当前日期与生日
     * 生日大 则直接减算天数
     * 生日小 则设置生日年为下一年
     *       然后减 算天数
     */

    public int getBeforeBirth(String birth){

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date now ,birthday;
        //当前时间
        now = new Date();
        Long time ;
        try {
            birthday = df.parse(birth);
            Log.e(TAG, "now ->"+now );
            Log.e(TAG, "birthday ->"+birthday );
            Log.e(TAG, "birthday.getyear ->"+birthday.getYear() );
            //今年生日
            birthday.setYear(now.getYear());
            Log.e(TAG, "birthday ->"+birthday );
            if(birthday.getTime() < now.getTime()){
                //今年生日已过
                birthday.setYear(now.getYear()+1);
            }
            time = birthday.getTime()-now.getTime();
            time = time/1000/60/60/24;
            return time.intValue()+1;
            //+1是因为birthday的时间是00:00

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return 0;
    }

    /**
     *
     * @param call 上次通话时间
     * @return 返回 现在距离上次通话时间的天数
     *         "距离上次通话xxx天"
     */
    public int getPassCall(String call){
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        Date now,callTime;
        now = new Date();
        Long time;
        try {
            callTime = df.parse(call);
            if(now.getTime() > callTime.getTime()){
                time = now.getTime() - callTime.getTime();
                time = time/1000/60/60/24;

                return time.intValue();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     *
     * @param inspect 上次体检时间
     * @return "距离上次体检xxx天"
     *          返回天数
     */
    public int getPassInspect(String inspect){

        SimpleDateFormat df = new SimpleDateFormat(pattern);

        Date now,inspectTime;
        Long time;

        now = new Date();

        try {
            inspectTime = df.parse(inspect);
            if(now.getTime() > inspectTime.getTime()){
                time = now.getTime() - inspectTime.getTime();
                time = time/1000/60/60/24;

                return time.intValue();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
