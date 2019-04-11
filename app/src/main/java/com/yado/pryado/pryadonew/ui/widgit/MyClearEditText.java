package com.yado.pryado.pryadonew.ui.widgit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.yado.pryado.pryadonew.R;


@SuppressLint("AppCompatCustomView")
public class MyClearEditText extends EditText implements TextWatcher {

    private Drawable leftIcon;
    private Drawable icDelete;
    private Context mContext;

    public MyClearEditText(Context context) {
        this(context, null);
        init();
    }

    public MyClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyClearEditText);

        // 解析集合中的属性circle_color属性
        // 该属性的id为:R.styleable.CircleView_circle_color
        // 将解析的属性传入到画圆的画笔颜色变量当中（本质上是自定义画圆画笔的颜色）
        // 第二个参数是默认设置颜色（即无指定circle_color情况下使用）
        leftIcon = ta.getDrawable(R.styleable.MyClearEditText_leftIcon);

        // 解析后释放资源
        ta.recycle();
        init();
    }

    public MyClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        // getCompoundDrawables() Returns drawables for the left(0), top(1), right(2) and bottom(3)
//        leftIcon = ContextCompat.getDrawable(mContext, R.drawable.ic_user);
        icDelete = ContextCompat.getDrawable(mContext, R.drawable.ic_clear);

        setImage();

    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟clear点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (icDelete != null && event.getAction() == MotionEvent.ACTION_UP) {
            // 起始位置
            int start = getWidth() - getTotalPaddingRight() + getPaddingRight();
            // 结束位置
            int end = getWidth();
            boolean available = (event.getX() > start) && (event.getX() < end);
            if (available) {
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    private void setImage() {
        /*
            setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
            意思是设置Drawable显示在text的左、上、右、下位置。
         */
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, icDelete, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int end, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int end, int after) {
        setImage();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(5));
    }


    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}
