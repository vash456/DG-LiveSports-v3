package com.dg_livesports.dg_livesports;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
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
public class EquiposFragment extends ListFragment implements AdapterView.OnItemClickListener{

    private final String HTTP_EVENT="http://apiclient.resultados-futbol.com/scripts/api/api.php";

    //private String keyAPI = "abe05176484293b0fea3c0f265e2106c";
    private String keyAPI = "6c03727c604e6ae9b9c7d4b1a77d2db8";
    private String tzAPI = "America/Bogota";
    private String formatAPI = "json";
    private String reqAPI = "teams";
    private String leagueAPI;
    private String groupAPI = "all";
    //private String dateAPI = "";

    //private String URL_API = HTTP_EVENT + "?key="+keyAPI+"&tz="+tzAPI+"&format="+formatAPI+"&req="+reqAPI+"&date="+dateAPI;

    private String FIREBASE_URL="https://final-dygsports.firebaseio.com";
    //private String FIREBASE_URL="https://equiposfavoritos-36db4.firebaseio.com";
    private Firebase firebasedatos;


    String idTeam;
    SharedPreferences prefs;
    private String user;
    private String[] nameTeam;

    ArrayAdapter adaptador;
    HttpURLConnection con;
    ProgressDialog progressDialog;

    public EquiposFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        prefs = getActivity().getSharedPreferences("preferencia", Context.MODE_PRIVATE);
        user = String.valueOf(prefs.getString("var_name","Nombre no definido"));

        Firebase.setAndroidContext(getContext());
        firebasedatos = new Firebase(FIREBASE_URL);

        Bundle extras;

        extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            leagueAPI = extras.getString("id_league");
        }

        String URL_API = HTTP_EVENT + "?key="+keyAPI+"&tz="+tzAPI+"&format="+formatAPI+"&req="
                +reqAPI+"&league="+leagueAPI+"&group="+groupAPI;

        try {

            new JsonTaskEquip(getContext()).execute(new URL(URL_API));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return super.onCreateView(inflater,container,savedInstanceState);

    }

    public void onStart(){
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Equipos item2 = (Equipos) adaptador.getItem(position);

        Intent intent = new Intent(getActivity(), DetallesEquipoActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("idTeam", item2.getId());
        startActivityForResult(intent, 1234);


       /* Toast.makeText(getActivity(), "Buscando Estadio...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);//1*/

    }

    private class Equipos {
        private String id;
        private String nameShow;
        private String UrlShield;

        public Equipos() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNameShow() {
            return nameShow;
        }

        public void setNameShow(String nameShow) {
            this.nameShow = nameShow;
        }

        public String getUrlShield() {
            return UrlShield;
        }

        public void setUrlShield(String urlShield) {
            UrlShield = urlShield;
        }
    }

    public class AdaptadorEquipos extends ArrayAdapter<Equipos> {

        public AdaptadorEquipos(Context context, List<Equipos> objects) {
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
                        R.layout.item_lista_equipos,
                        parent,
                        false);
            }

            //Obteniendo instancias de los elementos
            TextView t_nombreEquipo = (TextView)v.findViewById(R.id.t_nombreEquipo);
            ImageView imagen_Shield = (ImageView)v.findViewById(R.id.imagen_Shield);
            final ImageButton botonfavorito = (ImageButton) v.findViewById(R.id.Imagenbutton);


            //Obteniendo instancia de la Tarea en la posición actual
            final Equipos item = getItem(position);

            t_nombreEquipo.setText(item.getNameShow());
            if(imagen_Shield != null) {
                new LoadImage(imagen_Shield).execute(item.getUrlShield());
            }

            //checkeo del boton favoritos
            idTeam = item.getId();
            // firebase no acepta valores con puntos(.)
            /*if(idTeam.equals("R. Sociedad")){
                idTeam = "R Sociedad";
            }*/
            final String id = user+"_EquiposFav "+ idTeam;

            // metodo que identifica si hubo un cambio en la base de datos y asi
            // cambiar a la imagen correspondiente
            firebasedatos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(id).exists()){
                        //Log.i("data", dataSnapshot.child(id).getValue().toString());

                        //count_c1++;

                        //Toast.makeText(getContext(), "true f" + count_c1, Toast.LENGTH_SHORT).show();

                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.favoritos_buttom_on);
                        botonfavorito.setImageBitmap(bmp);

                        //guardar nombre de la imagen en una etiqueta
                        String tag = getContext().getResources().getResourceEntryName(R.drawable.favoritos_buttom_on);
                        botonfavorito.setTag(tag);
                        //equipExist = true;

                    }else {

                        //count_c1++;

                        //Toast.makeText(getContext(), "false f" + count_c1, Toast.LENGTH_SHORT).show();

                        //Toast.makeText(getApplicationContext(),"no encontrado",Toast.LENGTH_SHORT).show();
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.favoritos_buttom_off);
                        botonfavorito.setImageBitmap(bmp);

                        //guardar nombre de la imagen en una etiqueta
                        String tag = getContext().getResources().getResourceEntryName(R.drawable.favoritos_buttom_off);
                        botonfavorito.setTag(tag);
                        //equipExist = false;
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            botonfavorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //cargar nombre de la etiqueta de la imagen
                    String tag = botonfavorito.getTag().toString();
                    idTeam = item.getId();

                    if(tag.equals("favoritos_buttom_on")){

                        //Toast.makeText(getContext(), "imagen " + botonfavorito.getTag(), Toast.LENGTH_SHORT).show();

                        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.favoritos_buttom_off);
                        //botonfavorito.setImageBitmap(bmp);

                        //borrando de favoritos
                        Firebase firebd = firebasedatos.child(user+"_EquiposFav "+ idTeam);
                        firebd.removeValue();

                    } else {

                        //Toast.makeText(getContext(), "imagen " + botonfavorito.getTag(), Toast.LENGTH_SHORT).show();

                        //agregando a favoritos
                        Firebase firebd = firebasedatos.child(user+"_EquiposFav " + idTeam);
                        Equipos equipos = new Equipos();
                        equipos.setId(item.getId());
                        equipos.setNameShow(item.getNameShow());
                        equipos.setUrlShield(item.getUrlShield());
                        firebd.setValue(equipos);

                    }

                }
            });


            //Devolver al ListView la fila creada
            return v;

        }

    }


    public class JsonTaskEquip extends AsyncTask<URL, Void, List<Equipos>> {

        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(
                    context, "Por favor espere", "Procesando...");
        }

        public JsonTaskEquip(Context context) {
            this.context = context;
        }

        @Override
        protected List<Equipos> doInBackground(URL... urls) {
            List<Equipos> EquiposList = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    EquiposList = new ArrayList<>();
                    EquiposList.add(new Equipos());

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());



                    // GsonAnimalParser parser = new GsonAnimalParser();
                    JsonTablasParser parser = new JsonTablasParser();

                    EquiposList = parser.leerFlujoJson(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return EquiposList;
        }

        @Override
        protected void onPostExecute(List<Equipos> EquiposList) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            progressDialog.dismiss();

            if(EquiposList!=null) {

                adaptador = new AdaptadorEquipos(getContext(), EquiposList);

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

        public List<Equipos> leerFlujoJson(InputStream in) throws IOException {

            // CREAMOS LA INSTANCIA DE LA CLASE
            ArrayList<Equipos> lista = new ArrayList<>();

            String jsonStr = inputStreamToString(in).toString();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray pers = jsonObj.getJSONArray("team");

                    // looping through All Equipos
                    for (int i = 0; i < pers.length(); i++) {
                        JSONObject c = pers.getJSONObject(i);

                        Equipos equipos = new Equipos();

                        equipos.setId(c.getString("id"));
                        equipos.setNameShow(c.getString("nameShow"));
                        equipos.setUrlShield(c.getString("shield"));
                        lista.add(equipos);
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