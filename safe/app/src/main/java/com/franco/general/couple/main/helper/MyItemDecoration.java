package com.franco.general.couple.main.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//StackOverflow http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;

    public MyItemDecoration() {
        this.spanCount = 3;
        this.spacing = 10;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildPosition(view); // item position
        int column = position % spanCount; // item column

        outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
        outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= spanCount) {
            outRect.top = spacing; // item top

        }
    }
}
