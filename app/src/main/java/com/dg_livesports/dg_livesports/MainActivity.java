package com.dg_livesports.dg_livesports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String user;
    private String password;
    private String email;
    private String sesion;
    private String otraAct;
    boolean P_otraAct = false;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        /////detectar si hay internet////////


            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }


        //////preferencias compartidas y cerrar sesion////////

        Bundle extras;

        extras = getIntent().getExtras();

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

        //////navigation drawer///////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("DG LiveSports");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_principal);
        navigationView.setNavigationItemSelectedListener(this);

        //////datos usuario en navigation////////

        View hView =  navigationView.getHeaderView(0);
        TextView t_nav_user = (TextView) hView.findViewById(R.id.t_nav_user);
        t_nav_user.setText(user);
        TextView t_nav_email = (TextView) hView.findViewById(R.id.t_nav_email);
        t_nav_email.setText(email);

        ///////tabs///////
        /*tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });*/


        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new MainTabsFragment()).commit();


      }//End OnCreate

    public void logout(){
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        goLoginActivity();
    }

    private void goLoginActivity() {
        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sesion","cerrada");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        ///control para ocultar o mostrar elementos del menu overflow
        MenuItem MI_Sesion = menu.findItem(R.id.action_cerrar_sesion);
        MenuItem MI_Login = menu.findItem(R.id.action_login);

        if (sesion.equals("abierta")){
            MI_Sesion.setVisible(true);
            MI_Login.setVisible(false);
            this.invalidateOptionsMenu();
        }else {
            MI_Sesion.setVisible(false);
            MI_Login.setVisible(true);
            this.invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            //finish();

            return true;
        }

        if (id == R.id.action_cerrar_sesion) {
            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //intent.putExtra("sesion","cerrada");
            logout();
            //startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        /////// fragment tabs///////
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


        switch (id) {
            case R.id.nav_principal:
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                //finish();
                mFragmentTransaction.replace(R.id.containerView,new MainTabsFragment()).commit();
                toolbar.setTitle("DG LiveSports");
                break;
        /*    case R.id.nav_resultados:
                mFragmentTransaction.replace(R.id.containerView,new M1_ResultadosFragment()).commit();
                toolbar.setTitle("Resultados");
                break;
         */   case R.id.nav_partidos_tablas:
                //Intent intent3 = new Intent(getApplicationContext(), TablasEquiposActivity.class);
                //startActivityForResult(intent3, 123);
                //finish();
                mFragmentTransaction.replace(R.id.containerView,new CompeticionesFragment()).commit();
                toolbar.setTitle("Partidos y Tablas");
                break;
            case R.id.nav_noticias:
                mFragmentTransaction.replace(R.id.containerView,new M3_NoticiasFragment()).commit();
                toolbar.setTitle("Noticias");
                break;
            case R.id.nav_videos:
                mFragmentTransaction.replace(R.id.containerView,new M4_VideosFragment()).commit();
                toolbar.setTitle("Videos");
                break;
        /*    case R.id.nav_social:
                //mFragmentTransaction.replace(R.id.containerView,new M5_SocialFragment()).commit();
                //toolbar.setTitle("Social");
                break;
            case R.id.nav_notificaciones:
                /*Intent intent7 = new Intent(getApplicationContext(), NotificacionesActivity.class);
                startActivity(intent7);
                finish();
               break;
        */    case R.id.nav_configuracion:
                Intent intent8 = new Intent(getApplicationContext(), ConfiguracionesActivity.class);
                startActivity(intent8);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new AyerFragment();
                case 1 : return new AyerFragment();
                case 2 : return new AyerFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "AYER";
                case 1 :
                    return "HOY";
                case 2 :
                    return "MAÑANA";
            }
            return null;
        }
    }

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
        sesion = String.valueOf(prefs.getString("var_sesion","cerrada"));
    }

   /* private void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Debe ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                //Object toRemove = adaptador.getItemViewType(position1);
                //Intent intent = new Intent(getContext(), PerfilActivity.class);
                //intent.putExtra("tabFlag",true);
                //startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Toast.makeText(getActivity(), "cancelar", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

}


