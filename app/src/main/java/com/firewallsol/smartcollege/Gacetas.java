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

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.firewallsol.smartcollege.Gaceta.Gaceta;
import com.firewallsol.smartcollege.Gaceta.RVAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Gacetas extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AutoScrollViewPager pager;
    public static ArrayList<ItemsInicio> arrayInicio;

    private Activity activity;
    private View root;


    //private List<Gaceta> gacetas;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    int pagina = 0;
    RVAdapter adapter;
    Boolean primera = false;
    Boolean gotomove = false;
    List<NameValuePair> params;
    List<NameValuePair> paramsSend;


    public Gacetas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Avisos.
     */
    // TODO: Rename and change types and number of parameters
    public static Gacetas newInstance(String param1, String param2) {
        Gacetas fragment = new Gacetas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_gaceta, container, false);
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

        if (MainActivity.gacetas == null) {
            MainActivity.gacetas = new ArrayList<>();
            primera =  true;
            Log.i("Accion","nuevo");

        } else {
            initializeAdapter();
            Log.i("Accion", "Actuaiza");
            primera =  false;
        }

        if (primera) {
            paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
            new DescargaGacetas().execute();
        }

       // swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.setOnRefreshListener(this);



        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int totalItemCount = llm.getItemCount();
                int lastVisibleItem = llm.findLastVisibleItemPosition();

                if (totalItemCount > 1) {
                    if (lastVisibleItem >= totalItemCount - 2 && !MainActivity.endLove) {
                        gotomove = true;
                        MainActivity.banderaPage++;
                        pagina = (MainActivity.banderaPage * 10) - 10;
                        Log.i("Pagina", pagina+"");
                        paramsSend = new ArrayList<>();
                        paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
                        paramsSend.add(new BasicNameValuePair("indice", pagina + ""));
                        new DescargaGacetas().execute();
                    }
                }
            }


        });


        return root;
    }


    private void initializeAdapter() {
        if (adapter == null) {
            adapter = new RVAdapter(MainActivity.gacetas, activity);
            rv.setAdapter(adapter);
            Log.i("Accion", "Crear Adapter");
        } else {
            adapter.notifyDataSetChanged();
            Log.e("Accion", "Notificar cambio");
        }
    }


    public void llenado(String result) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        Log.i("Resulta", result);

        if (result.length() > 10) {

            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("gaceta")) {
                JSONArray gaceta = jsonObj.getJSONArray("gaceta");
                for (int i = 0; i < gaceta.length(); i++) {
                    JSONObject c = gaceta.getJSONObject(i);
                    final String datos = c.toString();

                    MainActivity.gacetas.add(new Gaceta(c.getString("id"), c.getString("tutor"), c.getString("avatar_tutor"), c.getString("titulo"),
                            c.getString("texto"), c.getString("fecha"), c.getString("foto"), c.getString("url")));

                }
            } else {
                MainActivity.endLove = true;
            }
        }
        initializeAdapter();
    }


    public void onRefresh() {
        gotomove = true;
        adapter = null;

        MainActivity.banderaPage = 1;
        MainActivity.endLove = false;
        MainActivity.gacetas.clear();


        swipeRefreshLayout.setRefreshing(false);

        paramsSend = new ArrayList<>();
        paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
        new DescargaGacetas().execute();
        MainActivity.banderaPage = 1;
    }

    class DescargaGacetas extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(true);
            url = activity.getString(R.string.getGaceta);
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




}
