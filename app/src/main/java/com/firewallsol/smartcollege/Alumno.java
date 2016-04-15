package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.Database.Database;
import com.firewallsol.smartcollege.ExamenModel.ExamenModel;
import com.firewallsol.smartcollege.ExamenModel.TemarioModel;
import com.firewallsol.smartcollege.TareaModel.TareaModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.util.List;


public class Alumno extends Fragment {

    private Activity activity;
    private View root;
    public static CompactCalendarView compactCalendarView;
    public static int azul;
    public static Database db_sqlite;
    public static InputMethodManager inputManager;

    //variables para tareas
    public static int banderaPage = 1;
    public static Boolean endLove = false;
    public static List<TareaModel> tareas = null;
    public static List<ExamenModel> examenes = null;
    public static List<TemarioModel> temario = null;

    //contenido
    public static String json_calificaciones;
    public static String json_reportes;
    public static String json_premios;
    public static String json_tareas;
    public static String json_examenes;
    static Boolean cargadoCalif = false;
    static Boolean cargandoRepor = false;
    static Boolean cargandoPremio = false;
    static Boolean cargado = false;


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
        root =  inflater.inflate(R.layout.fragment_alumno, container, false);
        activity = getActivity();
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = getActivity();
        TextView nombreAlumno = (TextView) root.findViewById(R.id.alumno);
        nombreAlumno.setText(MainActivity.nombreAlumno);

        MaterialRippleLayout btncalif = (MaterialRippleLayout) root.findViewById(R.id.btn_calif);
        btncalif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity.getApplicationContext(), Boleta.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, R.anim.anim_null);

            }
        });
        MaterialRippleLayout btnReporte = (MaterialRippleLayout) root.findViewById(R.id.btn_reportes);
        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity.getApplicationContext(), Reporte.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, R.anim.anim_null);
            }
        });
        MaterialRippleLayout btnListaTE = (MaterialRippleLayout) root.findViewById(R.id.btn_tareas);
        btnListaTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(activity.getApplicationContext(), ListaTareasExamenes.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, R.anim.anim_null);

            }
        });
        MaterialRippleLayout btnPremio = (MaterialRippleLayout) root.findViewById(R.id.btn_premios);
        btnPremio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(activity.getApplicationContext(), Premios_Lista.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, R.anim.anim_null);

            }
        });

        return root;
    }







}
