package com.example.familyapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;

import java.util.List;

public class FmlGroupAdapter extends ArrayAdapter {

    private final int resourceId;

    public FmlGroupAdapter( Context context, int resource,  List objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }



    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        EMGroup group = (EMGroup) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象

        TextView name = (TextView) view.findViewById(R.id.item_name);//获取该布局内的文本视图

        name.setText(group.getGroupName());
        return view;
    }
}
