package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Eventos_Lista_Calendar extends AppCompatActivity {
    private Activity activity;
    private LayoutInflater inflater;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;
    String fecha;
    String eventos;
    LinearLayout padre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_lista_calendar);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();
        padre = (LinearLayout) findViewById(R.id.padre);

        Intent it = getIntent();
        if (it.hasExtra("fecha")) {
            fecha = it.getStringExtra("fecha");
            ((TextView) findViewById(R.id.fecha)).setText(calcularFechaFull(fecha));

        } else {
            fecha = "";
        }
        eventos = MainActivity.eventos;
        if (!TextUtils.isEmpty(eventos)){
            /** Descargar Eventos **/
            llenado(eventos);
        }




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
        /*if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }*/
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("EVENTOS");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

            }
        });

        iconoDerecho.setVisibility(View.GONE);
        iconoDerecho.setImageResource(R.drawable.comunidad);


        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    public void llenado(String datos){
        Log.i("Eventos", datos);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(datos);


            if (jsonObject.has("eventos")) {
                padre.removeAllViews();
                MainActivity.eventos = datos;
                JSONArray array = jsonObject.getJSONArray("eventos");
                for (int i = 0; i < array.length(); i++) {
                    final JSONObject c = array.getJSONObject(i);
                    if (c.getString("fecha").equals(fecha)) {
                        View evento = inflater.inflate(R.layout.adapter_item_eventos, null);
                        ((TextView) evento.findViewById(R.id.txtTitulo)).setText(c.getString("nombre"));
                        ((TextView) evento.findViewById(R.id.txtTexto)).setText(c.getString("descripcion"));

                        String[] fecha = c.getString("fecha").split("-");

                        ((TextView) evento.findViewById(R.id.txtDia)).setText(fecha[2]);
                        ((TextView) evento.findViewById(R.id.txtMes)).setText(mes(fecha[1]));
                        final String data = c.toString();
                        evento.findViewById(R.id.btn_aviso).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(activity, Eventos_Detalle.class);
                                it.putExtra("datos", data);
                                startActivity(it);
                                overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                            }
                        });
                        padre.addView(evento);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

            if (fecDateParse.equals(fecActual)) {
                fec = "HOY";
            } else if ((fecDateHoraParse.after(ayerInicio) || fecDateHoraParse.equals(ayerInicio)) && (fecDateHoraParse.before(ayerFinal) || fecDateHoraParse.equals(ayerFinal))) {
                fec = "AYER";
            } else {
                fec = fechaFormat.format(fecDateHoraParse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }

    public String mes(String mes){
        String lMes = "";
        switch (mes){
            case "01":
                lMes = "Ene";
                break;
            case "02":
                lMes = "Feb";
                break;
            case "03":
                lMes = "Mar";
                break;
            case "04":
                lMes = "Abr";
                break;
            case "05":
                lMes = "May";
                break;
            case "06":
                lMes = "Jun";
                break;
            case "07":
                lMes = "Jul";
                break;
            case "08":
                lMes = "Ago";
                break;
            case "09":
                lMes = "Sep";
                break;
            case "10":
                lMes = "Oct";
                break;
            case "11":
                lMes = "Nov";
                break;
            case "12":
                lMes = "Dic";
                break;

        }

        return lMes;
    }

}
