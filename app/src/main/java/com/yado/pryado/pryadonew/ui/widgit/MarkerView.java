package com.yado.pryado.pryadonew.ui.widgit;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yado.pryado.pryadonew.R;

public class MarkerView extends LinearLayout {

    private String title;
    private  int imageResource;
    private Context context;
    private TextView textView;
    private ImageView imageView;

    public MarkerView(Context context) {
        super(context);
        this.context=context;
        this.setOrientation(VERTICAL);
    }
    public MarkerView setTitle(int imageResource, String title){
        this.removeAllViews();
        this.title =title;
        this.imageResource=imageResource;
        if (title!=null){
            textView = new TextView(context);
            textView.setText(title);
            textView.setTextColor(Color.GRAY);
            textView.setPadding(10,5,10,5);
            textView.setBackgroundResource(R.drawable.shape_white);
            addView(textView);
        }
        imageView = new ImageView(context);
        imageView.setImageResource(imageResource);
        addView(imageView);
        return this;
    }
    public MarkerView setText(String title){
        textView.setText(title);
        return this;
    }
}
