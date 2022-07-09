package com.upn.proyectofinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.upn.proyectofinal.entidad.Burger;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upn.proyectofinal.entidad.Burger;

import java.util.ArrayList;
import java.util.List;

public class ListadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    public static int slideDown = R.anim.slide_down;
    public static int slideUp = R.anim.slide_up;
    public static int zoomOut = R.anim.zoom_out;
    public static int zoomIn = R.anim.zoom_in;

    RecyclerView rvCursos;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    //FIREBASE
    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth firebaseAuth;


    private List<Burger> listaPersonas = new ArrayList<>();
    AdaptadorPersonalizado adaptadorPersonalizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        asignarReferencias();
        inicializarFirebase();
        cargarDatos();
    }
    private void asignarReferencias() {
        rvCursos = findViewById(R.id.rvMisCursos);

        //PRINCIPALACTIVITY
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //getSupportFragmentManager().beginTransaction().add(R.id.content, new noticiafragmento()).commit();
        setTitle("Home");

        //Configurción
        setSupportActionBar(toolbar);

        toggle = setUpDrawerToggle();
        drawerLayout.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        //----------------------------------------

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int pos = viewHolder.getAdapterPosition();
                String id = listaPersonas.get(pos).getId();
                listaPersonas.remove(pos);
                adaptadorPersonalizado.notifyDataSetChanged();
                reference.child("Persona").child(id).removeValue();

            }
        }).attachToRecyclerView(rvCursos);

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }
    private void cargarDatos() {
        reference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            //Modificación Detección!
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPersonas.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    Burger p = item.getValue(Burger.class);
                    listaPersonas.add(p);
                }
                adaptadorPersonalizado = new AdaptadorPersonalizado(ListadoActivity.this,listaPersonas);
                rvCursos.setAdapter(adaptadorPersonalizado);
                rvCursos.setLayoutManager(new LinearLayoutManager(ListadoActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);
        return true;
    }




    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void selectItemNav(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft  = fm.beginTransaction();
        switch(item.getItemId()){
            case R.id.menu_3 :
                break;
            case R.id.menu_1:
                Intent intent = new Intent(ListadoActivity.this,MapaActivity.class);
                intent.putExtra("latitud","-12.016349");
                intent.putExtra("longitud","-77.031932");
                intent.putExtra("titulo","SEDE 1");
                startActivity(intent);
                break;
            case R.id.menu_2 :
                Intent intent2 = new Intent(ListadoActivity.this,MapaActivity.class);
                intent2.putExtra("latitud","-12.015827");
                intent2.putExtra("longitud","-77.031457");
                intent2.putExtra("titulo","SEDE 2");
                startActivity(intent2);
                break;
            case R.id.menu_11:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ListadoActivity.this,MainActivity.class));
                break;
        }
        setTitle(item.getTitle());
        drawerLayout.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}