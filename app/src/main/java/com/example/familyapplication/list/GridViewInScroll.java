package com.example.familyapplication.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewInScroll extends GridView {
    public GridViewInScroll(Context context) {
        super(context);
    }

    public GridViewInScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
