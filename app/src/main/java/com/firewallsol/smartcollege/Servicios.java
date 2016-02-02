package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.firewallsol.smartcollege.Servicio.RVAdapterServicios;
import com.firewallsol.smartcollege.Servicio.Servicio;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Servicios extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<ItemsInicio> arrayInicio;
    // TODO: Rename and change types of parameters
    LinearLayout padre;

    int pagina = 0;
    RVAdapterServicios adapter;
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


    public Servicios() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Servicios newInstance() {
        Servicios fragment = new Servicios();
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
        root = inflater.inflate(R.layout.fragment_servicios, container, false);
        activity = getActivity();

        activity = getActivity();

        rv = (RecyclerView) root.findViewById(R.id.RecView);
        final LinearLayoutManager llm = new LinearLayoutManager(activity.getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.background_actionbar_azul_claro);
        // MainActivity.baderau = "1";

        dialog = new ProgressDialog(activity);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);

        adapter = null;

        if (MainActivity.servicios == null) {
            MainActivity.servicios = new ArrayList<>();
            primera = true;
            Log.i("Accion", "nuevo");

        } else {
            initializeAdapter();
            Log.i("Accion", "Actuaiza");
            primera = false;
        }

        if (primera) {
            paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
            new DescargaServicios().execute();
        }

        // swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.setOnRefreshListener(this);


        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int totalItemCount = llm.getItemCount();
                int lastVisibleItem = llm.findLastVisibleItemPosition();

                if (totalItemCount > 1) {
                    if (lastVisibleItem >= totalItemCount - 2 && !MainActivity.SendLove) {
                        gotomove = true;
                        MainActivity.SbanderaPage++;
                        pagina = (MainActivity.SbanderaPage * 10) - 10;
                        Log.i("Pagina", pagina + "");
                        paramsSend = new ArrayList<>();
                        paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
                        paramsSend.add(new BasicNameValuePair("indice", pagina + ""));
                        new DescargaServicios().execute();
                    }
                }
            }


        });


        return root;
    }

    private void initializeAdapter() {
        if (adapter == null) {
            adapter = new RVAdapterServicios(MainActivity.servicios, activity);
            rv.setAdapter(adapter);
            Log.i("Accion", "Crear Adapter");
        } else {
            adapter.notifyDataSetChanged();
            Log.e("Accion", "Notificar cambio");
        }
    }


    public void onRefresh() {
        gotomove = true;
        adapter = null;

        MainActivity.SbanderaPage = 1;
        MainActivity.SendLove = false;
        MainActivity.servicios.clear();


        swipeRefreshLayout.setRefreshing(false);

        paramsSend = new ArrayList<>();
        paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
        new DescargaServicios().execute();
        MainActivity.SbanderaPage = 1;
    }


    class DescargaServicios extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(true);
            url = activity.getString(R.string.getServicios);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {
            String jsonRead = "";
            try {

//                Log.i("Pagina", MainActivity.banderaPage + "|Escuela:"+MainActivity.idEscuela+"|"+params.toString());
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
            try {
                llenado(aVoid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }

    public void llenado(String result) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        Log.i("Resulta", result);

        if (result.length() > 10) {

            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("servicios")) {
                JSONArray gaceta = jsonObj.getJSONArray("servicios");
                for (int i = 0; i < gaceta.length(); i++) {
                    JSONObject c = gaceta.getJSONObject(i);
                    final String datos = c.toString();

                    MainActivity.servicios.add(new Servicio(c.getString("id"), c.getString("nombre"), c.getString("descripcion"), c.getString("telefono"),
                            c.getString("correo"), c.getString("pagina"), c.getString("direccion"), c.getString("coordenadas"), c.getString("foto"), c.toString()));

                }
            } else {
                MainActivity.SendLove = true;
            }
        }
        initializeAdapter();
    }




}
