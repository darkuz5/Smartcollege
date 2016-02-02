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
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Premio extends AppCompatActivity {
    public static Activity activity;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static JSONObject json;
    public static JSONObject manJson;

    private ProgressDialog dialog;
    private LayoutInflater inflater;
    LinearLayout otros;
    String jsonRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_premio);

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

        //inicializando
        json = new JSONObject();
        manJson = new JSONObject();

        if(!Alumno.cargandoPremio){
            new DescargaReporte().execute();
        }else{
            jsonRead = Alumno.json_premios;
            llenado();
        }

    }


    class DescargaReporte extends AsyncTask<Void, Void, Void> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            url = getString(R.string.getTrofeos);
            arrayInicio = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(!isCancelled()) {
                try {
                    getReporte();

                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void getReporte() {

            try {

                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_alumno", MainActivity.alumno.trim()));
                jSONFunciones json = new jSONFunciones();
                jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
                Log.e("json", jsonRead);
                JSONObject jsonObject = new JSONObject(jsonRead);
                if (jsonObject.has("boletas")){

                    Alumno.json_premios = jsonRead;
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
                Alumno.cargandoPremio = false;
            }else if(jsonAux.has("boletas")){
                Alumno.cargandoPremio = true;
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
                    View aviso2 = inflater.inflate(R.layout.adapter_item_reporte,null);
                    ((TextView) aviso2.findViewById(R.id.txtFecha)).setText(c.getString("asunto"));
                    ((TextView) aviso2.findViewById(R.id.txtFecha)).setTextColor(getResources().getColor(R.color.color_negro));
                    ((TextView) aviso2.findViewById(R.id.txtFecha)).setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    ((TextView) aviso2.findViewById(R.id.txtResumen)).setText(c.getString("texto"));
                    aviso2.findViewById(R.id.txtTitulo).setVisibility(View.GONE);
                    aviso2.findViewById(R.id.txtHace).setVisibility(View.GONE);
                    ImageView fotogde = (ImageView) aviso2.findViewById(R.id.imgAlerta);
                    Picasso.with(activity).load(R.drawable.copaverde).into(fotogde);

                    aviso2.findViewById(R.id.btn_aviso).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                manJson.put("asunto",c.getString("asunto"));
                                manJson.put("texto",c.getString("texto"));
                                manJson.put("fecha",c.getString("fecha"));

                                JSONArray aux = new JSONArray();
                                aux.put(manJson);
                                json.put("premio",aux);
                                Log.e("json", json.toString());
                                Intent it = new Intent(getApplicationContext(), PremioDetalle.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.slide_left, R.anim.anim_null);
                            }catch (JSONException e){
                                Log.e("error",e.getMessage());
                            }

                        }
                    });
                    otros.addView(aviso2);
                }
            }
            if(jsonObject.has("error")){
                Alumno.cargandoPremio = false;
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
        textoPrincipal.setText("LOGROS");

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
