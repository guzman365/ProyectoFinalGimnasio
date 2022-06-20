package com.example.proyectofinalgimnasio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Ejercicio extends AppCompatActivity {
    TextView txvRutina, txvCantSeries, txvCantRepeticiones;
    TextInputEditText txtNota, txtPeso;
    private RequestQueue requestQueue;
    String idEjercicio="";
    ImageView imvEjercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio);

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String idCliente = String.valueOf(preferences.getInt("idCliente",0));

        txvRutina = findViewById(R.id.txvRutina);
        txvCantSeries = findViewById(R.id.txvCantSeries);
        txvCantRepeticiones = findViewById(R.id.txvCantRepeticiones);
        imvEjercicio = findViewById(R.id.imvEjercicio);
        txtNota = findViewById(R.id.txtNota);
        txtPeso = findViewById(R.id.txtPeso);

        Bundle bundle = getIntent().getExtras(); /*Obtiene valores enviados*/

        idEjercicio = bundle.getString("idEjercicio");

        String nombre = bundle.getString("nombre");
        txvRutina.setText(nombre);

        String series = bundle.getString("series");
        txvCantSeries.setText(series);

        String repeticiones = bundle.getString("repeticiones");
        txvCantRepeticiones.setText(repeticiones);

        String gif = bundle.getString("imagen");
        if(!(gif == "" || gif == null)){
            Glide.with(getApplicationContext()).load(BuildConfig.urlProyecto+"/imagenes/ejercicios/"+gif).into(imvEjercicio);
        }

        mostrarClienteEjercicio(idCliente, idEjercicio);

    }

    private void mostrarClienteEjercicio(String idCliente, String idEjercicio) {
        final String[] peso = {""};
        final String[] notas = {""};
        String URL = BuildConfig.urlProyecto+"/apis/ClienteEjercicio/mostrar.php?txtIdCliente="+idCliente+"&txtIdEjercicio="+idEjercicio;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response", response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jObject = jsonArray.getJSONObject(i);
                                peso[0]=jObject.getString("peso");
                                notas[0]=jObject.getString("notas");
                            }
                            txtNota.setText(notas[0]);
                            txtPeso.setText(peso[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error en cath", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String idCliente = String.valueOf(preferences.getInt("idCliente",0));
        guardarAvance(idCliente, idEjercicio);
    }

    private void guardarAvance(String idCliente, String idEjercicio) {
        String peso = txtPeso.getText().toString().trim();
        String nota = txtNota.getText().toString().trim();
        if(peso.isEmpty()){
            peso="0";
        }
        if(nota.isEmpty()){
            nota="";
        }
        //Implementando un ProgressDialog para ver errores
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        String finalPeso = peso;
        String finalNota = nota;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                BuildConfig.urlProyecto+"/apis/ClienteEjercicio/guardar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("Avances guardados")) {
                            Toast toast = Toast.makeText(
                                    getApplicationContext(),
                                    "Avances guardados",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            progressDialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(
                                    Ejercicio.this,
                                    response,
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(
                        Ejercicio.this,
                        error.getMessage(),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Cargar datos referenciados desde la base de datos
                Map<String,String>params = new HashMap<String,String>();
                params.put("txtIdCliente",idCliente);
                params.put("txtIdEjercicio",idEjercicio);
                params.put("txtPeso", finalPeso);
                params.put("txtNotas", finalNota);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Ejercicio.this);
        requestQueue.add(request);
    }
}