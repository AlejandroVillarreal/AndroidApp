package com.example.arduinoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private TextInputLayout textInputRestaurantName;
    private TextInputLayout textInputNumberOfTables;
    private Spinner maxCapacity;
    public String userid;
    private Button editButton;
    FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);
        userid = getArguments().getString("uid");

        textInputRestaurantName = (TextInputLayout) rootView.findViewById(R.id.text_input_edit_restaurant_name);
        textInputNumberOfTables = (TextInputLayout) rootView.findViewById(R.id.text_input_edit_number_of_tables);
        //maxCapacity =  (Spinner) rootView.findViewById(R.id.max_edit_capacity);
        editButton = (Button) rootView.findViewById(R.id.editar);
        editButton.setOnClickListener(this);
        //validateMaxCapacity();
        return rootView;


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
                    String propertyAddress = addressSnapshot.getValue(String.class);//.concat("%");
                    if (propertyAddress!=null){
                        propertyAddressList.add(propertyAddress);
                    }
                }


                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, propertyAddressList);
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

    public void confirmEdit(View v){

    }

    @Override
    public void onClick(View v) {
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (!validateNumTables() ){
            return;
        }

            DatabaseReference reference = database.getReference("Users").child(userid);
            //reference.child("restaurant_name").setValue(textInputRestaurantName.getEditText().getText().toString());
            reference.child("number_of_tables").setValue(textInputNumberOfTables.getEditText().getText().toString());
            //reference.child("max_capacity").setValue(maxCapacity.getSelectedItem().toString());
            //int intMax_capacity = Integer.parseInt(maxCapacity.getSelectedItem().toString());
            int intNumber_of_tables = Integer.parseInt(textInputNumberOfTables.getEditText().getText().toString());
            String tables_available = "0";
            int intTables_available = Integer.parseInt(tables_available);
            //if ( maxCapacity.getSelectedItem().toString().equals("Selecciona un valor de aforo maximo")) {
            //}
            //else{
            // double double_tables = Math.floor(intNumber_of_tables * intMax_capacity * 1.0E-02);
            // intTables_available = (int) Math.round(double_tables);
            // tables_available = Integer.toString(intTables_available);
            // reference.child("tables_available").setValue(tables_available);
            //hashMap.put("tables_available",tables_available);
            // }
            Toast.makeText(getActivity(), "Información Editada con Éxito", Toast.LENGTH_LONG).show();




    }
}
