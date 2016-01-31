package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Database.Database;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Database db_sqlite;
    public static InputMethodManager inputManager;
    public static Activity activity;
    public static String color;
    public static ImageView imagenPincipal;
    public static MaterialRippleLayout btn_tutor;
    public static CircleImageView foto_tutor;
    public static FragmentManager supportFragment;

    /** Contenido **/
    public static String contenido_avisos;
    public static String contenido_galerias="";


    /** Variables **/
    public static String urlImgPrincipal;
    public static String alumno;
    public static String nombreAlumno;
    public static String idEscuela;
    public static String idTutor;



    static Boolean cargado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        db_sqlite = new Database(activity);
        SQLiteDatabase db = db_sqlite.getWritableDatabase();


        imagenPincipal = (ImageView) toolbar.findViewById(R.id.img_principal);
        supportFragment = getSupportFragmentManager();


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_avisos);


        Intent it = getIntent();
        if(it.hasExtra("color")){
            color = it.getStringExtra("color");
        }
        if(it.hasExtra("alumno")){
            alumno = it.getStringExtra("alumno");

            Cursor alumn = db.rawQuery("select * from hijos where id = '"+alumno+"'",null);
            if(alumn.moveToFirst()){
                nombreAlumno = alumn.getString(1);
            }
            alumn.close();
        }

        if (color.length()<6){
            color = "#A01027";
        }
        toolbar.setBackgroundColor(Color.parseColor(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
        /**  DATOS DEL COLEGIO **/
        Cursor config = db.rawQuery("select * from colegio",null);
        if (config.moveToFirst()){
            idEscuela = config.getString(0);
            urlImgPrincipal = "http://smartcollege.mx/"+config.getString(9);
            Picasso.with(activity).load("http://smartcollege.mx/"+config.getString(9)).placeholder(R.drawable.logosc).into(imagenPincipal);
        } else {
            urlImgPrincipal ="";
        }

        /**  DATOS DEL TUTOR **/
        View header = navigationView.getHeaderView(0);
        btn_tutor = (MaterialRippleLayout) header.findViewById(R.id.btn_tutor);
        foto_tutor = (CircleImageView) header.findViewById(R.id.foto);
        Cursor tutor = db.rawQuery("select * from tutor",null);
        navigationView.setBackgroundColor(Color.parseColor(color));
        if (tutor.moveToFirst()){
            idTutor = tutor.getString(0);
            ((TextView) header.findViewById(R.id.nombre)).setText(tutor.getString(1));
            Picasso.with(activity).load(tutor.getString(6)).placeholder(R.drawable.logosc).into(foto_tutor);
        }
        btn_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Perfil.class);
                startActivity(it);
            }
        });

        new DescargaAvisos().execute();
        showFragmentView(20);
        config.close();
        db.close();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Realmente desea salir")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_avisos) {
            showFragmentView(0);
            Log.i("cargar","Avisos");
        } else if (id == R.id.nav_eventos) {
            showFragmentView(1);
            Log.i("cargar", "Eventos");
        } else if (id == R.id.nav_alumnos) {
            Log.i("cargar","Alumnos");
            showFragmentView(2);

        } else if (id == R.id.nav_galeria) {
            Log.i("cargar","Galeria");
            showFragmentView(3);

        } else if (id == R.id.nav_gaceta) {
            Log.i("cargar","Gaceta");
            showFragmentView(4);

        } else if (id == R.id.nav_documentos) {
            showFragmentView(5);

        } else if (id == R.id.nav_pagos) {
            showFragmentView(6);

        } else if (id == R.id.nav_servicios) {
            showFragmentView(7);
            Log.i("cargar", "Servicios");

        } else if (id == R.id.nav_contaco) {
            showFragmentView(8);
            Log.i("cargar", "Contacto");

        } else if (id == R.id.nac_alumno) {
            //showFragmentView(9);
            Log.i("cargar", "Cambiar Alumno");
            cambiarAlumno();

        } else if (id == R.id.nav_salir) {
            //showFragmentView(10);
            salir();
            Log.i("cargar", "Salir");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragmentView(int fragmentId) {
        Fragment f = null;
        if (cargado) {
            switch (fragmentId) {
                case 0:
                    f = Avisos.newInstance("", "");
                    break;
                case 1:
                    f = Eventos.newInstance("", "");
                    break;
                case 2:
                    f = Alumno.newInstance();
                    break;
                case 3:
                    f = Galerias.newInstance();
                    break;
                case 4:
                    f = Gacetas.newInstance("","");
                    break;
                case 9:
                    cambiarAlumno();
                    break;
                case 10:
                    salir();
                    break;
                default:
                    f = Loader.newInstance("", "");
                    break;

            }
        } else {
            f = Loader.newInstance("", "");
        }
        if(f != null){
            supportFragment.beginTransaction().replace(R.id.content_frame, f).commit();
        }
    }

    public void salir(){
        new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Salir")
                .setMessage("Cerrar Sesión")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase db = db_sqlite.getWritableDatabase();
                        db.execSQL("delete from tutor");
                        db.execSQL("delete from colegio");
                        db.execSQL("delete from directorio");
                        db.execSQL("delete from hijos");
                        db.close();
                        Intent it = new Intent(activity, SplashScreen.class);
                        startActivity(it);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void cambiarAlumno(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("accion", "Cambiar alumno");
                Intent it = new Intent(activity, SeleccionAlumno.class);
                startActivity(it);
            }
        });

    }


    class DescargaAvisos extends AsyncTask<Void, Void, Void> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.getAvisos);
            arrayInicio = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(!isCancelled()) {
                try {
                    getPortada();

                     Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void getPortada() {

            try {

                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_alumno", alumno.trim()));
                jSONFunciones json = new jSONFunciones();
                String jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
                Log.e("json", jsonRead);
                JSONObject jsonObject = new JSONObject(jsonRead);
                if (jsonObject.has("avisos")){

                    contenido_avisos = jsonRead;
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cargado = true;
            showFragmentView(0);
        }
    }

}
