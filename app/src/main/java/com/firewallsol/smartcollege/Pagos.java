package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Documento.RVAdapterDocumentos;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;

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

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Pagos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<ItemsInicio> arrayInicio;
    // TODO: Rename and change types of parameters


    int pagina = 0;
    RVAdapterDocumentos adapter;
    Boolean primera = false;
    Boolean gotomove = false;
    List<NameValuePair> params;
    List<NameValuePair> paramsSend;
    // TODO: Rename and change types of parameters
    private AutoScrollViewPager pager;
    private Activity activity;
    private View root;
    //private List<Gaceta> gacetas;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;

    public static  String idPago = "0";
    LayoutInflater minflater;
    LinearLayout padre;


    public Pagos() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Pagos newInstance() {
        Pagos fragment = new Pagos();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        minflater = inflater;
        root = inflater.inflate(R.layout.fragment_pagos, container, false);
        activity = getActivity();

        padre = (LinearLayout) root.findViewById(R.id.padre);

        dialog = new ProgressDialog(activity);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);

        new DescargaPagoos().execute();


        root.findViewById(R.id.btnInfoPago).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, Pagos_Info.class);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            }
        });

        root.findViewById(R.id.btnEnviarComprobante).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity, Pagos_Enviar.class);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            }
        });





        return root;
    }



    class DescargaPagoos extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            url = activity.getString(R.string.getPagosAlumno);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {
            String jsonRead = "";
            try {

//                Log.i("Pagina", MainActivity.banderaPage + "|Escuela:"+MainActivity.idEscuela+"|"+params.toString());
                jSONFunciones json = new jSONFunciones();
                List<NameValuePair> paramsSendx = new ArrayList<>();
                paramsSendx.add(new BasicNameValuePair("id_alumno", MainActivity.alumno.trim()));
                jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSendx);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonRead;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                llenado(aVoid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }

    public void llenado(String result) throws JSONException {
        Log.i("Resulta", result);
        padre.removeAllViews();
        if (result.length() > 10) {

            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("pagos")) {
                JSONArray gaceta = jsonObj.getJSONArray("pagos");
                for (int i = 0; i < gaceta.length(); i++) {
                    JSONObject c = gaceta.getJSONObject(i);
                    View pagos = minflater.inflate(R.layout.adapter_item_pagos, null);
                    final String datos = c.toString();
                    if(idPago.equals("0")){  idPago = c.getString("id");   }
                    ((TextView) pagos.findViewById(R.id.txtNombre)).setText(calcularFechaFull((c.getString("fecha_subida"))));
                    ((TextView) pagos.findViewById(R.id.txtFecha)).setText("Mensualidad de "+calcularMesPaho((c.getString("fecha_pago"))));
                    ((TextView) pagos.findViewById(R.id.txtTitulo)).setText(c.getString("titulo"));
                    ((TextView) pagos.findViewById(R.id.txtResumen)).setText(c.getString("descripcion"));

                    padre.addView(pagos);
                }
            } else {

            }
        }
    }
    private static String calcularFechaFull(String fecha) {
        String fec = "";

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


                fec = fechaFormat.format(fecDateHoraParse);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }
    private static String calcularMesPaho(String fecha) {
        String fec = "";

        SimpleDateFormat fechaFormat = new SimpleDateFormat("MMMM");
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


                fec = fechaFormat.format(fecDateHoraParse);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }




}
