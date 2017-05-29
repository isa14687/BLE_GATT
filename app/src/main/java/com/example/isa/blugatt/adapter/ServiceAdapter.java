package com.example.isa.blugatt.adapter;

import android.bluetooth.BluetoothGattService;
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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<BluetoothGattService> mBluetoothGattServices;
    private RecyclerClickEvent mRecyclerClickEvent;

    public void setBluetoothGattServices(List<BluetoothGattService> bluetoothGattServices) {
        mBluetoothGattServices = bluetoothGattServices;
        notifyDataSetChanged();
    }

    public void setRecyclerClickEvent(RecyclerClickEvent recyclerClickEvent) {
        mRecyclerClickEvent = recyclerClickEvent;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, final int position) {
        if (null == mBluetoothGattServices) return;
        holder.address.setText(mBluetoothGattServices.get(position).getUuid().toString());
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRecyclerClickEvent)
                    mRecyclerClickEvent.onItemClick(mBluetoothGattServices.get(holder.getAdapterPosition()).getUuid().toString()
                            , mBluetoothGattServices.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mBluetoothGattServices) return 0;
        else return mBluetoothGattServices.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView address;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

    public interface RecyclerClickEvent {
        void onItemClick(String uuid, BluetoothGattService service);
    }
}
