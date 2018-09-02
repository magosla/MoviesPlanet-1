package com.naijaplanet.magosla.android.moviesplanet.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * Creates a proportional Image view
 *
 * {@see @https://stackoverflow.com/questions/4677269/how-to-stretch-three-images-across-the-screen-preserving-aspect-ratio}
 */
public class ProportionalImageView extends AppCompatImageView {

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int diw = d.getIntrinsicWidth();
            if(diw > 0) {

                int w = MeasureSpec.getSize(widthMeasureSpec);
                int h = w * d.getIntrinsicHeight() / diw;
                setMeasuredDimension(w, h);
            }else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}