package com.naijaplanet.magosla.android.moviesplanet.util;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

public class GridItemsSpanSpacing {
    private int span;
    private final int spacing;
    private final boolean includeEdge;

    public GridItemsSpanSpacing(RecyclerView recyclerView, int itemResourceWidthID, int itemResourceHeightID, int orientation, boolean includeEdge){
        float boxSize, containerSize;
        this.includeEdge = includeEdge;
        DisplayMetrics displayMetrics = recyclerView.getResources().getDisplayMetrics();
        if(orientation == RecyclerView.VERTICAL && itemResourceWidthID > 0){
            int itemPixelWidth = recyclerView.getResources().getDimensionPixelSize(itemResourceWidthID);
            span = displayMetrics.widthPixels / itemPixelWidth;
            boxSize = itemPixelWidth;
            containerSize = displayMetrics.widthPixels;
        }else if(orientation == RecyclerView.HORIZONTAL && itemResourceHeightID > 0){
            int itemPixelHeight = recyclerView.getResources().getDimensionPixelSize(itemResourceHeightID);
            span = displayMetrics.heightPixels / itemPixelHeight;
            boxSize = itemPixelHeight;
            containerSize = displayMetrics.heightPixels;
        }else{
            // just set some default values to prevent errors
            span = 1;
            boxSize = 1;
            containerSize = 2;
            Log.d(this.getClass().getSimpleName(), "Orientation, (itemResourceWidthID or itemResourceHeightID depending on orientation) is required");
        }
        float remainingSpace = containerSize  - (boxSize * span);
        if(remainingSpace < 0 && span > 1){
            span--;
            remainingSpace = containerSize  - (boxSize * span);
        }
        spacing = (int) ((!includeEdge && span > 1) ? remainingSpace/ (span-1) : remainingSpace / (span+1));
    }

    public boolean isIncludeEdge() {
        return includeEdge;
    }

    public int getSpacing(){
        return spacing;
    }

    public int getSpan() {
        return span;
    }
}
