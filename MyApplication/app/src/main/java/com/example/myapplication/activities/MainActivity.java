package com.example.myapplication.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragments.HomeFragment;
import com.example.myapplication.services.ForegroundNotificationService;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MAIN_ACTIVITY";
    Boolean mSlideState = false;
    FrameLayout mainFrameLayout;
    TextView nameText;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    HomeFragment homeFragment;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            startActivity(new Intent(MainActivity.this, AskPermissions.class));
        } else {
            initView();
            toolbar.setVisibility(View.VISIBLE);
            switchToHome();
        }
    }

    public void initView() {

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        mainFrameLayout = findViewById(R.id.main_frame_layout);

        navigationView.setNavigationItemSelectedListener(item ->{

            switch (item.getItemId()) {
                case R.id.nav_my_appointment:
                    //my appointment intent
                    break;

                case R.id.nav_settings:
                    //settings intent;
                    break;

                case R.id.nav_about_us:
                    //aboutUs intent
                    break;

                case R.id.nav_account:
                    //account intent
                    break;
            }
            return false;
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                nameText = navigationView.findViewById(R.id.side_nav_user_name);
                mSlideState = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_black_24dp, null));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    if (mSlideState) {
                        drawerLayout.closeDrawer(Gravity.START);
                    }
                    else {
                        drawerLayout.openDrawer(Gravity.START);
                    }
                }
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void switchToHome() {

        toolbar.setVisibility(View.VISIBLE);
        if(homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame_layout, homeFragment, "HOME FRAGMENT");
        ft.commit();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_black_24dp));

        Intent intent = new Intent(this, ForegroundNotificationService.class);
        if (Build.VERSION.SDK_INT >- Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

}

