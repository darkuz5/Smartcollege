package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReporteDetalle extends AppCompatActivity {
    public static Activity activity;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    private ProgressDialog dialog;
    private LayoutInflater inflater;
    LinearLayout otros;
    String jsonRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_reporte_detalle);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //actionbar
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();

        TextView alumno = (TextView) findViewById(R.id.txtAlumno);
        alumno.setText(MainActivity.nombreAlumno);
        setDatos();
    }



    private void CustomActionBar() {
        // TODO Auto-generated method stub
        final LayoutInflater inflater = (LayoutInflater) mActionBar.getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.activity_main_actionbar, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        imagenPrincipal = (ImageView) customActionBarView.findViewById(R.id.imagenPrincipal);

        textoPrincipal = (TextView) customActionBarView.findViewById(R.id.textoPrincipal);
        textoSecundario = (TextView) customActionBarView.findViewById(R.id.textoSecundario);
        iconoDerecho = (ImageView) customActionBarView.findViewById(R.id.iconoDerecho);
        iconoIzquierdo = (ImageView) customActionBarView.findViewById(R.id.iconoIzquierdo);

        RelativeLayout contenedor = (RelativeLayout) customActionBarView.findViewById(R.id.contenedor);
        contenedor.setBackgroundColor(Color.parseColor(color));
        if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("REPORTE");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();

            }
        });

        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent =(Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }


    public void setDatos()  {
        if(Reporte.json.has("reporte")){
            try {
                JSONArray array = Reporte.json.getJSONArray("reporte");
                for (int i = 0; i < array.length(); i++) {
                    final JSONObject c = array.getJSONObject(i);
                    ((TextView) findViewById(R.id.txtFecha)).setText(calcularFechaFull(c.getString("fecha")));
                    ((TextView) findViewById(R.id.txtAsunto)).setText(c.getString("asunto"));
                    ((TextView) findViewById(R.id.txtTexto)).setText(c.getString("texto"));
                    ImageView fotogde = (ImageView) findViewById(R.id.imgFlag);
                    switch (c.getString("color")){
                        case "1":
                            Picasso.with(activity).load(R.drawable.flagamarilla).into(fotogde);
                            break;
                        case "2":
                            Picasso.with(activity).load(R.drawable.flagnaranja).into(fotogde);
                            break;
                        case "3":
                            Picasso.with(activity).load(R.drawable.flagroja).into(fotogde);
                            break;
                        default:
                            Picasso.with(activity).load(R.drawable.flagamarilla).into(fotogde);
                            break;
                    }
                }
            }catch(JSONException e){
                    Log.e("error", e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        //Login.clearVariables();
        finish();
        overridePendingTransition(R.anim.anim_null, R.anim.slide_right);
    }


    private static String calcularFecha(String fecha) {
        String fec = "";

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

        try {

            Date fecDateHoraParse = parseFormat.parse(fecha);

            Calendar today = Calendar.getInstance();
            Date fecActual = dateFormat.parse(dateFormat.format(today.getTime()));
            Date fecDateParse = dateFormat.parse(fecha);

            Calendar ayer = Calendar.getInstance();
            ayer.add(Calendar.DATE, -1);
            ayer.set(Calendar.HOUR_OF_DAY, 0);
            ayer.set(Calendar.MINUTE, 0);
            ayer.set(Calendar.SECOND, 0);

            Date ayerInicio = parseFormat.parse(parseFormat.format(ayer.getTime()));

            ayer = Calendar.getInstance();
            ayer.add(Calendar.DATE, -1);
            ayer.set(Calendar.HOUR_OF_DAY, 23);
            ayer.set(Calendar.MINUTE, 59);
            ayer.set(Calendar.SECOND, 59);

            Date ayerFinal = parseFormat.parse(parseFormat.format(ayer.getTime()));

            if(fecDateParse.equals(fecActual)) {
                fec = horaFormat.format(fecDateHoraParse);
            } else if ((fecDateHoraParse.after(ayerInicio) || fecDateHoraParse.equals(ayerInicio)) && (fecDateHoraParse.before(ayerFinal) || fecDateHoraParse.equals(ayerFinal))){
                fec = "AYER";
            } else {
                fec = fechaFormat.format(fecDateHoraParse);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }

    private static String calcularFechaFull(String fecha) {
        String fec = "";

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd ' de ' MMMM ' de ' yyyy");
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date fecDateHoraParse = parseFormat.parse(fecha);

            Calendar today = Calendar.getInstance();
            Date fecActual = dateFormat.parse(dateFormat.format(today.getTime()));
            Date fecDateParse = dateFormat.parse(fecha);

            Calendar ayer = Calendar.getInstance();
            ayer.add(Calendar.DATE, -1);
            ayer.set(Calendar.HOUR_OF_DAY, 0);
            ayer.set(Calendar.MINUTE, 0);
            ayer.set(Calendar.SECOND, 0);

            Date ayerInicio = parseFormat.parse(parseFormat.format(ayer.getTime()));

            ayer = Calendar.getInstance();
            ayer.add(Calendar.DATE, -1);
            ayer.set(Calendar.HOUR_OF_DAY, 23);
            ayer.set(Calendar.MINUTE, 59);
            ayer.set(Calendar.SECOND, 59);

            Date ayerFinal = parseFormat.parse(parseFormat.format(ayer.getTime()));

            if(fecDateParse.equals(fecActual)) {
                fec = "HOY";
            } else if ((fecDateHoraParse.after(ayerInicio) || fecDateHoraParse.equals(ayerInicio)) && (fecDateHoraParse.before(ayerFinal) || fecDateHoraParse.equals(ayerFinal))){
                fec = "AYER";
            } else {
                fec = fechaFormat.format(fecDateHoraParse);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }

    public static String diferenciaDias(String fecha) {
        String fec = "";
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date fecDateHoraParse = parseFormat.parse(fecha);
            Calendar today = Calendar.getInstance();
            Date fecActual = parseFormat.parse(parseFormat.format(today.getTime()));
            Date fecDateParse = parseFormat.parse(fecha);

            Date fpast = fecDateHoraParse; // June 20th, 2010
            Date ftoday = fecActual; // July 24th
            int days = Days.daysBetween(new DateTime(fpast), new DateTime(ftoday)).getDays(); // => 34
            int horas = Hours.hoursBetween(new DateTime(fpast), new DateTime(ftoday)).getHours();

            if (horas < 24){
                fec = "Hace "+horas+" horas";
            } else {
                fec = "Hace "+days+" días";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fec;
    }


}
