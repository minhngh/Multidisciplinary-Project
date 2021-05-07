package com.example.securitycamera.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.securitycamera.R;
import com.example.securitycamera.ui.history.HistoryFragment;
import com.example.securitycamera.ui.home.HomeFragment;
import com.example.securitycamera.ui.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigation, navController);

//        loadFragment(new HomeFragment());

    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment  fragment;
//            switch (item.getItemId())
//            {
//                case R.id.navigation_home:
//                    fragment = new HomeFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_history:
//                    fragment = new HistoryFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_setting:
//                    fragment = new SettingFragment();
//                    loadFragment(fragment);
//                    return true;
//            }
//            return false;
//        }
//    };

//    private void loadFragment(Fragment fragment)
//    {
//        // Load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.nav_host_fragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}