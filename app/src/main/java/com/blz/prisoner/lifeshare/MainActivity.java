package com.blz.prisoner.lifeshare;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button btnSignUp;
    ImageView imageView1,imageView2,imageView3,err1,err2;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    TextView textView;

    Button login;
    EditText phoneText,pass,codeText;
    CountryCodePicker ccp;

    LinearLayout codeLayout;

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;

    FirebaseAuth firebaseAuth;
    private int btnType = 0;


    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    String lattitude,longitude,adress,subLocality,throughfare,locality,subAdminArea,area;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setmPermission();

        setUpView();

        gps = new GPSTracker(MainActivity.this);

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


        // check if GPS enabled
        if(!gps.canGetLocation()){
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            updateDatabase();
            finish();
            startActivity(new Intent(MainActivity.this,Homepage.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()){

                    final String phoneNumber = ccp.getFullNumberWithPlus();

                    final String code = "667789";
                    /*Toast.makeText(MainActivity.this,firebaseAuth.getUid(),Toast.LENGTH_SHORT).show();*/


                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference userref = firebaseDatabase.getReference("Users");

                    userref.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    String password = data.child("password").getValue().toString();
                                    String mypass = pass.getText().toString();

                                    if (mypass.equals(password)){
                                        /*Toast.makeText(MainActivity.this,"user ready",Toast.LENGTH_SHORT).show();*/

                                        if(btnType == 0){

                                            phoneText.setEnabled(false);
                                            login.setEnabled(false);



                                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                    phoneNumber,        // Phone number to verify
                                                    60,                 // Timeout duration
                                                    TimeUnit.SECONDS,   // Unit of timeout
                                                    MainActivity.this,               // Activity (for callback binding)
                                                    mCallbacks);        // OnVerificationStateChangedCallbacks

                                            Toast.makeText(MainActivity.this,"Please Wait .... \n A Verification Code Will Be Sent To Your Phone Shortly",Toast.LENGTH_LONG).show();

                                        } else {

                                            login.setEnabled(false);

                                            String verificationCode = codeText.getText().toString();
                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                                            signInWithPhoneAuthCredential(credential);


                                        }

                                    }

                                    else {
                                        Toast.makeText(MainActivity.this,"Password Doesn't Match",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            else{

                                Toast.makeText(MainActivity.this,"User Doesn't Exist With This Phone Number",Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(MainActivity.this,"There Is Some Error In Verification",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                btnType = 1;
                login.setText(R.string.Veryfy_Code);
                login.setEnabled(true);
                codeLayout.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this,"Please Verify The Code Sent To You",Toast.LENGTH_LONG).show();


            }
        };





        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(imageView1,"imageTransition");
                pairs[1] = new Pair<View,String>(imageView1,"imageTransition1");
                pairs[2] = new Pair<View,String>(imageView1,"imageTransition2");
                pairs[3] = new Pair<View,String>(textView,"textTransition");


                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent, options.toBundle());

                /*Intent intent = new Intent(MainActivity.this,Homepage.class);

                startActivity(intent);*/



            }
        });


    }


    private  void setmPermission(){
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) !=PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setUpView(){

        btnSignUp = findViewById(R.id.btnSignUp);

        imageView1 = findViewById(R.id.fb1);
        imageView2 = findViewById(R.id.ggl1);
        imageView3 = findViewById(R.id.twt1);
        textView = findViewById(R.id.textView);
        gps = new GPSTracker(MainActivity.this);

        login = findViewById(R.id.login);
        phoneText = findViewById(R.id.phoneText);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);

        pass = findViewById(R.id.pass);
        err1 = findViewById(R.id.err1);
        err2 = findViewById(R.id.err2);
        codeText = findViewById(R.id.codeText);
        codeLayout = findViewById(R.id.codeLyout);

        firebaseAuth = FirebaseAuth.getInstance();




    }

    private Boolean validate(){

        Boolean result = false;

        String phone=phoneText.getText().toString();
        String password=pass.getText().toString();

        if(phone.isEmpty()){
            err1.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,"Please Enter A Valid Phne Number",Toast.LENGTH_LONG).show();
        }

        else if(phone.length()<10 || phone.length()>10){
            err1.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,"Please Enter A Valid Phne Number",Toast.LENGTH_LONG).show();

        }

        else if(password.isEmpty()){
            err1.setVisibility(View.INVISIBLE);
            err2.setVisibility(View.VISIBLE);
        }

        else {
            result = true;
            err1.setVisibility(View.INVISIBLE);
            err2.setVisibility(View.INVISIBLE);
        }

        return result;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();



                            Intent intent = new Intent(MainActivity.this,Homepage.class);
                            startActivity(intent);
                            finish();


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(MainActivity.this,"There Is Some Error",Toast.LENGTH_LONG).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void updateDatabase(){

        if(gps.canGetLocation()){

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());

            ref.child("lattitude").setValue(lattitude);
            ref.child("longitude").setValue(longitude);
            ref.child("subLocality").setValue(subLocality);
            ref.child("throughFare").setValue(throughfare);
            ref.child("locality").setValue(locality);
            ref.child("subAdminArea").setValue(subAdminArea);

            ref.child("area").setValue(area);
            ref.child("address").setValue(adress);
        }

    }



}
