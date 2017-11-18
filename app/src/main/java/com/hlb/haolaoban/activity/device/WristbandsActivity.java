package com.hlb.haolaoban.activity.device;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    public static final int BLUETOOTH_REQUEST_CODE = 10000;
    private BluetoothManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wristbands);
        manager = (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, BLUETOOTH_REQUEST_CODE);
            }
        }
        mDevices = new ArrayList<>();
        initView();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText("手环管理");
        ClientManager.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {

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
                checkBlueTooth();
            }
        });
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = DeviceDetailActivity.intentFor(mActivity, mDevices.get(position).getAddress(),mDevices.get(position).getName());
                startActivity(i);
            }
        });
    }

    private void checkBlueTooth() {
        if (manager != null) {
            BluetoothAdapter bluetoothAdapter = manager.getAdapter();
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }
                searchDevice();
            } else {
                showToast("蓝牙未开启,请您打开设置中的蓝牙功能!");
            }
        }
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
            DialogUtils.showLoading(mActivity,"正在搜索蓝牙设备中...");
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            DialogUtils.hideLoading(mActivity);
            if (!mDevices.contains(device)) {
                mDevices.add(device);
                mAdapter = new BlueToothAdapter(mDevices, mActivity);
                binding.listView.setAdapter(mAdapter);
            }
        }

        @Override
        public void onSearchStopped() {
            DialogUtils.hideLoading(mActivity);
        }

        @Override
        public void onSearchCanceled() {
            DialogUtils.hideLoading(mActivity);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        ClientManager.getClient().stopSearch();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BLUETOOTH_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, BLUETOOTH_REQUEST_CODE);
            }
        }
    }
}
