package com.example.arduinoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    //private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputPassword2;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textInputEmail = findViewById(R.id.text_input_email);
       // textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputPassword2 = findViewById(R.id.text_input_password2);

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


    public void confirmSignup(View v){
        if (!validateEmail() |   !validatePassword()){
            return;
        }
        //String email = textInputEmail.getEditText().getText().toString();
       // String password = textInputPassword.getEditText().getText().toString();

        String input = "Email: " + textInputEmail.getEditText().getText().toString();
        input += "\n";
        //input += "Username: " + textInputUsername.getEditText().getText().toString();
        //input += "\n";
        input += "Password" + textInputPassword.getEditText().getText().toString();

        //Toast.makeText(this, input, Toast.LENGTH_LONG).show();
        firebaseAuth.getInstance().createUserWithEmailAndPassword(textInputEmail.getEditText().getText().toString(),textInputPassword.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){


                            firebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(register.this, "Cuenta creada", Toast.LENGTH_LONG).show();

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
