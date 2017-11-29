package com.hlb.haolaoban.activity.device;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.device.BloodPressureBean;
import com.hlb.haolaoban.databinding.ActivityPhysicalDataBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/24.
 */

public class PhysicalDataActivity extends BaseActivity {
    ActivityPhysicalDataBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    List<BloodPressureBean> list = new ArrayList<>();

    public static Intent intentFor(Context context, String type) {
        Intent i = new Intent(context, PhysicalDataActivity.class);
        i.putExtra(Constants.TYPE, type);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_physical_data);
        initView();
        initData();
    }

    private void initView() {
        String title = "";
        switch (getType()) {
            case "1":
                title = "7日血压最高值变化";
                break;
            case "2":
                title = "7日血压低值变化";
                break;
            case "3":
                title = "7日心率变化";
                break;
            case "4":
                title = "7日血糖变化";
                break;
            case "5":
                title = "7日体重变化";
                break;
            case "6":
                title = "7日BMI变化";
                break;
            case "7":
                title = "7日体脂率变化";
                break;
            case "8":
                title = "7日体水分变化";
                break;
            case "14":
                title = "7日蛋白质变化";
                break;
        }
        binding.titlebar.tbTitle.setText(title);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        final long currentTime = System.currentTimeMillis() / 1000;
        final long sevenDayTimeStamp = currentTime - 86400 * 6;
        api.getBaseUrl(HttpUrls.getRealTime(Settings.getUserProfile().getMid() + "", getType(), sevenDayTimeStamp + "", currentTime + "")).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                if (response.length() < 7) {
                    showToast("暂无相关数据");
                    return;
                } else {
                    binding.llChart.setVisibility(View.VISIBLE);
                    list = gson.fromJson(response, new TypeToken<ArrayList<BloodPressureBean>>() {
                    }.getType());

                    List<String> lists = new ArrayList<>();
                    lists.add(Utils.stampToMonth(sevenDayTimeStamp + "000"));
                    lists.add(Utils.stampToMonth((currentTime - 86400 * 5) + "000"));
                    lists.add(Utils.stampToMonth((currentTime - 86400 * 4) + "000"));
                    lists.add(Utils.stampToMonth((currentTime - 86400 * 3) + "000"));
                    lists.add(Utils.stampToMonth((currentTime - 86400 * 2) + "000"));
                    lists.add(Utils.stampToMonth((currentTime - 86400 * 1) + "000"));
                    lists.add(Utils.stampToMonth(currentTime + "000"));
                    setupChart(binding.lineChart, lists);
                }

            }
        });
    }

    // 设置显示的样式
    void setupChart(LineChart chart, final List<String> lists) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            values.add(new Entry(i, list.get(i).getValue()));
        }
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (list.size() == 1) {
                    value = 0;
                }
                return lists.get((int) value);
            }

        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);  //最小轴步骤（间隔）为1
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        String title = "";
        switch (getType()) {
            case "1":
                title = "七日最高血压值变化";
                break;
            case "2":
                title = "七日最低血压值变化";
                break;
            case "3":
                title = "7日心率变化";
                break;
            case "4":
                title = "7日血糖变化";
                break;
            case "5":
                title = "7日体重变化(kg)";
                break;
            case "6":
                title = "7日BMI变化";
                break;
            case "7":
                title = "7日体脂率变化";
                break;
            case "8":
                title = "7日体水分变化";
                break;
            case "14":
                title = "7日蛋白质变化";
                break;
        }
        LineDataSet set1 = new LineDataSet(values, title);
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);
        chart.setData(data);
    }

    private String getType() {
        return getIntent().getStringExtra(Constants.TYPE);
    }


}


