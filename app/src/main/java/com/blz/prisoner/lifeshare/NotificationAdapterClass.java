package com.blz.prisoner.lifeshare;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationAdapterClass extends RecyclerView.Adapter<NotificationAdapterClass.NotificationViewHolder> {

    Context mCtx;
    List<NotificationData> notifList;


    public NotificationAdapterClass(Context mCtx, List<NotificationData> notifList) {
        this.mCtx = mCtx;
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mCtx).inflate(R.layout.notificatiion_list,viewGroup,false);

        NotificationViewHolder notificationViewHolder = new NotificationViewHolder(view);


        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationData data = notifList.get(position);

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


        holder.notif_text.setText(data.getFullName() + " Needs " + data.getBloodGroup() + "Blood");
        holder.address_text.setText(data.getAddress());
        holder.phone_text.setText(data.getPhone());

        android.location.Location.distanceBetween(latt1,longt1,latt2,longt2, result);

        if(result.length>=1){

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            dist = result[0]/1000;

            holder.distance_text.setText(df.format(dist) + " Km");
        }



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        String dateStop = simpleDateFormat.format(calendar.getTime());
        String dateStart = data.getDateTime();

        Date d1 = null;
        Date d2 = null;


        try {
            d1 = simpleDateFormat.parse(dateStart);
            d2 = simpleDateFormat.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            /* long diffSeconds = diff / 1000 % 60;*/
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffMinutes<1){
                holder.time_text.setText("Now");
            }

            else if(diffMinutes<60 && diffHours<1){
               holder.time_text.setText(Long.toString(diffMinutes) + " Minutes Ago");

            }

            else if(diffHours<24 && diffDays<1){
                holder.time_text.setText(Long.toString(diffHours) + " Hours Ago");
            }

            else if(diffDays>=1){
                holder.time_text.setText(Long.toString(diffDays) + " Days Ago");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callUser(NotificationAdapterClass.NotificationViewHolder notificationViewHolder){

        String s ="tel:"+notificationViewHolder.phone_text.getText();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(s));
        mCtx.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView notif_text,time_text,address_text,distance_text,phone_text;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            notif_text = itemView.findViewById(R.id.notif_text);
            time_text = itemView.findViewById(R.id.time_text);
            address_text = itemView.findViewById(R.id.address_text);
            distance_text = itemView.findViewById(R.id.distance_text);
            phone_text = itemView.findViewById(R.id.phone_text);
        }
    }

}
