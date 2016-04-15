package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Premios_Honor extends AppCompatActivity {
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

    LinearLayout padre;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_honor);
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
        final LinearLayoutManager llm = new LinearLayoutManager(activity.getApplicationContext());
        padre  = (LinearLayout) findViewById(R.id.contenido);


        // MainActivity.baderau = "1";
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando...");
        dialog.setCancelable(false);

            paramsSend = new ArrayList<>();
            paramsSend.add(new BasicNameValuePair("id_grupo", MainActivity.idGrupo));
            new DescargaGacetas().execute();
            Log.e("datos", paramsSend.toString());


        // swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);

    }



    public void llenado(String result) throws JSONException {

        Log.i("Resulta", result);

        if (result.length() > 10) {

            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("cuadrohonor")) {
                JSONArray tareasArr = jsonObj.getJSONArray("cuadrohonor");
                for (int i = 0; i < tareasArr.length(); i++) {
                    JSONObject c = tareasArr.getJSONObject(i);
                    View evento = inflater.inflate(R.layout.adapter_chonor, null);
                    ((TextView) evento.findViewById(R.id.lugar)).setText(c.getString("posicion"));
                    ((TextView) evento.findViewById(R.id.nombre)).setText(c.getString("alumno"));
                    ((TextView) evento.findViewById(R.id.calificacion)).setText(c.getString("calificacion"));

                    padre.addView(evento);
                }
            } else {
                Alumno.endLove = true;
            }
        }

    }




    class DescargaGacetas extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            url = activity.getString(R.string.getBCuedroHonor);
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
        textoPrincipal.setText("CUADRO DE HONOR");

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
