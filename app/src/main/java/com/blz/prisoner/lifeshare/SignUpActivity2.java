package com.blz.prisoner.lifeshare;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;


public class SignUpActivity2 extends AppCompatActivity {

    Button male,female,SignUp;
    UserData userData;
    EditText phoneText,codeText;
    LinearLayout codeLayout;
    String bloodGroup;
    ImageView err1;

    String phoneNumber,userId;
    CountryCodePicker ccp;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;


    private int btnType = 0;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);


        setUpView();


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userData.gender="male";
                male.setBackgroundResource(R.drawable.gender_button_selected);
                female.setBackgroundResource(R.drawable.gender_button);
                male.setEnabled(false);
                female.setEnabled(true);

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData.gender="female";
                female.setBackgroundResource(R.drawable.gender_button_selected);
                male.setBackgroundResource(R.drawable.gender_button);
                female.setEnabled(false);
                male.setEnabled(true);

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (validate()){
                    phoneNumber = ccp.getFullNumberWithPlus();
                    userData.phone = phoneNumber;
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference userref = firebaseDatabase.getReference("Users");


                    userref.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                //it means user already registered
                                //Add code to show your prompt
                                Toast.makeText(SignUpActivity2.this,"User Already Exist With This Phone Number \n Please Sign Up With Another Phone Number",Toast.LENGTH_LONG).show();
                            } else{

                                if(btnType == 0){

                                    phoneText.setEnabled(false);
                                    SignUp.setEnabled(false);



                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            phoneNumber,        // Phone number to verify
                                            60,                 // Timeout duration
                                            TimeUnit.SECONDS,   // Unit of timeout
                                            SignUpActivity2.this,               // Activity (for callback binding)
                                            mCallbacks);        // OnVerificationStateChangedCallbacks

                                    Toast.makeText(SignUpActivity2.this,"Please Wait .... \n A Verification Code Will Be Sent To Your Phone Shortly",Toast.LENGTH_LONG).show();

                                } else {

                                    SignUp.setEnabled(false);

                                    String verificationCode = codeText.getText().toString();
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                                    signInWithPhoneAuthCredential(credential);


                                }

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

                Toast.makeText(SignUpActivity2.this,"There Is Some Error In Verification",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                btnType = 1;
                SignUp.setText(R.string.Veryfy_Code);
                SignUp.setEnabled(true);
                codeLayout.setVisibility(View.VISIBLE);

                Toast.makeText(SignUpActivity2.this,"Please Verify The Code Sent To You",Toast.LENGTH_LONG).show();


            }
        };


////
    }


    private void setUpView(){
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        userData = new UserData();
        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        SignUp = findViewById(R.id.SignUp);
        codeLayout = findViewById(R.id.codeLyout);

        err1 = findViewById(R.id.err1);

        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        userData.fullName = extras.getString("fullName");
        userData.password = extras.getString("password");
        userData.birthday = extras.getString("birthday");
        userData.bloodGroup = extras.getString("bloodGroup");
        bloodGroup = extras.getString("bloodGroup");
        userData.lattitude = extras.getString("lattitude");
        userData.longitude = extras.getString("longitude");
        userData.address = extras.getString("address");
        userData.subLocality = extras.getString("subLocality");
        userData.throughFare = extras.getString("throughfare");
        userData.locality = extras.getString("locality");
        userData.subAdminArea = extras.getString("subAdminArea");
        userData.area = extras.getString("area");
        userData.age = extras.getString("age");


        if (bloodGroup.equals("A+")){

            userData.canGive_A_Pos = "yes";
            userData.canGive_AB_Pos = "yes";
        }

        if (bloodGroup.equals("A-")){

            userData.canGive_A_Neg = "yes";
            userData.canGive_A_Pos = "yes";
            userData.canGive_AB_Neg = "yes";
            userData.canGive_AB_Pos = "yes";
        }

        if (bloodGroup.equals("B+")){

            userData.canGive_B_Pos = "yes";
            userData.canGive_AB_Pos = "yes";
        }

        if (bloodGroup.equals("B-")){

            userData.canGive_B_Neg = "yes";
            userData.canGive_B_Pos = "yes";
            userData.canGive_AB_Neg = "yes";
            userData.canGive_AB_Pos = "yes";
        }

        if (bloodGroup.equals("AB+")){

            userData.canGive_AB_Pos= "yes";
        }

        if (bloodGroup.equals("AB-")){

            userData.canGive_AB_Pos = "yes";
            userData.canGive_AB_Neg = "yes";
        }

        if (bloodGroup.equals("O+")){

            userData.canGive_O_Pos = "yes";
            userData.canGive_A_Pos = "yes";
            userData.canGive_B_Pos = "yes";
            userData.canGive_AB_Pos = "yes";
        }

        if (bloodGroup.equals("O-")){

            userData.canGive_O_Pos = "yes";
            userData.canGive_A_Pos = "yes";
            userData.canGive_B_Pos = "yes";
            userData.canGive_AB_Pos = "yes";
            userData.canGive_O_Neg = "yes";
            userData.canGive_AB_Neg = "yes";
            userData.canGive_B_Neg = "yes";
            userData.canGive_A_Neg = "yes";
        }



    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();
                            String s = FirebaseInstanceId.getInstance().getToken();

                            userId =user.getUid();
                            userData.userId=userId;
                            userData.deviceToken=s;

                            sendUserData();

                            Intent intent = new Intent(SignUpActivity2.this,Homepage.class);
                            startActivity(intent);
                            finish();


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(SignUpActivity2.this,"There Is Some Error",Toast.LENGTH_LONG).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();

        myRef.child("Users").child(userId).setValue(userData);

        /*myRef.child("Users").push().setValue(userData);*/
    }


    private  Boolean validate(){

        boolean result=false;
        String phone = phoneText.getText().toString();
        String gn = userData.gender;

        if (gn.equals("")){
            Toast.makeText(SignUpActivity2.this,"Please Select your Gender",Toast.LENGTH_SHORT).show();
        }

        else if(phone.isEmpty()){
            err1.setVisibility(View.VISIBLE);
            Toast.makeText(SignUpActivity2.this,"Please Enter A Valid Phne Number",Toast.LENGTH_LONG).show();
        }

        else if(phone.length()<10 || phone.length()>10){
            err1.setVisibility(View.VISIBLE);
            Toast.makeText(SignUpActivity2.this,"Please Enter A Valid Phne Number",Toast.LENGTH_LONG).show();

        }

        else {
            err1.setVisibility(View.INVISIBLE);
            result = true;
        }
        return result;
    }



}
