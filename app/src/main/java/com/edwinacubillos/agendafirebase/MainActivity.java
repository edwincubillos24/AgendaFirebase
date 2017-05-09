package com.edwinacubillos.agendafirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText eId, eNombre, eCorreo, eTelefono;
    String id, nombre, correo, telefono;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Contactos contactos;
    int cont=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eId = (EditText) findViewById(R.id.eId);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eTelefono = (EditText) findViewById(R.id.eTelefono);
    }

    public void onClick(View v) {
        int idB = v.getId();
        database = FirebaseDatabase.getInstance();

        id = eId.getText().toString();
        nombre = eNombre.getText().toString();
        telefono = eTelefono.getText().toString();
        correo = eCorreo.getText().toString();

        switch (idB){
            case R.id.bGuardar:
                myRef = database.getReference("Contactos").child(String.valueOf(cont));
                contactos = new Contactos(String.valueOf(cont),nombre,telefono,correo);
                myRef.setValue(contactos);
                clear();
                cont++;
                 break;

            case R.id.bBuscar:
                myRef = database.getReference("Contactos");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<Contactos> lista = new ArrayList<Contactos>();
                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                            lista.add(userSnapshot.getValue(Contactos.class));
                        }
                        if (dataSnapshot.child(id).exists()){
                            contactos = dataSnapshot.child(id).getValue(Contactos.class);
                            eNombre.setText(contactos.getNombre());
                            eTelefono.setText(contactos.getTelefono());
                            eCorreo.setText(contactos.getCorreo());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.bActualizar:
                myRef = database.getReference("Contactos").child(id);

                Map<String, Object> nuevoNombre = new HashMap<>();
                nuevoNombre.put("nombre",nombre);
                myRef.updateChildren(nuevoNombre);
                Map<String, Object> nuevoTelefono = new HashMap<>();
                nuevoTelefono.put("telefono",telefono);
                myRef.updateChildren(nuevoTelefono);
                Map<String, Object> nuevoCorreo = new HashMap<>();
                nuevoCorreo.put("correo",correo);
                myRef.updateChildren(nuevoCorreo);
                 break;

            case R.id.bEliminar:
                myRef = database.getReference("Contactos").child(id);
                myRef.removeValue();
                break;

            case R.id.bVer:
                Intent intent = new Intent(MainActivity.this, ListaActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void clear() {
        eId.setText("");
        eNombre.setText("");
        eTelefono.setText("");
        eCorreo.setText("");
    }


}
