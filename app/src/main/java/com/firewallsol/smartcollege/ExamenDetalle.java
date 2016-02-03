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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.firewallsol.smartcollege.ExamenModel.ExamenModel;
import com.firewallsol.smartcollege.ExamenModel.TemarioModel;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
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


public class ExamenDetalle extends AppCompatActivity {
    public static Activity activity;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    private ProgressDialog dialog;
    private LayoutInflater inflater;
    List<TemarioModel> temario;
    LinearLayout otros;
    String jsonRead;
    public String idExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_examen_detalle);

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
        Intent it = this.getIntent();
       if(it.hasExtra("id_examen")){
            idExamen = it.getStringExtra("id_examen");
        }

        try {
            llenado(Alumno.json_examenes);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void llenado(String result)throws JSONException{
        //Log.e("json", jsonRead);
        if (result.length() > 0) {
            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("temario")) {
                JSONArray tareasArr = jsonObj.getJSONArray("temario");
                for (int i = 0; i < tareasArr.length(); i++) {
                    JSONObject c = tareasArr.getJSONObject(i);
                    final String datos = c.toString();

                    if(idExamen.equals(c.getString("id_examen"))){
                        View aviso2 = inflater.inflate(R.layout.adapter_item_temario, null);
                        ((TextView) aviso2.findViewById(R.id.txtTitulo)).setText(c.getString("titulo"));
                        ((TextView) aviso2.findViewById(R.id.txtDescrip)).setText(c.getString("descripcion"));


                        otros.addView(aviso2);
                    }

                }
            }
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
        textoPrincipal.setText("TEMARIO");

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
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }


}
