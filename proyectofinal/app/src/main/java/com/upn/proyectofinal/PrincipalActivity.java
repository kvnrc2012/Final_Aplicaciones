package com.upn.proyectofinal;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
//import android.widget.Toolbar;

import com.upn.proyectofinal.entidad.Burger;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static int zoomOut = R.anim.zoom_out;
    public static int zoomIn = R.anim.zoom_in;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        asignarReferencias();


    }


    private void asignarReferencias() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new burgersfragmento()).commit();
        setTitle("Home");

        //Configurción
        setSupportActionBar(toolbar);

        toggle = setUpDrawerToggle();
        drawerLayout.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onPause(){
        super.onPause();

        overridePendingTransition(ListadoActivity.zoomIn,ListadoActivity.zoomOut);
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
            case R.id.menu_1 :
                Intent intent = new Intent(PrincipalActivity.this,MapaActivity.class);
                intent.putExtra("latitud","-12.085431");
                intent.putExtra("longitud","-77.032190");
                intent.putExtra("titulo","UPN - SEDE LINCE");
                startActivity(intent);
                break;
            case R.id.menu_2 :
                Intent intent2 = new Intent(PrincipalActivity.this,MapaActivity.class);
                intent2.putExtra("latitud","-11.959382");
                intent2.putExtra("longitud","-77.068428");
                intent2.putExtra("titulo","UPN - SEDE LOS OLIVOS");
                startActivity(intent2);
            case R.id.menu_3 :
                ft.replace(R.id.content, new burgersfragmento()).commit();
                break;
            case R.id.menu_11:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(PrincipalActivity.this,MainActivity.class));
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