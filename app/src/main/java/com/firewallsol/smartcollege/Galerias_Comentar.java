package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;

public class Galerias_Comentar extends AppCompatActivity {
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;
    String id, titulo, url;
    EditText comentario;
    ImageView foto;
    Button enviar;
    String texto;
    AlertDialog.Builder alert;
    private Activity activity;
    private LayoutInflater inflater;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerias__comentar);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();

        foto = (ImageView) findViewById(R.id.imgFoto);
        comentario = (EditText) findViewById(R.id.txtComentario);
        enviar = (Button) findViewById(R.id.btnEnviar);


        alert = new AlertDialog.Builder(Galerias_Comentar.this);

        dialog = new ProgressDialog(activity);
        dialog.setMessage("Enviando...");
        dialog.setCancelable(false);


        Intent it = getIntent();
        if (it.hasExtra("id")) {


            id = it.getStringExtra("id");
            titulo = it.getStringExtra("titulo");
            url = it.getStringExtra("url");


            Picasso.with(activity).load(url).into(foto);

            GradientDrawable drawable = (GradientDrawable) enviar.getBackground();
            drawable.setColor(Color.parseColor(color));


            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    texto = comentario.getText().toString();
                    if (texto.length() > 2) {
                        new ComentarFoto().execute();
                    } else {
                        Toast.makeText(activity, "Escriba su comentario para poder continuar", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            finish();
        }

    }


    private void CustomActionBar() {
        // TODO Auto-generated method stub
        final LayoutInflater inflater = (LayoutInflater) mActionBar.getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.activity_main_actionbar, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(parseColor(color));
        }
        mActionBar.setBackgroundDrawable(new ColorDrawable(parseColor(color)));

        imagenPrincipal = (ImageView) customActionBarView.findViewById(R.id.imagenPrincipal);
        textoPrincipal = (TextView) customActionBarView.findViewById(R.id.textoPrincipal);
        textoSecundario = (TextView) customActionBarView.findViewById(R.id.textoSecundario);
        iconoDerecho = (ImageView) customActionBarView.findViewById(R.id.iconoDerecho);
        iconoIzquierdo = (ImageView) customActionBarView.findViewById(R.id.iconoIzquierdo);

        RelativeLayout contenedor = (RelativeLayout) customActionBarView.findViewById(R.id.contenedor);
        contenedor.setBackgroundColor(parseColor(color));
        /*if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }*/
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("NUEVO COMENTARIO");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

            }
        });

        iconoDerecho.setVisibility(View.GONE);
        iconoDerecho.setImageResource(R.drawable.lapizcomenta);

        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    public void resultado(String data) {


        if (data.contains("0")) {
            alert.setTitle("Aviso");
            alert.setMessage("Error al enviar comentario");
            alert.setIcon(android.R.drawable.stat_notify_error);
            alert.setPositiveButton("OK", null);
            alert.show();

        } else if (data.contains("1")) {
            comentario.setText("");
            alert.setTitle("Aviso");
            alert.setIcon(android.R.drawable.stat_sys_upload_done);
            alert.setMessage("Comentario enviado");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                }
            });
            alert.show();

        } else {

        }


    }

    class ComentarFoto extends AsyncTask<Void, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.comentaFoto);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonRead = "";
            try {
                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_foto", id));
                paramsSend.add(new BasicNameValuePair("id_tutor", MainActivity.idTutor));
                paramsSend.add(new BasicNameValuePair("texto", texto));
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
            dialog.dismiss();
            resultado(aVoid);
        }
    }

}