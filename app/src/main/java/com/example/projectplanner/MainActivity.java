package com.example.projectplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectplanner.Adapter.ProjectAdapter;
import com.example.projectplanner.Fragments.Dashboard;
import com.example.projectplanner.Fragments.Motivation;
import com.example.projectplanner.Fragments.ProjectFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView navbar;
    Dashboard fragment;
    ProjectFragment projectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navbar = findViewById(R.id.navbar);
        fragment = new Dashboard();
        projectFragment = new ProjectFragment();

        startFragment(fragment);

        navbar.setOnNavigationItemSelectedListener(this);
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.dashboard:
                startFragment(fragment);
                break;
            case R.id.projects:
                startFragment(new ProjectFragment());
                break;
            case R.id.motivation:
                startFragment(new Motivation());
                break;
        }

        return true;
    }

}