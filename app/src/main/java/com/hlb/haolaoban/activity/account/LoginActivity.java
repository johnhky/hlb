package com.hlb.haolaoban.activity.account;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.BuildConfig;
import com.hlb.haolaoban.MainActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.bean.Userbean;
import com.hlb.haolaoban.databinding.ActivityLoginBinding;
import com.hlb.haolaoban.handler.MsgHandler;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.FinishChatEvent;
import com.hlb.haolaoban.otto.JoinVideoEvent;
import com.hlb.haolaoban.otto.ShowNotificationEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Utils;
import com.orhanobut.hawk.Hawk;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

/**
 * Created by heky on 2017/10/31.
 */

public class LoginActivity extends BaseActivity {
    Gson gson;
    ActivityLoginBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    private final String TAG = this.getClass().getSimpleName();
    WebSocketClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        gson = new GsonBuilder().create();
        initView();
    }

    private void initView() {
        if (null != Hawk.get(Constants.PHONE)) {
            binding.etPhone.setText(Hawk.get(Constants.PHONE) + "");
        }
        if (null != Hawk.get(Constants.PASSWORD)) {
            binding.etPassword.setText(Hawk.get(Constants.PASSWORD) + "");
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        binding.tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgetPasswordActivity1.class);
            }
        });
    }

    private void login() {
        DialogUtils.showLoading(mActivity, "正在登录...");
        if (TextUtils.isEmpty(binding.etPhone.getText().toString().trim()) || binding.etPhone.getText().toString().trim().length() < 1) {
            showToast("手机号码不能为空!");
            return;
        }

        if (!Utils.isMobile(binding.etPhone.getText().toString().trim())) {
            showToast("请输入正确的手机号码!");
            return;
        }
        if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim()) || binding.etPassword.getText().toString().trim().length() < 1) {
            showToast("密码不能为空!");
            return;
        }
        api.login(HttpUrls.login(binding.etPhone.getText().toString().trim(), binding.etPassword.getText().toString().trim())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                DialogUtils.hideLoading(mActivity);
                Userbean data = gson.fromJson(response, Userbean.class);
                saveMsg(data.getMid() + "");
                Hawk.put(Constants.PHONE, binding.etPhone.getText().toString().trim());
                Hawk.put(Constants.PASSWORD, binding.etPassword.getText().toString().trim());
                Hawk.put(Constants.MID, data.getMid());
                Hawk.put(Constants.CLUB_ID, data.getClub_id());
                loginWebClient(data.getMid() + "", data.getClub_id() + "");
                startActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.hideLoading(mActivity);
            }
        });
    }

    private void saveMsg(String id) {
        String s = "{\n" +
                "    \"from\": \"143\",\n" +
                "    \"type\": \"2\",\n" +
                "    \"msg\": \"用药提醒规则,收到后系统配置成闹钟模式\",\n" +
                "    \"data\": {\n" +
                "        \"id\": \"15111669839712\",\n" +
                "        \"msg\": \"1312421414214 的用药提醒~\",\n" +
                "        \"data\": [\n" +
                "            {\n" +
                "                \"msg\": \"养胃宁胶囊(天泰) 3粒\",\n" +
                "                \"endtime\": \"1511771783\",\n" +
                "                \"stat_his\": \"20:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"msg\": \"养胃宁胶囊(天泰) 3粒\",\n" +
                "                \"endtime\": \"1511771783\",\n" +
                "                \"stat_his\": \"09:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"msg\": \"摩罗丹(华山牌) 2箱\",\n" +
                "                \"endtime\": \"1511598983\",\n" +
                "                \"stat_his\": \"17:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"msg\": \"摩罗丹(华山牌) 2箱\",\n" +
                "                \"endtime\": \"1511598983\",\n" +
                "                \"stat_his\": \"11:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"msg\": \"摩罗丹(华山牌) 2箱\",\n" +
                "                \"endtime\": \"1511598983\",\n" +
                "                \"stat_his\": \"07:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"msg\": \"达比加群酯胶囊(泰毕全) 6支\",\n" +
                "                \"endtime\": \"1511595509\",\n" +
                "                \"stat_his\": \"20:00\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"time\": \"1511166983\",\n" +
                "    \"taskid\": 35,\n" +
                "    \"mode\": \"message\",\n" +
                "    \"to\": 143\n" +
                "}";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                jsonObject = new JSONObject(data);
                String drugs = jsonObject.getString("data");
                List<DrugRemind> list = gson.fromJson(drugs, new TypeToken<ArrayList<DrugRemind>>() {
                }.getType());
           /*     long currentTime = System.currentTimeMillis();
                List<DrugRemind> descList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    DrugRemind drug = new DrugRemind();
                    String date = Utils.stampToDate(currentTime + "") + " " + list.get(i).getStat_his() + ":00";
                    long time = Utils.timeToStamp(date);
                    long endTime = Long.parseLong(list.get(i).getEndtime());
                    drug.setEndtime(list.get(i).getEndtime());
                    if (endTime > time) {
                        drug.setStat_his(list.get(i).getStat_his());
                        drug.setId(i);
                        drug.setMid(id);
                        descList.add(drug);
                    }
                }*/
                MsgHandler.saveMsg(list, id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginWebClient(final String id, String club_id) {
        String url = BuildConfig.BASE_WEBSOCKET_URL + "?mid=" + id + "&club_id=" + club_id;
        client = new WebSocketClient(URI.create(url), new Draft_17(), null, 3000) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("eeee", "连接成功!");
            }

            @Override
            public void onMessage(String s) {
                Log.e(TAG, "onMessage:" + s);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(s);
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
                        switch (type) {
                            case "1":
                            case "4":

                                break;
                            case "2":
                                String data = jsonObject.getString("data");
                                if (!TextUtils.isEmpty(data)) {
                                    jsonObject = new JSONObject(data);
                                    String drugs = jsonObject.getString(data);
                                    Log.e("eeee", drugs);
                                }
                                break;
                            case "3":
                                DialogUtils.showRemindMsg(mActivity, msg, new DialogUtils.OnDialogItemClickListener() {
                                    @Override
                                    public void onItemClick(int which) {

                                    }
                                });
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {

            }
        };
        client.connect();
/*        Timer timer = new Timer();
        timer.schedule(timerTask, 10000);*/
    }

  /*  TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            msg.arg1 = 1;
            msg.sendToTarget();
        }
    };*/
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.arg1) {
//                case 1:
//                    if (client != null) {
//                        try {
//                            client.send("");
//                        } catch (WebsocketNotConnectedException e) {
//                        }
//                    }
//                    break;
//
//            }
//        }
//    };
}
