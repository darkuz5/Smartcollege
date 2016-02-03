package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Documento.Documento;
import com.firewallsol.smartcollege.Documento.RVAdapterDocumentos;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Pagos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<ItemsInicio> arrayInicio;
    // TODO: Rename and change types of parameters
    LinearLayout padre;

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
        root = inflater.inflate(R.layout.fragment_servicios, container, false);
        activity = getActivity();






        return root;
    }



    class DescargaPagoos extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(true);
            url = activity.getString(R.string.getDocumentos);
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
            if (jsonObj.has("documentos")) {
                JSONArray gaceta = jsonObj.getJSONArray("documentos");
                for (int i = 0; i < gaceta.length(); i++) {
                    JSONObject c = gaceta.getJSONObject(i);
                    final String datos = c.toString();

                    MainActivity.documentos.add(new Documento(c.getString("id"), c.getString("titulo"), c.getString("url")));

                }
            } else {
                MainActivity.DendLove = true;
            }
        }
    }




}
