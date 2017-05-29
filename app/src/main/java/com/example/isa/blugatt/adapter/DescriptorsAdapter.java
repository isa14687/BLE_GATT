package com.example.isa.blugatt.adapter;

import android.bluetooth.BluetoothGattDescriptor;
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

public class DescriptorsAdapter extends RecyclerView.Adapter<DescriptorsAdapter.DescriptorsViewHolder> {

    private List<BluetoothGattDescriptor> mBluetoothGattDescriptors;
    private  RecyclerClickEvent mRecyclerClickEvent;

    public void setBluetoothGattDescriptors(List<BluetoothGattDescriptor> bluetoothGattDescriptors) {
        mBluetoothGattDescriptors = bluetoothGattDescriptors;
    }

    public void setRecyclerClickEvent(RecyclerClickEvent recyclerClickEvent) {
        mRecyclerClickEvent = recyclerClickEvent;
    }

    @Override
    public DescriptorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DescriptorsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(final DescriptorsViewHolder holder, int position) {
        if (null == mBluetoothGattDescriptors) return;
        holder.address.setText(mBluetoothGattDescriptors.get(position).getUuid().toString());
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRecyclerClickEvent)
                    mRecyclerClickEvent.onItemClick(
                            mBluetoothGattDescriptors.get(holder.getAdapterPosition()).getUuid().toString()
                            , mBluetoothGattDescriptors.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mBluetoothGattDescriptors) return 0;
        else return mBluetoothGattDescriptors.size();
    }

    class DescriptorsViewHolder extends RecyclerView.ViewHolder {
        private TextView address;

        public DescriptorsViewHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

    public interface RecyclerClickEvent {
        void onItemClick(String uuid, BluetoothGattDescriptor service);
    }
}
