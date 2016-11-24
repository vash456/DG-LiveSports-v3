package com.dg_livesports.dg_livesports;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

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
public class TablasFragment extends Fragment {

    private final String HTTP_EVENT="http://apiclient.resultados-futbol.com/scripts/api/api.php";

    //private String keyAPI = "abe05176484293b0fea3c0f265e2106c";
    private String keyAPI = "6c03727c604e6ae9b9c7d4b1a77d2db8";
    private String tzAPI = "Europe/Madrid";
    private String formatAPI = "json";
    private String reqAPI = "tables";
    private String leagueAPI;
    private String groupAPI = "1";

    HttpURLConnection con;
    ProgressDialog progressDialog;

    Tabla tabla;

    public TablasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_tablas, container, false);

        Bundle extras;

        extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            leagueAPI = extras.getString("id_league");
        }

        String URL_API = HTTP_EVENT + "?key="+keyAPI+"&tz="+tzAPI+"&format="+formatAPI
                +"&req="+reqAPI+"&league="+leagueAPI+"&group"+groupAPI;


        View x =  inflater.inflate(R.layout.fragment_tablas,null);

        tabla = new Tabla(getActivity(), (TableLayout)x.findViewById(R.id.tabla));

        try {

            new JsonTask(getContext()).execute(new URL(URL_API));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return x;

    }

    public class JsonTask extends AsyncTask<URL, Void, List<Clasificaciones>>{

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
        protected List<Clasificaciones> doInBackground(URL... urls) {
            List<Clasificaciones> tablaclasi = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    tablaclasi = new ArrayList<>();
                    tablaclasi.add(new Clasificaciones());

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());



                    // GsonAnimalParser parser = new GsonAnimalParser();
                    JsonTablasParser parser = new JsonTablasParser();

                    tablaclasi = parser.leerFlujoJson(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return tablaclasi;
        }

        @Override
        protected void onPostExecute(List<Clasificaciones> tablaclasi) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            progressDialog.dismiss();

            if(tablaclasi!=null) {

                tabla.agregarCabecera(R.array.cabecera_tabla);
                for(int i = 0; i < tablaclasi.size(); i++)
                {
                    ArrayList<String> elementos = new ArrayList<String>();
                    elementos.add(Integer.toString(i+1));
                    elementos.add(tablaclasi.get(i).getDirection());
                    elementos.add(tablaclasi.get(i).getURLshield());
                    elementos.add(tablaclasi.get(i).getTeam());
                    elementos.add(tablaclasi.get(i).getPoints());
                    elementos.add(tablaclasi.get(i).getRound());
                    elementos.add(tablaclasi.get(i).getWins());
                    elementos.add(tablaclasi.get(i).getDraws());
                    elementos.add(tablaclasi.get(i).getLosses());
                    elementos.add(tablaclasi.get(i).getGf());
                    elementos.add(tablaclasi.get(i).getGa());
                    elementos.add(tablaclasi.get(i).getAvg());
                    tabla.agregarFilaTabla(elementos);
                }

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

        public List<Clasificaciones> leerFlujoJson(InputStream in) throws IOException {

            // CREAMOS LA INSTANCIA DE LA CLASE
            ArrayList<Clasificaciones> lista = new ArrayList<>();

            String jsonStr = inputStreamToString(in).toString();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray pers = jsonObj.getJSONArray("table");

                    // looping through All Equipos
                    for (int i = 0; i < pers.length(); i++) {
                        JSONObject c = pers.getJSONObject(i);


                        Clasificaciones clasificacion = new Clasificaciones();

                        clasificacion.setPos(c.getString("pos"));
                        clasificacion.setDirection(c.getString("direction"));
                        clasificacion.setURLshield(c.getString("shield"));
                        clasificacion.setTeam(c.getString("team"));
                        clasificacion.setPoints(c.getString("points"));
                        clasificacion.setRound(c.getString("round"));
                        clasificacion.setWins(c.getString("wins"));
                        clasificacion.setDraws(c.getString("draws"));
                        clasificacion.setLosses(c.getString("losses"));
                        clasificacion.setGf(c.getString("gf"));
                        clasificacion.setGa(c.getString("ga"));
                        clasificacion.setAvg(c.getString("avg"));

                        lista.add(clasificacion);

                    }
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
