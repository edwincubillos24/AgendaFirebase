package com.edwinacubillos.agendafirebase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    ListView list;
    ArrayList<Contactos> contactos;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        database = FirebaseDatabase.getInstance();
        contactos = new ArrayList<Contactos>();

        myRef = database.getReference("Contactos");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    contactos.add(userSnapshot.getValue(Contactos.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list = (ListView) findViewById(R.id.list);
        Adapter adapter = new Adapter (this, contactos);
        list.setAdapter(adapter);

    }

    class Adapter extends ArrayAdapter<Contactos> {

        public Adapter(Context context, ArrayList<Contactos> contactos) {
            super(context, R.layout.list_item, contactos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            Contactos contactos = getItem(position);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_item, null);

            TextView nombre = (TextView) item.findViewById(R.id.tNombre);
            nombre.setText(contactos.getNombre());

            TextView telefono = (TextView) item.findViewById(R.id.tTelefono);
            telefono.setText(contactos.getTelefono());

            TextView correo = (TextView) item.findViewById(R.id.tCorreo);
            correo.setText(contactos.getCorreo());

            return item;
        }
    }
}
