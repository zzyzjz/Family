package com.example.familyapplication;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FmlContactAdapter extends ArrayAdapter {

    private final int resourceId;

    public FmlContactAdapter(Context context, int resource, List<Contacted> contacts) {

        super(context, resource, contacts);

        this.resourceId = resource;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        //传进adapter的应该是处理过的 name（String）和head（String，图片路径：/pic/a_1.png）组成的List
        //name 取值的优先级：name（contacts）-> nickname(users) -> userId(users)

//        Users user = (Users) getItem(position);
        Contacted contacted = (Contacted) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView head = (ImageView) view.findViewById(R.id.contacted_head);//获取该布局内的图片视图
        TextView name = (TextView) view.findViewById(R.id.contacted_name);//获取该布局内的文本视图
//        head.setImageResource(user.getHead());//为图片视图设置图片资源
//        head.setImageBitmap(BitmapFactory.decodeFile(user.getHead()));
//        name.setText(user.getNickname());//为文本视图设置文本内容
        head.setImageResource(contacted.getImage());
        name.setText(contacted.getName());
        return view;


    }
}
