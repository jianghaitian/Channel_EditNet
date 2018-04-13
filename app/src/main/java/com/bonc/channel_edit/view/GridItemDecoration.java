package com.bonc.channel_edit.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by leeky on 2017/11/6.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public GridItemDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}
