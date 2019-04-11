package com.yado.pryado.pryadonew.zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.bean.OrderList;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.net.PRRetrofit;
import com.yado.pryado.pryadonew.subscriber.DefaultDisposablePoolImpl;
import com.yado.pryado.pryadonew.subscriber.PRSubscriber;
import com.yado.pryado.pryadonew.ui.device_detail.DeviceDetailActivity;
import com.yado.pryado.pryadonew.ui.roomDetail.RoomDetailActivity;
import com.yado.pryado.pryadonew.ui.task.TaskActivity;
import com.yado.pryado.pryadonew.ui.widgit.ResultDialog;
import com.yado.pryado.pryadonew.util.FixMemLeak;
import com.yado.pryado.pryadonew.util.NFCUtil;
import com.yado.pryado.pryadonew.util.RealPathUtil;
import com.yado.pryado.pryadonew.zxing.camera.CameraManager;
import com.yado.pryado.pryadonew.zxing.decoding.CaptureActivityHandler;
import com.yado.pryado.pryadonew.zxing.decoding.InactivityTimer;
import com.yado.pryado.pryadonew.zxing.decoding.RGBLuminanceSource;
import com.yado.pryado.pryadonew.zxing.view.FinderView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
@Route(path = "/zxing/activity/CaptureActivity")
public class CaptureActivity extends AppCompatActivity implements Callback, DialogInterface.OnDismissListener {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private FinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    private NFCUtil nfcUtil;
    private boolean supportNFC = false;
    private String TAG = "CaptureActivity";
    private ResultDialog dialog;
    private String scanResult;
    private boolean showChoiceOrder = false;
    private boolean nfcStarted = false;
    private ImageView nfcView;
    private CheckBox checkBox;
    private ArrayList<RoomListBean.RowsEntity> rooms;
    private boolean hasThisRoom = false;
    private String type;
    private String content;
    private String pid;
    private TextView tv_white;
    private Camera.Parameters parameters;
    //    private Camera camlignt;
    //	private Button cancelScanButton;

    DefaultDisposablePoolImpl disposablePool;

