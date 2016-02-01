package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.firewallsol.smartcollege.Database.Database;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;


public class Alumno extends Fragment {

    public static CompactCalendarView compactCalendarView;
    public static int azul;
    public static Database db_sqlite;
    public static InputMethodManager inputManager;
    private Activity activity;
    private View root;


    public Alumno() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Alumno newInstance() {
        Alumno fragment = new Alumno();
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
        root = inflater.inflate(R.layout.fragment_alumno, container, false);
        activity = getActivity();


        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = getActivity();


        TextView nombreAlumno = (TextView) root.findViewById(R.id.alumno);
        nombreAlumno.setText(MainActivity.nombreAlumno);


        return root;
    }


}
