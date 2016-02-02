package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Eventos_Lista extends Fragment {

    public static CompactCalendarView compactCalendarView;
    public static int azul;
    private Activity activity;
    private View root;
    LayoutInflater minflater;

    String eventos;
    LinearLayout padre;


    public Eventos_Lista() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Eventos_Lista newInstance() {
        Eventos_Lista fragment = new Eventos_Lista();
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
        root = inflater.inflate(R.layout.fragment_eventoslista, container, false);


        padre = (LinearLayout) root.findViewById(R.id.padre);


        activity = getActivity();
        eventos = MainActivity.eventos;
        minflater = inflater;

        if (TextUtils.isEmpty(eventos)){
            /** Descargar Eventos **/
            new DescargarEventos().execute();
        } else {
            llenado(eventos);
        }



        return root;
    }



    class DescargarEventos extends AsyncTask<Void, Void, String> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.getEventos);
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

                    View evento = minflater.inflate(R.layout.adapter_item_eventos, null);
                    ((TextView) evento.findViewById(R.id.txtTitulo)).setText(c.getString("nombre"));
                    ((TextView) evento.findViewById(R.id.txtTexto)).setText(c.getString("descripcion"));

                    String[] fecha = c.getString("fecha").split("-");

                    ((TextView) evento.findViewById(R.id.txtDia)).setText(fecha[2]);
                    ((TextView) evento.findViewById(R.id.txtMes)).setText(mes(fecha[1]));
                    final String data  = c.toString();
                    evento.findViewById(R.id.btn_aviso).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(getContext(), Eventos_Detalle.class);
                            it.putExtra("datos", data);
                            startActivity(it);
                            getActivity().overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                        }
                    });
                    padre.addView(evento);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




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
