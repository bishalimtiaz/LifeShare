package com.blz.prisoner.lifeshare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRequest extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestAdapterClass adapter;

    List<RequestData> requestDataList;
    List<String> lst = new ArrayList<String>();
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        Toolbar toolbar=findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(MyRequest.this));

        requestDataList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();
        query =reference.child("Request").orderByChild("receiver_uid").equalTo(firebaseAuth.getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestDataList.clear();
                lst.clear();

                if(dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        String s = data.getKey();
                        lst.add(s);
                        /*Toast.makeText(MyRequest.this,s,Toast.LENGTH_SHORT).show();*/
                        RequestData request = data.getValue(RequestData.class);
                        requestDataList.add(request);
                        adapter = new RequestAdapterClass(MyRequest.this,requestDataList,lst);
                        recyclerView.setAdapter(adapter);

                    }
                }
                else {
                    adapter = new RequestAdapterClass(MyRequest.this,requestDataList,lst);
                    recyclerView.setAdapter(adapter);
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
        inflater.inflate(R.menu.logout,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MyRequest.this,MainActivity.class));

    }
}
