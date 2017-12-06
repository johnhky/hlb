package com.hlb.haolaoban.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.mine.BigImageActivity;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.PrescriptionDetailBean;
import com.hlb.haolaoban.databinding.ActivityPrescriptionDetailBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.http.WechatCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.otto.BusProvider;
import com.hlb.haolaoban.otto.PaySuccessEvent;
import com.hlb.haolaoban.otto.RefreshOrderEvent;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Utils;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heky on 2017/11/15.
 */

public class PrescriptionDetailActivity extends BaseActivity {

    ActivityPrescriptionDetailBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    int type = 1;

    public static Intent intentFor(Context context, String oid) {
        Intent i = new Intent(context, PrescriptionDetailActivity.class);
        i.putExtra(Constants.DATA, oid);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_prescription_detail);
        initView();
        getData();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("处方详情");
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        api.getBaseUrl(HttpUrls.getOrderDetail(getOid())).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                PrescriptionDetailBean data = gson.fromJson(response, PrescriptionDetailBean.class);
                initData(data);
            }
        });
    }

    private void initData(final PrescriptionDetailBean data) {
        switch (data.getStatus()) {
            case "1":
                binding.tvDone.setText("已付款");
                binding.tvStatus.setText("订单已付款");
                break;
            case "2":
                binding.tvDone.setVisibility(View.GONE);
                binding.tvPay.setVisibility(View.VISIBLE);
                binding.llDone.setVisibility(View.GONE);
                binding.llPay.setVisibility(View.VISIBLE);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long maxTime = data.getAddtime() + 86400;
                String date = Utils.formatData(maxTime);
                long currentTime = System.currentTimeMillis() / 1000;
                String date1 = Utils.formatData(currentTime);
                try {
                    Date date2 = df.parse(date);
                    Date date3 = df.parse(date1);
                    long millis = date2.getTime() - date3.getTime();
                    CountDownTimer timer = new CountDownTimer(millis, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long days = millisUntilFinished / (1000 * 60 * 60 * 24);
                            long hours = (millisUntilFinished - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                            long minutes = (millisUntilFinished - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                            long seconds = (millisUntilFinished - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
                            binding.tvTime.setText(hours + "时" + minutes + "分" + seconds + "秒" + "后自动过期");
                        }

                        @Override
                        public void onFinish() {
                            finish();
                            Utils.showToastLong("订单已过期!");
                            BusProvider.getInstance().postEvent(new RefreshOrderEvent());
                        }
                    };
                    timer.start();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                binding.tvDone.setText("已配送");
                binding.tvStatus.setText("订单已配送");
                break;
            case "4":
                binding.tvDone.setText("已完成");
                binding.tvStatus.setText("订单已完成");
                break;
            case "5":
                binding.tvDone.setText("已过期");
                binding.tvStatus.setText("订单已过期");
                break;
        }
        Glide.with(mActivity).load(data.getKuaizhao_img()).fitCenter().into(binding.ivPicture);
        binding.tvAddress.setText(data.getShip_address() + "");
        binding.tvFreight.setText(data.getCost_freight() + "");
        binding.tvDrugCount.setText(data.getCost_item() + "");
        binding.tvReceive.setText(data.getShip_name());
        String phone = "";
        if (!TextUtils.isEmpty(data.getShip_mobile())) {
            phone = data.getShip_mobile();
            binding.tvPhone.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
        }
        binding.tvOrderId.setText("订单编号:" + data.getOid());
        binding.tvAddTime.setText("下单时间:" + Utils.formatData(data.getAddtime()));
        binding.tvName.setText(data.getDiagnosis() + "");
        binding.tvTotal.setText(data.getTotal_fee() + "");
        binding.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = BigImageActivity.intentFor(mActivity, data.getKuaizhao_img());
                startActivity(i);
            }
        });
        binding.tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showPayDialog(mActivity, v, new DialogUtils.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int which) {
                        switch (which) {
                            case 1:
                                type = 1;
                                break;
                            case 2:
                                type = 2;
                                break;
                            case 3:
                                if (type == 1) {
                                    wechatPay(String.valueOf(data.getMid()), data.getOid());
                                }
                                break;
                        }
                    }
                });
            }
        });
    }

    private void wechatPay(String mid, String oid) {
        api.getBaseUrl(HttpUrls.wechatPay(mid, oid)).enqueue(new WechatCallback(this));
    }

    private String getOid() {
        return getIntent().getStringExtra(Constants.DATA);
    }


    @Subscribe
    public void onReceiveEvent(PaySuccessEvent event) {
        Utils.showToastLong("支付成功!");
        getData();
    }

}
