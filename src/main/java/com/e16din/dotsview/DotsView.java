package com.e16din.dotsview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

public class DotsView extends LinearLayout {

    public static final int DEFAULT_STYLE_ATTR = android.R.attr.buttonBarStyle;

    public DotsView(Context context) {
        super(context, null, DEFAULT_STYLE_ATTR);
        init(null);
    }

    public DotsView(Context context, AttributeSet attrs) {
        super(context, attrs, DEFAULT_STYLE_ATTR);
        init(attrs);
    }

    public DotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, DEFAULT_STYLE_ATTR);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, DEFAULT_STYLE_ATTR, defStyleRes);
        init(attrs, defStyleRes);
    }

    private void init(AttributeSet attrs) {
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyleRes) {
        if (isInEditMode()) {
            return;
        }

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        if (attrs == null) {
            return;
        }

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.DotsView,
                DEFAULT_STYLE_ATTR, defStyleRes);

        int count = 1;
        int padding = 0;
        int size = LayoutParams.WRAP_CONTENT;

        Drawable selector = null;

        @ColorInt
        int colorDefault = 0;
        @ColorInt
        int colorSelected = 0;

        int viewPagerId = 0;

        try {
            count = a.getInteger(R.styleable.DotsView_count, count);
            padding = a.getLayoutDimension(R.styleable.DotsView_padding, padding);
            size = a.getLayoutDimension(R.styleable.DotsView_size, size);

            selector = a.getDrawable(R.styleable.DotsView_selector);

            colorDefault = a.getColor(R.styleable.DotsView_colorDefault, colorDefault);
            colorSelected = a.getColor(R.styleable.DotsView_colorSelected, colorSelected);

            viewPagerId = a.getResourceId(R.styleable.DotsView_viewPager, viewPagerId);
        } finally {
            a.recycle();
        }

        for (int i = 0; i < count; i++) {
            Button btnDot = new Button(getContext(), null, android.R.attr.buttonBarButtonStyle);
            LayoutParams params = new LayoutParams(size, size);
            if (i < count) {
                params.rightMargin = padding;
            }
            btnDot.setLayoutParams(params);

            if (selector != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnDot.setBackground(selector);
                } else {
                    btnDot.setBackgroundDrawable(selector);
                }
            } else {
                //todo: create dots selector with values colorDefault and colorSelected
            }

            addView(btnDot);
        }

        bindViewPager(viewPagerId);
    }

    public void bindViewPager(int viewPagerId) {
        if (viewPagerId != 0) {
            ViewPager vPager = (ViewPager) getRootView().findViewById(viewPagerId);
            bindViewPager(vPager);
        }
    }

    public void bindViewPager(ViewPager vPager) {
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                resetDots();

                Button btnDot = (Button) getChildAt(position);
                btnDot.setSelected(true);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void resetDots() {
        for (int i = 0; i < getChildCount(); i++) {
            Button btnDot = (Button) getChildAt(i);
            btnDot.setSelected(false);
        }
    }
}
