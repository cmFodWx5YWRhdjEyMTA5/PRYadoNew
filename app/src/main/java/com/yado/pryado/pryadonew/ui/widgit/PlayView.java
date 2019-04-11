package com.yado.pryado.pryadonew.ui.widgit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yado.pryado.pryadonew.R;

public class PlayView extends View {

    private Paint paint;
    private int mWidth;
    private int mHeight;
    private int minH;
    private int div;
    private int range = 3;
    private Thread task;
    private Bitmap iR;
    private Rect dstI;
    private RectF[] oval1;

    public PlayView(Context context) {
        super(context);
        init(context);
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        iR = BitmapFactory.decodeResource(getResources(), R.drawable.laba);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawBitmap(iR, null, dstI, paint);
        for (int i = 0; i < range; i++) {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(oval1[i], -60, 120, false, paint);
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        minH = (Math.min(mWidth, mHeight)) / 5;
        div = (Math.max(mWidth, mHeight)) / 7;
        paint.setStrokeWidth(div / 2);
        Log.e("lcy", mWidth + ":" + mHeight);
        dstI = new Rect();
        dstI.left = (int) (-minH * 1.5);
        dstI.top = (int) (-minH * 1.5);
        dstI.right = minH / 4;
        dstI.bottom = (int) (minH * 1.5);
        oval1 = new RectF[]{new RectF(-minH, -minH, minH, minH), new RectF(-minH * 1.25f, -minH * 1.25f, minH * 1.25f, minH * 1.25f), new RectF(-minH * 1.5f, -minH * 1.5f, minH * 1.5f, minH * 1.5f)};
        Log.e("lcy", div + "");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    volatile boolean playing = false;
    private int ii = -1;

    public void startPlay() {
        if (!playing) {
            paint.setColor(Color.GREEN);
            playing = true;
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (playing) {
                        range = range + ii;
                        if (range >= 3 || range <= 0)
                            ii = -ii;
                        postInvalidate();
                        SystemClock.sleep(200);
                    }
                }
            });
            task.start();
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void stopPlay() {
        if (playing) {
            paint.setColor(Color.GRAY);
            range = 3;
            ii = -1;
            playing = false;
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        playing = false;
        Log.e("lcy", "onDetachedFromWindow");
    }
}
