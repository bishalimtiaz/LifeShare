package com.blz.prisoner.lifeshare;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class Homepage extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;


    private DonorsFragment donorsFragment;
    private NotificationsFragment notificationsFragment;
    private  ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setUpView();

        setFragment(donorsFragment);



        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){


                    case R.id.nav_donor :
                        /*mainNav.setItemBackgroundResource(R.color.colorPrimary);*/

                        setFragment(donorsFragment);

                        return  true;

                    case R.id.nav_notif :
                        /*mainNav.setItemBackgroundResource(R.color.colorAccent);*/

                        setFragment(notificationsFragment);

                        return true;

                    case R.id.nav_pro :
                        /*mainNav.setItemBackgroundResource(R.color.colorPrimaryDark);*/
                        setFragment(profileFragment);

                        return true;

                    default:
                        return false;


                }



            }
        });



    }


    private void setUpView(){
        mainNav = findViewById(R.id.main_nav);
        mainFrame = findViewById(R.id.main_frame);
        donorsFragment = new DonorsFragment();
        notificationsFragment = new NotificationsFragment();
        profileFragment = new ProfileFragment();

        Toolbar toolbar=findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);

    }

    private void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }

}
