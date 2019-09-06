package com.blz.prisoner.lifeshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class DonorAdapterClass extends RecyclerView.Adapter<DonorAdapterClass.DonorViewHolder> {

    Context mCtx;
    List<DonorData> donorList;

    public DonorAdapterClass(Context mCtx, List<DonorData> donorList) {

        this.mCtx = mCtx;
        this.donorList = donorList;
    }



    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.donor_list,viewGroup,false);
        DonorViewHolder donorViewHolder = new DonorViewHolder(view);
        return donorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        DonorData data = donorList.get(position);


        double latt1,longt1,latt2,longt2;
        GPSTracker gps = new GPSTracker(mCtx);
        latt1 = gps.getLatitude();
        longt1 = gps.getLongitude();
        String lattitude,longtitude;
        lattitude = data.getLattitude();
        longtitude = data.getLongitude();

        latt2 = Double.parseDouble(lattitude);
        longt2 = Double.parseDouble(longtitude);

        float dist;
        float[] result = new float[1];

        holder.fullName_list.setText(data.getFullName());
        holder.bloodGroup_list.setText(data.getBloodGroup());
        holder.gender_list.setText(data.getGender());
        holder.age_list.setText(data.getAge());
        holder.address_list.setText(data.getAddress());
        holder.phone.setText(data.getPhone());
        holder.uid.setText(data.getUserId());
        holder.device_token.setText(data.getDeviceToken());

        android.location.Location.distanceBetween(latt1,longt1,latt2,longt2, result);

        if(result.length>=1){

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            dist = result[0]/1000;

            holder.distance_list.setText(df.format(dist) + " Km");
        }


    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }


    public void callUser(DonorViewHolder donorViewHolder){

        String s ="tel:"+donorViewHolder.phone.getText();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(s));
        mCtx.startActivity(intent);

    }

    class DonorViewHolder extends RecyclerView.ViewHolder{

        TextView fullName_list,bloodGroup_list,gender_list,age_list,address_list,distance_list,phone,uid,device_token;

        public DonorViewHolder(@NonNull final View itemView) {
            super(itemView);
            fullName_list = itemView.findViewById(R.id.fullName_list);
            device_token = itemView.findViewById(R.id.device_token);
            bloodGroup_list = itemView.findViewById(R.id.bloodGroup_list);
            gender_list = itemView.findViewById(R.id.gender_list);
            age_list = itemView.findViewById(R.id.age_list);
            address_list = itemView.findViewById(R.id.address_list);
            distance_list = itemView.findViewById(R.id.distance_list);
            phone = itemView.findViewById(R.id.phone);
            uid = itemView.findViewById(R.id.uid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = fullName_list.getText().toString();
                    String userId = uid.getText().toString();
                    /*Toast.makeText(mCtx,userId,Toast.LENGTH_SHORT).show();*/
                    String token = device_token.getText().toString();
                    Intent intent = new Intent(mCtx,Informations.class);
                    Bundle extras=new Bundle();
                    extras.putString("fullName",name);
                    extras.putString("userId",userId);
                    extras.putString("r_Token",token);
                    intent.putExtras(extras);
                    mCtx.startActivity(intent);
                }
            });
        }


    }
}
