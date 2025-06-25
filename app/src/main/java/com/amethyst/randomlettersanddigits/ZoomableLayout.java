package com.amethyst.randomlettersanddigits;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

public class ZoomableLayout extends ViewGroup {

    public ZoomableLayout(Context context) {
        super(context);
    }

    public ZoomableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(0, top, width, top + height);
            top += height;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int maxWidth = 0;
        int totalHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(childWidthSpec, childHeightSpec);
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
            totalHeight += child.getMeasuredHeight();
        }

        int scaledWidth = (int) (maxWidth * scaleFactor);
        int scaledHeight = (int) (totalHeight * scaleFactor);

        setMeasuredDimension(scaledWidth, scaledHeight);
    }

    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.0f;

    {
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f));
                invalidate();
                requestLayout();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (scaleFactor != 1.0f) {
            MotionEvent scaledEvent = MotionEvent.obtain(ev);
            scaledEvent.setLocation(ev.getX() / scaleFactor, ev.getY() / scaleFactor);
            boolean handled = super.dispatchTouchEvent(scaledEvent);
            scaledEvent.recycle();
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

}