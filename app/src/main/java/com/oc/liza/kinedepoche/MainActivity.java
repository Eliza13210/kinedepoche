package com.oc.liza.kinedepoche;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView navigationView;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private TextView mTextMessage;

    //BOTTOM NAVIGATION
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_theory:
                    mTextMessage.setText(R.string.title_theory);
                    return true;
                case R.id.navigation_exercises:
                    mTextMessage.setText(R.string.title_exercises);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTextMessage = findViewById(R.id.message);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initToolbar();
        configureDrawerLayout();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initDrawerHeader();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawerHeader() {
        //Inflate header layout
        View navView = navigationView.inflateHeaderView(R.layout.drawer_header_main);

        //Find views in header
        ImageView user_photo = navView.findViewById(R.id.photo);
        TextView user_name = navView.findViewById(R.id.user_name);
        TextView user_email = navView.findViewById(R.id.user_email);

        //Set photo

    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_statistics:
                System.out.println("statistics");
                break;
            case R.id.action_settings:
                System.out.println("settings");
                break;
            case R.id.action_notification:
                System.out.println("notification");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
