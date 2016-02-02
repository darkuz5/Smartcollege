package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;


import com.firewallsol.smartcollege.Gaceta.Gaceta;
import com.firewallsol.smartcollege.Gaceta.RVAdapter;
import com.firewallsol.smartcollege.TareaModel.RVAdapterT;
import com.firewallsol.smartcollege.TareaModel.TareaModel;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Tarea extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match

    public static ArrayList<ItemsInicio> arrayInicio;
    int pagina = 0;
    RVAdapterT adapter;
    Boolean primera = false;
    Boolean gotomove = false;
    List<NameValuePair> params;
    List<NameValuePair> paramsSend;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoScrollViewPager pager;
    private Activity activity;
    private LayoutInflater inflater;
    private View root;
    //private List<Gaceta> gacetas;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;

    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static String color;

    public static JSONObject json;
    public static JSONObject manJson;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tarea);
        // Inflate the layout for this fragment
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        activity = this;
        ((TextView) findViewById(R.id.alumno)).setText(MainActivity.nombreAlumno);
        //inicializando
        json = new JSONObject();
        manJson = new JSONObject();
        //actionbar
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();
        rv = (RecyclerView) findViewById(R.id.RecView);
        final LinearLayoutManager llm = new LinearLayoutManager(activity.getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.background_actionbar_azul_claro);
        // MainActivity.baderau = "1";
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);

        adapter = null;

        if (Alumno.tareas == null) {
            Alumno.tareas  = new ArrayList<>();
            primera = true;
            Log.i("Accion", "nuevo");

        } else {
            initializeAdapter();
            Log.i("Accion", "Actuaiza");
            primera = false;
        }

        if (primera) {
            paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("id_grupo", MainActivity.idGrupo));
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
                    if (lastVisibleItem >= totalItemCount - 2 && !Alumno.endLove) {
                        gotomove = true;
                        Alumno.banderaPage++;
                        pagina = (Alumno.banderaPage * 10) - 10;
                        Log.i("Pagina", pagina + "");
                        paramsSend = new ArrayList<>();
                        paramsSend.add(new BasicNameValuePair("id_grupo", MainActivity.idGrupo));
                        paramsSend.add(new BasicNameValuePair("indice", pagina + ""));
                        new DescargaGacetas().execute();
                    }
                }
            }


        });
    }




    private void initializeAdapter() {
        if (adapter == null) {
            adapter = new RVAdapterT(Alumno.tareas, activity);
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
            if (jsonObj.has("tareas")) {
                JSONArray tareasArr = jsonObj.getJSONArray("tareas");
                for (int i = 0; i < tareasArr.length(); i++) {
                    JSONObject c = tareasArr.getJSONObject(i);
                    final String datos = c.toString();

                    Alumno.tareas.add(new TareaModel(c.getString("id"),c.getString("materia"),c.getString("titulo"),c.getString("descripcion"),calcularFechaFull(c.getString("fecha"))));

                }
            } else {
                Alumno.endLove = true;
            }
        }
        initializeAdapter();
    }


    public void onRefresh() {
        gotomove = true;
        adapter = null;

        Alumno.banderaPage = 1;
        Alumno.endLove = false;
        Alumno.tareas.clear();


        swipeRefreshLayout.setRefreshing(false);

        paramsSend = new ArrayList<>();
        paramsSend.add(new BasicNameValuePair("id_grupo", MainActivity.idGrupo));
        new DescargaGacetas().execute();
        Alumno.banderaPage = 1;
    }

    class DescargaGacetas extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(true);
            url = activity.getString(R.string.getTareas);
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

    private static String calcularFecha(String fecha) {
        String fec = "";

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat parseFormat = new SimpleDateFormat("MM-dd");
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

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd ' de ' MMMM");
        SimpleDateFormat parseFormat = new SimpleDateFormat("MM-dd");
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
