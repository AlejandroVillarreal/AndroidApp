package com.example.arduinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;
//import android.support.v4.widget.Toolbar;

public class principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    View headerView;
    TextView header_view_nombre_restaurante;
    TextView header_view_email;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Firebase Instance
        String userID = getIntent().getStringExtra("uid");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Set nombreRestaurante y email

        //setting drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        //View
        header_view_nombre_restaurante = (TextView) headerView.findViewById(R.id.header_view_nombre_restaurante);
        header_view_email = (TextView) headerView.findViewById(R.id.header_view_email);
        //Set nombre_restaurante y mail
        DatabaseReference referenceRestaurantName = database.getReference("Users").child(userID).child("restaurant_name");
        referenceRestaurantName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                header_view_nombre_restaurante.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                header_view_nombre_restaurante.setText("NombreRestaurante");
            }
        });
        DatabaseReference referenceEmail = database.getReference("Users").child(userID).child("email");
        referenceEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                header_view_email.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                header_view_email.setText("email@mail.com");
            }
        });




        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null){
            Bundle bundle =  new Bundle();
            FirebaseDatabase databases = FirebaseDatabase.getInstance();
            bundle.putString("uid",firebaseAuth.getCurrentUser().getUid());
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainFragment).commit();
        navigationView.setCheckedItem(R.id.nav_main);}

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Bundle bundle =  new Bundle();
        FirebaseDatabase databases = FirebaseDatabase.getInstance();
        bundle.putString("uid",firebaseAuth.getCurrentUser().getUid());
        switch (menuItem.getItemId()){
            case R.id.nav_main:
                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainFragment).commit();
                break;
            case R.id.nav_profile:

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profileFragment).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutFragment()).commit();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(principal.this,login_activity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}