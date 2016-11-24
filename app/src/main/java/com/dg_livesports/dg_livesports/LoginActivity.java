package com.dg_livesports.dg_livesports;

//import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Usuarios_data> info;

    private String FIREBASE_URL="https://final-dygsports.firebaseio.com";
    //private String FIREBASE_URL="https://equiposfavoritos-36db4.firebaseio.com";
    private Firebase firebasedatos;

    Button b_entrar;
    EditText et_usuario, et_password;
    TextView t_Registro;

    private String user;
    private String password;
    private String email;
    private String sesion;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    //GOogle SIgn
    /*private SignInButton mGoogleBtn;
    private static final int RC_SIGN_IN = 1;
    private  GoogleApiClient mGoogleApiClient;
    private int optLog;*/

    private LoginButton loginButton;        //Login con Facebook
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;      //Configuracion de Firebase
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private static final int RC_SIGN_IN = 1;    //Login con Google
    private static final String TAG ="LoginActivity" ;
    private Button mGoogleBtn;
    private GoogleApiClient mGoogleApiClient;

    private int optLog; //1. login con google 2. login con Facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mGoogleBtn = (Button) findViewById(R.id.sign_in_button);

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Error en login", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //goMainActivity();
                handleFacebookAccessToken(loginResult.getAccessToken());
                optLog = 2;
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error login Facebook", Toast.LENGTH_LONG).show();

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user2 = firebaseAuth.getCurrentUser();
                if (user2!=null){
                    Toast.makeText(getApplicationContext(), "Login exitoso"
                            +"\n"+user2.getDisplayName()
                            +"\n"+user2.getEmail(), Toast.LENGTH_LONG).show();
                    user = user2.getDisplayName();
                    email = user2.getEmail();
                    goMainActivity();
                }
            }//Id del usuario user.getUid();
        };

        info = new ArrayList<>();
        Firebase.setAndroidContext(this);
        firebasedatos = new Firebase(FIREBASE_URL);

        Bundle extras;

        extras = getIntent().getExtras();

        ////////////////prefencias compartidas/////////////////

        prefs = getSharedPreferences("preferencia", Context.MODE_PRIVATE);
        editor = prefs.edit();

        refreshPrefs();
        if (extras != null) {
            sesion = extras.getString("sesion");
            Toast.makeText(this, "Sesión "+sesion,Toast.LENGTH_SHORT).show();
            user = "Invitado";
            password = "";
            email = "";
            savePrefs();
            editor.putString("var_sesion",sesion);
            editor.commit();
        }
        if (sesion.equals("abierta")) {
            //Intent intent3 = new Intent(this, MainActivity.class);
            //startActivity(intent3);
            //finish();
        }else if (sesion.equals("cerrada")){
            user = "Invitado";
            password = "";
            email = "";
            savePrefs();
        }


        ////////////////////////////////////////

        b_entrar = (Button) findViewById(R.id.b_entrar);
        et_usuario = (EditText) findViewById(R.id.et_usuario);
        et_password = (EditText) findViewById(R.id.et_password);
        t_Registro = (TextView) findViewById(R.id.t_Registro);



        SpannableString content = new SpannableString(t_Registro.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        t_Registro.setText(content);

        t_Registro.setOnClickListener(this);

        b_entrar.setOnClickListener(this);

        ////// botón de atrás////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ////actionbar transparente//////
        //actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setStatusBarTranslucent(true);

    }


    private void signIn() {
        optLog = 1;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (optLog == 1) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    // Google Sign In failed, update UI appropriately
                    // ...
                }
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error en login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void goMainActivity() {
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        sesion = "abierta";
        savePrefs();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.t_Registro:
                Intent intent3 = new Intent(this, RegistroActivity.class);
                //startActivity(intent);//1
                startActivityForResult(intent3, 1234);//2
                break;
            //case R.id.b_registro:
            //Intent intent = new Intent(this, RegistroActivity.class);
            //startActivity(intent);//1
            //startActivityForResult(intent, 1234);//2
            //    break;

            case R.id.b_entrar:

                if (TextUtils.isEmpty(et_usuario.getText().toString())) {
                    et_usuario.setError("Este campo no puede estar vacio");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    et_password.setError("Este campo no puede estar vacio");
                    return;
                }

                user = et_usuario.getText().toString();
                password = et_password.getText().toString();

                //verifica si ya existe el contacto
                final String user1 = "Usuarios_data "+user;

                firebasedatos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user1).exists()){
                            Log.d("DATOSREGISTRO",dataSnapshot.child(user1).getValue().toString());
                            //Toast.makeText(getApplicationContext(),dataSnapshot.child(user1).getValue().toString(),Toast.LENGTH_SHORT).show();

                            //info.add(dataSnapshot.child("Usuarios_data"+user).getValue(Usuarios_data.class));
                            //if (password.equals(info.get(0).getPassword())){
                            //    email = info.get(0).getEmail();
                            Usuarios_data usuarios_data = dataSnapshot.child("Usuarios_data "
                                    +user).getValue(Usuarios_data.class);
                            if (password.equals(usuarios_data.getPassword())){
                                email = usuarios_data.getEmail();
                                sesion = "abierta";
                                savePrefs();
                                Toast.makeText(getApplicationContext(), "Bienvenido !!!",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent2);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta.",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                break;
        }

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//2
        if (requestCode == 1234 && resultCode == RESULT_OK){
            //user = data.getExtras().getString("usuario");

            editor.putString("var_sesion","cerrada");
            editor.commit();

            Toast.makeText(this, "Usuario registrado exitosamente.",Toast.LENGTH_SHORT).show();
        }

        if (requestCode==1234 && resultCode == RESULT_CANCELED){
            Log.d("mensaje","no se cargaron datos");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }*/

    public void savePrefs(){
        editor.putString("var_name",user);
        editor.putString("var_pass",password);
        editor.putString("var_email",email);
        editor.putString("var_sesion",sesion);
        editor.commit();
    }
    public void refreshPrefs(){
        user = String.valueOf(prefs.getString("var_name","Nombre no definido"));
        password = String.valueOf(prefs.getString("var_pass","contraseña no definida"));
        email = String.valueOf(prefs.getString("var_email","Email no definido"));
        sesion = String.valueOf(prefs.getString("var_sesion","sesion no definida"));
    }

    ////// botón de atrás////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///transparencias actionbar
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}