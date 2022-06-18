package com.example.proyectofinalgimnasio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrincipalUsuario extends AppCompatActivity {
    private RequestQueue requestQueue;
    SharedPreferences.Editor editor;
    TextView txvCliente, txvObjetivo, txvNumSesiones, txvEntrenador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_usuario);
        txvCliente=findViewById(R.id.txvNombreCliente);
        txvEntrenador = findViewById(R.id.txvNombreEntrenador);
        txvNumSesiones = findViewById(R.id.txvNumSesiones);
        txvObjetivo = findViewById(R.id.txvObjetivo);
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String idCliente = String.valueOf(preferences.getInt("idCliente",0));
        String nombres = preferences.getString("nombres", "Usuario no identificado");
        txvCliente.setText(nombres);
        obtenerEntrenamientoCliente(idCliente);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.item1){
            SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("idCliente",0);
            editor.putString("nombres","");
            editor.putBoolean("sesion",false);
            editor.commit();

            Intent cerrarSerion = new Intent(this, loginUsuarios.class);
            startActivity(cerrarSerion);
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerEntrenamientoCliente(String idCliente) {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        editor = preferences.edit();
        final String[] idEntrenamiento = {""};
        final String[] entrenador = {""};
        final String[] numeroSesiones = {""};
        final String[] objetivo = {""};
        String URL = BuildConfig.urlProyecto+"/apis/Entrenamiento/obtenerEntrenamientoCliente.php?txtIdCliente="+idCliente;
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
                                idEntrenamiento[0]=jObject.getString("idEntrenamiento");
                                entrenador[0]=jObject.getString("entrenador");
                                numeroSesiones[0]=jObject.getString("numeroSesiones");
                                objetivo[0]=jObject.getString("objetivo");
                            }
                            editor.putString("idEntrenamiento",idEntrenamiento[0]);
                            editor.commit();
                            txvEntrenador.setText(entrenador[0]);
                            txvNumSesiones.setText(numeroSesiones[0]);
                            txvObjetivo.setText((objetivo[0]));
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
}