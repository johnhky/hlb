package com.hlb.haolaoban.activity.device;

import android.bluetooth.BluetoothDevice;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.databinding.ActivityDeviceDetailBinding;
import com.hlb.haolaoban.utils.DialogUtils;
/*import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.utils.BluetoothUtils;*/

import java.util.ArrayList;
import java.util.List;

//import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by heky on 2017/11/17.
 */

public class DeviceDetailActivity extends BaseActivity {

    ActivityDeviceDetailBinding binding;
    /**
     * 蓝牙主要操作对象，建议单例。
     */
    /**
     * 默认异常处理器
     */
    List<DetailItem> items;
    private BluetoothDevice mDevice;

    public static Intent intentFor(Context context, String address, String name) {
        Intent i = new Intent(context, DeviceDetailActivity.class);
        i.putExtra("mac", address);
        i.putExtra("name", name);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_device_detail);
        initView();
   /*     mDevice = BluetoothUtils.getRemoteDevice(getAddress());
        ClientManager.getClient().registerConnectStatusListener(mDevice.getAddress(), mConnectStatusListener);*/
        connectDevice();
    }

    private void initView() {
        binding.titlebar.tbTitle.setText(getIntent().getStringExtra("name"));
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

/*    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            BluetoothLog.v(String.format("DeviceDetailActivity onConnectStatusChanged %d in %s",
                    status, Thread.currentThread().getName()));
            connectDevice();
        }
    };*/

    private void connectDevice() {
        DialogUtils.showLoading(mActivity,"正在连接设备...");
    /*    BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();

        ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                DialogUtils.hideLoading(mActivity);
                if (code == REQUEST_SUCCESS) {
                    List<DetailItem> list = setItems(data);
                    for (int i = 0; i < list.size(); i++) {
                        Log.e(TAG, i + "次  serviceId " + list.get(i).getService() + " characterId:  " + list.get(i).getUuid());
                    }
                } else {
                    showToast("设备连接失败!");
                }
            }

        });*/

    }

 /*   public List<DetailItem> setItems(BleGattProfile profile) {
        List<DetailItem> items = new ArrayList<>();
        List<BleGattService> services = profile.getServices();
        for (BleGattService service : services) {
            items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
            List<BleGattCharacter> characters = service.getCharacters();
            for (BleGattCharacter character : characters) {
                items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
            }
        }
        return items;
    }*/

    private String getAddress() {
        return getIntent().getStringExtra("mac");
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientManager.getClient().disconnect(mDevice.getAddress());
        ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mConnectStatusListener);
    }*/
}