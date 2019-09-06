package com.blz.prisoner.lifeshare;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class DonorsFragment extends Fragment {



    Query query;
    private View rootView;
    /*final ColorDrawable background = new ColorDrawable(Color.RED);*/
    Drawable icon;
    Drawable background;



    RecyclerView recyclerView;
    DonorAdapterClass adapter;

    List<DonorData> donorDataList;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;


    public DonorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView =inflater.inflate(R.layout.fragment_donors, container, false);

        icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_phone);
        background = ContextCompat.getDrawable(getActivity(), R.drawable.borderless_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        donorDataList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();
        query =reference.child("Users");

        /*fetchData(query);*/

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.callUser((DonorAdapterClass.DonorViewHolder) viewHolder);
            }




            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                int margin =(viewHolder.itemView.getHeight() - icon.getIntrinsicHeight())/2;

                background.setBounds((int) (viewHolder.itemView.getRight()+ dX),viewHolder.itemView.getTop(),viewHolder.itemView.getRight(),viewHolder.itemView.getBottom());
                icon.setBounds(viewHolder.itemView.getRight() - margin - icon.getIntrinsicHeight(),viewHolder.itemView.getTop() + margin,
                        viewHolder.itemView.getRight() - margin ,viewHolder.itemView.getBottom() - margin);
                background.draw(c);
                c.save();

                c.clipRect((int) (viewHolder.itemView.getRight()+ dX),viewHolder.itemView.getTop(),viewHolder.itemView.getRight(),viewHolder.itemView.getBottom());
                icon.draw(c);
                c.restore();
            }
        }).attachToRecyclerView(recyclerView);





        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();

        fetchData(query);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blood_group_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.a_pos: {
                query = reference.child("Users").orderByChild("canGive_A_Pos").equalTo("yes");
                fetchData(query);
                break;
            }

            case R.id.a_neg: {

                query =reference.child("Users").orderByChild("canGive_A_Neg").equalTo("yes");
                fetchData(query);
                break;
            }


            case R.id.b_pos: {


                query =reference.child("Users").orderByChild("canGive_B_Pos").equalTo("yes");
                fetchData(query);
                break;
            }

            case R.id.b_neg: {

                query =reference.child("Users").orderByChild("canGive_B_Neg").equalTo("yes");
                fetchData(query);
                break;
            }

            case R.id.ab_pos: {

                query =reference.child("Users").orderByChild("canGive_AB_Pos").equalTo("yes");
                donorDataList.clear();
                fetchData(query);
                break;
            }

            case R.id.ab_neg: {

                query =reference.child("Users").orderByChild("canGive_AB_Neg").equalTo("yes");
                donorDataList.clear();
                fetchData(query);
                break;
            }

            case R.id.o_pos: {

                query =reference.child("Users").orderByChild("canGive_O_Pos").equalTo("yes");
                donorDataList.clear();
                fetchData(query);
                break;
            }

            case R.id.o_neg: {

                query =reference.child("Users").orderByChild("canGive_O_Neg").equalTo("yes");
                donorDataList.clear();
                fetchData(query);
                break;
            }

            case R.id.list_all: {

                query =reference.child("Users");
                donorDataList.clear();
                fetchData(query);

                break;
            }

            case R.id.logout: {

                logout();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }







    private void logout() {
        firebaseAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(),MainActivity.class));

    }

    private void fetchData(Query query){


       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               donorDataList.clear();
               if(dataSnapshot.exists()){

                   for(DataSnapshot data : dataSnapshot.getChildren()){


                       if(data.child("isDonor").getValue().toString().equals("yes") && !data.child("userId").getValue().toString().equals(firebaseAuth.getUid())){
                           DonorData donor = data.getValue(DonorData.class);
                           donorDataList.add(donor);
                           adapter = new DonorAdapterClass(getActivity(),donorDataList);
                           recyclerView.setAdapter(adapter);
                       }
                       else {
                           adapter = new DonorAdapterClass(getActivity(),donorDataList);
                           recyclerView.setAdapter(adapter);
                       }


                   }
               }
               else{
                   adapter = new DonorAdapterClass(getActivity(),donorDataList);
                   recyclerView.setAdapter(adapter);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



    }



}
