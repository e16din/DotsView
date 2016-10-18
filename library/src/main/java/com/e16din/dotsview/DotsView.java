package com.e16din.dotsview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

public class DotsView extends LinearLayout {

    public static final int DEFAULT_STYLE_ATTR = android.R.attr.buttonBarStyle;
    public static final int WRONG_VALUE = -1;


    private int mViewPagerId = 0;

    private int mExitFadeDuration = 0;
    private int mEnterFadeDuration = 0;

    private final ViewPager.OnPageChangeListener mOnPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    resetDots();

                    updateDot(position, true);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            };

    // - attrs
    private int mSelector;
    private int mSize;
    private int mPadding;
    @ColorInt
    private int mColorDefault;
    @ColorInt
    private int mColorChecked;


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

        mPadding = 0;
        mSize = LayoutParams.WRAP_CONTENT;

        mSelector = 0;

        mColorDefault = WRONG_VALUE;
        mColorChecked = WRONG_VALUE;

        try {
            mPadding = a.getLayoutDimension(R.styleable.DotsView_padding, mPadding);
            mSize = a.getLayoutDimension(R.styleable.DotsView_size, mSize);

            mSelector = a.getResourceId(R.styleable.DotsView_selector, mSelector);

            mColorDefault = a.getColor(R.styleable.DotsView_colorDefault, WRONG_VALUE);
            mColorChecked = a.getColor(R.styleable.DotsView_colorChecked, WRONG_VALUE);

            if (mColorDefault == WRONG_VALUE) {
                if (!TextUtils.isEmpty(a.getString(R.styleable.DotsView_colorDefault))) {
                    mColorDefault = Color.parseColor(a.getString(R.styleable.DotsView_colorDefault));
                    mColorChecked = Color.parseColor(a.getString(R.styleable.DotsView_colorChecked));
                } else {
                    mColorDefault = ContextCompat.getColor(getContext(), R.color.colorDefault);
                    mColorChecked = ContextCompat.getColor(getContext(), R.color.colorChecked);
                }
            }

            mExitFadeDuration = a.getInteger(R.styleable.DotsView_exitFadeDuration,
                    mExitFadeDuration);
            mEnterFadeDuration = a.getInteger(R.styleable.DotsView_enterFadeDuration,
                    mEnterFadeDuration);

            mViewPagerId = a.getResourceId(R.styleable.DotsView_viewPager, mViewPagerId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }


    }

    private void createDots(int count,
                            int padding,
                            int size,
                            int selector,
                            int colorDefault,
                            int colorChecked) {
        removeAllViews();

        for (int i = 0; i < count; i++) {
            Button btnDot = new Button(getContext(), null, android.R.attr.buttonBarButtonStyle);

            LayoutParams params = new LayoutParams(size, size);
            if (i < count - 1) {
                params.rightMargin = padding;
            }
            btnDot.setLayoutParams(params);

            if (selector != 0) {
                btnDot.setBackgroundResource(selector);
            } else {
                StateListDrawable selectorDrawable = makeSelector(size, colorDefault, colorChecked);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnDot.setBackground(selectorDrawable);
                } else {
                    btnDot.setBackgroundDrawable(selectorDrawable);
                }
            }

            addView(btnDot);
        }
    }

    private ShapeDrawable drawCircle(int size, int color) {
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        oval.setIntrinsicHeight(size);
        oval.setIntrinsicWidth(size);
        oval.getPaint().setColor(color);
        return oval;
    }

    private StateListDrawable makeSelector(int size, int colorDefault, int colorChecked) {
        StateListDrawable selector = new StateListDrawable();
        selector.setExitFadeDuration(mExitFadeDuration);
        selector.setEnterFadeDuration(mExitFadeDuration);

        Drawable drawableChecked = drawCircle(size, colorChecked);
        Drawable drawableUnchecked = drawCircle(size, colorDefault);

        selector.addState(new int[]{android.R.attr.state_selected}, drawableChecked);
        selector.addState(new int[]{-android.R.attr.state_selected}, drawableUnchecked);
        selector.addState(new int[]{}, drawableUnchecked);

        return selector;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPagerId != 0) {
            bindViewPager(mViewPagerId);
        }
    }

    /**
     * Bind dots to the view pager
     *
     * @param viewPagerId id of your ViewPager
     */
    public void bindViewPager(int viewPagerId) {
        ViewPager vPager = (ViewPager) Utils.scanForActivity(getContext()).findViewById(viewPagerId);
        bindViewPager(vPager);
    }

    /**
     * Bind dots to the view pager
     *
     * @param vPager your ViewPager
     */
    public void bindViewPager(ViewPager vPager) {
        createDots(vPager.getAdapter().getCount(),
                mPadding, mSize, mSelector, mColorDefault, mColorChecked);

        vPager.removeOnPageChangeListener(mOnPageChangeListener);
        vPager.addOnPageChangeListener(mOnPageChangeListener);
        updateDot(0, true);

    }

    private void updateDot(int position, boolean selected) {
        Button btnDot = (Button) getChildAt(position);
        btnDot.setSelected(selected);
    }

    private void resetDots() {
        for (int i = 0; i < getChildCount(); i++) {
            updateDot(i, false);
        }
    }
}
