package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Eventos_Detalle extends AppCompatActivity {
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;
    Date date = null;
    String titulox = "";
    String descripcionx = "";
    String idEvento;
    String allEventos;
    private Activity activity;
    private LayoutInflater inflater;
    private GoogleMap map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(this);
        setContentView(R.layout.activity_eventos_detalle);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();

        allEventos = MainActivity.eventos;


        Intent it = getIntent();
        if (it.hasExtra("datos")) {

            try {
                JSONObject c = new JSONObject(it.getStringExtra("datos"));
                ((TextView) findViewById(R.id.titulo)).setText(c.getString("nombre"));
                ((TextView) findViewById(R.id.fecha)).setText(calcularFechaFull(c.getString("fecha")));
                ((TextView) findViewById(R.id.texto)).setText(c.getString("descripcion"));

                idEvento = c.getString("id");

                titulox = c.getString("nombre");
                descripcionx = c.getString("descripcion");

                String hora = c.getString("hora");
                if (hora.length() < 6) {
                    hora = c.getString("hora") + ":00";
                }
                String input = c.getString("fecha") + " " + hora;

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(input);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final long milliseconds = date.getTime();
                //long millisecondsFromNow = milliseconds - (new Date()).getTime();


                /** Fotos  **/

                LinearLayout contenido_fotos = (LinearLayout) findViewById(R.id.contenido_fotos);
                LinearLayout gal1 = (LinearLayout) findViewById(R.id.gal1);
                LinearLayout gal2 = (LinearLayout) findViewById(R.id.gal2);

                gal1.setVisibility(View.GONE);
                gal2.setVisibility(View.GONE);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(allEventos);


                    if (jsonObject.has("fotos")) {
                        contenido_fotos.removeAllViews();
                        JSONArray array = jsonObject.getJSONArray("fotos");
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject f = array.getJSONObject(i);

                            if (f.getString("id_evento").equals(idEvento)) {
                                View evento = inflater.inflate(R.layout.adapter_fotoeventos, null);
                                ImageView foto = (ImageView) evento.findViewById(R.id.lafoto);
                                gal1.setVisibility(View.VISIBLE);
                                gal2.setVisibility(View.VISIBLE);
                                if (f.getString("foto").contains("fotos")) {
                                    try {

                                        Picasso.with(activity).load(f.getString("foto")).into(foto);
                                        final String data = f.getString("foto");
                                        evento.findViewById(R.id.lafoto).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.e("error", "clic" + data);
                                                Intent it = new Intent(getApplicationContext(), Eventos_VerFoto.class);
                                                it.putExtra("foto", data);
                                                startActivity(it);
                                                overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                                            }
                                        });
                                        contenido_fotos.addView(evento);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String coordenadas = c.getString("coordenadas");
                if (TextUtils.isEmpty(coordenadas)) {
                    findViewById(R.id.ubica).setVisibility(View.GONE);
                } else {
                    String[] coordena = coordenadas.split(", ");
                    final LatLng KIEL = new LatLng(Double.parseDouble(coordena[0] + "0"), Double.parseDouble(coordena[1] + "0"));
                    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                            .getMap();
                    Marker kiel = map.addMarker(new MarkerOptions()
                            .position(KIEL)
                            .title(c.getString("nombre"))
                            .snippet(c.getString("descripcion")));

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(KIEL, 15));
                    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

        }

    }

    private void CustomActionBar() {
        // TODO Auto-generated method stub
        final LayoutInflater inflater = (LayoutInflater) mActionBar.getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.activity_main_actionbaragenda, null);

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

        customActionBarView.findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarEvento();
            }
        });

        iconoDerecho.setVisibility(View.VISIBLE);
        iconoDerecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarEvento();
            }
        });


        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    public void agregarEvento() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", date.getTime());
        intent.putExtra("allDay", true);
        intent.putExtra("endTime", date.getTime() + 60 * 60);
        intent.putExtra("title", titulox);
        intent.putExtra("description", descripcionx);
        startActivity(intent);
    }

}
