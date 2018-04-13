package com.bonc.channel_edit;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.bonc.channel_edit.view.WrapContentViewPager;
import com.bonc.mobile.ui.widget.pagerindicator.CirclePageIndicator;

/**
 * Created by jht on 2017/12/14.
 */

public class MenuLayoutView extends FrameLayout {
    private View view;
    public MenuLayoutView(@NonNull Context context) {
        this(context,null);
    }

    public MenuLayoutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MenuLayoutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(context).inflate(R.layout.menu_layout_view,this,false);
        this.addView(view);
    }

    public WrapContentViewPager getViewPager(){
        WrapContentViewPager viewPager = (WrapContentViewPager) view.findViewById(R.id.view_pager);
        return viewPager;
    }

    public CirclePageIndicator getCirclePageIndicator(){
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        return circlePageIndicator;
    }
}
