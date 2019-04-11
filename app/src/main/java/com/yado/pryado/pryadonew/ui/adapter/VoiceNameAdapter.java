package com.yado.pryado.pryadonew.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yado.pryado.pryadonew.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceNameAdapter extends BaseAdapter {

    private List<String> ringList;
    private Context mContext;
    private RingtoneManager rm;
    @SuppressLint("UseSparseArrays")
    public Map<Integer, Boolean> map = new HashMap<>();
    private ViewHolder holder;
    private boolean firstItemState = true;

    /**
     * 构造方法，index参数作为记录所选铃声的position传入SharedPreferences记录并调取。
     */
    public VoiceNameAdapter(Context context, int index) {
        this.mContext = context;
        if (firstItemState) {
            firstItemState = false;
            map.put(index, true);
        }
        getRing();
    }

    public void getRing() {
        /* 新建一个arraylist来接收从系统中获取的短信铃声数据 */
        ringList = new ArrayList<String>();
        /* 添加“跟随系统”选项 */
        ringList.add("跟随系统");
        /* 获取RingtoneManager */
        rm = new RingtoneManager(mContext);
        /* 指定获取类型为短信铃声 */
        rm.setType(RingtoneManager.TYPE_NOTIFICATION);
        /* 创建游标 */
        Cursor cursor = rm.getCursor();
        /* 游标移动到第一位，如果有下一项，则添加到ringlist中 */
        if (cursor.moveToFirst()) {
            do { // 游标获取RingtoneManager的列inde x
                ringList.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public int getCount() {
        return ringList.size();
    }

    @Override
    public Object getItem(int position) {
        return ringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.voice_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setBackgroundResource(map.get(position) == null ? R.drawable.pressed: R.drawable.checked);
        holder.tv.setText(ringList.get(position));
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv;
        public ImageView iv;

        public ViewHolder(View v) {
            this.tv =  v.findViewById(R.id.tv_voice_name);
            this.iv =  v.findViewById(R.id.iv_selected);
        }
    }
}