package com.example.arduinoapp;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    TextView mesas;
    TextView capmax;
    TextView databaseMesa;
    TextView databaseCapMax;
    TextView databaseMesaDisponible;
    FirebaseAuth firebaseAuth;
    private Spinner maxCapacity;
    //String userid = "2u2G4AzFKSPLrdmIpPCXEzxljBy1";
    String userid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        rootView.getBackground().setAlpha(100);
        userid = getArguments().getString("uid");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseMesa = (TextView) rootView.findViewById(R.id.databaseMesa);
        databaseMesaDisponible = (TextView) rootView.findViewById(R.id.databaseMesaDisponible);
        databaseCapMax = (TextView) rootView.findViewById(R.id.databaseCapMax);
        maxCapacity =  (Spinner) rootView.findViewById(R.id.edit_max_capacity);

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
                databaseCapMax.setText(snapshot.getValue(String.class).concat("%"));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseCapMax.setText("NombreRestaurante");
            }
        });
        DatabaseReference referenceMesasDisponibles = database.getReference("Users").child(userid).child("tables_available");
        referenceMesasDisponibles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseMesaDisponible.setText(snapshot.getValue(String.class));
                if(snapshot.getValue(String.class).equals("0")){
                    databaseMesaDisponible.setTextColor(Color.RED);
                    Toast toast = Toast.makeText(getActivity(),"No quedan mas mesas!",Toast.LENGTH_LONG);
                    View toastView =  toast.getView();
                    toastView.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    TextView text = toastView.findViewById(android.R.id.message);
                    text.setTextColor(Color.WHITE);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseMesaDisponible.setText("Ninguna");
            }
        });

        //return inflater.inflate(R.layout.fragment_main,container,false);

            validateMaxCapacity();
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
                propertyAddressList.add("Selecciona un valor de aforo maximo");
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
                //maxCapacity.setSelected(false);
                maxCapacity.setSelection(addressAdapter.NO_SELECTION, false);

                //maxCapacity.setSelection(0,false);
                maxCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override

                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(maxCapacity.getSelectedItem().toString().equals("Selecciona un valor de aforo maximo")){

                            }else {
                                firebaseAuth = firebaseAuth.getInstance();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users").child(userid);
                                int selected_item = Integer.parseInt(maxCapacity.getSelectedItem().toString());
                                int intDatabaseMesa = Integer.parseInt(databaseMesa.getText().toString());
                                String tables_available = "0";
                                int intTables_available = Integer.parseInt(tables_available);

                                double double_tables = Math.floor(intDatabaseMesa * selected_item * 1.0E-02);
                                intTables_available = (int) Math.round(double_tables);
                                tables_available = Integer.toString(intTables_available);
                                reference.child("max_capacity").setValue(maxCapacity.getSelectedItem().toString());
                                reference.child("tables_available").setValue(tables_available);

                                //hashMap.put("tables_available",tables_available);
                                //maxCapacity.setSelected(false);
                                //maxCapacity.setSelection(0,false);
                            }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                    }
                });
                String selectedMaxCapacity = maxCapacity.getSelectedItem().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return true;
    }
}
