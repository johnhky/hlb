package com.hlb.haolaoban.activity.device;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.databinding.ActivityWristbandsBinding;
import com.hlb.haolaoban.utils.DialogUtils;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 2017/11/16.
 */
public class WristbandsActivity extends BaseActivity {

    ActivityWristbandsBinding binding;
    private List<SearchResult> mDevices;
    private BlueToothAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wristbands);
        mDevices = new ArrayList<>();
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("手环管理");
        ClientManager.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                Log.e("eeee", openOrClosed + "");
            }
        });
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevice();
            }
        });
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = DeviceDetailActivity.intentFor(mActivity, mDevices.get(position).getAddress());
                startActivity(i);
            }
        });
    }

    private void searchDevice() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();
        ClientManager.getClient().search(request, mSearchResponse);
    }

    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            mDevices.clear();
            DialogUtils.showLoading("正在搜索蓝牙设备中...");
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            DialogUtils.hideLoading();
            if (!mDevices.contains(device)) {
                mDevices.add(device);
                mAdapter = new BlueToothAdapter(mDevices, mActivity);
                binding.listView.setAdapter(mAdapter);
            }
        }

        @Override
        public void onSearchStopped() {
            DialogUtils.hideLoading();
        }

        @Override
        public void onSearchCanceled() {
            DialogUtils.hideLoading();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        ClientManager.getClient().stopSearch();
    }
}
