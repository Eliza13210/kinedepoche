package com.oc.liza.kinedepoche.controllers;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.injections.Injection;
import com.oc.liza.kinedepoche.injections.ViewModelFactory;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.activity_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //FOR TABLET ONLY
    private View navView;
    private NavigationView navigationView;

    private NavHostFragment host;
    private UserViewModel viewModel;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initNavHost();
        initToolbar();
        initViewModel();
        initSharedPref();
        checkIfTablet();
    }

    private void initNavHost() {
        host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    private void initViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel.class);
    }

    private void initSharedPref() {
        sharedPref = this.getSharedPreferences("KineDePoche", MODE_PRIVATE);
    }


    private void setupNavigationMenu(NavController navController) {
        // TODO STEP 9.4 - Use NavigationUI to set up a Navigation View
//        // In split screen mode, you can drag this view out from the left
//        // This does NOT modify the actionbar
//        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
//        sideNavView?.setupWithNavController(navController)
        // TODO END STEP 9.4
    }

    private void setupActionBar(NavController navController,
                                AppBarConfiguration appBarConfig) {
        // TODO STEP 9.6 - Have NavigationUI handle what your ActionBar displays
//        // This allows NavigationUI to decide what label to show in the action bar
//        // By using appBarConfig, it will also determine whether to
//        // show the up arrow or drawer menu icon
//        setupActionBarWithNavController(navController, appBarConfig)
        // TODO END STEP 9.6
    }


    private void checkIfTablet() {
        //IF TABLET, SHOW DRAWER
        if (getResources().getBoolean(R.bool.isTablet)) {
            setNavigationTablet();
        }
        //IF PHONE SHOW BOTTOM NAVIGATION
        else {
            setNavigationPhone();
        }
    }

    //SET UP BOTTOM NAVIGATION
    private void setNavigationPhone() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        NavigationUI.setupWithNavController(bottomNavigationView,
                host.getNavController());
    }

    //SET UP DRAWER NAVIGATION
    private void setNavigationTablet() {
        navigationView = findViewById(R.id.activity_main_navigation_view);
        NavigationUI.setupWithNavController(navigationView,
                host.getNavController());
        initDrawerHeader(navigationView);
        if (drawerLayout != null) configureDrawerLayout();
    }


    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    // Configure Drawer Layout if tablet
    private void initDrawerHeader(NavigationView navigationView) {
        //Inflate header layout
        navView = navigationView.inflateHeaderView(R.layout.drawer_header_main);

        //Find views in header
        TextView user_name = navView.findViewById(R.id.user_name);

        String userName = sharedPref.getString("CurrentUserName", "Error");
        user_name.setText(userName);
        String todayDate = Utils.getTodayDate(Calendar.getInstance().getTime());
        //CHECK IF TODAY EXISTS IN DATABASE
        viewModel.getDate(todayDate, sharedPref.getLong("CurrentUser", 100)).observe(this, this::initProgress);
    }

    //SHOW TODAY PROGRESS IN DRAWER HEADER
    private void initProgress(ExerciseDate exerciseDate) {
        TextView user_progress = navView.findViewById(R.id.progress_text);
        StringBuilder str = new StringBuilder();
        str.append(getResources().getString(R.string.today_progress_drawer));
        if (exerciseDate != null) {
            str.append(exerciseDate.getProgress());
        } else {
            str.append("0");
        }
        str.append("%");
        user_progress.setText(str.toString());
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //  navigation_view.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        return super.onOptionsItemSelected(item);
    }

}