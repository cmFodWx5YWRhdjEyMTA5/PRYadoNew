package com.yado.pryado.pryadonew.ui.widgit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.webkit.WebView;

/**
 * Created by eado on 2016/5/5.
 */
public class MyWebView extends WebView {

    private long down_time;
    private long up_time;
    private float velocity;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    long last_time = 0;

    float downX = 0;
    float upX = 0;
    float downY = 0;
    float upY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        ViewParent parent = getParent();
//        if (parent != null) {
//            requestDisallowInterceptTouchEvent(true);
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                down_time = System.currentTimeMillis();
                long current_time = System.currentTimeMillis();
                long d_time = current_time - last_time;
                if (d_time < 300) {
                    last_time = current_time;
                    if (doubleClick != null) {
                        doubleClick.onDoubleClick();
                    }
                    return true;
                } else {
                    last_time = current_time;
                }
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                up_time = System.currentTimeMillis();
                velocity = Math.abs(upX - downX) / Math.abs(up_time - down_time);
//                KLog.e("LCY","速度="+velocity);
//                if (up_time != 0 && down_time != 0 && upX != 0 && downX != 0 && velocity > 3) {
                if (Math.abs(upX - downX) > 10 && Math.abs(upX - downX) > Math.abs(upY - downY)) {
                    if (onFlingListner != null) {
                        onFlingListner.onFling();
                        up_time = 0;
                        down_time = 0;
                        upX = 0;
                        downX = 0;
                        velocity = 0;
                        return true;
                    }
                }
                up_time = 0;
                down_time = 0;
                upX = 0;
                downX = 0;
                velocity = 0;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private OnDoubleClickListener doubleClick;
    private OnFlingListner onFlingListner;

    public void setOnDoubleClickListener(OnDoubleClickListener doubleClick) {
        this.doubleClick = doubleClick;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick();
    }

    public interface OnFlingListner {
        void onFling();
    }

    public void setOnFlingListner(OnFlingListner onFlingListner) {
        this.onFlingListner = onFlingListner;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
