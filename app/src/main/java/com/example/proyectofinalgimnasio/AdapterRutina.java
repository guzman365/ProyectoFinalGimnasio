package com.example.proyectofinalgimnasio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRutina extends RecyclerView.Adapter<AdapterRutina.MyViewHolder>{
    private Context context;
    private List<ModeloRutina> mData;

    public AdapterRutina(Context context, List<ModeloRutina> mData){
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterRutina.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.rutinas,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView idRutina, nombre, objetivo;
        LinearLayout container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            idRutina = itemView.findViewById(R.id.txvIdRutinaMenu);
            nombre = itemView.findViewById(R.id.txvNombreRutinaMenu);
            objetivo = itemView.findViewById(R.id.txvObjetivoMenu);
            container = itemView.findViewById(R.id.idContainerMenu);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRutina.MyViewHolder myViewHolder, final int j) {
        final int i=j;
        final int idRutina = mData.get(i).getIdRutina();
        final String nombre = mData.get(i).getNombre();
        final String objetivo = mData.get(i).getObjetivo();

        myViewHolder.idRutina.setText(String.valueOf(idRutina));
        myViewHolder.nombre.setText(nombre);
        myViewHolder.objetivo.setText(objetivo);

        myViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent;
                switch (i){
                    case 0:
                        intent = new Intent(context,Rutina.class);
                        break;
                    default:
                        intent = new Intent(context,Rutina.class);
                        break;
                }
                intent.putExtra("idRutina",idRutina);
                intent.putExtra("nombre",nombre);
                intent.putExtra("objetivo", objetivo);
                context.startActivity(intent);
            }
        });
    }
}
