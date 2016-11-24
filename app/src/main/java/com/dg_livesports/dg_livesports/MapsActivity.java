package com.dg_livesports.dg_livesports;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private int positione;
    private GoogleMap mMap;
    private double[] latitud = {42.8371821,
            43.2642396,
            40.4017243,
            41.380896,
            42.2111431,
            43.3687184,
            43.181774,
            41.3479735,
            37.152892,
            28.1004063,
            40.340211,
            36.7335796,
            42.7966922,
            43.3013934,
            37.3565037,
            40.453054,
            37.3840655,
            43.5361873,
            39.4746083,
            39.9441038};
    private double[] longitud = {-2.6880434,
            -2.9494213,
            -3.7206352,
            2.1228198,
            -8.7423679,
            -8.4174835,
            -2.4758586,
            2.0747653,
            -3.5956996,
            -15.4567456,
            -3.759781,
            -4.4266511,
            -1.6371164,
            -1.973682,
            -5.9817521,
            -3.688359,
            -5.9706902,
            -5.6374561,
            -0.360419,
            -0.1034635};
    private String[] estadio = {"Estadio de Mendizorroza",
            "Estadio San Mamés",
            "Estadio Vicente Calderón",
            "Estadio Camp Nou",
            "Estadio de atletismo Balaídos",
            "Estadio Ciudad Deportiva de Riazor",
            "Estadio Ipurua",
            "Estadio Cornellà-El Prat",
            "Estadio Nuevo Los Cármenes",
            "Estadio Gran Canaria",
            "Estadio Municipal Butarque",
            "Estadio La Rosaleda",
            "Estadio El Sadar",
            "Estadio Anoeta",
            "Estadio Benito Villamarín",
            "Estadio Santiago Bernabéu",
            "Estadio Ramón Sánchez-Pizjuán",
            "Estadio El Molinón",
            "Estadio Mestalla",
            "Estadio El Madrigal"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle extras = getIntent().getExtras();//Recibo info de posicion de la tabla
        positione = extras.getInt("position");
        //Toast.makeText(getApplicationContext(), "position = "+positione, Toast.LENGTH_LONG).show();

        ////// botÃ³n de atrÃ¡s////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.temaGrisApp)));
        //actionBar.setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.configuracion) + "</font>"));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    ////// botÃ³n de atrÃ¡s////////
    /*@Override
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
    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.icon_stadium);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACC) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            Toast.makeText(getApplicationContext(),"no hay permisos", Toast.LENGTH_SHORT).show();
        }*/

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        final LatLng stadium = new LatLng(latitud[positione] , longitud[positione]);
        mMap.addMarker(new MarkerOptions()
                .position(stadium)
                .title(estadio[positione])
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stadium,15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Marker")
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                    Toast.makeText(getApplicationContext(),toString().valueOf(latitud[positione])+" , "+toString().valueOf(longitud[positione]), Toast.LENGTH_SHORT).show();

            }
        });
    }

    ////// botón de atrás////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
           /*     Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            */
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

