package com.example.arduinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
        }else {
            textInputNumberOfTables.setError(null);
            return true;
        }
    }

}