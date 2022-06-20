package com.example.proyectofinalgimnasio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterEjercicio  extends RecyclerView.Adapter<AdapterEjercicio.MyViewHolder> {
    private Context context;
    private List<ModeloEjercicio> mData;

    public AdapterEjercicio(Context context, List<ModeloEjercicio> mData){
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterEjercicio.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.ejercicios,viewGroup,false);
        return new AdapterEjercicio.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    /*MODELO PARA EL VIEW HOLDER*/
    public class  MyViewHolder extends RecyclerView.ViewHolder{
        ImageView icono;
        TextView nombre, series, repeticiones;
        LinearLayout container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            icono = itemView.findViewById(R.id.imgEjercicio);
            nombre = itemView.findViewById(R.id.txvNombreEjercicioMenu);
            series = itemView.findViewById(R.id.txvCantSerieMenu);
            repeticiones = itemView.findViewById(R.id.txvCantRepeticionesMenu);
            container = itemView.findViewById(R.id.idContainerEjerciciosMenu);
        }
    }

    /*MAQUETACION DE VIEWHOLDER*/
    @Override
    public void onBindViewHolder(@NonNull AdapterEjercicio.MyViewHolder myViewHolder, final int j) {
        final int i=j;

        final String idEjercicio = mData.get(i).getIdEjercicio();
        final String icono = "pesa.png";
        final String nombre = mData.get(i).getNombreRutina();
        final String series = mData.get(i).getSeries();
        final String repeticiones = mData.get(i).getRepeticiones();
        final String imagen = mData.get(i).getImagen();

        Picasso.get().load(BuildConfig.urlProyecto+"/imagenes/iconos/"+icono).into(myViewHolder.icono);

        myViewHolder.nombre.setText(nombre);
        myViewHolder.series.setText(series);
        myViewHolder.repeticiones.setText(repeticiones);

        myViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent;
                switch (i){
                    case 0:
                        intent = new Intent(context,Ejercicio.class);
                        break;
                    default:
                        intent = new Intent(context,Ejercicio.class);
                        break;
                }
                intent.putExtra("idEjercicio",idEjercicio);
                intent.putExtra("nombre",nombre);
                intent.putExtra("series",series);
                intent.putExtra("repeticiones", repeticiones);
                intent.putExtra("imagen",imagen);
                context.startActivity(intent);
            }
        });
    }
}
