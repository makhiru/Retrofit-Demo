package com.example.retrofitdemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.fragment.AddProductFragment;
import com.example.retrofitdemo.fragment.ShowAllProductFragment;
import com.example.retrofitdemo.fragment.ShowProductFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    DrawerLayout drawer;
    NavigationView navigation;
    Toolbar toolbar;
    TextView drawer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);

        preferences = getSharedPreferences("Login_pref",MODE_PRIVATE);
        editor = preferences.edit();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigation.setCheckedItem(R.id.nav_add_product);
        toolbar.setTitle("Add Product");

        String name = preferences.getString("name", "User");
        View headerView = navigation.getHeaderView(0);
        drawer_name = (TextView) headerView.findViewById(R.id.drawer_name);
        drawer_name.setText("" + name);

        setFragment(new AddProductFragment());
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_add_product){
                    toolbar.setTitle("Add Product");
                    setFragment(new AddProductFragment());
                } else if (item.getItemId() == R.id.nav_show_product) {
                    toolbar.setTitle("Show Product");
                    setFragment(new ShowProductFragment());
                } else if (item.getItemId() == R.id.nav_show_all_product) {
                    toolbar.setTitle("Show All Product");
                    setFragment(new ShowAllProductFragment());
                } else if (item.getItemId() == R.id.menu_logout){
                    editor.putBoolean("logged_in", false);
                    editor.commit();

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}