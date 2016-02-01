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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;


import com.firewallsol.smartcollege.TareaModel.RVAdapterT;
import com.firewallsol.smartcollege.TareaModel.TareaModel;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Tarea extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{
    public static Activity activity;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;

    private LayoutInflater inflater;
    LinearLayout otros;
    String jsonRead;

    //recycler
    private List<TareaModel> tareas;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    int pagina = 0;
    boolean endLove = false;
    int banderaPage = 1;
    RVAdapterT adapter;
    LinearLayoutManager llm;

    public Tarea(){
        //required empty constructor s
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tarea);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        TextView alumno = (TextView) findViewById(R.id.alumno);
        alumno.setText(MainActivity.nombreAlumno);
        activity = this;
        //actionbar
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();

        //recycler view
        rv = (RecyclerView) findViewById(R.id.RecView);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.background_actionbar_azul_claro);

        dialog = new ProgressDialog(activity);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);
        tareas = new ArrayList<>();

        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int totalItemCount = llm.getItemCount();
                int lastVisibleItem = llm.findLastVisibleItemPosition();

                if (totalItemCount > 1) {
                    if (lastVisibleItem >= totalItemCount - 4 && !endLove) {
                        banderaPage++;
                        int pagina = banderaPage * 10 - 10;
                        pagina = pagina;
                        new DescargaTareas().execute();

                    }


                }
            }


        });
        new DescargaTareas().execute();
        // swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeRefreshLayout.setOnRefreshListener(this);


    }

    private void initializeAdapter() {
        if (adapter == null) {
            adapter = new RVAdapterT(tareas,this);
            rv.setAdapter(adapter);
            Log.i("Accion", "Crear Adapter");
        } else {
            adapter.notifyDataSetChanged();
            Log.e("Accion", "Notificar cambio");
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        pagina = 0;
        tareas.clear();
        adapter.notifyDataSetChanged();
        new DescargaTareas().execute();
    }


    class DescargaTareas extends AsyncTask<Void, Void, String> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            url = getString(R.string.getTareas);
            arrayInicio = new ArrayList<>();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String cadena = "null";
            if(!isCancelled()) {
                try {
                        try {

                            List<NameValuePair> paramsSend = new ArrayList<>();
                            paramsSend.add(new BasicNameValuePair("id_grupo", MainActivity.idGrupo.trim()));
                            paramsSend.add(new BasicNameValuePair("indice", pagina+""));
                            jSONFunciones json = new jSONFunciones();
                            jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
                            Log.e("json", jsonRead);
                            JSONObject jsonObject = new JSONObject(jsonRead);
                            if (jsonObject.has("tareas")){

                                ListaTareasExamenes.json_tareas = jsonRead;

                            }
                            cadena = jsonRead;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    //Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return cadena;
        }



        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            llenado(aVoid);
            dialog.dismiss();
            JSONObject jsonAux =null;
            try{
                jsonAux = new JSONObject(jsonRead);
            }catch (JSONException e){
                e.getMessage();
            }
            if(jsonAux.has("error")){
                ListaTareasExamenes.isCargadoTarea = false;
            }else if(jsonAux.has("tareas")){
                ListaTareasExamenes.isCargadoTarea = true;
            }
        }
    }

    public void llenado(String datos){
        Log.e("json", datos);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(datos);
            if (jsonObject.has("tareas")) {
                JSONArray array = jsonObject.getJSONArray("tareas");
                for (int i=0; i< array.length(); i++){
                    final JSONObject c = array.getJSONObject(i);
                    tareas.add(new TareaModel(c.getString("id"),c.getString("materia"),c.getString("descripcion"),c.getString("fecha")));
                }
            }
            if(jsonObject.has("error")){
                Alumno.cargadoCalif = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initializeAdapter();
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
        textoPrincipal.setText("TAREAS");

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
