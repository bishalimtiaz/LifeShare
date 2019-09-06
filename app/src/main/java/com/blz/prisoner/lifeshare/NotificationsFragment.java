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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
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
public class NotificationsFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapterClass adapter;

    Drawable background;
    Drawable icon;


    Query query;
    View rootView;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    List<NotificationData> notifDataList;

    GPSTracker gps;
    String lattitude,longtitude,phoneCall;
    double latt1,longt1,latt2,longt2;


    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        rootView =inflater.inflate(R.layout.fragment_notifications, container, false);
        icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_phone);
        background = ContextCompat.getDrawable(getActivity(), R.drawable.borderless_layout);
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        notifDataList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        query =reference.child("Notifications");
        /*fetchData(query);*/


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.callUser((NotificationAdapterClass.NotificationViewHolder) viewHolder);
            }

            /* @Override
             public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                 View foregroudView = viewHolder.itemView
             }
 */
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




        //
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
        inflater.inflate(R.menu.logout,menu);
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
        getActivity().finish();
        startActivity(new Intent(getActivity(),MainActivity.class));

    }

    private void fetchData(Query query){

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifDataList.clear();
                if (dataSnapshot.exists()){


                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        if (!data.child("userid").getValue().toString().equals(firebaseAuth.getUid())){
                            NotificationData notif = data.getValue(NotificationData.class);
                            notifDataList.add(notif);
                            adapter = new NotificationAdapterClass(getActivity(),notifDataList);
                            recyclerView.setAdapter(adapter);
                        }





                    }
                }

                else {

                    adapter = new NotificationAdapterClass(getActivity(),notifDataList);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
