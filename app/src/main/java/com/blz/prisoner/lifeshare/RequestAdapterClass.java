package com.blz.prisoner.lifeshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RequestAdapterClass extends RecyclerView.Adapter<RequestAdapterClass.RequestViewHolder> {

    Context mCtx;
    List<RequestData> request_list;
    List<String> lst;

    Task<Void> informationReference;

    public RequestAdapterClass(Context mCtx, List<RequestData> request_list,List<String> lst) {

        this.mCtx = mCtx;
        this.request_list = request_list;
        this.lst = lst;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.request_list,viewGroup,false);
        RequestViewHolder holder = new RequestViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestData data = request_list.get(position);

        String name = data.getSender();

        String str = data.getRequest_Type();
        String type = "";
        if(str.equals("wants to donate")){
            type = "Wants to Donate Blood To You";
        }
        else if(str.equals("wants to receive")){
            type = "Wants to Receive Blood From You";
        }
        holder.name_request.setText(name + " " + type);

    }

    @Override
    public int getItemCount() {
        return request_list.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder{

        TextView name_request;
        Button accept_button,decline_button;

        public RequestViewHolder(@NonNull final View itemView) {
            super(itemView);
            name_request = itemView.findViewById(R.id.name_request);

            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);

            accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int position = getAdapterPosition();
                    RequestData data = request_list.get(position);

                    String s = data.getSender();
                    String r = data.getReciver();
                    String s_uid = data.sender_uid;
                    String r_uid = data.receiver_uid;
                    String t = data.request_Type;
                    /*Toast.makeText(mCtx,lst.get(position),Toast.LENGTH_SHORT).show();*/

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
                    String date=simpleDateFormat.format(calendar.getTime());
                    InformationData informationData;

                    if(t.equals("wants to donate")){
                        informationData = new InformationData(s,r,s_uid,r_uid,date);
                        informationReference = FirebaseDatabase.getInstance().getReference().child("Information").push().setValue(informationData);

                    }
                    else if(t.equals("wants to receive")){
                        informationData = new InformationData(r,s,r_uid,s_uid,date);
                        informationReference = FirebaseDatabase.getInstance().getReference().child("Information").push().setValue(informationData);
                    }

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Request").child(lst.get(position));
                    databaseReference.removeValue();


                }
            });

            decline_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Request").child(lst.get(position));
                    databaseReference.removeValue();
                }
            });
        }
    }
}
