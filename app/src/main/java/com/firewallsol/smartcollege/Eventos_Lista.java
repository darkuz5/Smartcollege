package com.firewallsol.smartcollege;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;


public class Eventos_Lista extends Fragment {

    private Activity activity;
    private View root;
    public static CompactCalendarView compactCalendarView;
    public static int azul;









    public Eventos_Lista() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Eventos_Lista newInstance() {
        Eventos_Lista fragment = new Eventos_Lista();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_eventoslista, container, false);
        activity = getActivity();




        return root;
    }




}
