package com.example.isa.blugatt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isa.blugatt.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by isa on 2017/5/25.
 */

public class BleAdapter extends RecyclerView.Adapter<BleAdapter.BleHolder> {

    private List<String> addressList;
    private RecyclerClickEvent mRecyclerClickEvent;

    public void setRecyclerClickEvent(RecyclerClickEvent recyclerClickEvent) {
        mRecyclerClickEvent = recyclerClickEvent;
    }

    @Override
    public BleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(final BleHolder holder, int position) {
        if (null == addressList) return;
        holder.address.setText(addressList.get(position));
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRecyclerClickEvent)
                    mRecyclerClickEvent.onItemClick(holder.getAdapterPosition(), addressList.get(holder.getAdapterPosition()));
            }
        });
    }

    public void setAddressSet(Set<String> addressSet) {
        this.addressList = new ArrayList<>(addressSet);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == addressList) return 0;
        else return addressList.size();
    }

    class BleHolder extends RecyclerView.ViewHolder {
        private TextView address;

        public BleHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.address);
        }
    }

    public interface RecyclerClickEvent {
        void onItemClick(int position, String address);
    }
}
