package com.upn.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.upn.proyectofinal.PrincipalActivity;

public class MainActivity extends AppCompatActivity {

    private TextView Marquee;

    Button btnIngresar;
    EditText txtUsuario,txtPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        asignarReferencias();
    }
    @Override
    public void onPause(){
        super.onPause();

        overridePendingTransition(PrincipalActivity.zoomIn,PrincipalActivity.zoomOut);
    }
    private void asignarReferencias() {

        Marquee = (TextView) findViewById(R.id.txtMarquee);

        Marquee.setSelected(true);

        btnIngresar = findViewById(R.id.btnIngresar);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = txtUsuario.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(usuario.isEmpty() && password.isEmpty()){
                    ventana("Complete todos los campos !!","").show();
                }else{
                    login(usuario,password);
                }

            }
        });
    }

    private void login(String usuario, String password) {
        firebaseAuth.signInWithEmailAndPassword(usuario,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(MainActivity.this,PrincipalActivity.class));
                    ventana("BIENVENIDO 😀😀","").show();
                    Toast.makeText(MainActivity.this, "Beinvenido", Toast.LENGTH_SHORT).show();
                }else{
                    ventana("Usuario/Contraseña Incorrecta","").show();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ventana("Error","").show();
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this,PrincipalActivity.class));
            finish();
        }
    }

    private AlertDialog ventana(String msaje, String msje2){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        return dialog;
    }
}