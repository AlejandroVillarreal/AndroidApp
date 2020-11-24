package com.example.arduinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class user_settings extends AppCompatActivity {
    private TextView textViewEmail;
    private TextInputLayout textInputRestaurantName;
    private TextInputLayout textInputNumberOfTables;
    private Spinner maxCapacity;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        textViewEmail = findViewById(R.id.text_view_email);
        textInputRestaurantName = findViewById(R.id.text_input_restaurant_name);
        textInputNumberOfTables = findViewById(R.id.text_input_number_of_tables);
        maxCapacity = findViewById(R.id.max_capacity);
        showEmail();
        validateMaxCapacity();
       // validateMaxCapacity();
        //validateNumTables();

    }

    private void showEmail(){
        String userID = getIntent().getStringExtra("uid");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(userID).child("email");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewEmail.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //textViewEmail.setText(reference.toString());


        //String uid = firebaseAuth.getCurrentUser().getUid();
        //reference.child(userID).child("email");

       // userEmail.toString();
        //String emailTextView = textViewEmail.setText(userEmail);
    }

    private boolean validateRestaurantName(){
        String restaurantNameInput = textInputRestaurantName.getEditText().getText().toString().trim();
        if (restaurantNameInput.isEmpty()){
            textInputRestaurantName.setError("Filed can't be empty");
            return false;
        } else if (restaurantNameInput.length() > 15){
            textInputRestaurantName.setError("Username too long");
            return false;
        } else {
            textInputRestaurantName.setError(null);
            return true;
        }
    }

    private boolean validateNumTables(){
        String numberOfTables =  textInputNumberOfTables.getEditText().getText().toString().trim();
        if (numberOfTables.isEmpty()){
            textInputNumberOfTables.setError("Filed can't be empty");
            return false;
        }else if (numberOfTables.length()>4){
            textInputNumberOfTables.setError("Type a valid number of Tables");
            return false;
        }
        else {
            textInputNumberOfTables.setError(null);
            return true;
        }
    }

    private boolean validateMaxCapacity(){
        //String userID = getIntent().getStringExtra("uid");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Capacity");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<String> propertyAddressList = new ArrayList<String>();
                for (DataSnapshot addressSnapshot: snapshot.getChildren()) {
                    String propertyAddress = addressSnapshot.getValue(String.class).concat("%");
                    if (propertyAddress!=null){
                        propertyAddressList.add(propertyAddress);
                    }
                }


                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(user_settings.this, android.R.layout.simple_spinner_item, propertyAddressList);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinnerProperty.setAdapter(addressAdapter);
                maxCapacity.setAdapter(addressAdapter);
                String selectedMaxCapacity = maxCapacity.getSelectedItem().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return true;
    }

    public void confirmUserSettings(View v){
        if (!validateNumTables() |   !validateRestaurantName() | !validateMaxCapacity()){
            return;
        }
        String userID = getIntent().getStringExtra("uid");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(userID);
        reference.child("restaurant_name").setValue(textInputRestaurantName.getEditText().getText().toString());
        reference.child("number_of_tables").setValue(textInputNumberOfTables.getEditText().getText().toString());
        reference.child("max_capacity").setValue(maxCapacity.getSelectedItem().toString());
        //startActivity(new Intent(user_settings.this, principal.class));
        //Intent intent = new Intent(user_settings.this,principal.class);
       // startActivity(intent);
        Toast.makeText(user_settings.this, "Registro Completado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(user_settings.this,principal.class);
        intent.putExtra("uid",userID);
        startActivity(intent);
        finish();
        Log.d("Accion Realizada","Abriendo Principal");

    }

    private void spinner(){

    }
}