package com.example.isa.blugatt.adapter;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isa.blugatt.R;

import java.util.List;

/**
 * Created by isa on 2017/5/26.
 */

public class CharacteristicAdapter extends RecyclerView.Adapter<CharacteristicAdapter.CharacteristicAdapterViewHolder> {
    private List<BluetoothGattCharacteristic> mBluetoothGattCharacteristics;
    private RecyclerClickEvent mRecyclerClickEvent;

    public void setBluetoothGattCharacteristics(List<BluetoothGattCharacteristic> bluetoothGattCharacteristics) {
        mBluetoothGattCharacteristics = bluetoothGattCharacteristics;
    }

    public void setRecyclerClickEvent(RecyclerClickEvent recyclerClickEvent) {
        mRecyclerClickEvent = recyclerClickEvent;
    }

    @Override
    public CharacteristicAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CharacteristicAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(final CharacteristicAdapterViewHolder holder, int position) {
        if (null == mBluetoothGattCharacteristics) return;
        holder.address.setText(mBluetoothGattCharacteristics.get(position).getUuid().toString());
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRecyclerClickEvent)
                    mRecyclerClickEvent.onItemClick(
                            mBluetoothGattCharacteristics.get(holder.getAdapterPosition()).getUuid().toString()
                            , mBluetoothGattCharacteristics.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mBluetoothGattCharacteristics) return 0;
        else return mBluetoothGattCharacteristics.size();
    }

    class CharacteristicAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView address;

        public CharacteristicAdapterViewHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

    public interface RecyclerClickEvent {
        void onItemClick(String uuid, BluetoothGattCharacteristic characteristic);
    }
}
