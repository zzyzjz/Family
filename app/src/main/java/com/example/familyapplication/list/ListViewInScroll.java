package com.example.familyapplication.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewInScroll extends ListView {
    //ScrollView中嵌套ListView，ListView只显示一行
    //为解决冲突的问题，重写了ListView的onMeasure方法
    public ListViewInScroll(Context context) {
        super(context);
    }

    public ListViewInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewInScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public ListViewInScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
