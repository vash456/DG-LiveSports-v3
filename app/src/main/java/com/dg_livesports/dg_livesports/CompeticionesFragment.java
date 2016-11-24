package com.dg_livesports.dg_livesports;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompeticionesFragment extends Fragment implements AdapterView.OnItemClickListener{

    private League[] datos = new League[]{
            new League("1","Primera Division", R.drawable.ban_espana),
            new League("50","Clausura Colombia", R.drawable.ban_colombia),
            new League("675","Primera División Argentina", R.drawable.ban_argentina),
            new League("24","Liga Brasileña", R.drawable.ban_brasil),
            new League("120","Clasificación Mundial Sudamérica", R.drawable.ban_clas_sudamerica)};

    public CompeticionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View x =  inflater.inflate(R.layout.fragment_competiciones,null);

        Adapter adaptador = new Adapter(getContext(), datos);

        ListView listView = (ListView)x.findViewById(R.id.listviewLigas);

        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(this);

        //setListAdapter(adaptador);

        return x;
        //return super.onCreateView(inflater,container,savedInstanceState);
    }

    /*public void onStart(){
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setOnItemClickListener(this);

    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Equipos item2 = (Equipos) adaptador.getItem(position);

        Intent intent = new Intent(getActivity(), TablasEquiposActivity.class);
        intent.putExtra("id_league", datos[position].getIdLeague());
        startActivityForResult(intent, 123);

    }

    class Adapter extends ArrayAdapter<League> {
        public Adapter(Context context, League[] datos) {
            super(context, R.layout.item_leagues, datos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.item_leagues, null);

            ImageView imagen = (ImageView) item.findViewById(R.id.img_league);
            imagen.setImageResource(datos[position].getUrlbandera());

            TextView nombre = (TextView) item.findViewById(R.id.t_league);
            nombre.setText(datos[position].getName());


            return (item);
        }
    }

    private class League {
        private String idLeague;
        private String name;
        private int Urlbandera;

        public League() {
        }

        public League(String idLeague, String name, int urlbandera) {
            this.idLeague = idLeague;
            this.name = name;
            Urlbandera = urlbandera;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUrlbandera() {
            return Urlbandera;
        }

        public void setUrlbandera(int urlbandera) {
            Urlbandera = urlbandera;
        }

        public String getIdLeague() {
            return idLeague;
        }

        public void setIdLeague(String idLeague) {
            this.idLeague = idLeague;
        }
    }

}
