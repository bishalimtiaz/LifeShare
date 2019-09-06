
package com.blz.prisoner.lifeshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class Informations extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    String receiver_name,receiver_uid,sender_uid,request_Type ="",sender_name,receiver_token;
    FirebaseAuth firebaseAuth;
    DatabaseReference requestReference;
    Query q;
    RecyclerView recyclerView;
    TextView notText;
    InformationAdapterClass adapter;

    List<InformationData> infoDataList;
    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
        setupView();

        recyclerView = findViewById(R.id.recyclerView);
        //new
        notText = findViewById(R.id.notText);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(Informations.this));
       /* Toast.makeText(Informations.this,sender_uid,Toast.LENGTH_SHORT).show();*/

        infoDataList = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference();
        q =myref.child("Information").orderByChild("senderI_uid").equalTo(receiver_uid);
        //bugs here

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                infoDataList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        InformationData info = data.getValue(InformationData.class);
                        infoDataList.add(info);
                        adapter = new InformationAdapterClass(Informations.this,infoDataList);
                        recyclerView.setAdapter(adapter);
                    }
                }
                else {
                    adapter = new InformationAdapterClass(Informations.this,infoDataList);
                    recyclerView.setAdapter(adapter);
                    //new
                    recyclerView.setVisibility(View.GONE);
                    notText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.information,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.req: {
                openDialog();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView(){
        Toolbar toolbar=findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        receiver_name = extras.getString("fullName");
        receiver_uid = extras.getString("userId");
        receiver_token = extras.getString("r_Token");
        /*senders_id = extras.getString("r_Token");*/

        firebaseAuth = FirebaseAuth.getInstance();
        requestReference = FirebaseDatabase.getInstance().getReference().child("Request");
        sender_uid = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users");

        userref.orderByChild("userId").equalTo(sender_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        sender_name = data.child("fullName").getValue().toString();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openDialog(){

        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"Example Dialog");
    }

    @Override
    public void applyText(String reqType) {
        if(reqType.equals("1")){
            request_Type = "wants to donate";
        }
        else if(reqType.equals("2")){
            request_Type = "wants to receive";
        }

        sendRequest();

    }
    private void sendRequest(){

        final RequestData req = new RequestData(receiver_uid,sender_uid,sender_name,receiver_name,request_Type,receiver_token);


        requestReference.orderByChild("sender_uid").equalTo(sender_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String canReq = "Yes";
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        String type = data.child("request_Type").getValue().toString();

                        if(type.equals(request_Type)){
                            Toast.makeText(Informations.this,"You Already Sent A RequestData",Toast.LENGTH_SHORT).show();
                            canReq = "No";
                        }

                    }

                    if(canReq.equals("Yes")) {
                        requestReference.push().setValue(req);
                        Toast.makeText(Informations.this,"Requested Successfully",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    requestReference.push().setValue(req);
                    Toast.makeText(Informations.this,"Requested Successfully",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
