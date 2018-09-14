package com.uk.progresstracker.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.uk.progresstracker.Fragments.HomeFragment;
import com.uk.progresstracker.Fragments.StatisticsFragment;
import com.uk.progresstracker.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomNavigationView;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    private void initialize() {

        mBottomNavigationView = findViewById(R.id.bottomnav);

        fm = getSupportFragmentManager();
        setFragment(new HomeFragment());    //Default;

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Log.d(TAG, "onNavigationItemSelected: Id is " + item.getItemId());

                switch (item.getItemId()){
                    case R.id.home:
                        return setFragment(new HomeFragment());
                    case R.id.stats:
                        return setFragment(new StatisticsFragment());
                }

                return false;
            }
        });


    }

    private boolean setFragment(Fragment fragment) {

        if (fragment != null) {
            Log.d(TAG, "setFragment: fragment not null");

            fm.beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();

            return true;
        }

        Log.d(TAG, "setFragment: fragment null");

        return false;
    }


}
