package com.naijaplanet.magosla.android.moviesplanet.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.naijaplanet.magosla.android.moviesplanet.R;


/**
 * Creates a expandable text view
 * Source {{@see @https://codexplo.wordpress.com/2013/09/07/android-expandable-textview/}}
 *
 */
@SuppressWarnings("unused")
public class ExpandableTextView extends AppCompatTextView {
    private Listener listener;

    private boolean isExpandable;

    @SuppressWarnings("WeakerAccess")
    public interface Listener {
        /**
         * If the TextView content is expandable
         * @param isExpandable the state of the TextView
         */
        void expandable(boolean isExpandable);

        /**
         * if the text view is being expanded
         * @param expanded {{boolean}} value of the state true if expanded
         */
        void expanded(boolean expanded);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public void removeListener(){
        this.listener = null;
    }

    private static final int DEFAULT_TRIM_LENGTH = 200;
    private static final String ELLIPSIS = "...";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;

    private static final String BUNDLE_TRIM_STATE = "trim";
    private static final String BUNDLE_STATE = "instance";

    public ExpandableTextView(Context context){
        super(context);
    }
    public ExpandableTextView(Context context, AttributeSet attrSet){
        super(context, attrSet);
        initialize(context ,attrSet);
    }

    public ExpandableTextView(Context context, AttributeSet attrSet, int defStyleAttr){
        super(context, attrSet, defStyleAttr);
        initialize(context, attrSet);
    }

    private void initialize(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = !trim;
                setText();
                requestFocusFromTouch();
                if(listener != null){
                    listener.expanded(!trim);
                }
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(BUNDLE_TRIM_STATE, trim);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            trim = bundle.getBoolean(BUNDLE_TRIM_STATE);
            state = bundle.getParcelable(BUNDLE_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
        if(listener != null){
            // notify the listener of the expandable state of the content
            listener.expandable(isExpandable);
        }
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
            this.isExpandable = true;
            return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(ELLIPSIS);
        } else {
            return originalText;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }
}
