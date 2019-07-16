package com.yado.pryado.pryadonew.ui;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.VoiceChange;
import com.yado.pryado.pryadonew.ui.adapter.VoiceNameAdapter;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/ui/ChoiceVoiceActivity")
public class ChoiceVoiceActivity extends BaseActivity {

    @BindView(R.id.tv_shouye)
    LinearLayout tvShouye;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.lv_voice_names)
    ListView listView_names;
    @BindView(R.id.tv_pre)
    TextView tvPre;
    private String Sp_key = "ring";
    private VoiceNameAdapter mAdapter;

    @Override
    public int inflateContentView() {
        return R.layout.activity_choice_voice;
    }

    @Override
    protected void initData() {
        tvShouye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            name.setText(title);
            if (getResources().getString(R.string.voice_notice).equals(title)) {
                Sp_key = "ring";
            } else {
                Sp_key = "ring2";
            }
        }
        tvPre.setText(getResources().getString(R.string.back));
        mAdapter = new VoiceNameAdapter(mContext, SharedPrefUtil.getInstance(MyApplication.getInstance()).getInt(Sp_key, 0));
        listView_names.setAdapter(mAdapter);
        listView_names.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView_names.setOnItemClickListener(mOnItemClickListener);

    }

    /*listView的按钮点击事件*/
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            VoiceNameAdapter.ViewHolder mHolder = new VoiceNameAdapter.ViewHolder(parent);
            /*设置Imageview不可被点击*/
            mHolder.iv.setClickable(false);
            /*清空map*/
            mAdapter.map.clear();
            // mAdapter.map.put(position, 1);
            /*将所点击的位置记录在map中*/
            mAdapter.map.put(position, true);

            SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(Sp_key, listView_names.getCheckedItemPosition());
            ToastUtils.showShort("提示音保存成功!");
            EventBus.getDefault().post(new VoiceChange(Sp_key, position));
//            }
            /*刷新数据*/
            mAdapter.notifyDataSetChanged();
            /*判断位置不为0则播放的条目为position-1*/
            if (position != 0) {
                try {
                    RingtoneManager rm = new RingtoneManager(mContext);
                    rm.setType(RingtoneManager.TYPE_NOTIFICATION);
                    Cursor cursor = rm.getCursor();
                    rm.getRingtone(position - 1).play();

//                    SharedPrefUtil.getInstance(ChoiceVoiceActivity.this).saveObject(CURSOR_POSITION, position-1);
                    Uri uri = rm.getRingtoneUri(position - 1);
                    SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.CURSOR_POSITION, uri.toString());
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*position为0是跟随系统，先得到系统所使用的铃声，然后播放*/
            if (position == 0) {
                Uri uri = RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_NOTIFICATION);
                SharedPrefUtil.getInstance(MyApplication.getInstance()).saveObject(MyConstants.CURSOR_POSITION, uri.toString());
                RingtoneManager.getRingtone(mContext, uri).play();
            }
        }
    };

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

}
