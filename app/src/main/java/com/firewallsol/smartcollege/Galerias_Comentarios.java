package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Galerias_Comentarios extends AppCompatActivity {

    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;
    String id, titulo, url;
    LinearLayout padre;
    private Activity activity;
    private LayoutInflater inflater;
    private ProgressDialog dialog;

    private static String calcularFechaFull(String fecha) {
        String fec = "";


        if (fecha.contains(" ")) {
            fecha = fecha.substring(0, 10);
            Log.i("Fecha", fecha);
        }

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd ' de ' MMMM");
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
        setContentView(R.layout.activity_galeria__comentarios);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();

        Intent it = getIntent();
        if (it.hasExtra("id")) {


            id = it.getStringExtra("id");
            titulo = it.getStringExtra("titulo");
            url = it.getStringExtra("url");

            padre = (LinearLayout) findViewById(R.id.padre);

            //new DescargaGalerias().execute();


        } else {
            finish();
        }


    }

    public void onResume() {
        super.onResume();
        new DescargaGalerias().execute();
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
        textoPrincipal.setText("COMENTARIOS");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

            }
        });

        iconoDerecho.setVisibility(View.VISIBLE);
        iconoDerecho.setImageResource(R.drawable.lapizcomenta);
        iconoDerecho.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoDerecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Ir", "a nuevo comentario");
                Intent itn = new Intent(activity, Galerias_Comentar.class);
                itn.putExtra("id", id);
                itn.putExtra("titulo", titulo);
                itn.putExtra("url", url);
                startActivity(itn);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });

        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    public void llenado(String datos) {
        Log.i("datos", datos);
        padre.removeAllViews();
        try {
            JSONObject jsonObject = new JSONObject(datos);
            if (jsonObject.has("comentarios")) {
                JSONArray array = jsonObject.getJSONArray("comentarios");

                for (int i = 0; i < array.length(); i++) {
                    final JSONObject c = array.getJSONObject(i);
                    View item = inflater.inflate(R.layout.adapter_item_comentariogaleria, null);

                    CircleImageView foto = (CircleImageView) item.findViewById(R.id.fotoperfil);
                    String urlFoto = c.getString("foto_tutor");
                    Log.i("utl", foto + "");
                    try {
                        Picasso.with(activity).load(urlFoto).into(foto);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    ((TextView) item.findViewById(R.id.txtNombre)).setText(c.getString("tutor"));
                    ((TextView) item.findViewById(R.id.txtResumen)).setText(c.getString("comentario"));
                    ((TextView) item.findViewById(R.id.txtFecha)).setText(calcularFechaFull(c.getString("fecha")));


                    padre.addView(item);

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class DescargaGalerias extends AsyncTask<Void, Void, String> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.getComentariosFoto);
            arrayInicio = new ArrayList<>();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonRead = "";
            try {
                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_foto", id));
                jSONFunciones json = new jSONFunciones();
                jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonRead;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            llenado(aVoid);
        }
    }


}
