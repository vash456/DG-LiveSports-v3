package com.dg_livesports.dg_livesports;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetallesEquipoFragment extends Fragment {

    ImageView URLshield;
    ImageView URLimg_stadium;
    TextView tnameShow;
    TextView tfullName;
    TextView tshort_name;
    TextView tName;//pais
    TextView tcity;
    TextView tchairman;
    TextView tmanagerNow;
    TextView tyearFoundation;
    TextView tyearlyBudget;
    TextView tpatrocinador;
    TextView tteam_b;
    TextView tproveedor;
    TextView tlugar_entrenamiento;
    TextView twebsite;
    TextView tstadium;
    TextView tseats;
    TextView taddress;
    TextView tfans;
    TextView tyearBuilt;
    TextView tsize;

    private int position;
    String idTeam;
    private String competicion;

//tz=Europe/Madrid&format=xml&req=team&id=2107
    private final String HTTP_EVENT="http://apiclient.resultados-futbol.com/scripts/api/api.php";

    //private String keyAPI = "abe05176484293b0fea3c0f265e2106c";
    private String keyAPI = "6c03727c604e6ae9b9c7d4b1a77d2db8";
    private String tzAPI = "Europe/Madrid";
    private String formatAPI = "json";
    private String reqAPI = "team";
    private String idAPI="2107";

    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progressDialog;

    public DetallesEquipoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle extras;

        extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            //competicion = extras.getString("competicion");
            position = extras.getInt("position");
            idAPI = extras.getString("idTeam");
            //Toast.makeText(getActivity(), "idAPI = "+idAPI, Toast.LENGTH_LONG).show();
            //Toast.makeText(getActivity(), "position = "+position, Toast.LENGTH_LONG).show();
        }

        String URL_API = HTTP_EVENT + "?key="+keyAPI+"&tz="+tzAPI+"&format="+formatAPI+"&req="+reqAPI+"&id="+idAPI;

        try {

            new JsonTask(getContext()).execute(new URL(URL_API));
            //new JsonTask(getContext()).execute(new URL(URL_API2));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        View x =  inflater.inflate(R.layout.fragment_detalles_equipo,null);

        URLshield =  (ImageView) x.findViewById(R.id.shield);
        URLimg_stadium =  (ImageView) x.findViewById(R.id.img_stadium);
        tnameShow = (TextView) x.findViewById(R.id.tnameShow);
        tfullName = (TextView) x.findViewById(R.id.tfullName);
        tshort_name = (TextView) x.findViewById(R.id.tshort_name);
        tName = (TextView) x.findViewById(R.id.tName);
        tcity = (TextView) x.findViewById(R.id.tcity);
        tchairman = (TextView) x.findViewById(R.id.tchairman);
        tmanagerNow = (TextView) x.findViewById(R.id.tmanagerNow);
        tyearFoundation = (TextView) x.findViewById(R.id.tyearFoundation);
        tyearlyBudget = (TextView) x.findViewById(R.id.tyearlyBudget);
        tpatrocinador = (TextView) x.findViewById(R.id.tpatrocinador);
        tteam_b = (TextView) x.findViewById(R.id.tteam_b);
        tproveedor = (TextView) x.findViewById(R.id.tproveedor);
        tlugar_entrenamiento = (TextView) x.findViewById(R.id.tlugar_entrenamiento);
        twebsite = (TextView) x.findViewById(R.id.twebsite);
        tstadium = (TextView) x.findViewById(R.id.tstadium);
        tseats = (TextView) x.findViewById(R.id.tseats);
        taddress = (TextView) x.findViewById(R.id.taddress);
        tfans = (TextView) x.findViewById(R.id.tfans);
        tyearBuilt = (TextView) x.findViewById(R.id.tyearBuilt);
        tsize = (TextView) x.findViewById(R.id.tsize);

        URLimg_stadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (competicion.equals(""))
                Toast.makeText(getActivity(), "Buscando Estadio...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, 567);
            }
        });

        return x;
    }


    public class JsonTask extends AsyncTask<URL, Void, List<DetallesEquipo>> {

        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(
                    context, "Por favor espere", "Procesando...");
        }

        public JsonTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<DetallesEquipo> doInBackground(URL... urls) {
            List<DetallesEquipo> detallesEquipos = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    detallesEquipos = new ArrayList<>();
                    detallesEquipos.add(new DetallesEquipo());

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());



                    // GsonAnimalParser parser = new GsonAnimalParser();
                    JsonTablasParser parser = new JsonTablasParser();

                    detallesEquipos = parser.leerFlujoJson(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return detallesEquipos;
        }

        @Override
        protected void onPostExecute(List<DetallesEquipo> detallesEquipos) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            progressDialog.dismiss();

            if(detallesEquipos!=null) {

                String img_shield = detallesEquipos.get(0).getURLshield();
                if(URLshield != null) {
                    new LoadImage(URLshield).execute(img_shield);
                }
                String img_stadium = detallesEquipos.get(0).getURLimg_stadium();
                if(URLimg_stadium != null) {
                    new LoadImage(URLimg_stadium).execute(img_stadium);
                }
                tnameShow.setText(detallesEquipos.get(0).getNameShow());
                tName.setText(detallesEquipos.get(0).getName());
                taddress.setText(detallesEquipos.get(0).getAddress());
                tchairman.setText(detallesEquipos.get(0).getChairman());

                String city = detallesEquipos.get(0).getCity();
                if (city.equals("0")||detallesEquipos.get(0).getCity()==null||city.equals("null"))tcity.setText("-");
                else tcity.setText(detallesEquipos.get(0).getCity());

                tfans.setText(detallesEquipos.get(0).getFans());
                tfullName.setText(detallesEquipos.get(0).getFullName());
                tlugar_entrenamiento.setText(detallesEquipos.get(0).getLugar_entrenamiento());
                tmanagerNow.setText(detallesEquipos.get(0).getManagerNow());
                tpatrocinador.setText(detallesEquipos.get(0).getPatrocinador());
                tproveedor.setText(detallesEquipos.get(0).getProveedor());
                tshort_name.setText(detallesEquipos.get(0).getShort_name());
                tsize.setText(detallesEquipos.get(0).getSize());
                tstadium.setText(detallesEquipos.get(0).getStadium());
                tteam_b.setText(detallesEquipos.get(0).getTeam_b());
                twebsite.setText(detallesEquipos.get(0).getWebsite());
                tyearBuilt.setText(detallesEquipos.get(0).getYearBuilt());
                tyearFoundation.setText(detallesEquipos.get(0).getYearFoundation());
                tyearlyBudget.setText(detallesEquipos.get(0).getYearlyBudget());
                tseats.setText(detallesEquipos.get(0).getSeats());


            }else{
                Toast.makeText(
                        getContext(),
                        "Ocurrió un error de Parsing Json",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    public class JsonTablasParser {

        public List<DetallesEquipo> leerFlujoJson(InputStream in) throws IOException {

            // CREAMOS LA INSTANCIA DE LA CLASE
            final ArrayList<DetallesEquipo> lista = new ArrayList<>();

            String jsonStr = inputStreamToString(in).toString();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    JSONObject c = jsonObj.getJSONObject("team");

                    //JSONObject pers2 = jsonObj.getJSONObject("team");

                    /*String img_shield = pers2.getString("shield");
                    if(URLshield != null) {
                        new LoadImage(URLshield).execute(img_shield);
                    }
                    String img_stadium = pers2.getString("img_stadium");
                    if(URLimg_stadium != null) {
                        new LoadImage(URLimg_stadium).execute(img_stadium);
                    }
                    tnameShow.setText(pers2.getString("nameShow"));
                    tName.setText(pers2.getString("Name"));
                    taddress.setText(pers2.getString("address"));
                    tchairman.setText(pers2.getString("chairman"));
                    tcity.setText(pers2.getString("city"));
                    tfans.setText(pers2.getString("fans"));
                    tfullName.setText(pers2.getString("fullName"));
                    tlugar_entrenamiento.setText(pers2.getString("lugar_entrenamiento"));
                    tmanagerNow.setText(pers2.getString("managerNow"));
                    tpatrocinador.setText(pers2.getString("patrocinador"));
                    tproveedor.setText(pers2.getString("proveedor"));
                    tshort_name.setText(pers2.getString("short_name"));
                    tsize.setText(pers2.getString("size"));
                    tstadium.setText(pers2.getString("stadium"));
                    tteam_b.setText(pers2.getString("team_b"));
                    twebsite.setText(pers2.getString("website"));
                    tyearBuilt.setText(pers2.getString("yearBuilt"));
                    tyearFoundation.setText(pers2.getString("yearFoundation"));
                    tyearlyBudget.setText(pers2.getString("yearlyBudget"));
                    tseats.setText(pers2.getString("seats"));*/

                    DetallesEquipo detalles = new DetallesEquipo();

                    detalles.setNameShow(c.getString("nameShow"));
                    detalles.setName(c.getString("Name"));
                    detalles.setAddress(c.getString("address"));
                    detalles.setChairman(c.getString("chairman"));
                    detalles.setCity(c.getString("city"));
                    detalles.setFans(c.getString("fans"));
                    detalles.setFullName(c.getString("fullName"));
                    detalles.setLugar_entrenamiento(c.getString("lugar_entrenamiento"));
                    detalles.setManagerNow(c.getString("managerNow"));
                    detalles.setPatrocinador(c.getString("patrocinador"));
                    detalles.setProveedor(c.getString("proveedor"));
                    detalles.setShort_name(c.getString("short_name"));
                    detalles.setSize(c.getString("size"));
                    detalles.setStadium(c.getString("stadium"));
                    detalles.setTeam_b(c.getString("team_b"));
                    detalles.setURLimg_stadium(c.getString("img_stadium"));
                    detalles.setURLshield(c.getString("shield"));
                    detalles.setWebsite(c.getString("website"));
                    detalles.setYearBuilt(c.getString("yearBuilt"));
                    detalles.setYearFoundation(c.getString("yearFoundation"));
                    detalles.setYearlyBudget(c.getString("yearlyBudget"));
                    detalles.setSeats(c.getString("seats"));

                    lista.add(detalles);

                    return lista;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Esta habiendo problemas para cargar el JSON");
            }

            return null;

        }

        private StringBuilder inputStreamToString(InputStream is)
        {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
            try
            {
                while( (line = rd.readLine()) != null )
                {
                    stringBuilder.append(line);
                }
            }
            catch( IOException e)
            {
                e.printStackTrace();
            }

            return stringBuilder;
        }

    }

}