    private boolean autoEnlarged = false;//是否具有自动放大功能(功能仅仅限扫描的是二维码，条形码不放大)

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        autoEnlarged = getIntent().getBooleanExtra("autoEnlarged", false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            int permission = checkSelfPermission(Manifest.permission.CAMERA);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }

        }
        CameraManager.init(getApplication());
        viewfinderView = (FinderView) findViewById(R.id.viewfinder_content);
        nfcView = (ImageView) findViewById(R.id.iv_nfc);
        viewfinderView.setOnTextClickListener(new FinderView.OnTextClickListener() {
            @Override
            public void OnTextClick(int checkedPos) {
                switch (checkedPos) {
                    case 1://二维码
                        nfcStarted = false;
                        stopNFC();
                        nfcView.setVisibility(View.GONE);
                        break;
                    case 2://NFC
                        nfcStarted = true;
                        startNFC();
                        nfcView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        disposablePool = new DefaultDisposablePoolImpl();
        initTopBar();
        getRoomList();
    }

    private void initTopBar() {
        findViewById(R.id.tv_shouye).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
        checkBox = (CheckBox) findViewById(R.id.cb_light);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CameraManager cameraManager = CameraManager.get();
//                try {
//                    Class userCla = (Class) cameraManager.getClass();
//                    Field[] fs = userCla.getDeclaredFields();
//                    for (int i = 0; i < fs.length; i++) {
//                        Field field = fs[i];
//                        field.setAccessible(true); //设置些属性是可以访问的
//                        Object val = null;//得到此属性的值
//                        val = field.get(cameraManager);
//                        if ("camera".equals(field.getName())) {
////                            KLog.e(field.getType() + "\t是camera");
//                            Camera.Parameters p = ((Camera) val).getParameters();
//                            String mode = p.getFlashMode();
////                            KLog.e("之前="+mode);
//                            if (checkBox.isChecked()) {
//                                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                            } else {
//                                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                            }
//                            ((Camera) val).setParameters(p);
//                        }
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
                setTorch(checkBox.isChecked());

            }
        });
        tv_white = (TextView) findViewById(R.id.tv_white);
        tv_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parameters = CameraManager.get().getCamera().getParameters();
                changeWhite(tv_white);
            }
        });
    }

    public void setTorch(boolean isOpen) {
        Camera.Parameters parameters = CameraManager.get().getCamera().getParameters();
        // 判断闪光灯当前状态來修改
        if (isOpen) {
            turnOn(parameters);
        } else  {
            turnOff(parameters);
        }
    }

    private void turnOff(Camera.Parameters parameters) {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        CameraManager.get().getCamera().setParameters(parameters);
    }

    private void turnOn(Camera.Parameters parameters) {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        CameraManager.get().getCamera().setParameters(parameters);
    }


    //5、设置白平衡
    public void changeWhite(final TextView tv_white) {
        PopupMenu pop = new PopupMenu(this, tv_white);
        pop.getMenuInflater().inflate(R.menu.menu_white, pop.getMenu());
        pop.show();
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.white_balance_auto) {
                    parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                } else if (i == R.id.white_balance_cloudy_daylight) {
                    parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
                } else if (i == R.id.white_balance_daylight) {
                    parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_DAYLIGHT);
                } else if (i == R.id.white_balance_fluorescent) {
                    parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
                } else if (i == R.id.white_balance_incandescent) {
                    parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_INCANDESCENT);
                }
                tv_white.setText("白平衡/" + item.getTitle());
                CameraManager.get().getCamera().setParameters(parameters);
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (dialog != null && dialog.isVisible()) {
            return;
        }
        setIntent(intent);
        if (nfcUtil != null && nfcUtil.readFromTag(intent)) {
            Log.e(TAG, "获取到:" + nfcUtil.getReadResult());
            this.scanResult = nfcUtil.getReadResult();
            startActivityByResult();
        }
    }

    private void stopNFC() {
//        TUtil.t("stop NFC");
        if (nfcUtil != null) {
            nfcUtil.disableForegroundDispatch();
        }
    }

    private void startNFC() {
        nfcStarted = true;
        if (nfcUtil == null) {
            try {
                nfcUtil = new NFCUtil(this);
                nfcUtil.init();
                nfcUtil.enableForegroundDispatch();
                supportNFC = true;
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort("NFC开启失败!请检查是否支持。");
                supportNFC = false;
                nfcUtil = null;
                return;
            }
        }
        if (supportNFC) {
            nfcUtil.enableForegroundDispatch();
        }
//        if (nfcUtil != null && supportNFC)
//            TUtil.t("请靠近NFC标签");
    }

    private void continuePreview() {
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_local:
                //打开手机中的相册
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
                this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    Uri originalUri = data.getData();        //获得图片的uri
                    photo_path = RealPathUtil.getRealPathFromURI_API19(this, originalUri);
                    Log.e("lcy", "path=" + photo_path);
                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(true);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
//                                Message m = handler.obtainMessage();
//                                m.what = R.id.decode_succeeded;
//                                m.obj = hasOrders.getText();
//                                handler.sendMessage(m);
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("hasOrders", result.getText());
                                mProgress.dismiss();
                                Log.e("lcy", "hasOrders=" + result);
//                                bundle.putParcelable("bitmap",hasOrders.get);
                                resultIntent.putExtras(bundle);
                                CaptureActivity.this.setResult(RESULT_OK, resultIntent);

                            } else {
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("hasOrders", "Scan failed!");
                                mProgress.dismiss();
                                Log.e("lcy", "hasOrders=" + result);
//                                bundle.putParcelable("bitmap",hasOrders.get);
                                resultIntent.putExtras(bundle);
                                CaptureActivity.this.setResult(RESULT_OK, resultIntent);
//                                Message m = handler.obtainMessage();
//                                m.what = R.id.decode_failed;
//                                m.obj = "Scan failed!";
//                                handler.sendMessage(m);
                            }
                            CaptureActivity.this.finish();
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
//        QRCodeReader reader = new QRCodeReader(CaptureActivity.this);
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        restartNfc();
        //quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				CaptureActivity.this.finish();
//			}
//		});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setNull();
        FixMemLeak.fixLeak(this);

    }

    private void setNull() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
            inactivityTimer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer = null;
            beepListener = null;
        }
        if (nfcUtil != null) {
            nfcUtil.reset();
        }
        parameters = null;
        CameraManager.get().reset();
        if (rooms != null) {
            rooms.clear();
            rooms = null;
        }
        disposablePool.clearPool();
        disposablePool = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
