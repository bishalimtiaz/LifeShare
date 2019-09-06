package com.blz.prisoner.lifeshare;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    DonorData donor;

    private Spinner blood;
    private String[] bloodgrp;
    private View rootView;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private String name,gender,birthday,address,phone,isdonor,lattitude,longitude,bloods;

    private TextView name_text,gender_text,birthday_text,address_text,phone_text,blood_text;
    private Switch aSwitch;
    Button post_buttton;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    boolean buttonCheck = false;


    List<String> lst = new ArrayList<String>();





    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //Initialization
        blood = rootView.findViewById(R.id.blood);

        name_text =rootView.findViewById(R.id.name_text);
        gender_text = rootView.findViewById(R.id.gender_text);
        birthday_text = rootView.findViewById(R.id.birthday_text);
        address_text = rootView.findViewById(R.id.address_text);
        phone_text = rootView.findViewById(R.id.phone_text);
        aSwitch = rootView.findViewById(R.id.switch1);
        post_buttton = rootView.findViewById(R.id.post_button);
        blood_text = rootView.findViewById(R.id.blood_text);


        //spinner setting

        bloodgrp = getResources().getStringArray(R.array.Blood_group);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.pro_layout,R.id.txtItem,bloodgrp);
        blood.setAdapter(adapter);


        //getting userdata from database

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Query query = databaseReference.child("Users").orderByChild("userId").equalTo(firebaseAuth.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if (dataSnapshot.exists()){

                    donor =dataSnapshot.getValue(DonorData.class);





                    for(DataSnapshot data : dataSnapshot.getChildren()){

                        donor = data.getValue(DonorData.class);




                        name = data.child("fullName").getValue().toString();
                        gender = data.child("gender").getValue().toString();
                        birthday = data.child("birthday").getValue().toString();
                        phone = data.child("phone").getValue().toString();
                        address = data.child("address").getValue().toString();
                        isdonor = data.child("isDonor").getValue().toString();
                        lattitude = data.child("lattitude").getValue().toString();
                        longitude = data.child("longitude").getValue().toString();
                        bloods = data.child("bloodGroup").getValue().toString();
                        lst.clear();
                        lst.add(name);
                        lst.add(phone);
                        lst.add(address);
                        lst.add(lattitude);
                        lst.add(longitude);
                        lst.add(isdonor);


                        name_text.setText(name);
                        gender_text.setText(gender);
                        birthday_text.setText(birthday);
                        address_text.setText(address);
                        phone_text.setText(phone);
                        blood_text.setText(bloods);

                        if(isdonor.equals("yes")){

                            if(!aSwitch.isChecked()){
                                /*aSwitch.setOnCheckedChangeListener(null);*/
                                aSwitch.setChecked(true);

                            }
                        }
                        else {

                            if(aSwitch.isChecked()){
                                /*aSwitch.setOnCheckedChangeListener(null);*/
                                aSwitch.setChecked(false);
                            }
                        }



                    } //for
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



        //notification post button



        post_buttton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

                String date=simpleDateFormat.format(calendar.getTime());
                NotificationData notificationData =new NotificationData(lst.get(0),date,blood.getSelectedItem().toString(),lst.get(1),lst.get(2),lst.get(3),lst.get(4),firebaseAuth.getUid());

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference();

                /*myRef.child("Users").child(userId).setValue(userData);*/

                myRef.child("Notifications").push().setValue(notificationData);
                Toast.makeText(getActivity(),"Posted Successfully",Toast.LENGTH_SHORT).show();

            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Called","oncheckChanged");
                Log.d("inCheck",""+ buttonCheck);

                if(buttonCheck){
                    if(lst.get(5).equals("yes") && !isChecked){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                        ref.child("isDonor").setValue("no");

                    }

                    else if(lst.get(5).equals("no") && isChecked){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
                        ref.child("isDonor").setValue("yes");

                    }
                }

                buttonCheck = false;

                Log.d("inCheck",""+ buttonCheck);
            }
        });

        aSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d("Called","onTouch");
                Log.d("inTouch",""+ buttonCheck);
                buttonCheck = true;
                Log.d("inTouch",""+ buttonCheck);
                return false;
            }
        });



    lst.clear();





        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile,menu);
        /*Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.notification_color), PorterDuff.Mode.SRC_IN);*/
       /* menu.getItem(0).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_blood_donation));*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log:
                logout();
                break;

            case R.id.req_list:
                Intent intent = new Intent(getActivity(),MyRequest.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    private void logout() {
        firebaseAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(),MainActivity.class));

    }



}
