package com.dg_livesports.dg_livesports;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DARLIN on 07/11/2016.
 */
public class Tabla {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla

    /**
     * Constructor de la tabla
     * @param actividad Actividad donde va a estar la tabla
     * @param tabla TableLayout donde se pintará la tabla
     */
    public Tabla(Activity actividad, TableLayout tabla)
    {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
    }

    /**
     * Añade la cabecera a la tabla
     * @param recursocabecera Recurso (array) donde se encuentra la cabecera de la tabla
     */
    public void agregarCabecera(int recursocabecera)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);

        String[] arraycabecera = rs.getStringArray(recursocabecera);
        COLUMNAS = arraycabecera.length;

        for(int i = 0; i < arraycabecera.length; i++)
        {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arraycabecera[i]), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextAppearance(actividad, R.style.estilo_celda);
            texto.setBackgroundResource(R.drawable.tabla_celda_cabecera);
            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }
    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */
    public void agregarFilaTabla(ArrayList<String> elementos)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {

            String UrlImagen = elementos.get(i).toString();

            if (UrlImagen.length()>40){
                ImageView imagen_shield = new ImageView(actividad);
                //imagen_shield.setBackgroundResource(R.drawable.tabla_celda2);
                imagen_shield.setBackground(actividad.getResources().getDrawable(R.color.white));
                new LoadImage(imagen_shield).execute(UrlImagen);
                //imagen_shield.setBackgroundResource(R.drawable.tabla_celda);
                layoutCelda = new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT);
                imagen_shield.setLayoutParams(layoutCelda);

                fila.addView(imagen_shield);

            } else {
                if(UrlImagen.equals("u")){
                    ImageView flecha_up = new ImageView(actividad);
                    //flecha_up.setImageResource(R.drawable.arrowup_flech);
                    flecha_up.setBackground(actividad.getResources().getDrawable(R.color.white));
                    //flecha_up.setBackgroundResource(R.drawable.tabla_celda2);
                    Bitmap bmp = BitmapFactory.decodeResource(actividad.getResources(), R.drawable.arrowup_flech);
                    flecha_up.setImageBitmap(bmp);
                    layoutCelda = new TableRow.LayoutParams(40, 40);
                    flecha_up.setLayoutParams(layoutCelda);

                    fila.addView(flecha_up);

                }else if (UrlImagen.equals("d")){
                    ImageView flecha_down = new ImageView(actividad);
                    //flecha_down.setImageResource(R.drawable.arrowdown_flech);
                    flecha_down.setBackground(actividad.getResources().getDrawable(R.color.white));
                    //flecha_down.setBackgroundResource(R.drawable.tabla_celda2);
                    Bitmap bmp = BitmapFactory.decodeResource(actividad.getResources(), R.drawable.arrowdown_flech);
                    flecha_down.setImageBitmap(bmp);
                    layoutCelda = new TableRow.LayoutParams(40, 40);
                    flecha_down.setLayoutParams(layoutCelda);

                    fila.addView(flecha_down);

                }else {
                    TextView texto = new TextView(actividad);
                    texto.setText(String.valueOf(elementos.get(i)));
                    texto.setGravity(Gravity.CENTER_HORIZONTAL);
                    texto.setTextAppearance(actividad, R.style.estilo_celda);
                    texto.setBackgroundResource(R.drawable.tabla_celda);
                    layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
                    texto.setLayoutParams(layoutCelda);

                    fila.addView(texto);
                }

            }

        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }
    /**
     * Obtiene el ancho en píxeles de un texto en un String
     * @param texto Texto
     * @return Ancho en píxeles del texto
     */
    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        if (bounds.width()<50)return 50;
        return bounds.width();
    }

}
