package com.example.webservicedni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText txtDNI, txtNombre, txtPaterno, txtMaterno;
    Button btnBuscar;
    ListView lst;
    adminSqlLite admin = new adminSqlLite(this,"DBPersona",null,1);
    String url = "https://api.apis.net.pe/v1/dni?numero=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDNI=findViewById(R.id.txtDNI);
        txtPaterno=findViewById(R.id.txtApPaterno);
        txtMaterno=findViewById(R.id.txtApMaterno);
        txtNombre=findViewById(R.id.txtNombre);
        btnBuscar=findViewById(R.id.btnBuscar);
        lst=findViewById(R.id.List);

        txtDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(txtDNI.getText().length()==8){
                    DNI();
                    btnBuscar.setEnabled(true);
                }else{
                    txtNombre.setText("");
                    txtPaterno.setText("");
                    txtMaterno.setText("");
                    btnBuscar.setEnabled(false);
                }
            }
        });

        llenarList();
    }

    void llenarList(){
        SQLiteDatabase BD =  admin.getWritableDatabase();
        ArrayList<String> Persona = new ArrayList<>();
        Cursor fila = BD.rawQuery("select * from Persona", null);
        if (fila.moveToFirst()) {
            do {
                Persona.add(fila.getString(0)+" | "+fila.getString(2)+" "+fila.getString(3)+","+fila.getString(1));
            }while (fila.moveToNext());
        }
        BD.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Persona);
        lst.setAdapter(adapter);
    }

    void DNI(){
        StringRequest post = new StringRequest(Request.Method.GET, url + txtDNI.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jso = new JSONObject(response.toString());
                    txtNombre.setText(jso.getString("nombres"));
                    txtPaterno.setText(jso.getString("apellidoPaterno"));
                    txtMaterno.setText(jso.getString("apellidoMaterno"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "DNI nO EnCoNtRaDo", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(post);
    }

    public  void btnClick (View view){
        SQLiteDatabase BD =  admin.getWritableDatabase();
        ContentValues Datos = new ContentValues();

        Datos.put("DNI",txtDNI.getText().toString());
        Datos.put("Nombres",txtNombre.getText().toString());
        Datos.put("apPaterno",txtPaterno.getText().toString());
        Datos.put("apMaterno",txtMaterno.getText().toString());

        BD.insert("Persona",null,Datos);
        BD.close();
        txtDNI.setText("");
        Toast.makeText(this, "Registro Existoso", Toast.LENGTH_SHORT).show();
        llenarList();
    }
}