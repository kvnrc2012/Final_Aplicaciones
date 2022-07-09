package com.upn.proyectofinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.upn.proyectofinal.entidad.Burger;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link burgersfragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class burgersfragmento extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public burgersfragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment burgersfragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static burgersfragmento newInstance(String param1, String param2) {
        burgersfragmento fragment = new burgersfragmento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_burgersfragmento, container, false);
        asignarReferencias(root);
        asignarReferencias2(root);
        inicializarFirebase();
        cargarDatos();
        return  root;
    }
    FloatingActionButton btnAgregar;
    //Listado
    RecyclerView rvBurgers;

    FirebaseDatabase database;
    DatabaseReference reference;


    // Carousel
    List<CarouselItem> list = new ArrayList<>();
    ImageCarousel carousel;

    private List<Burger> listaCursos = new ArrayList<>();
    AdaptadorPersonalizado adaptadorPersonalizado;
    public void asignarReferencias(View v){

        carousel = v.findViewById(R.id.carousel);
        list.add(new CarouselItem("https://cdn.cuponidad.pe/images/Deals/Combo2-comixs22.jpg","PROMO!"));
        list.add(new CarouselItem("https://laopinion.com/wp-content/uploads/sites/3/2021/05/Hamburguesas-Caleb-Oquendo-en-Pexels.jpg?quality=80&strip=all&w=1200","PROMO2"));
        list.add(new CarouselItem("https://www.elcorral.com/img/our-cart/el-corral-hamburguesas-combo-de-combos-para-dos..jpeg","PROMO3"));
        list.add(new CarouselItem("https://cdn.cuponidad.pe/images/Deals/Combo2-comixs22.jpg","PROMO!"));
        list.add(new CarouselItem("https://laopinion.com/wp-content/uploads/sites/3/2021/05/Hamburguesas-Caleb-Oquendo-en-Pexels.jpg?quality=80&strip=all&w=1200","PROMO2"));
        list.add(new CarouselItem("https://www.elcorral.com/img/our-cart/el-corral-hamburguesas-combo-de-combos-para-dos..jpeg","PROMO3"));
        carousel.setCarouselListener(new CarouselListener() {
            @Override
            public void onLongClick(int i, CarouselItem carouselItem) {

            }

            @Override
            public ViewBinding onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                return null;
            }

            @Override
            public void onBindViewHolder(ViewBinding viewBinding, CarouselItem carouselItem, int i) {

            }

            @Nullable
            @Override
            public void onClick(int position, @NotNull CarouselItem carouselItem) {
                Toast.makeText(getContext(),"IMG : "+carouselItem.getCaption(),Toast.LENGTH_LONG).show();
            }
        });
        carousel.addData(list);

        btnAgregar = v.findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(),FormularioBurger.class));
                Toast.makeText(getContext(), "HOLA :X", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void asignarReferencias2(View v) {
        rvBurgers = v.findViewById(R.id.rvBurgers);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int pos = viewHolder.getAdapterPosition();
                String id = listaCursos.get(pos).getId();
                listaCursos.remove(pos);
                adaptadorPersonalizado.notifyDataSetChanged();
                reference.child("Persona").child(id).removeValue();

            }
        }).attachToRecyclerView(rvBurgers);

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getContext());
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    //STOAGRE FIREBASE
    private void cargarDatos() {
        reference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            //Modificación Detección!
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCursos.clear();
                for (DataSnapshot item: snapshot.getChildren()){
                    Burger c = item.getValue(Burger.class);
                    listaCursos.add(c);
                }
                adaptadorPersonalizado = new AdaptadorPersonalizado(getContext(), listaCursos);
                rvBurgers.setAdapter(adaptadorPersonalizado);
                rvBurgers.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}