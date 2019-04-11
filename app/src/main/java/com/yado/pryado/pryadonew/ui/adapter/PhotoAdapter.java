package com.yado.pryado.pryadonew.ui.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.R;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class PhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    @Inject
    public PhotoAdapter() {
        super(R.layout.image_item_photo, null);
    }

    public PhotoAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext)
                .load(item)
                .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into((ImageView) helper.getView(R.id.iv_photo));
    }
}
