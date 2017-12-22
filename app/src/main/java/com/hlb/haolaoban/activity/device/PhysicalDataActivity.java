package com.hlb.haolaoban.activity.device;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
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
    String title = "";
    private boolean isLoading = false;
    long currentTime;
    long sevenDayTimeStamp;

    public static Intent intentFor(Context context, String type) {
        Intent i = new Intent(context, PhysicalDataActivity.class);
        i.putExtra(Constants.TYPE, type);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_physical_data);
        currentTime = System.currentTimeMillis() / 1000;
        sevenDayTimeStamp = currentTime - 86400 * 29;
        initView();
        initData(currentTime,sevenDayTimeStamp);
    }

    private void initView() {
        switch (getType()) {
            case "1,2":
                title = "7日血压值变化";
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
            case "9":
                title = "7日基础代谢量变化";
                break;
            case "10":
                title = "7日皮下脂肪率变化";
                break;
            case "11":
                title = "7日内脏脂肪等级变化";
                break;
            case "12":
                title = "7日骨骼肌率变化";
                break;
            case "13":
                title = "7日骨量变化";
                break;
            case "14":
                title = "7日蛋白质变化";
                break;
            case "15":
                title = "7日体年龄变化";
                break;
            case "16":
                title = "7日去脂体重变化";
                break;
            case "17":
                title = "7日肌肉量变化";
                break;
            case "18":
                title = "人脸";
                break;
            case "19":
                title = "7日体温变化";
                break;
            case "99":
                title = "其他";
        }
        binding.titlebar.tbTitle.setText(title);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(final long currentTime, long sevenDayTimeStamp) {

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
                    for (int i = 0; i <= 29; i++) {
                        lists.add(Utils.stampToMonth(currentTime - (86400 * i) + "000"));
                    }
                    setupChart(binding.lineChart, lists);
                }

            }
        });
    }

    // 设置显示的样式
    void setupChart(final LineChart chart, final List<String> lists) {
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            switch (getType()) {
                case "1,2":
                    values.add(new Entry(i, list.get(i).getItems().getHypertension()));
                    values2.add(new Entry(i, list.get(i).getItems().getHypotension()));
                    break;
                case "3":
                    values.add(new Entry(i, list.get(i).getItems().getHeart_rate()));
                    break;
                case "4":
                    values.add(new Entry(i, list.get(i).getItems().getBlood_sugar()));
                    break;
                case "5":
                    values.add(new Entry(i, list.get(i).getItems().getWeight()));
                    break;
                case "6":
                    values.add(new Entry(i, list.get(i).getItems().getBmi()));
                    break;
                case "7":
                    values.add(new Entry(i, list.get(i).getItems().getBody_fat()));
                    break;
                case "8":
                    values.add(new Entry(i, list.get(i).getItems().getBody_moisture()));
                    break;
                case "9":
                    values.add(new Entry(i, list.get(i).getItems().getBasal_metabolic_rate()));
                    break;
                case "10":
                    values.add(new Entry(i, list.get(i).getItems().getSubcutaneous_fat_rate()));
                    break;
                case "11":
                    values.add(new Entry(i, list.get(i).getItems().getVisceral_adipose_grade()));
                    break;
                case "12":
                    values.add(new Entry(i, list.get(i).getItems().getSkeletal_muscle_rate()));
                    break;
                case "13":
                    values.add(new Entry(i, list.get(i).getItems().getBone_mass()));
                    break;
                case "14":
                    values.add(new Entry(i, list.get(i).getItems().getProtein()));
                    break;
                case "15":
                    values.add(new Entry(i, list.get(i).getItems().getBody_age()));
                    break;
                case "16":
                    values.add(new Entry(i, list.get(i).getItems().getLbm()));
                    break;
                case "17":
                    values.add(new Entry(i, list.get(i).getItems().getMuscle_mass()));
                    break;
                case "18":
                    values.add(new Entry(i, list.get(i).getItems().getFace()));
                    break;
                case "19":
                    values.add(new Entry(i, list.get(i).getItems().getTemperature()));
                    break;
                case "99":
                    values.add(new Entry(i, list.get(i).getItems().getOther()));
                    break;
            }
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
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if (getType().equals("1,2")) {
            LineDataSet set1 = new LineDataSet(values, "最高血压值变化");
            set1.setColor(getResources().getColor(R.color.blue));
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setValueTextSize(9f);
            dataSets.add(set1);
            LineDataSet set = new LineDataSet(values2, "最低血压值变化");
            set.setColor(getResources().getColor(R.color.green));
            set.setLineWidth(1f);
            set.setCircleRadius(3f);
            set.setValueTextSize(9f);
            dataSets.add(set);
            LineData data = new LineData(dataSets);
            chart.setData(data);
        } else {
            LineDataSet set1 = new LineDataSet(values, title);
            set1.setColor(getResources().getColor(R.color.blue));
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);
            chart.setData(data);
        }
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setTouchEnabled(true); // 设置是否可以触摸
        chart.setScaleEnabled(false);
        chart.setVisibleXRangeMaximum(7);
        chart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        chart.setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        chart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        chart.invalidate();
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                isLoading = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                int rightIndex = (int) chart.getHighestVisibleX();
                int size = (int) chart.getLineData().getXMax();
                if (lastPerformedGesture == ChartTouchListener.ChartGesture.DRAG) {
                    isLoading = true;
                    if (rightIndex == size - 1 || rightIndex == size) {
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }


    private String getType() {
        return getIntent().getStringExtra(Constants.TYPE);
    }


}


