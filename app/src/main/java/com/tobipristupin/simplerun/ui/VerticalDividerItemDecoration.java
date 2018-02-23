package com.tobipristupin.simplerun.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView ItemDecoration that sets separation between recycler view items. Same as effect as listview's divider attr.
 */

public class VerticalDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalDividerHeight;

    public VerticalDividerItemDecoration(int verticalDividerHeight){
        this.verticalDividerHeight = verticalDividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = verticalDividerHeight;
    }
}
