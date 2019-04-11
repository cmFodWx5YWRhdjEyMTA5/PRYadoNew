package com.yado.pryado.pryadonew.ui.widgit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.util.ProgressTextUtils;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

public abstract class AudioRecoderDialog extends BasePopupWindow implements View.OnTouchListener {

    private ImageView imageView;
    private TextView textView;
    private TextView tv_record;
    private TextView tv_cancel;
    private int downY;
    private int upY;

    public AudioRecoderDialog(Context context) {
        super(context);
        if (SharedPrefUtil.getInstance(MyApplication.getInstance()).getT(MyConstants.IsFirst, true)) {
            startRecord();
            SystemClock.sleep(2);
            stopRecord();
            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.IsFirst, false);
            this.dismiss();
        }
        View contentView = LayoutInflater.from(context).inflate(R.layout.record_dialog, null);
        imageView = (ImageView) contentView.findViewById(android.R.id.progress);
        textView = (TextView) contentView.findViewById(android.R.id.text1);
        tv_record = (TextView) contentView.findViewById(android.R.id.button1);
        tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        tv_record.setOnTouchListener(this);
        setContentView(contentView);
    }

    public void setLevel(int level) {
        Drawable drawable = imageView.getDrawable();
        drawable.setLevel(600 * level / 100);
    }

    public void setTime(long time) {
        textView.setText(ProgressTextUtils.getProgressText(time));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tv_cancel.setVisibility(View.VISIBLE);
                downY = (int) event.getY();
                textView.setVisibility(View.VISIBLE);
                startRecord();
                tv_record.setBackgroundResource(R.drawable.shape_recoder_btn_recoding);
                tv_record.setText(getContext().getString(R.string.up));
                return true;
            case MotionEvent.ACTION_UP:
                tv_cancel.setVisibility(View.INVISIBLE);
                upY = (int) event.getY();
                if (downY - upY > 200) {
                    cancelRecord();
                } else {
                    stopRecord();
                }
                tv_record.setBackgroundResource(R.drawable.shape_recoder_btn_normal);
                tv_record.setText(getContext().getString(R.string.down));
                setLevel(0);
                textView.setVisibility(View.INVISIBLE);
                this.dismiss();
                return true;
            default:
                break;
        }
        return false;
    }

    protected abstract void cancelRecord();

    protected abstract void stopRecord();

    protected abstract void startRecord();
}
