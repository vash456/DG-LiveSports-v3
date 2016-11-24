package com.dg_livesports.dg_livesports;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTabsFragment extends Fragment {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    String filtro_mostrar;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;

    public MainTabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_m1_resultados, container, false);

        // modificar menu overflow
        //setHasOptionsMenu(true);

        prefs = getActivity().getSharedPreferences("preferencia", Context.MODE_PRIVATE);
        editor = prefs.edit();

        View x =  inflater.inflate(R.layout.fragment_main_tabs,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        Toolbar toolbar = (Toolbar) x.findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("Mostrar: ");
        toolbar.setTitleTextAppearance(getActivity(), R.style.estilo_toolbarFiltro);
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.icon_field_football));

        final TextView t_filtro = (TextView) x.findViewById(R.id.t_filtro);
        t_filtro.setTextAppearance(getActivity(), R.style.estilo_toolbarFiltro);

        //filtro en toolbar
        final Spinner cmbToolbar = (Spinner) x.findViewById(R.id.CmbToolbar);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.appbar_filter_title,
                new String[]{""," Todos ", " Favoritos "});

        adapter.setDropDownViewResource(R.layout.appbar_filter_list);

        cmbToolbar.setAdapter(adapter);

        filtro_mostrar = String.valueOf(prefs.getString("var_filtro","Todos"));

        //if (filtro_mostrar.equals("Favoritos")) cmbToolbar.setSelection(1);
        t_filtro.setText(filtro_mostrar);

        cmbToolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //... Acciones al seleccionar una opción de la lista
                Log.i("Toolbar 3", "Seleccionada opción " + i);
                //Toast.makeText(getContext(), "Seleccionada opción " + i, Toast.LENGTH_SHORT).show();

                if (i == 1){
                    filtro_mostrar = "Todos";
                    editor.putString("var_filtro",filtro_mostrar);
                    editor.commit();
                    viewPager.getAdapter().notifyDataSetChanged();
                    int curr_item = viewPager.getCurrentItem();
                    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
                    viewPager.setCurrentItem(curr_item);
                    t_filtro.setText(filtro_mostrar);
                    /*String sesion;
                    sesion = String.valueOf(prefs.getString("var_sesion","abierto"));
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("var_sesion",sesion);
                    startActivity(intent);*/

                }else if(i == 2){
                    filtro_mostrar = "Favoritos";
                    editor.putString("var_filtro",filtro_mostrar);
                    editor.commit();
                    viewPager.getAdapter().notifyDataSetChanged();
                    int curr_item = viewPager.getCurrentItem();
                    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
                    viewPager.setCurrentItem(curr_item);
                    t_filtro.setText(filtro_mostrar);
                    /*String sesion;
                    sesion = String.valueOf(prefs.getString("var_sesion","abierto"));
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("var_sesion",sesion);
                    startActivity(intent);*/
                }

                cmbToolbar.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //... Acciones al no existir ningún elemento seleccionado

            }
        });

        return x;

    }


    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filtro, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_todos) {
            return true;
        }

        if (id == R.id.action_favoritos) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {

            switch (position){
                case 0 : return new AyerFragment();
                case 1 : return new HoyFragment();
                case 2 : return new MananaFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "AYER";
                case 1 :
                    return "HOY";
                case 2 :
                    return "MAÑANA";
            }
            return null;
        }
    }
}
