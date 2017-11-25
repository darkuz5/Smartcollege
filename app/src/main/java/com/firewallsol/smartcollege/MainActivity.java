package com.firewallsol.smartcollege;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Database.Database;
import com.firewallsol.smartcollege.Documento.Documento;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.firewallsol.smartcollege.Gaceta.Gaceta;
import com.firewallsol.smartcollege.Servicio.Servicio;
//import com.parse.ParsePush;
import com.firewallsol.smartcollege.service.MyFirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import com.firewallsol.smartcollege.app.Config;
import com.firewallsol.smartcollege.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Database db_sqlite;
    public static InputMethodManager inputManager;
    public static Activity activity;
    public static String color;
    public static MaterialRippleLayout btn_tutor;
    public static CircleImageView foto_tutor;
    public static FragmentManager supportFragment;
    public static final String MyPREFERENCES = "MyPrefs" ;

    /**
     * Action Bar
     **/
    public static ImageView imagenPincipal;
    public static TextView textoPrincipal;
    public static ImageView iconoDerecho;


    /**
     * Contenido
     **/
    public static String contenido_avisos="";
    public static String contenido_galerias = "";


    /**
     * Variables
     **/
    public static String urlImgPrincipal="";
    public static String alumno="";
    public static String nombreAlumno="";
    public static String idEscuela="";
    public static String idTutor="";
    public static String idGrupo="";



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

    /** Pinchi barra de menu de abajo **/
    MaterialRippleLayout btn_mavisos, btn_meventos, btn_malumnos, btn_mgalerias;
    ImageView img_mavisos, img_meventos, img_malumnos, img_mgalerias;
    TextView txt_mavisos, txt_meventos, txt_malumnos, txt_mgalerias;


    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;

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


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String timestamp = intent.getStringExtra("timestamp");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                     MyFirebaseMessagingService mf = new MyFirebaseMessagingService();
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String format = simpleDateFormat.format(new Date());

                    mf.showNotificationMessage(getApplicationContext(), message, message, format, resultIntent);

                    //addNotification();



                }
            }
        };


        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseMessaging.getInstance().subscribeToTopic("global");

        btn_mavisos = (MaterialRippleLayout) findViewById(R.id.btn_mavisos);
        btn_meventos = (MaterialRippleLayout) findViewById(R.id.btn_meventos);
        btn_malumnos = (MaterialRippleLayout) findViewById(R.id.btn_malumnos);
        btn_mgalerias = (MaterialRippleLayout) findViewById(R.id.btn_mgalerias);

        btn_mavisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmenu(R.id.nav_avisos);
            }
        });
        btn_meventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmenu(R.id.nav_eventos);
            }
        });
        btn_malumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmenu(R.id.nav_alumnos);
            }
        });
        btn_mgalerias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xmenu(R.id.nav_galeria);
            }
        });
        img_mavisos = (ImageView) findViewById(R.id.img_mavisos);
        img_meventos = (ImageView) findViewById(R.id.img_meventos);
        img_malumnos = (ImageView) findViewById(R.id.img_malumnos);
        img_mgalerias = (ImageView) findViewById(R.id.img_mgalerias);

        txt_mavisos = (TextView) findViewById(R.id.txt_mavisos);
        txt_meventos = (TextView) findViewById(R.id.txt_meventos);
        txt_malumnos = (TextView) findViewById(R.id.txt_malumnos);
        txt_mgalerias = (TextView) findViewById(R.id.txt_mgalerias);




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
            Log.e("alumno",alumno);

            Cursor alumn = db.rawQuery("select * from hijos where id = '" + alumno + "'", null);
            if (alumn.moveToFirst()) {
                nombreAlumno = alumn.getString(1);
                idGrupo = alumn.getString(5);
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
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("color", color);
        editor.putString("alumno", alumno);
        editor.commit();

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


        barramenu(0);


    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Notifications Example");
        builder.setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

        Log.d("","Prueba");

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

        xmenu(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void xmenu(int id){
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
    }

    public void showFragmentView(int fragmentId) {
        Fragment f = null;
        if (cargado) {
            switch (fragmentId) {
                case 0:
                    f = Avisos.newInstance("", "");
                    barramenu(0);
                    break;
                case 1:
                    f = Eventos.newInstance("", "");
                    barramenu(1);
                    break;
                case 2:
                    f = Alumno.newInstance();
                    barramenu(2);
                    break;
                case 3:
                    f = Galerias.newInstance();
                    barramenu(3);
                    break;
                case 4:
                    f = Gacetas.newInstance("", "");
                    barramenu(4);
                    break;
                case 5:
                    f = Documentos.newInstance();
                    barramenu(5);
                    break;
                case 6:
                    f = Pagos.newInstance();
                    barramenu(6);
                    break;
                case 7:
                    f = Servicios.newInstance();
                    barramenu(7);
                    break;
                case 8:
                    f = Contacto.newInstance();
                    barramenu(8);
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
            try {
                supportFragment.beginTransaction().replace(R.id.content_frame, f).commit();
            } catch (IllegalStateException e){
                e.printStackTrace();
            }
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


                        FirebaseMessaging.getInstance().unsubscribeFromTopic("e" + MainActivity.idEscuela);
                        //ParsePush.unsubscribeInBackground("e" + MainActivity.idEscuela);
                        Cursor hijos = db.rawQuery("select * from hijos", null);
                        if (hijos.moveToFirst()) {
                            do {

                                FirebaseMessaging.getInstance().unsubscribeFromTopic("a" + hijos.getString(0));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("ga" + hijos.getString(3));
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("gu" + hijos.getString(5));
                                /*ParsePush.unsubscribeInBackground();
                                ParsePush.unsubscribeInBackground();
                                ParsePush.unsubscribeInBackground();*/
                            } while (hijos.moveToNext());
                        }
                        hijos.close();
                        Cursor materias = db.rawQuery("select * from materias", null);
                        if (materias.moveToFirst()) {

                            do {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("m" + materias.getString(0));
                                //ParsePush.unsubscribeInBackground();
                            } while (materias.moveToNext());
                        }
                        materias.close();
                        db.execSQL("delete from tutor");
                        db.execSQL("delete from colegio");
                        db.execSQL("delete from directorio");
                        db.execSQL("delete from hijos");
                        db.execSQL("delete from materias");
                        db.close();

                        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("color", "");
                        editor.putString("alumno", "");
                        editor.clear();
                        editor.commit();


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
                        FirebaseMessaging.getInstance().subscribeToTopic("m"+c.getString("id"));
                        //ParsePush.subscribeInBackground();
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



    /*** para la pinchi barra de menu ***/

    public  void barramenu(int estado){

        switch (estado){
            case 0: //Avisos
                img_mavisos.setColorFilter(Color.parseColor(color));
                img_meventos.setColorFilter(Color.parseColor("#333333"));
                img_malumnos.setColorFilter(Color.parseColor("#333333"));
                img_mgalerias.setColorFilter(Color.parseColor("#333333"));

                txt_mavisos.setTextColor(Color.parseColor(color));
                txt_meventos.setTextColor(Color.parseColor("#333333"));
                txt_malumnos.setTextColor(Color.parseColor("#333333"));
                txt_mgalerias.setTextColor(Color.parseColor("#333333"));
                break;
            case 1: //Avisos
                img_mavisos.setColorFilter(Color.parseColor("#333333"));
                img_meventos.setColorFilter(Color.parseColor(color));
                img_malumnos.setColorFilter(Color.parseColor("#333333"));
                img_mgalerias.setColorFilter(Color.parseColor("#333333"));

                txt_mavisos.setTextColor(Color.parseColor("#333333"));
                txt_meventos.setTextColor(Color.parseColor(color));
                txt_malumnos.setTextColor(Color.parseColor("#333333"));
                txt_mgalerias.setTextColor(Color.parseColor("#333333"));
                break;

            case 2: //Avisos
                img_mavisos.setColorFilter(Color.parseColor("#333333"));
                img_meventos.setColorFilter(Color.parseColor("#333333"));
                img_malumnos.setColorFilter(Color.parseColor(color));
                img_mgalerias.setColorFilter(Color.parseColor("#333333"));

                txt_mavisos.setTextColor(Color.parseColor("#333333"));
                txt_meventos.setTextColor(Color.parseColor("#333333"));
                txt_malumnos.setTextColor(Color.parseColor(color));
                txt_mgalerias.setTextColor(Color.parseColor("#333333"));
                break;

            case 3: //Avisos
                img_mavisos.setColorFilter(Color.parseColor("#333333"));
                img_meventos.setColorFilter(Color.parseColor("#333333"));
                img_malumnos.setColorFilter(Color.parseColor("#333333"));
                img_mgalerias.setColorFilter(Color.parseColor(color));

                txt_mavisos.setTextColor(Color.parseColor("#333333"));
                txt_meventos.setTextColor(Color.parseColor("#333333"));
                txt_malumnos.setTextColor(Color.parseColor("#333333"));
                txt_mgalerias.setTextColor(Color.parseColor(color));
                break;
            default:
                img_mavisos.setColorFilter(Color.parseColor("#333333"));
                img_meventos.setColorFilter(Color.parseColor("#333333"));
                img_malumnos.setColorFilter(Color.parseColor("#333333"));
                img_mgalerias.setColorFilter(Color.parseColor("#333333"));

                txt_mavisos.setTextColor(Color.parseColor("#333333"));
                txt_meventos.setTextColor(Color.parseColor("#333333"));
                txt_malumnos.setTextColor(Color.parseColor("#333333"));
                txt_mgalerias.setTextColor(Color.parseColor("#333333"));
                break;


        }
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
