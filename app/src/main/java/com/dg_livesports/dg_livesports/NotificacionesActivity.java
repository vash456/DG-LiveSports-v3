package com.dg_livesports.dg_livesports;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class NotificacionesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        //////navigation drawer///////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*switch (id) {
            case R.id.nav_principal:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_resultados:
                Intent intent2 = new Intent(getApplicationContext(), ResultadosActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_partidos_tablas:
                Intent intent3 = new Intent(getApplicationContext(), ParttablasActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.nav_noticias:
                Intent intent4 = new Intent(getApplicationContext(), NoticiasActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.nav_videos:
                Intent intent5 = new Intent(getApplicationContext(), VideosActivity.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.nav_social:
                Intent intent6 = new Intent(getApplicationContext(), SocialActivity.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.nav_notificaciones:
                Intent intent7 = new Intent(getApplicationContext(), NotificacionesActivity.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.nav_configuracion:
                Intent intent8 = new Intent(getApplicationContext(), ConfiguracionesActivity.class);
                startActivity(intent8);
                finish();
                break;
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
