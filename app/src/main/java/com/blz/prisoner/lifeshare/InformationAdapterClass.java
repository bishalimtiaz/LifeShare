package com.blz.prisoner.lifeshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InformationAdapterClass extends RecyclerView.Adapter<InformationAdapterClass.InformationViewHolder> {

    Context mCtx;
    List<InformationData> infoList;

    public InformationAdapterClass(Context mCtx, List<InformationData> infoList) {
        this.mCtx = mCtx;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.information_list,viewGroup,false);
        InformationAdapterClass.InformationViewHolder infoViewHolder = new InformationAdapterClass.InformationViewHolder(view);
        return infoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position ) {

        InformationData data = infoList.get(position);
        String d = "Date: " + data.getDates();
        String i = "Donated Blood To " + data.getReceiverI();
        holder.dates.setText(d);
        holder.info.setText(i);


    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class InformationViewHolder extends RecyclerView.ViewHolder{

        TextView dates,info;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);

            dates = itemView.findViewById(R.id.dates);
            info = itemView.findViewById(R.id.info);
        }
    }
}
