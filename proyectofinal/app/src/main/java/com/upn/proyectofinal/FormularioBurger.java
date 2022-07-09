package com.upn.proyectofinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.upn.proyectofinal.entidad.Burger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upn.proyectofinal.entidad.Burger;

import java.util.HashMap;
import java.util.UUID;

public class FormularioBurger extends AppCompatActivity {

    EditText txtDescripcion, txtNombre, txtPrecio;
    Button btnRegistrar;
    TextView lblTitulo;

    Burger burger;

    FirebaseDatabase database;
    DatabaseReference reference;

    boolean registra = true;
    String id;
    HashMap map = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_burger);
        asignarReferencias();
        inicializarFirebase();
        obtenerValores();
    }

    @Override
    public void onPause(){
        super.onPause();

        overridePendingTransition(ListadoActivity.zoomIn,ListadoActivity.zoomOut);
    }


    private void obtenerValores() {
        if(getIntent().hasExtra("pid")){

            lblTitulo.setText("Modificando Hamburguesa");
            btnRegistrar.setText("Modificar Hamburguesa");
            registra = false;
            id = getIntent().getStringExtra("pid");
            txtNombre.setText(getIntent().getStringExtra("pnombre"));
            txtDescripcion.setText(getIntent().getStringExtra("pdescripcion"));
            txtPrecio.setText(getIntent().getStringExtra("pprecio"));
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

    }

    private void asignarReferencias() {
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        lblTitulo = findViewById(R.id.lblTitulo);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> {

            if(capturarDatos()){
                String msaje = "";
                if(registra){
                    reference.child("Persona").child(burger.getId()).setValue(burger);
                    ventana("Hamburguesa Agregada","").show();
                }else{
                    reference.child("Persona").child(id).updateChildren(map);
                    ventana("Hamburguesa Actualizada","").show();
                }
            }else{
                ventana("Completa todos los campos.","").show();
            }
        });
    }
    private boolean capturarDatos(){
        boolean valida  = true;
        String descripcion = txtDescripcion.getText().toString();
        String nombre = txtNombre.getText().toString();
        Double precio = Double.parseDouble(txtPrecio.getText().toString());
        if(descripcion.equals("")){
            txtDescripcion.setError("La Descripción es Obligatorio");
            valida = false;
        }
        if(nombre.equals("")){
            txtNombre.setError("El Nombre es Obligatorio");
            valida = false;
        }
        if(precio.equals("")){
            txtPrecio.setError("El Precio es Obligatoria");
            valida = false;
        }

        if(valida){
            if(registra){
                burger = new Burger(UUID.randomUUID().toString(),descripcion,nombre,precio);
            }else{
                map.put("descripcion",descripcion);
                map.put("nombre",nombre);
                map.put("precio",precio);
            }
        }
        return valida;
    }
    private AlertDialog ventana(String msaje, String msje2){
        AlertDialog.Builder builder = new AlertDialog.Builder(FormularioBurger.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();


        TextView text = view.findViewById(R.id.txtResultados);

        text.setText(msaje+msje2);

        Button btnAceptar = view.findViewById(R.id.btnAceptars);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FormularioBurger.this,PrincipalActivity.class);
                startActivity(intent);

            }
        });

        return dialog;
    }
}