package com.yado.pryado.pryadonew.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;


import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.zxing.activity.CaptureActivity;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Created by Bob on 2016/9/5.11:40
 * Email: 631554401@qq.com
 */

public class NFCUtil {

    private WeakReference<Activity> mActivity;
    private NfcAdapter mAdapter;
    private String readResult;

    public NFCUtil(Activity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
        mAdapter = NfcAdapter.getDefaultAdapter(mActivity);
    }

    public void reset() {
        mAdapter = null;
        readResult = null;
    }

    public void init() {
        if (mAdapter == null) {
            Toast.makeText(mActivity.get(), R.string.no_nfc_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(mActivity.get(), R.string.open_nfc_toast, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= 16) {
                mActivity.get().startActivity(new Intent("android.settings.NFC_SETTINGS"));
            } else {
                mActivity.get().startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            }
        }

    }

    /* 开启 */
    public void enableForegroundDispatch() {
        PendingIntent mPendingIntent = PendingIntent.getActivity(mActivity.get(), 0, new Intent(mActivity.get(), CaptureActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(mActivity.get(), mPendingIntent, new IntentFilter[0], null);
    }

    /* 关闭 */
    public void disableForegroundDispatch() {
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(mActivity.get());
        }
    }

    /* 读取信息 - 在Activity的onNewIntent中调用*/
    public boolean readFromTag(Intent mIntent) {
        String action = mIntent.getAction();

        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
                action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {

            Tag mTag = mIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            for (int i = 0; i < mTag.getTechList().length; i++) {
                Log.i("NFCUtil",mTag.getTechList()[i]);
            }

            if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {

                Parcelable[] rawArray = mIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefMessage mNdefMessage = (NdefMessage) rawArray[0];

                for (int i = 0; i < mNdefMessage.getRecords().length; i++) {

                    NdefRecord mNdefRecord = mNdefMessage.getRecords()[i];

                    if (mNdefRecord != null && Arrays.equals(mNdefRecord.getType(), NdefRecord.RTD_TEXT)) {

                        byte[] data = mNdefRecord.getPayload();
                        String codeFormat;
                        if ((data[0] & 0x80) != 0) {
                            codeFormat = "UTF-16";
                        } else {
                            codeFormat = "UTF-8";
                        }
                        int n = data[0] & 0x33;

                        Log.i("NFCUtil",mNdefRecord.toMimeType() + " " + mNdefRecord.toString());

                        try {
                            readResult = new String(data, n + 1, data.length - n - 1, codeFormat);
                            Log.i("NFCUtil","readTag:" + readResult + " " + mNdefRecord.getTnf());
                            Log.i("NFCUtil","readTag:" + readResult.length());
                            return true;
                        } catch (UnsupportedEncodingException e) {
                            Log.e("NFCUtil", e.getMessage());
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }

    public String getReadResult() {
        return readResult;
    }
}

