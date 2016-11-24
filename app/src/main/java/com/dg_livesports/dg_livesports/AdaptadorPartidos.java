package com.dg_livesports.dg_livesports;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by DARLIN on 08/11/2016.
 */
public class AdaptadorPartidos extends ArrayAdapter<Partidos> {

    public AdaptadorPartidos(Context context, List<Partidos> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View v = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            v = inflater.inflate(
                    R.layout.item_lista,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView t_compentition_name = (TextView)v.findViewById(R.id.t_competition_name);
        TextView t_date = (TextView)v.findViewById(R.id.t_date);
        TextView t_local = (TextView)v.findViewById(R.id.t_local);
        TextView t_resultado = (TextView)v.findViewById(R.id.t_result);
        TextView t_liveminute = (TextView)v.findViewById(R.id.t_liveminute);
        TextView t_visitor = (TextView)v.findViewById(R.id.t_visitor);
        ImageView imagen_bandera = (ImageView)v.findViewById(R.id.imagen_bandera);
        ImageView imagen_local = (ImageView)v.findViewById(R.id.imagen_localShield);
        ImageView imagen_visitor = (ImageView)v.findViewById(R.id.imagen_visitorShield);


        //Obteniendo instancia de la Tarea en la posición actual
        Partidos item = getItem(position);

        String jugado = item.getResult();
        String hora = item.getHour()+":"+item.getMinute();

        t_compentition_name.setText(item.getCompetition_name());
        //t_date.setText(item.getLive_minute());

        if (item.getLive_minute() == null || item.getLive_minute().toString().equals("")) {
            t_date.setText(item.getExtraTxt());
            t_liveminute.setText("");
            t_date.setTextColor(Color.BLACK);
        }
        else {
            t_date.setText("En vivo");
            t_liveminute.setText("min "+item.getLive_minute());
            t_date.setTextColor(Color.RED);
        }


        t_local.setText(item.getLocal());

        if (jugado.equals("x-x")) t_resultado.setText(hora);
        else t_resultado.setText(item.getResult());

        t_visitor.setText(item.getVisitor());

        //imagenAnimal.setImageResource(convertirRutaEnId(item.getImagen()));


        if(imagen_bandera != null) {
            new LoadImage(imagen_bandera).execute(item.getURLcflag());
        }
        if(imagen_local != null) {
            new LoadImage(imagen_local).execute(item.getURLlocal_shield());
        }
        if(imagen_visitor != null) {
            new LoadImage(imagen_visitor).execute(item.getURLvisitor_shield());
        }

        //Devolver al ListView la fila creada
        return v;

    }

}
