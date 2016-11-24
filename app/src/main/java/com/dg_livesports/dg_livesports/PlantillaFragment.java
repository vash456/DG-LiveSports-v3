package com.dg_livesports.dg_livesports;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
public class PlantillaFragment extends ListFragment {

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

    public PlantillaFragment() {
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

        return super.onCreateView(inflater,container,savedInstanceState);

    }

    public void onStart(){
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //getListView().setOnItemClickListener(this);

    }

    private class Plantilla {

        private String nick;
        private String role;
        private String squadNumber;
        private String CountryCode;
        private String Urlimage;

        public Plantilla() {
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getSquadNumber() {
            return squadNumber;
        }

        public void setSquadNumber(String squadNumber) {
            this.squadNumber = squadNumber;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String countryCode) {
            CountryCode = countryCode;
        }

        public String getUrlimage() {
            return Urlimage;
        }

        public void setUrlimage(String urlimage) {
            Urlimage = urlimage;
        }
    }

    public class AdaptadorPlantilla extends ArrayAdapter<Plantilla> {

        public AdaptadorPlantilla(Context context, List<Plantilla> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent){

            //Obteniendo una instancia del inflater
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Salvando la referencia del View de la fila
            View v = convertView;

            //Comprobando si el View no existe
            if (null == convertView) {
                //Si no existe, entonces inflarlo
                v = inflater.inflate(
                        R.layout.item_plantilla,
                        parent,
                        false);
            }

            //Obteniendo instancias de los elementos
            ImageView imagen_image = (ImageView)v.findViewById(R.id.img_jugador);
            TextView t_nick = (TextView)v.findViewById(R.id.t_nick);
            TextView t_role = (TextView)v.findViewById(R.id.t_role);
            TextView t_squadNumber = (TextView)v.findViewById(R.id.t_squadNumber);
            TextView t_CountryCode = (TextView)v.findViewById(R.id.t_CountryCode);

            //Obteniendo instancia de la Tarea en la posición actual
            Plantilla item = getItem(position);

            t_nick.setText(item.getNick());

            switch (item.getRole()){
                case "1":
                    t_role.setText("Portero");
                    break;
                case "2":
                    t_role.setText("Defensa");
                    break;
                case "3":
                    t_role.setText("Volante");
                    break;
                case "4":
                    t_role.setText("Delantero");
                    break;
                default:
                    t_role.setText("-");
                    break;
            }

            String number = item.getSquadNumber();
            if (item.getSquadNumber()==null||number.equals("null"))t_squadNumber.setText("---");
            else {
                if (number.length() < 2) t_squadNumber.setText(" "+item.getSquadNumber()+" ");
                else t_squadNumber.setText(item.getSquadNumber());
            }

            t_CountryCode.setText("("+item.getCountryCode()+")");

            if(imagen_image != null) {
                new LoadImage(imagen_image).execute(item.getUrlimage());
            }

            //Devolver al ListView la fila creada
            return v;

        }

    }

    public class JsonTask extends AsyncTask<URL, Void, List<Plantilla>> {

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
        protected List<Plantilla> doInBackground(URL... urls) {
            List<Plantilla> plantillas = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    plantillas = new ArrayList<>();
                    plantillas.add(new Plantilla());

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());



                    // GsonAnimalParser parser = new GsonAnimalParser();
                    JsonTablasParser parser = new JsonTablasParser();

                    plantillas = parser.leerFlujoJson(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return plantillas;
        }

        @Override
        protected void onPostExecute(List<Plantilla> plantillasList) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            progressDialog.dismiss();

            if(plantillasList!=null) {

                adaptador = new AdaptadorPlantilla(getContext(), plantillasList);

                setListAdapter(adaptador);


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

        public List<Plantilla> leerFlujoJson(InputStream in) throws IOException {

            // CREAMOS LA INSTANCIA DE LA CLASE
            final ArrayList<Plantilla> lista = new ArrayList<>();

            String jsonStr = inputStreamToString(in).toString();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    JSONObject c = jsonObj.getJSONObject("team");

                    // Getting JSON Array node
                    JSONArray pers = c.getJSONArray("squad");

                    // looping through All Equipos
                    for (int i = 0; i < pers.length(); i++) {
                        JSONObject c2 = pers.getJSONObject(i);

                        Plantilla plantilla = new Plantilla();

                        plantilla.setUrlimage(c2.getString("image"));
                        plantilla.setNick(c2.getString("nick"));
                        plantilla.setRole(c2.getString("role"));
                        plantilla.setSquadNumber(c2.getString("squadNumber"));
                        plantilla.setCountryCode(c2.getString("CountryCode"));

                        lista.add(plantilla);
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
