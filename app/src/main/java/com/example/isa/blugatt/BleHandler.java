package com.example.isa.blugatt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;

/**
 * Created by isa on 2017/5/25.
 */

public class BleHandler {
    private final static String TAG = BleHandler.class.getName();
    private Context mContext;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BleSearchEvent mBleSearchEvent;
    private boolean enable = false;
    private Handler mHandler = new Handler();

    public void setBleSearchEvent(BleSearchEvent bleSearchEvent) {
        mBleSearchEvent = bleSearchEvent;
    }

    public BleHandler(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        scanDevice();
        mHandler.postDelayed(mRunnable, 8000);
    }

    public void  scanDevice(){
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void stopScanDevice(){
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (null != mBleSearchEvent)
                mBleSearchEvent.onstartSearch(device.getAddress(), device.getName());

        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            stopScanDevice();
            if (null != mBleSearchEvent)mBleSearchEvent.onstartSearchStop();
        }
    };

    interface BleSearchEvent {
        void onstartSearch(String address, String name);

        void onstartSearchStop();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
