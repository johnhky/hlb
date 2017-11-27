package com.hlb.haolaoban.activity.device;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.bean.device.BloodPressureBean;
import com.hlb.haolaoban.databinding.ActivityBloodPressureBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Settings;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;


/**
 * Created by heky on 2017/11/24.
 */

public class BloodPressureActivity extends BaseActivity {
    ActivityBloodPressureBinding binding;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();
    List<BloodPressureBean> list = new ArrayList<>();
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();

    public static Intent intentFor(Context context, String type) {
        Intent i = new Intent(context, BloodPressureActivity.class);
        i.putExtra(Constants.TYPE, type);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blood_pressure);
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
        long currentTime = System.currentTimeMillis() / 1000;
        long sevenDayTimeStamp = currentTime - 86400 * 7;
        api.getBaseUrl(HttpUrls.getRealTime(Settings.getUserProfile().getMid() + "", getType(), sevenDayTimeStamp + "", currentTime + "")).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                if (response.length() > 7) {
                    binding.llChart.setVisibility(View.VISIBLE);
                    list = gson.fromJson(response, new TypeToken<ArrayList<BloodPressureBean>>() {
                    }.getType());
                    getAxisXLables();
                    getAxisPoints();
                    initLineChart();
                }
            }
        });
    }


    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(1);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#333333"));//灰色

//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setTextColor(Color.parseColor("#333333"));
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        binding.lineChart.setInteractive(true);
        binding.lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);  //缩放类型，水平
        binding.lineChart.setMaxZoom((float) 0);//缩放比例
        binding.lineChart.setLineChartData(data);
        binding.lineChart.setVisibility(View.VISIBLE);
        Viewport v = new Viewport(binding.lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        binding.lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < list.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(list.get(i).getDate()));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < list.size(); i++) {
            mPointValues.add(new PointValue(i, list.get(i).getValue()));
        }
    }

    private String getType() {
        return getIntent().getStringExtra(Constants.TYPE);
    }


}


