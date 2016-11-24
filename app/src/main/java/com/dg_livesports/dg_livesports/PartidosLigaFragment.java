package com.dg_livesports.dg_livesports;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
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
public class PartidosLigaFragment extends ListFragment {

    //req=matchs&league=129&order=twin&twolegged=1
    private final String HTTP_EVENT="http://apiclient.resultados-futbol.com/scripts/api/api.php";

    //private String keyAPI = "abe05176484293b0fea3c0f265e2106c";
    private String keyAPI = "6c03727c604e6ae9b9c7d4b1a77d2db8";
    private String tzAPI = "America/Bogota";
    private String formatAPI = "json";
    private String reqAPI = "matchs";
    private String leagueAPI;
    private String orderAPI = "twin";
    private String twoleggedAPI = "1";

    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progressDialog;

    public PartidosLigaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle extras;

        extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            leagueAPI = extras.getString("id_league");
        }

        String URL_API = HTTP_EVENT + "?key="+keyAPI+"&tz="+tzAPI+"&format="+formatAPI+"&req="
                +reqAPI+"&league="+leagueAPI+"&order="+orderAPI+"twolegged"+twoleggedAPI;

        try {

            new JsonTask(getContext()).execute(new URL(URL_API));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    public void onStart(){
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //getListView().setOnItemClickListener(this);

    }

    public class JsonTask extends AsyncTask<URL, Void, List<Partidos>> {

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
        protected List<Partidos> doInBackground(URL... urls) {
            List<Partidos> partidosList = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    partidosList = new ArrayList<>();
                    partidosList.add(new Partidos());

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());



                    // GsonAnimalParser parser = new GsonAnimalParser();
                    JsonTablasParser parser = new JsonTablasParser();

                    partidosList = parser.leerFlujoJson(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return partidosList;
        }

        @Override
        protected void onPostExecute(List<Partidos> partidosList) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            progressDialog.dismiss();

            if(partidosList!=null) {

                adaptador = new AdaptadorPartidos(getContext(), partidosList);

                setListAdapter(adaptador);

                //((ArrayAdapter<Partidos>) getListView().getAdapter()).notifyDataSetChanged();
                //adaptador.notifyDataSetChanged();

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

        public List<Partidos> leerFlujoJson(InputStream in) throws IOException {

            // CREAMOS LA INSTANCIA DE LA CLASE
            final ArrayList<Partidos> lista = new ArrayList<>();

            String jsonStr = inputStreamToString(in).toString();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray pers = jsonObj.getJSONArray("match");

                    // looping through All Equipos
                    for (int i = 0; i < pers.length(); i++) {

                        final JSONObject c = pers.getJSONObject(i);

                        Partidos partidos = new Partidos();

                        partidos.setLocal(c.getString("local"));
                        partidos.setVisitor(c.getString("visitor"));
                        partidos.setCompetition_name(c.getString("competition_name"));
                        partidos.setURLcflag(null);
                        partidos.setURLlocal_shield(c.getString("local_shield"));
                        partidos.setURLvisitor_shield(c.getString("visitor_shield"));
                        partidos.setDate(c.getString("date"));
                        partidos.setResult(c.getString("result"));
                        partidos.setExtraTxt(c.getString("extraTxt"));
                        partidos.setHour(c.getString("hour"));
                        partidos.setMinute(c.getString("minute"));

                        lista.add(partidos);

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
