package com.example.arduinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class register extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    //private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputPassword2;
    private TextInputLayout textInputRestaurantName;
    private TextInputLayout textInputNumberOfTables;
    private Spinner maxCapacity;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textInputEmail = findViewById(R.id.text_input_email);
       // textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputPassword2 = findViewById(R.id.text_input_password2);

        textInputRestaurantName = findViewById(R.id.text_input_restaurant_name);
        textInputNumberOfTables = findViewById(R.id.text_input_number_of_tables);
        maxCapacity = findViewById(R.id.max_capacity);

        validateMaxCapacity();

    }

    private boolean validateEmail(){
        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()){
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

   /* private boolean validateUsername(){
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()){
            textInputEmail.setError("Filed can't be empty");
            return false;
        } else if (usernameInput.length() > 15){
            textInputUsername.setError("Username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }*/

    private boolean validatePassword(){
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String passwordConfirm = textInputPassword2.getEditText().getText().toString().trim();
        if (!passwordConfirm.equals(passwordInput)){
            Toast.makeText(register.this, "Password Not matching", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (passwordInput.isEmpty()){
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
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
                propertyAddressList.add("Selecciona un valor");
                for (DataSnapshot addressSnapshot: snapshot.getChildren()) {
                    String propertyAddress = addressSnapshot.getValue(String.class);
                    if (propertyAddress!=null){
                        propertyAddressList.add(propertyAddress);
                    }
                }


                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_item, propertyAddressList);
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


    public void confirmSignup(View v){
        if (!validateEmail() |   !validatePassword() | !validateNumTables() |   !validateRestaurantName() | !validateMaxCapacity()){
            return;
        }
        //String email = textInputEmail.getEditText().getText().toString();
       // String password = textInputPassword.getEditText().getText().toString();

        String input = "Email: " + textInputEmail.getEditText().getText().toString();
        input += "\n";
        //input += "Username: " + textInputUsername.getEditText().getText().toString();
        //input += "\n";
        input += "Password" + textInputPassword.getEditText().getText().toString();
        firebaseAuth = firebaseAuth.getInstance();
        //Toast.makeText(this, input, Toast.LENGTH_LONG).show();
        firebaseAuth.createUserWithEmailAndPassword(textInputEmail.getEditText().getText().toString(),textInputPassword.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String userid = user.getUid();
                            String restaurant_name = textInputRestaurantName.getEditText().getText().toString();
                            String number_of_tables = textInputNumberOfTables.getEditText().getText().toString();
                            String max_capacity = maxCapacity.getSelectedItem().toString();
                            int intMax_capacity = Integer.parseInt(max_capacity);
                            int intNumber_of_tables = Integer.parseInt(number_of_tables);
                            String tables_available = "0";
                            int intTables_available = Integer.parseInt(tables_available) ;


                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("userid",userid);
                            hashMap.put("restaurant_name",restaurant_name);
                            hashMap.put("number_of_tables",number_of_tables);
                            hashMap.put("max_capacity",Integer.toString(50));
                            if ( maxCapacity.getSelectedItem().toString().equals("Selecciona un valor")) {
                            }
                            else {
                                double double_tables = Math.floor(intNumber_of_tables * intMax_capacity * 1.0E-02);
                                intTables_available = (int) Math.round(double_tables);
                                tables_available = Integer.toString(intTables_available);
                                hashMap.put("tables_available",tables_available);
                            }

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(userid).setValue(hashMap);
                            firebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(register.this, "Cuenta creada", Toast.LENGTH_LONG).show();
                                        Toast.makeText(register.this, "Verifica tu email", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(register.this, login_activity.class));

                                    }else{
                                        Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void openLogin(View v){
        startActivity(new Intent(register.this, login_activity.class));
    }

}
