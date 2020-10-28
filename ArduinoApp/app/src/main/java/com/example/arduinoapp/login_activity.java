package com.example.arduinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class login_activity extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    FirebaseAuth firebaseAuth;
    public String user_restaurant_name ="" ;
    public String user_number_of_tables ="";
   public String user_max_capacity ="";
   private ArrayList dataList;
    public ArrayList <String> listaDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_activity);
        textInputEmail = findViewById(R.id.text_input_email);
        //textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);

        dataList = new ArrayList<>();

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

    private boolean validateUsername(){
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
    }

    private boolean validatePassword(){
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()){
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }


    public void confirmInput(View v){
        if (!validateEmail() |   !validatePassword()){
            return;
        }
        String email = textInputEmail.getEditText().getText().toString();
        String password = textInputPassword.getEditText().getText().toString();
        //firebaseAuth.getInstance().getCurrentUser().isEmailVerified();
        firebaseAuth = firebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            //HashMap<Object,String> hashMap = new HashMap<>();
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){

                                readData(new FirebaseCallBack() {
                                    @Override
                                    public void onCallback(List<String> list) {

                                        if (list.get(0).equals("") && list.get(1).equals("") && list.get(2).equals("")){
                                            Intent intent = new Intent(login_activity.this,user_settings.class);
                                            intent.putExtra("uid",firebaseAuth.getCurrentUser().getUid());
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Intent intent = new Intent(login_activity.this,principal.class);
                                            intent.putExtra("uid",firebaseAuth.getCurrentUser().getUid());
                                            startActivity(intent);
                                            finish();
                                        }

                                        //listaDatos = ;
                                        /*if (list.get(0).equals("") && list.get(1).equals("") && list.get(2).equals("")){
                                            Intent intent = new Intent(login_activity.this,user_settings.class);
                                            intent.putExtra("uid",firebaseAuth.getCurrentUser().getUid());
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Intent intent = new Intent(login_activity.this,principal.class);
                                            intent.putExtra("uid",firebaseAuth.getCurrentUser().getUid());
                                            startActivity(intent);
                                            finish();
                                        }*/
                                    }

                                });



                        /*String userID = firebaseAuth.getCurrentUser().getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference referenceRestaurantName = database.getReference("Users").child(userID).child("restaurant_name");
                        DatabaseReference referenceMaxCapacity = database.getReference("Users").child(userID).child("max_capacity");
                        DatabaseReference referenceNumberOfTables = database.getReference("Users").child(userID).child("number_of_tables");
                        if (user_restaurant_name.equals("") && user_number_of_tables.equals("") && user_max_capacity.equals("")){
                            Intent intent = new Intent(login_activity.this,user_settings.class);
                            intent.putExtra("uid",firebaseAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(login_activity.this,principal.class);
                            intent.putExtra("uid",firebaseAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            finish();
                        }*/
                        // Deciding the activity to start depending if user has already data on the following keys





                        //startActivity(new Intent(login_activity.this, user_settings.class));
                    }if (!firebaseAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(login_activity.this, "Please Verify Your Email", Toast.LENGTH_LONG).show();
                    }

                }else{
                    //startActivity(new Intent(login_activity.this, login_activity.class));
                    Toast.makeText(login_activity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    private void readData(final FirebaseCallBack firebaseCallBack){
        String userID = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(userID);
        DatabaseReference referenceMaxCapacity = database.getReference("Users").child(userID).child("max_capacity");
        DatabaseReference referenceNumberOfTables = database.getReference("Users").child(userID).child("number_of_tables");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_restaurant_name = snapshot.child("restaurant_name").getValue(String.class);
                user_number_of_tables = snapshot.child("number_of_tables").getValue(String.class);
                user_max_capacity = snapshot.child("max_capacity").getValue(String.class);
                dataList.add(user_restaurant_name);
                dataList.add(user_number_of_tables);
                dataList.add(user_max_capacity);

                firebaseCallBack.onCallback(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Getting values from the user

    }
    private interface  FirebaseCallBack{
        void onCallback(List<String>list);
    }

    public void confirmRegistro(View v){
        startActivity(new Intent(login_activity.this, register.class));
    }

}
