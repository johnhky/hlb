package com.hlb.haolaoban;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.activity.ChatActivity;
import com.hlb.haolaoban.activity.PrescriptionActivity;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.bean.TokenBean;
import com.hlb.haolaoban.databinding.ActivityMainBinding;
import com.hlb.haolaoban.fragment.main.MainClubFragment;
import com.hlb.haolaoban.fragment.main.MainHomeFragment;
import com.hlb.haolaoban.fragment.main.MainMineFragment;
import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.FinishChatEvent;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.otto.LoginWebSocketEvent;
import com.hlb.haolaoban.otto.QueryMessageEvent;
import com.hlb.haolaoban.otto.TokenOutEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.NotificationUtil;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;


/**
 * Created by heky on 2017/10/31.
 */

public class MainActivity extends FragmentActivity {
    ActivityMainBinding binding;
    Fragment mainHome, mainClub, mainMine;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    public static final int PERMISSION_REQUEST_CODE = 2002;
    private long time = 100000000000L;
    WebSocketConnection webSocketConnection = new WebSocketConnection();
    private WebSocketOptions mWebSocketOptions = new WebSocketOptions();
    String webStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebSocketOptions.setSocketConnectTimeout(30000);
        mWebSocketOptions.setSocketReceiveTimeout(10000);
        BusProvider.getInstance().register(this);
        parseData();
        showToken();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkPermission();
        binding.titlebar.tbTitle.setText("好老伴");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mainHome == null) {
            mainHome = new MainHomeFragment();
            transaction.add(R.id.fragment_container, mainHome);
            binding.titlebar.toolBar.setNavigationIcon(null);
        }
        transaction.commit();
        initView();
        if (null != Hawk.get(Constants.MID)) {
            webStr = BuildConfig.BASE_WEBSOCKET_URL + "mid=" + Hawk.get(Constants.MID);
            login(webStr);
        }
    }

    private void login(final String url) {
        try {
            webSocketConnection.connect(url, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    sendMsg();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    if (code==2){
                        BusProvider.getInstance().postEvent(new LoginWebSocketEvent(webStr));
                    }
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(payload);
                        int code = jsonObject.optInt("nFlag");
                        String type = jsonObject.getString("type");
                        String channel = "";
                        if (code == 1) {
                            if (!TextUtils.isEmpty(type)) {
                                if (type.equals("calling")) {
                                    channel = jsonObject.getString("channel");
                                }
                                switch (type) {
                                    case "meet":
                                        BusProvider.getInstance().postEvent(new JoinVideoEvent(type));
                                        break;
                                    case "refuse":
                                        BusProvider.getInstance().postEvent(new FinishChatEvent("finish"));
                                        break;
                                    case "calling":
                                        BusProvider.getInstance().postEvent(new JoinVideoEvent(type, channel));
                                        break;
                                }
                            }
                        } else {
                            String msg = jsonObject.getString("msg");
                            String mode = jsonObject.getString("mode");
                            if (mode.equals("order")) {
                                switch (type) {
                                    case "1":
                                    case "2":
                                    case "3":
                                    case "4":
                                    case "5":
                                        NotificationUtil.showNotification(MyApplication.mContext, type, msg);
                                        break;
                                }
                            } else if (mode.equals("message")) {
                                switch (type) {
                                    case "1":
                                    case "4":
                                        NotificationUtil.showNotification(MyApplication.mContext, "", msg);
                                        break;
                                    case "2":
                                        if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                            saveMsg(payload, Hawk.get(Constants.MID) + "");
                                        }
                                        break;
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, mWebSocketOptions);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }

    }

    private void sendMsg() {
        CountDownTimer timer = new CountDownTimer(time, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                webSocketConnection.sendTextMessage("");
                Log.e("eeee", System.currentTimeMillis() + "");
            }

            @Override
            public void onFinish() {
                time = 10000000000L;
                sendMsg();
            }
        };
        timer.start();
    }

    public void showToken() {
        api.getToken(HttpUrls.getToken()).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                TokenBean data = gson.fromJson(response, TokenBean.class);
                if (null != data) {
                    Hawk.put(Constants.TOKEN, data.getToken());
                    Hawk.put(Constants.TOKENOUT, data.getTokenout());
                }
            }
        });
    }

    public void initView() {
        if (!isNotificationEnabled(MainActivity.this)) {
            Utils.showToast("为了您能准时收到消息和紧急通知,请开启好老伴的通知栏权限");
        }
        binding.mainRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                switch (checkedId) {
                    case R.id.main_radio_home:
                        if (mainHome == null) {
                            mainHome = new MainHomeFragment();
                            transaction.add(R.id.fragment_container, mainHome);
                        } else {
                            transaction.show(mainHome);
                        }
                        binding.titlebar.tbTitle.setText("好老伴");
                        binding.mainRadioHome.setTextColor(getResources().getColor(R.color.main_tab_color));
                        binding.mainRadioClub.setTextColor(getResources().getColor(R.color.gray_33));
                        binding.mainRadioMine.setTextColor(getResources().getColor(R.color.gray_33));
                        break;
                    case R.id.main_radio_club:
                        if (mainClub == null) {
                            mainClub = new MainClubFragment();
                            transaction.add(R.id.fragment_container, mainClub);
                        } else {
                            transaction.show(mainClub);
                        }
                        binding.titlebar.tbTitle.setText("俱乐部");
                        binding.mainRadioClub.setTextColor(getResources().getColor(R.color.main_tab_color));
                        binding.mainRadioHome.setTextColor(getResources().getColor(R.color.gray_33));
                        binding.mainRadioMine.setTextColor(getResources().getColor(R.color.gray_33));
                        break;
                    case R.id.main_radio_mine:
                        if (null != com.hlb.haolaoban.utils.Settings.getUserProfile()) {
                            if (mainMine == null) {
                                mainMine = new MainMineFragment();
                                transaction.add(R.id.fragment_container, mainMine);
                            } else {
                                transaction.show(mainMine);
                            }
                            binding.titlebar.tbTitle.setText("我的");
                            binding.mainRadioMine.setTextColor(getResources().getColor(R.color.main_tab_color));
                            binding.mainRadioHome.setTextColor(getResources().getColor(R.color.gray_33));
                            binding.mainRadioClub.setTextColor(getResources().getColor(R.color.gray_33));
                        } else {
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        break;
                }
                transaction.commit();
            }
        });
    }

    public void hideAllFragment(FragmentTransaction transaction) {
        if (mainHome != null) {
            transaction.hide(mainHome);
        }
        if (mainClub != null) {
            transaction.hide(mainClub);
        }
        if (mainMine != null) {
            transaction.hide(mainMine);
        }
    }

    @Subscribe
    public void onReceiveEvent(TokenOutEvent event) {
        if (event.getCode() == -99) {
            showToken();
        }
    }

    @Subscribe
    public void onReciveEvent(JoinVideoEvent event) {
        if (event.getType().equals("calling")) {
            Intent i = ChatActivity.intentFor(MainActivity.this, event.getChannel());
            startActivity(i);
        }
    }

    @Subscribe
    public void onReciveEvent(LoginWebSocketEvent event) {
        login(event.getUrl());
    }

    @Subscribe
    public void onReciveEvent(QueryMessageEvent event) {
        MsgHandler.queryMsg(com.hlb.haolaoban.utils.Settings.getUserProfile().getMid() + "", MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        checkPermission();
                    }
                }
                break;

        }
    }

    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        List<String> mPermissionList = new ArrayList<>();
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {
            String[] permission = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permission, PERMISSION_REQUEST_CODE);
        }
        Utils.mkDirs(Environment.getExternalStorageDirectory().getPath() + "/hlb/record/");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    boolean isExit = false;

    private void exit() {
        if (!isExit) {
            isExit = true;
            Utils.showToast("再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void parseData() {
        if (!TextUtils.isEmpty(getData())) {
            DialogUtils.showRemindMsg(MainActivity.this, getData(), new DialogUtils.OnDialogItemClickListener() {
                @Override
                public void onItemClick(int which) {
                    switch (which) {
                        case 1:
                            Intent i = new Intent(MainActivity.this, PrescriptionActivity.class);
                            startActivity(i);
                            break;
                    }
                }
            });
        }
    }

    private void saveMsg(String s, String id) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                jsonObject = new JSONObject(data);
                String drugs = jsonObject.getString("data");
                List<DrugRemind> list = gson.fromJson(drugs, new TypeToken<ArrayList<DrugRemind>>() {
                }.getType());
                MsgHandler.saveMsg(list, id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getData() {
        return getIntent().getStringExtra(Constants.DATA);
    }


}
