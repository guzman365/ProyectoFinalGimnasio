package com.example.proyectofinalgimnasio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rutina extends AppCompatActivity {
    TextView txvIdRutina, txvNombre, txvObjetivo;
    private RecyclerView recyclerView;
    private List<ModeloEjercicio> lsEjercicios;
    private RequestQueue requestQueue;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina);

        lsEjercicios = new ArrayList<ModeloEjercicio>();
        lsEjercicios.clear();

        txvIdRutina = findViewById(R.id.txvIdRutina);
        txvNombre = findViewById(R.id.txvNombreRutina);
        txvObjetivo = findViewById(R.id.txvObjetivo2);
        recyclerView = findViewById(R.id.rcvEjercicios);

        Bundle bundle = getIntent().getExtras();

        int idRutina = bundle.getInt("idRutina");
        txvIdRutina.setText(String.valueOf(idRutina));

        String nombre = bundle.getString("nombre");
        txvNombre.setText(nombre);

        String objetivo = bundle.getString("objetivo");
        txvObjetivo.setText(objetivo);

        obtenerRutinaEjercicio(idRutina);
    }

    /*Apartado para menu*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.item1){
            SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("idCliente",0);
            editor.putString("nombres","");
            editor.putBoolean("sesion",false);
            editor.putInt("idEntrenamiento",0);
            editor.commit();

            Intent cerrarSerion = new Intent(this, loginUsuarios.class);
            startActivity(cerrarSerion);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDataRecyclerAdapter(List<ModeloEjercicio> lsEjercicio){
        AdapterEjercicio myadapter = new AdapterEjercicio(this,lsEjercicio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
    }
    /*Fin de apartado para menu*/

    private void obtenerRutinaEjercicio(int idRutina){
        String URL = BuildConfig.urlProyecto+"/apis/RutinaEjercicio/obtenerRutinaEjercicio.php?txtIdRutinaEjercicio="+idRutina;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        try {
                            Log.i("Error Response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObject = jsonArray.getJSONObject(i);
                                ModeloEjercicio mEjercicio = new ModeloEjercicio();
                                mEjercicio.setIdEjercicio(jObject.getString("idEjercicio"));
                                mEjercicio.setNombreRutina(jObject.getString("nombre"));
                                mEjercicio.setSeries(jObject.getString("series"));
                                mEjercicio.setRepeticiones(jObject.getString("repeticiones"));
                                mEjercicio.setImagen(jObject.getString("gif"));
                                lsEjercicios.add(mEjercicio);
                            }
                            setDataRecyclerAdapter(lsEjercicios);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error Catch", e.toString());
                        }
                    }//FIn OnResponse
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error",error.toString());
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}