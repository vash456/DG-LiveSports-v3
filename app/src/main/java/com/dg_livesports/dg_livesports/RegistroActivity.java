package com.dg_livesports.dg_livesports;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class RegistroActivity extends AppCompatActivity {

    private String FIREBASE_URL="https://final-dygsports.firebaseio.com";
    //private String FIREBASE_URL="https://equiposfavoritos-36db4.firebaseio.com";
    private Firebase firebasedatos;

    private Button b_aceptar, b_cancelar;
    private EditText et_usuario, et_password1, et_password2, et_email;
    private String nombre,password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Firebase.setAndroidContext(this);
        firebasedatos = new Firebase(FIREBASE_URL);

        b_aceptar = (Button) findViewById(R.id.b_aceptar);
        b_cancelar = (Button) findViewById(R.id.b_cancelar);
        et_usuario = (EditText) findViewById(R.id.et_usuario);
        et_password1 = (EditText) findViewById(R.id.et_password1);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        et_email = (EditText) findViewById(R.id.et_email);


        b_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password1 = et_password1.getText().toString();

                if (TextUtils.isEmpty(et_usuario.getText().toString())){
                    et_usuario.setError("Este campo no puede estar vacio");
                    return;
                }
                if (TextUtils.isEmpty(et_password1.getText().toString())){
                    et_password1.setError("Este campo no puede estar vacio");
                    return;
                }
                if (TextUtils.isEmpty(et_password2.getText().toString())){
                    et_password2.setError("Este campo no puede estar vacio");
                    return;
                }
                if (password1.equals(et_password2.getText().toString()) != true){
                    et_password2.setError("La contraseña debe ser la misma");
                    et_password2.setText("");
                    return;
                }
                if (TextUtils.isEmpty(et_email.getText().toString())){
                    et_email.setError("Este campo no puede estar vacio");
                    return;
                }

                nombre = et_usuario.getText().toString();
                password = et_password2.getText().toString();
                email = et_email.getText().toString();

                final Firebase[] firebd = new Firebase[1];

                //verifica si ya existe el contacto
                final String user = "Usuarios_data "+nombre;

                firebasedatos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user).exists()){
                            et_usuario.setError("Este usuario ya existe");
                            //Toast.makeText(getApplicationContext(),"Usuario existente",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(),dataSnapshot.child(user).getValue().toString(),Toast.LENGTH_SHORT).show();
                            Log.d("DATOSREGISTRO",dataSnapshot.child(user).getValue().toString());
                            return;
                        }else {
                            //Toast.makeText(getApplicationContext(),"no encontrado",Toast.LENGTH_SHORT).show();
                            firebd[0] = firebasedatos.child("Usuarios_data "+nombre);
                            Usuarios_data usuarios_data = new Usuarios_data(nombre,password,email);
                            firebd[0].setValue(usuarios_data);


                            Intent intent = new Intent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });




        b_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
    }
}
