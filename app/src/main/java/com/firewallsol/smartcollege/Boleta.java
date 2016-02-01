package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Boleta extends AppCompatActivity {
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

        setContentView(R.layout.fragment_boleta);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        TextView alumno = (TextView) findViewById(R.id.textView2);
        alumno.setText(MainActivity.nombreAlumno);


        otros = (LinearLayout) findViewById(R.id.otros);

        //actionbar
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();

        //Cargando
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);


        if(!Alumno.cargadoCalif){
            new DescargaBoletas().execute();
        }else{
            jsonRead = Alumno.json_calificaciones;
            llenado();
        }

    }


    class DescargaBoletas extends AsyncTask<Void, Void, Void> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            url = getString(R.string.getBoletasAlumno);
            arrayInicio = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(!isCancelled()) {
                try {
                    getBoletas();

                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void getBoletas() {

            try {

                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_alumno", MainActivity.alumno.trim()));
                jSONFunciones json = new jSONFunciones();
                jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
                Log.e("json", jsonRead);
                JSONObject jsonObject = new JSONObject(jsonRead);
                if (jsonObject.has("boletas")){

                    Alumno.json_calificaciones = jsonRead;
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            llenado();
            dialog.dismiss();
            JSONObject jsonAux =null;
            try{
                jsonAux = new JSONObject(jsonRead);
            }catch (JSONException e){
                e.getMessage();
            }

            if(jsonAux.has("error")){
                Alumno.cargadoCalif = false;
            }else if(jsonAux.has("boletas")){
                Alumno.cargadoCalif = true;
            }


        }
    }

    public void llenado(){
        //Log.e("json", jsonRead);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonRead);
            if (jsonObject.has("boletas")) {
                JSONArray array = jsonObject.getJSONArray("boletas");
                for (int i=0; i<array.length(); i++){
                    final JSONObject c = array.getJSONObject(i);
                    View aviso2 = inflater.inflate(R.layout.adapter_item_boleta,null);
                    ((TextView) aviso2.findViewById(R.id.txtTitulo)).setText(c.getString("titulo"));
                    aviso2.findViewById(R.id.btn_url).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(c.getString("url")));
                                startActivity(browserIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    otros.addView(aviso2);
                }
            }
            if(jsonObject.has("error")){
                Alumno.cargadoCalif = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
        if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("CALIFICACIONES");

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

    @Override
    public void onBackPressed() {
        //Login.clearVariables();
        finish();
        overridePendingTransition(R.anim.anim_null, R.anim.slide_right);
    }


}
