package com.blz.prisoner.lifeshare;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SignUpActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{

    private Button btnLogin,btnNext;
    private ImageView imageView1,imageView2,imageView3,err1,err2,err3;
    TextView textView;
    EditText edtdate,edtpass,edtname;
    Spinner blood;
    String[] bloodgrp;

    GPSTracker gps;

    String name,pass,birth,age;
    String lattitude,longitude,adress,subLocality,throughfare,locality,subAdminArea,area;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setupView();

        //bloodgrp = getResources().getStringArray(R.array.Blood_group);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUpActivity.this,R.array.Blood_group,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood.setAdapter(adapter);

        blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(imageView3,"imageTransition2");
                pairs[1] = new Pair<View,String>(textView,"textTransition");
                pairs[2] = new Pair<View,String>(imageView3,"imageTransition1");
                pairs[3] = new Pair<View,String>(imageView3,"imageTransition");

                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
                startActivity(intent, options.toBundle());
            }
        });



        btnNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {




                /**/

                if(validate()){






                    Pair[] pairs = new Pair[4];
                    pairs[0] = new Pair<View,String>(imageView2,"imageTransition2");
                    pairs[1] = new Pair<View,String>(textView,"textTransition");
                    pairs[2] = new Pair<View,String>(imageView2,"imageTransition1");
                    pairs[3] = new Pair<View,String>(imageView2,"imageTransition");

                    Intent intent = new Intent(SignUpActivity.this,SignUpActivity2.class);

                    Bundle extras=new Bundle();

                    extras.putString("fullName",name);
                    extras.putString("password",pass);
                    extras.putString("birthday",birth);
                    extras.putString("bloodGroup",blood.getSelectedItem().toString());
                    extras.putString("lattitude",lattitude);
                    extras.putString("longitude",longitude);
                    extras.putString("address",adress);
                    extras.putString("subLocality",subLocality);
                    extras.putString("throughfare",throughfare);
                    extras.putString("locality",locality);
                    extras.putString("subAdminArea",subAdminArea);
                    extras.putString("area",area);
                    extras.putString("age",age);
                    intent.putExtras(extras);


                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
                    startActivity(intent, options.toBundle());
                }


            }
        });

        edtdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateDialog dateDialog = new DateDialog();

                    dateDialog.show(getSupportFragmentManager(),"DatePicker");
                }
            }
        });

        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dateDialog = new DateDialog();

                dateDialog.show(getSupportFragmentManager(),"DatePicker");
            }
        });
    }

    private void setupView(){

        btnLogin = findViewById(R.id.btnLogin);
        btnNext = findViewById(R.id.btnNext);
        imageView1 = findViewById(R.id.fb2);
        imageView2 = findViewById(R.id.ggl2);
        imageView3 = findViewById(R.id.twt2);
        err1 = findViewById(R.id.err1);
        err2 = findViewById(R.id.err2);
        err3 = findViewById(R.id.err3);


        textView = findViewById(R.id.textView);

        edtdate = findViewById(R.id.edtdate);
        edtpass = findViewById(R.id.edtpass);
        edtname = findViewById(R.id.edtname);


        blood = findViewById(R.id.blood);/*spinner*/

        gps = new GPSTracker(SignUpActivity.this);

        if(gps.canGetLocation()){

            lattitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());
            adress = gps.getLocationAdress();
            subLocality = gps.getMsubLocality();
            throughfare = gps.getMthroughfare();
            locality = gps.getMlocality();
            subAdminArea = gps.getMsubAdminArea();
            area = gps.getMarea();



        }

    }


    private Boolean validate(){

        Boolean result = false;

        name = edtname.getText().toString();
        pass = edtpass.getText().toString();
        birth = edtdate.getText().toString();


        if(name.isEmpty()){
            err1.setVisibility(View.VISIBLE);
        }

        else if(pass.isEmpty()){
            err1.setVisibility(View.INVISIBLE);
            err2.setVisibility(View.VISIBLE);
        }

        else if(birth.isEmpty()){
            err2.setVisibility(View.INVISIBLE);
            err3.setVisibility(View.VISIBLE);
        }

        else {
            err3.setVisibility(View.INVISIBLE);
            result = true;
        }

        return result;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate = DateFormat.getDateInstance().format(c.getTime());
        birth=currentDate;
        edtdate.setText(currentDate);

        age = Integer.toString(calculateAge(c.getTimeInMillis()));

    }

    int calculateAge(long date){

        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int age= today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return  age;
    }
}
