package com.example.isa.blugatt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
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
    private final static String BUNDLE_KEY_FLAG = "bundle_key_flag";
    private final static String BUNDLE_KEY_VALUE = "bundle_key_value";
    private boolean isNeedRestart = false;
    ;
    private TextView value1;
    private TextView value2;
    private TextView value3;
    private BluetoothGatt mBluetoothGatt;
    private BleHandler mBleHandler;
    private Set<String> mAddressSet;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (null == bundle) return;
            String value = bundle.getString(BUNDLE_KEY_VALUE);
            switch (bundle.getString(BUNDLE_KEY_FLAG)) {
                case "0":
                    value1.setText(value);
                    break;
                case "1":
                    value2.setText(value);
                    break;
                case "2":
                    value3.setText(value);
                    break;
            }
        }
    };

    private final int ROUND = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(mBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        value1 = (TextView) findViewById(R.id.value1);
        value2 = (TextView) findViewById(R.id.value2);
        value3 = (TextView) findViewById(R.id.value3);
        showSuport();
        init();

    }

    private void showSuport() {
        PackageManager pm = getPackageManager();
        boolean hasBLE = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!hasBLE) Toast.makeText(this,"not support BLE",Toast.LENGTH_SHORT).show();
    }

    private void init() {
        mAddressSet = new HashSet<>();
        mBleHandler = new BleHandler(this);
        mBleHandler.setBleSearchEvent(new BleHandler.BleSearchEvent() {
            @Override
            public void onstartSearch(String address, String name) {
                mAddressSet.add(address);
            }

            @Override
            public void onstartSearchStop() {
                connectToGatt();
            }
        });
    }

//    private BleAdapter.RecyclerClickEvent mRecyclerClickEvent = new BleAdapter.RecyclerClickEvent() {
//        @Override
//        public void onItemClick(int position, String address) {
//            device = mBleHandler.getBluetoothAdapter().getRemoteDevice(address);
//            mBluetoothGatt = device.connectGatt(MainActivity.this, false, btleGattCallback);
//        }
//    };

//    private ServiceAdapter.RecyclerClickEvent mServiceRecyclerClickEvent = new ServiceAdapter.RecyclerClickEvent() {
//        @Override
//        public void onItemClick(String uuid, BluetoothGattService service) {
////            Message message = Message.obtain();
////            message.what = WHAT_CHARACTERISTICS;
////            message.obj = service.getCharacteristics();
////            mHandler.sendMessage(message);
//
//            CharacteristicAdapter characteristicAdapter = new CharacteristicAdapter();
//            mRecyclerView.setAdapter(characteristicAdapter);
//            characteristicAdapter.setRecyclerClickEvent(mCharacteristicRecyclerClickEvent);
//            characteristicAdapter.setBluetoothGattCharacteristics(service.getCharacteristics());
//        }
//    };


    //    private CharacteristicAdapter.RecyclerClickEvent mCharacteristicRecyclerClickEvent = new CharacteristicAdapter.RecyclerClickEvent() {
//        @Override
//        public void onItemClick(String uuid, BluetoothGattCharacteristic characteristic) {
////            DescriptorsAdapter descriptorsAdapter =new DescriptorsAdapter();
////            mRecyclerView.setAdapter(descriptorsAdapter);
////            descriptorsAdapter.setRecyclerClickEvent(mDescriptorsRecyclerClickEvent);
////            descriptorsAdapter.setBluetoothGattDescriptors(characteristic.getDescriptors());
//            mBluetoothGatt.readCharacteristic(characteristic);
//        }
//    };
//
//    private DescriptorsAdapter.RecyclerClickEvent mDescriptorsRecyclerClickEvent = new DescriptorsAdapter.RecyclerClickEvent() {
//        @Override
//        public void onItemClick(String uuid, BluetoothGattDescriptor service) {
//            mBluetoothGatt.readDescriptor(service);
//            if (null == service.getValue()) return;
//            try {
//                String val = new String(service.getValue(), "utf-8");
//                Log.d("BluetoothGattDescriptor", val + "");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//    };
    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            Log.d("service", " " + gatt.getServices());

        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            Log.d("BLE", "BLE STATUS  " + status);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
                isNeedRestart = false;
//                mStatus.setText("連線成功");
            } else {
//                BluetoothGattService server = gatt.getService(UUID.fromString(mServiceUuid[flag]));
//                BluetoothGattCharacteristic characteristic = server.getCharacteristic(UUID.fromString(mCUuid[flag]));
//                gatt.writeCharacteristic(characteristic);
//                connectToGatt();
                close();
                mBleHandler.getBluetoothAdapter().disable();
                isNeedRestart = true;
                ++flag;
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
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            byte data[] = characteristic.getValue();
//            String value = "";
//            try {
//                value = new String(data, "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_KEY_FLAG, String.valueOf(flag));
            bundle.putString(BUNDLE_KEY_VALUE, String.valueOf(data[0]));
            Message message = Message.obtain();
            message.setData(bundle);
            mHandler.sendMessage(message);
            ++flag;
            connectToGatt();
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                     int status) {
            byte data[] = descriptor.getValue();
            for (byte b : data) {
                Log.i("onCharacteristicRead", "  " + b);
            }
        }
    };
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
    }
    private void connectToGatt() {
        if (!mBleHandler.getBluetoothAdapter().isEnabled()) {
            mBleHandler.getBluetoothAdapter().enable();
        }
        BluetoothDevice device = mBleHandler.getBluetoothAdapter()
                .getRemoteDevice(mAttrs[flag %= ROUND]);

        mBluetoothGatt = device.connectGatt(MainActivity.this, false, btleGattCallback);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        close();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mBleHandler.getBluetoothAdapter().enable();

                        //Indicates the local Bluetooth adapter is off.
                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d("BLE", "BLE TURN_ON");
                        //Indicates the local Bluetooth adapter is turning on. However local clients should wait for STATE_ON before attempting to use the adapter.
                        break;

                    case BluetoothAdapter.STATE_ON:
                        if (isNeedRestart) {
                            connectToGatt();
                        }
                        //Indicates the local Bluetooth adapter is on, and ready for use.
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("BLE", "BLE TURN_OFF");
                        //Indicates the local Bluetooth adapter is turning off. Local clients should immediately attempt graceful disconnection of any remote links.
                        break;
                }
            }
        }
    };
}
