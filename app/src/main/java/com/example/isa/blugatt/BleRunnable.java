package com.example.isa.blugatt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.Set;
import java.util.UUID;

/**
 * Created by isa on 2017/5/27.
 */

public class BleRunnable implements Runnable {
    private int flag = 0;
    private String[] mAttrs = {
            "B8:27:EB:73:7B:1C"
//            , "B8:27:EB:CD:1F:16"
//            , "B8:27:EB:B8:A3:78"
            , "B8:27:EB:25:BB:D1"
            , "B8:27:EB:34:A4:70"
//            , "B8:27:EB:BA:88:0C"
    };


    private String[] mServiceUuid = {
            "00002803-0000-1000-8000-00805f9b34e0"
//            , "00002803-0000-1000-8000-00805f9b34d0"
//            , "00002803-0000-1000-8000-00805f9b34c0"
            , "00002803-0000-1000-8000-00805f9b34b0"
            , "00002803-0000-1000-8000-00805f9b34a0"
//            , "00002803-0000-1000-8000-00805f9b34f0"
    };

    private String[] mCUuid = {
            "00002803-0000-1000-8000-00805f9b34e1"
//            , "00002803-0000-1000-8000-00805f9b34d1"
//            , "00002803-0000-1000-8000-00805f9b34c1"
            , "00002803-0000-1000-8000-00805f9b34b1"
            , "00002803-0000-1000-8000-00805f9b34a1"
//            , "00002803-0000-1000-8000-00805f9b34f1"
    };

    private BluetoothGatt mBluetoothGatt;
    private BluetoothDevice device;
    private BleHandler mBleHandler;
    private Set<String> mAddressSet;
    private Context mContext;
    private final int ROUND = 3;

    public BleRunnable(Context context) {
        mContext = context;
    }

    private void init() {
        mBleHandler = new BleHandler(mContext);
        mBleHandler.setBleSearchEvent(new BleHandler.BleSearchEvent() {
            @Override
            public void onstartSearch(String address, String name) {
                mAddressSet.add(address);
//                mBleAdapter.setAddressSet(mAddressSet);
            }

            @Override
            public void onstartSearchStop() {
                device = mBleHandler.getBluetoothAdapter().getRemoteDevice(mAttrs[flag %= ROUND]);
                device.connectGatt(mContext, false, btleGattCallback);
            }
        });
    }

    @Override
    public void run() {
        init();
    }

    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.d("service", " " + gatt.getServices());

        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
//                mStatus.setText("連線成功");
            } else {
                mBleHandler.getBluetoothAdapter().disable();
//                mStatus.setText("連線失敗");
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService server = gatt.getService(UUID.fromString(mServiceUuid[flag]));
                BluetoothGattCharacteristic characteristic = server.getCharacteristic(UUID.fromString(mCUuid[flag]));
                gatt.readCharacteristic(characteristic);

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte data[] = characteristic.getValue();
            for (byte b : data) {
                Log.i("onCharacteristicRead", "  " + b);
            }
            ++flag;
            mBleHandler.getBluetoothAdapter()
                    .getRemoteDevice(mAttrs[flag %= ROUND])
                    .connectGatt(mContext, false, btleGattCallback);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            byte data[] = descriptor.getValue();
            for (byte b : data) {
                Log.i("onCharacteristicRead", "  " + b);
            }
        }

    };
}
