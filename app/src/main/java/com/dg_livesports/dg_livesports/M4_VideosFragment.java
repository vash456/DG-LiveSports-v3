package com.dg_livesports.dg_livesports;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class M4_VideosFragment extends Fragment {


    public M4_VideosFragment() {
        // Required empty public constructor
    }

    private String FIREBASE_URL="https://final-dygsports.firebaseio.com", vid, vid2;
    private Firebase firebasedatos;

    private Button b_guardar, b_quitar, b_guardar2, b_quitar2;

    private Firebase firebd;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.fragment_m4__videos,null);
        Firebase.setAndroidContext(getContext());
        firebasedatos = new Firebase(FIREBASE_URL);

        b_guardar = (Button) x.findViewById(R.id.bGuardar);
        b_quitar = (Button) x.findViewById(R.id.bQuitar);
        b_guardar2 = (Button) x.findViewById(R.id.bGuardar2);
        b_quitar2 = (Button) x.findViewById(R.id.bQuitar2);

        b_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vid = toString().valueOf(R.id.video1);
                Videos_data video = new Videos_data(vid);
                firebd = firebasedatos.child("video_"+vid);
                firebd.setValue(video);
            }
        });

        b_quitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vid = toString().valueOf(R.id.video1);
                firebd = firebasedatos.child("video_"+vid);
                firebd.removeValue();
            }
        });
/////////////////////////////////////////////////////
        b_guardar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vid2 = toString().valueOf(R.id.video2);
                Videos_data video2 = new Videos_data(vid2);
                firebd = firebasedatos.child("video_"+vid2);
                firebd.setValue(video2);
            }
        });

        b_quitar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vid2 = toString().valueOf(R.id.video2);
                firebd = firebasedatos.child("video_"+vid2);
                firebd.removeValue();
            }
        });

        return x;

    }

}
