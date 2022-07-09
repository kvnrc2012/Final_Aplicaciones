package com.upn.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.proyectofinal.entidad.Burger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPersonalizado extends RecyclerView.Adapter<AdaptadorPersonalizado.MyViewHolder> {

    private Context context;
    private List<Burger> listaBurgers= new ArrayList<>();


    public AdaptadorPersonalizado(Context context, List<Burger> listaBrugers){
        this.context = context;
        this.listaBurgers = listaBrugers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View vista = inflater.inflate(R.layout.fila, parent,false);

        return new MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.filaNombre.setText(listaBurgers.get(position).getNombre()+"");
        holder.filaDescripcion.setText(listaBurgers.get(position).getDescripcion()+"");

        holder.filaPrecio.setText(listaBurgers.get(position).getPrecio()+"");
        holder.fila.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context,FormularioBurger.class);
                intent.putExtra("pid", listaBurgers.get(position).getId()+"");
                intent.putExtra("pdescripcion", listaBurgers.get(position).getDescripcion()+"");
                intent.putExtra("pnombre", listaBurgers.get(position).getNombre()+"");
                intent.putExtra("pgrado", listaBurgers.get(position).getPrecio()+"");
                context.startActivity(intent);
                return false;
            }
        });
        holder.filaEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FormularioBurger.class);
                intent.putExtra("pid", listaBurgers.get(position).getId()+"");
                intent.putExtra("pnombre", listaBurgers.get(position).getNombre()+"");
                intent.putExtra("pdescripcion", listaBurgers.get(position).getDescripcion()+"");
                intent.putExtra("pprecio", listaBurgers.get(position).getPrecio()+"");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaBurgers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView filaDescripcion,filaNombre,filaPrecio;
        ImageButton filaEditar, filaEliminar;
        LinearLayout fila;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filaDescripcion = itemView.findViewById(R.id.filaDescripcion);
            filaNombre = itemView.findViewById(R.id.filaNombre);
            filaPrecio = itemView.findViewById(R.id.filaPrecio);
            filaEditar = itemView.findViewById(R.id.filaEditar);
            filaEliminar = itemView.findViewById(R.id.filaEliminar);
            fila = itemView.findViewById(R.id.fila);

        }
    }
}
