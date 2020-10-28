package com.example.arduinoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainFragment extends Fragment {
    TextView mesas;
    TextView capmax;
    TextView databaseMesa;
    TextView databaseCapMax;
    FirebaseAuth firebaseAuth;
    //String userid = "2u2G4AzFKSPLrdmIpPCXEzxljBy1";
    String userid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        userid = getArguments().getString("uid");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseMesa = (TextView) rootView.findViewById(R.id.databaseMesa);
        databaseCapMax = (TextView) rootView.findViewById(R.id.databaseCapMax);
        DatabaseReference referenceMesas = database.getReference("Users").child(userid).child("number_of_tables");
        referenceMesas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseMesa.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseMesa.setText("VALOR_MESAS");
            }
        });
        DatabaseReference referenceCapMax = database.getReference("Users").child(userid).child("max_capacity");
        referenceCapMax.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseCapMax.setText(snapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseCapMax.setText("NombreRestaurante");
            }
        });
        //return inflater.inflate(R.layout.fragment_main,container,false);
        return rootView;
    }
}
