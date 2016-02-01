package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Database.Database;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
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


public class Galerias extends Fragment {

    public static CompactCalendarView compactCalendarView;
    public static int azul;
    public static Database db_sqlite;
    public static InputMethodManager inputManager;
    public static LayoutInflater inflate;
    String contenido_galerias;
    LinearLayout padre;
    private Activity activity;
    private View root;


    public Galerias() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Galerias newInstance() {
        Galerias fragment = new Galerias();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        root = inflater.inflate(R.layout.fragment_galerias, container, false);
        activity = getActivity();

        inflate = inflater;

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = getActivity();

        padre = (LinearLayout) root.findViewById(R.id.padre);

        contenido_galerias = MainActivity.contenido_galerias;

        if (contenido_galerias.length() < 10) {
            new DescargaGalerias().execute();
        } else {
            llenado(contenido_galerias);
        }


        return root;
    }

    public void llenado(String datos) {
        padre.removeAllViews();
        try {
            JSONObject jsonObject = new JSONObject(datos);
            if (jsonObject.has("galerias")) {
                JSONArray array = jsonObject.getJSONArray("galerias");

                for (int i = 1; i < array.length(); i++) {
                    final JSONObject c = array.getJSONObject(i);
                    View item = inflate.inflate(R.layout.adapter_item_galerias, null);
                    Picasso.with(activity).load(MainActivity.urlImgPrincipal).into((ImageView) item.findViewById(R.id.fotoGal));
                    final String data = c.toString();
                    ((TextView) item.findViewById(R.id.txtTitulo)).setText(c.getString("titulo"));
                    ((TextView) item.findViewById(R.id.txtFecha)).setText(calcularFechaFull(c.getString("fecha")));
                    final String id_galeria = c.getString("id");
                    item.findViewById(R.id.btn_gal).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(getContext(), Galerias_Fotos.class);
                            it.putExtra("id", id_galeria);
                            startActivity(it);
                            activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                        }
                    });

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
            url = activity.getString(R.string.getGaleria);
            arrayInicio = new ArrayList<>();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonRead = "";
            try {
                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
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