//            handleFocus(event, CameraManager.get().getCamera());
        } else {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = getFingerSpacing(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newDist = getFingerSpacing(event);
                    if (newDist > oldDist) {
                        handleZoom(true, CameraManager.get().getCamera());
                    } else if (newDist < oldDist) {
                        handleZoom(false, CameraManager.get().getCamera());
                    }
                    oldDist = newDist;
                    break;
                default:
                    break;
            }
        }
        return true;
    }


    private static void handleFocus(MotionEvent event, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        Camera.Size previewSize = params.getPreviewSize();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, previewSize);

        camera.cancelAutoFocus();

        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            Log.i("CaptureActivity", "focus areas not supported");
        }
        final String currentFocusMode = params.getFocusMode();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        camera.setParameters(params);

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(currentFocusMode);
                camera.setParameters(params);
            }
        });

        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, previewSize);

        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 800));
            params.setMeteringAreas(meteringAreas);
        } else {
            Log.i("CaptureActivity", "metering areas not supported");
        }

    }

    private static Rect calculateTapArea(float x, float y, float coefficient, Camera.Size previewSize) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / previewSize.width - 1000);
        int centerY = (int) (y / previewSize.height - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();
            if (isZoomIn && zoom < maxZoom) {
                zoom++;
            } else if (zoom > 0) {
                zoom--;
            }
            params.setZoom(zoom);
            camera.setParameters(params);
        } else {
            Log.i(TAG, "zoom not supported");
        }
    }

    private float oldDist = 1f;


    /**
     * Handler scan hasOrders
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            ToastUtils.showShort("扫描失败！");
        } else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("hasOrders", resultString);
//            judge(resultString);
            Log.e("lcy", "二维码result=" + resultString);
            this.scanResult = resultString;
            startActivityByResult();
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
        }
//        CaptureActivity.this.finish();
    }

    private void startActivityByResult() {
        if (scanResult.length() > 11 || scanResult.length() < 10) {
            ToastUtils.showShort("非法编码");
            continuePreview();
            return;
        }
        if (scanResult.length() == 10) {
            scanResult += "0";
        }
//        if (TextUtils.isEmpty(scanResult) || scanResult.length() != 11) {
//            TUtil.t("非法编码");
//            continuePreview();
//            return;
//        }
        judge();
    }

    /**
     * 判断是否显示 处理工单；
     */
    public void judge() {
        showChoiceOrder = false;
        type = scanResult.substring(0, 1);
        content = scanResult.substring(1, 10);
        pid = "" + Integer.valueOf(content);
        if (rooms != null && rooms.size() > 0) {
            hasThisRoom = false;
            for (int i = 0; i < rooms.size(); i++) {
                if (pid.equals(rooms.get(i).getPID() + "")) {
                    hasThisRoom = true;
                    RoomListBean.RowsEntity room = rooms.get(i);
                    break;
                }
            }
        } else {
            ToastUtils.showShort(R.string.error_room);
        }
        if ("1".equals(type)) {//配电房编码
            if (hasThisRoom) {
                getOrderList(pid);
            } else {
                ToastUtils.showShort(R.string.has_no_room);
                continuePreview();
            }
        } else if ("2".equals(type)) {//设备编码
            showDialogFragment();
        }
    }

    private void getRoomList() {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi().getPDRList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<RoomListBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposablePool.addDisposable(d);
                    }

                    @Override
                    public void onNext(RoomListBean roomListBean) {
                        rooms = roomListBean.getRows();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "--onError--" + e.toString());
                        ToastUtils.showShort("请求服务器错误...");
                    }


                });
    }

    private void getOrderList(String pid) {
        PRRetrofit.getInstance(MyApplication.getInstance()).getApi().getOrderList(pid, MyConstants.ORDER_TYPE_ACCEPTET)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PRSubscriber<OrderList>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposablePool.addDisposable(d);
                    }

                    @Override
                    public void onNext(OrderList orderList) {
                        try {
                            int orderID = orderList.getListmap().get(0).getOrderID();
                            Log.e(TAG, orderList.getListmap().size() + "orderID=" + orderID);
                            if (orderID > 0) showChoiceOrder = true;
                            else {
                                showChoiceOrder = false;
                            }
                        } catch (Exception e) {
                            showChoiceOrder = false;
                        } finally {
                            showDialogFragment();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                });

    }

    /**
     * 显示对话框是否包含工单；
     */
    private void showDialogFragment() {
        if (dialog != null && dialog.isVisible()) {
            return;
        }
        if (dialog == null) {
            dialog = new ResultDialog();
            Bundle bundle = new Bundle();
            bundle.putBoolean(MyConstants.HASORDERS, showChoiceOrder);
            bundle.putBoolean(MyConstants.HASROOM, hasThisRoom);
            bundle.putString(MyConstants.DangerType, type);
            dialog.setArguments(bundle);
            dialog.show(getSupportFragmentManager(), scanResult);
            dialog.setOnClickListenerOrder(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//处理工单
                    Log.e(TAG, "type=" + type);
                    switch (type) {
                        case "1":
                            Log.e(TAG, "配电房ID=" + pid);
                            if (showChoiceOrder) {//有已经受理的工单；
//                                Intent intent = new Intent(activity, TaskActivity.class);
//                                intent.putExtra(MyConstants.PID, Integer.valueOf(pid));
//                                intent.putExtra(MyConstants.FROM_SCAN, true);
//                                startActivity(intent);
                                ARouter.getInstance().build(MyConstants.TASK)
                                        .withBoolean(MyConstants.FROM_SCAN, true)
                                        .withInt(MyConstants.PID, Integer.valueOf(pid))
                                        .navigation();
                            }
                            break;
                        default:
                            ToastUtils.showShort("非配电房编码");
                            break;
                    }
                    dialogDismiss();
                }
            });
            dialog.setOnClickListenerGoRoom(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rooms != null && rooms.size() > 0) {
                        hasThisRoom = false;
                        for (int i = 0; i < rooms.size(); i++) {
                            if (pid.equals(rooms.get(i).getPID() + "")) {
                                hasThisRoom = true;
                                RoomListBean.RowsEntity room = rooms.get(i);
//                                Intent intent = new Intent(CaptureActivity.this, RoomDetailActivity.class);
//                                intent.putExtra("room", room);
//                                intent.putParcelableArrayListExtra("room_list", rooms);
//                                intent.putExtra("position", i);
//                                startActivity(intent);
                                ARouter.getInstance().build(MyConstants.ROOM_DETAIL)
                                        .withParcelable("room", room)
                                        .withParcelableArrayList("room_list", rooms)
                                        .withInt("position", i)
                                        .navigation();
                                break;
                            }
                        }
                    } else {
                        ToastUtils.showShort(R.string.error_room);
                    }
                    dialogDismiss();
                }
            });
            dialog.setOnClickListenerUp(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//隐患上报；
                    if (!TextUtils.isEmpty(scanResult)) {
                        switch (type) {
                            case "2":
                                Log.e(TAG, "设备ID=" + pid);
//                                Intent intent = new Intent(activity, DeviceDetailActivity.class);
//                                intent.putExtra(MyConstants.DID, pid);
//                                intent.putExtra(MyConstants.ENCODE, scanResult);
//                                startActivity(intent);
                                ARouter.getInstance().build(MyConstants.DEVICE_DETAIL)
                                        .withString(MyConstants.DID, pid)
                                        .withString(MyConstants.ENCODE, scanResult)
                                        .navigation();
                                finish();
                                break;
                            default:
                                ToastUtils.showShort("非设备编码");
                                break;
                        }
                        dialogDismiss();
                    }
                }
            });
            dialog.setOnClickListenerClose(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDismiss();
                }
            });
        } else {
            try {
                Bundle bundle = new Bundle();
                bundle.putBoolean(MyConstants.HASORDERS, showChoiceOrder);
                bundle.putBoolean(MyConstants.HASROOM, hasThisRoom);
                bundle.putString(MyConstants.DangerType, type);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), scanResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dialogDismiss() {
        dialog.dismiss();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public FinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private OnCompletionListener beepListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    public void onDismiss(DialogInterface dialog) {
        continuePreview();
    }

    private void restartNfc() {
        if (nfcStarted) {
            startNFC();
        } else {
            stopNFC();
        }
    }

    public boolean isAutoEnlarged() {
        return autoEnlarged;
    }

}