package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import com.firewallsol.smartcollege.Documento.Documento;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.firewallsol.smartcollege.Gaceta.Gaceta;
import com.firewallsol.smartcollege.Servicio.Servicio;
import com.parse.ParsePush;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
    public static MaterialRippleLayout btn_tutor;
    public static CircleImageView foto_tutor;
    public static FragmentManager supportFragment;

    /**
     * Action Bar
     **/
    public static ImageView imagenPincipal;
    public static TextView textoPrincipal;
    public static ImageView iconoDerecho;


    /**
     * Contenido
     **/
    public static String contenido_avisos;
    public static String contenido_galerias = "";


    /**
     * Variables
     **/
    public static String urlImgPrincipal;
    public static String alumno;
    public static String nombreAlumno;
    public static String idEscuela;
    public static String idTutor;
    public static String idGrupo;



    /**
     * Varibales para Gacetas
     **/
    public static List<Gaceta> gacetas = null;
    public static int banderaPage = 1;
    public static Boolean endLove = false;
    static Boolean cargado = false;


    /**
     * Varibales para Servicios
     **/
    public static List<Servicio> servicios = null;
    public static int SbanderaPage = 1;
    public static Boolean SendLove = false;
    static Boolean Scargado = false;



    /**
     * Varibales para Documentos
     **/
    public static List<Documento> documentos = null;
    public static int DbanderaPage = 1;
    public static Boolean DendLove = false;
    static Boolean Dcargado = false;



    public static TextView menuNombreUser;


    /** Variable para Eventos **/
    public static String eventos=null;


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
        iconoDerecho = (ImageView) toolbar.findViewById(R.id.iconoDerecho);
        textoPrincipal = (TextView) toolbar.findViewById(R.id.textoPrincipal);
        supportFragment = getSupportFragmentManager();


        iconoDerecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), Gacetas_Subir.class);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_avisos);


        Intent it = getIntent();
        if (it.hasExtra("color")) {
            color = it.getStringExtra("color");
        }
        if (it.hasExtra("alumno")) {
            alumno = it.getStringExtra("alumno");

            Cursor alumn = db.rawQuery("select * from hijos where id = '" + alumno + "'", null);
            if (alumn.moveToFirst()) {
                nombreAlumno = alumn.getString(1);
                idGrupo = alumn.getString(3);
            }
            alumn.close();

            Cursor matealumnos = db.rawQuery("select * from materias where alumno='"+alumno+"'",null);
            if (matealumnos.getCount() < 1){
                new DescargaMaterias().execute();
            }
            matealumnos.close();
        }

        if (color.length() < 6) {
            color = "#A01027";
        }
        toolbar.setBackgroundColor(Color.parseColor(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
        /**  DATOS DEL COLEGIO **/
        Cursor config = db.rawQuery("select * from colegio", null);
        if (config.moveToFirst()) {
            idEscuela = config.getString(0);
            urlImgPrincipal = "http://smartcollege.mx/" + config.getString(9);
            Picasso.with(activity).load("http://smartcollege.mx/" + config.getString(9)).placeholder(R.drawable.logosc).into(imagenPincipal);
        } else {
            urlImgPrincipal = "";
        }

        /**  DATOS DEL TUTOR **/
        View header = navigationView.getHeaderView(0);
        btn_tutor = (MaterialRippleLayout) header.findViewById(R.id.btn_tutor);
        foto_tutor = (CircleImageView) header.findViewById(R.id.foto);
        menuNombreUser = (TextView) header.findViewById(R.id.nombre);

        Cursor tutor = db.rawQuery("select * from tutor", null);
        navigationView.setBackgroundColor(Color.parseColor(color));
        if (tutor.moveToFirst()) {
            idTutor = tutor.getString(0);
            menuNombreUser.setText(tutor.getString(1));
            if (tutor.getString(6) != null && tutor.getString(6).length() > 5)
                Picasso.with(activity).load(tutor.getString(6)).placeholder(R.drawable.logosc).into(foto_tutor);
        }
        btn_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Perfil.class);
                startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });

        new DescargaAvisos().execute();
        showFragmentView(20);
        config.close();
        db.close();

    }

    public static void cambiardatosUser(String urlFoto){


            if (urlFoto!= null && urlFoto.length()> 5)
                Picasso.with(activity).load(urlFoto).placeholder(R.drawable.logosc).into(foto_tutor);


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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_avisos) {
            showFragmentView(0);
            imagenPincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setVisibility(View.GONE);
            textoPrincipal.setText("AVISOS");
            iconoDerecho.setVisibility(View.GONE);
            Log.i("cargar", "Avisos");

        } else if (id == R.id.nav_eventos) {
            showFragmentView(1);
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("EVENTOS");
            iconoDerecho.setVisibility(View.GONE);

            Log.i("cargar", "Eventos");
        } else if (id == R.id.nav_alumnos) {
            Log.i("cargar", "Alumnos");
            showFragmentView(2);
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("ALUMNOS");
            iconoDerecho.setVisibility(View.GONE);

        } else if (id == R.id.nav_galeria) {
            Log.i("cargar", "Galeria");
            showFragmentView(3);
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("GALERÍA");
            iconoDerecho.setVisibility(View.GONE);

        } else if (id == R.id.nav_gaceta) {
            Log.i("cargar", "Gaceta");
            showFragmentView(4);
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("GACETA");
            iconoDerecho.setImageResource(R.drawable.new_gaceta);
            iconoDerecho.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_documentos) {
            showFragmentView(5);
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("DOCUMENTOS");
            iconoDerecho.setVisibility(View.GONE);

        } else if (id == R.id.nav_pagos) {
            showFragmentView(6);
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("PAGOS");
            iconoDerecho.setVisibility(View.GONE);

        } else if (id == R.id.nav_servicios) {
            showFragmentView(7);
            Log.i("cargar", "Servicios");
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("SERVICIOS");
            iconoDerecho.setVisibility(View.GONE);

        } else if (id == R.id.nav_contaco) {
            showFragmentView(8);
            Log.i("cargar", "Contacto");
            imagenPincipal.setVisibility(View.GONE);
            textoPrincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setText("CONTACTO");
            iconoDerecho.setVisibility(View.GONE);

        } else if (id == R.id.nac_alumno) {
            //showFragmentView(9);
            Log.i("cargar", "Cambiar Alumno");
            cambiarAlumno();
            /*imagenPincipal.setVisibility(View.VISIBLE);
            textoPrincipal.setVisibility(View.GONE);
            textoPrincipal.setText("AVISOS");
            iconoDerecho.setVisibility(View.GONE);*/

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
                    f = Gacetas.newInstance("", "");
                    break;
                case 5:
                    f = Documentos.newInstance();
                    break;
                case 7:
                    f = Servicios.newInstance();
                    break;
                case 8:
                    f = Contacto.newInstance();
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
        if (f != null) {
            supportFragment.beginTransaction().replace(R.id.content_frame, f).commit();
        }
    }

    public void salir() {
        new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Salir")
                .setMessage("Cerrar Sesión")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase db = db_sqlite.getWritableDatabase();
                        ParsePush.unsubscribeInBackground("e" + MainActivity.idEscuela);
                        Cursor hijos = db.rawQuery("select * from hijos", null);
                        if (hijos.moveToFirst()) {
                            do {
                                ParsePush.unsubscribeInBackground("a" + hijos.getString(0));
                                ParsePush.unsubscribeInBackground("ga" + hijos.getString(3));
                                ParsePush.unsubscribeInBackground("gu" + hijos.getString(5));
                            } while (hijos.moveToNext());
                        }
                        hijos.close();
                        Cursor materias = db.rawQuery("select * from materias", null);
                        if (materias.moveToFirst()) {

                            do {
                                ParsePush.unsubscribeInBackground("m" + materias.getString(0));
                            } while (materias.moveToNext());
                        }
                        materias.close();
                        db.execSQL("delete from tutor");
                        db.execSQL("delete from colegio");
                        db.execSQL("delete from directorio");
                        db.execSQL("delete from hijos");
                        db.execSQL("delete from materias");
                        db.close();
                        Intent it = new Intent(activity, SplashScreen.class);
                        startActivity(it);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void cambiarAlumno() {
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
            if (!isCancelled()) {
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
                if (jsonObject.has("avisos")) {

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

    class DescargaMaterias extends AsyncTask<Void, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.getMateriasAlumno);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonRead = "";
            if (!isCancelled()) {
                try {

                    List<NameValuePair> paramsSend = new ArrayList<>();
                    paramsSend.add(new BasicNameValuePair("id_alumno", alumno.trim()));
                    jSONFunciones json = new jSONFunciones();
                    jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);
                    Log.e("json", jsonRead);

                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return jsonRead;
        }


        @Override
        protected void onPostExecute(String aVoid) {


            try {
                JSONObject jsonObject = new JSONObject(aVoid);

                if (jsonObject.has("materias")) {
                    JSONArray array = jsonObject.getJSONArray("materias");
                    SQLiteDatabase db = db_sqlite.getWritableDatabase();
                    db.execSQL("delete from materias where alumno='"+alumno+"'");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        ParsePush.subscribeInBackground("m"+c.getString("id"));
                        String sql = "insert into materias (id, alumno, clave, nombre, tipo) " +
                                "values (?, ?, ? ,? , ?)";
                        SQLiteStatement insertStmt = db.compileStatement(sql);
                        insertStmt.clearBindings();
                        insertStmt.bindString(1, c.getString("id"));
                        insertStmt.bindString(2, alumno);
                        insertStmt.bindString(3, c.getString("nombre"));
                        insertStmt.bindString(4, c.getString("nombre"));
                        insertStmt.bindString(5, c.getString("tipo"));
                        insertStmt.executeInsert();

                    }
                    db.close();


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
