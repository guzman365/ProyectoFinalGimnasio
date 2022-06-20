package com.example.proyectofinalgimnasio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.DialogInterface;

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

public class loginUsuarios extends AppCompatActivity {
    private RequestQueue requestQueue;
    EditText txtDui,txtContra;
    Button btnIngresar;
    CheckBox chkEntrenador,chkGuardar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String llave = "sesion";

    @Override
    protected void onRestart() {
        super.onRestart();
        revisarSesion();
        if(revisarSesion()){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuarios);

        inicializarElementos();
        revisarSesion();

        final int[] id = {0};
        final String[] nombres = {""};

        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                id[0] = buscarUsuario();
            }
        });

        if(revisarSesion() || id[0]>0){
            startActivity(new Intent(this, PrincipalUsuario.class));
        }
    }

    private void inicializarElementos(){
        preferences = this.getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        editor = preferences.edit();
        btnIngresar =findViewById(R.id.btnIngresar);
        chkGuardar = findViewById(R.id.chkGuardarSesion);
        txtDui = findViewById(R.id.txtDui);
        txtContra = findViewById(R.id.txtContra);
    }

    private boolean revisarSesion(){
        boolean res = this.preferences.getBoolean(llave,false);
        return res;
    }

    private void guardarSesion(boolean checked, int id, String nombres){
        editor.putBoolean(llave, checked);
        editor.putInt("idCliente",id);
        editor.putString("nombres",nombres);
        editor.commit();
    }

    public void entrar(){
        Intent principalUsuario = new Intent(this, PrincipalUsuario.class);
        startActivity(principalUsuario);
        finish();
    }

    private boolean validarCampos() {
        boolean correcto = true;

        if(txtDui.getText().toString().trim().isEmpty()){
            txtDui.setError("Es obligatorio");
            correcto = false;
        }else if(txtContra.getText().toString().trim().isEmpty()){
            txtContra.setError("Es obligatorio");
            correcto = false;
        }
        return correcto;
    }

    private int buscarUsuario() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        String dui = txtDui.getText().toString().trim();
        String contra = txtContra.getText().toString().trim();
        final String[] nombres = {""};
        final int[] id = {0};
        String URL = BuildConfig.urlProyecto+"/apis/Cliente/validar.php?txtDui="+dui+"&txtContra="+contra;
        if(validarCampos()){
            StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");

                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jObject = jsonArray.getJSONObject(i);

                                id[0]=Integer.valueOf(jObject.getString("idCliente"));
                                nombres[0]=jObject.getString("nombres");
                            }
                            guardarSesion(chkGuardar.isChecked(), id[0], nombres[0]); //guardo datos recibidos
                            entrar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Error en cath: ", e.toString());
                        }

                        if (id[0]>0) {
                            Toast toast = Toast.makeText(
                                    getApplicationContext(),
                                    "Bienvenido",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            progressDialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(
                                    loginUsuarios.this,
                                    "Datos incorrectos",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            ){
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("txtDui",dui);
                    params.put("txtContra",contra);
                    return params;
                }
            };
            requestQueue = Volley.newRequestQueue(loginUsuarios.this);
            requestQueue.add(request);
        }
        return id[0];
    }
}