package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Servicios_Detalle extends AppCompatActivity {
    private Activity activity;
    private LayoutInflater inflater;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    public static InputMethodManager inputManager;

    private GoogleMap map;
    MapView mMapView;
    private GoogleMap googleMap;
    String titulox="";
    String descripcionx ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(this);
        setContentView(R.layout.activity_servicios_detalle);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mActionBar = getSupportActionBar();
        color = MainActivity.color;
        CustomActionBar();


        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }





        Intent it = getIntent();
        if(it.hasExtra("datos")){
            try {
                JSONObject c = new JSONObject(it.getStringExtra("datos"));

                ImageView fotoGal = (ImageView) findViewById(R.id.fotoGal);
                if (c.getString("foto").length() > 0)
                    Picasso.with(activity).load(c.getString("foto")).into(fotoGal);
                else
                    fotoGal.setVisibility(View.GONE);

                ((TextView) findViewById(R.id.txtTitulo)).setText(c.getString("nombre"));
                ((TextView) findViewById(R.id.txtFecha)).setText(c.getString("descripcion"));
                ((TextView) findViewById(R.id.textTel)).setText(c.getString("telefono"));


                titulox = c.getString("nombre");
                descripcionx=c.getString("descripcion");

                final String phone =c.getString("telefono");
                findViewById(R.id.textTel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Intent chooser = Intent.createChooser(intent, "Seleccione");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            activity.startActivity(chooser);
                        }
                        activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                    }
                });

                ((TextView) findViewById(R.id.txtEmail)).setText(c.getString("correo"));
                final String emaildir = c.getString("correo");
                findViewById(R.id.txtEmail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + emaildir)); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, emaildir);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Intent chooser = Intent.createChooser(intent, "Seleccione");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            activity.startActivity(chooser);
                        }
                        activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                    }
                });

                ((TextView) findViewById(R.id.txtLink)).setText(c.getString("pagina"));
                final String url = c.getString("pagina");
                findViewById(R.id.txtLink).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String myurl = url;
                        if (!myurl.contains("http"))
                                myurl = "http://"+myurl;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myurl));

                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Intent chooser = Intent.createChooser(browserIntent, "Seleccione");
                        if (browserIntent.resolveActivity(getPackageManager()) != null) {
                            activity.startActivity(chooser);
                        }
                        activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                    }
                });

                ((TextView) findViewById(R.id.txtDirecion)).setText(c.getString("direccion"));


                String coordenadas = c.getString("coordenadas");
                if (TextUtils.isEmpty(coordenadas)){
                    findViewById(R.id.ubica).setVisibility(View.GONE);
                } else {
                    final String[] coordena = coordenadas.split(", ");

                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            googleMap = mMap;
                            try {
                                // For showing a move to my location button
                                googleMap.setMyLocationEnabled(true);
                            } catch (Exception ex){

                            }

                            // For dropping a marker at a point on the Map
                            LatLng sydney = new LatLng(Double.parseDouble(coordena[0] + "0"), Double.parseDouble(coordena[1] + "0"));
                            googleMap.addMarker(new MarkerOptions().position(sydney).title(titulox).snippet(descripcionx));

                            // For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    });

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void CustomActionBar() {
        // TODO Auto-generated method stub
        final LayoutInflater inflater = (LayoutInflater) mActionBar.getThemedContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.activity_main_actionbar, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            try {
            getWindow().setStatusBarColor(Color.parseColor(color));
            } catch (Exception ex) {
            Log.e("Error", ex.toString());
            }
        }
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

        imagenPrincipal = (ImageView) customActionBarView.findViewById(R.id.imagenPrincipal);
        textoPrincipal = (TextView) customActionBarView.findViewById(R.id.textoPrincipal);
        textoSecundario = (TextView) customActionBarView.findViewById(R.id.textoSecundario);
        iconoDerecho = (ImageView) customActionBarView.findViewById(R.id.iconoDerecho);
        iconoIzquierdo = (ImageView) customActionBarView.findViewById(R.id.iconoIzquierdo);

        RelativeLayout contenedor = (RelativeLayout) customActionBarView.findViewById(R.id.contenedor);
        contenedor.setBackgroundColor(Color.parseColor(color));
        /*if ((MainActivity.urlImgPrincipal).length() > 10){
            Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(imagenPrincipal);
        }*/
        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("SERVICIO");

        iconoIzquierdo.setVisibility(View.VISIBLE);
        iconoIzquierdo.setImageResource(R.drawable.ic_action_icon_left);
        iconoIzquierdo.setColorFilter(Color.parseColor("#FFFFFF"));
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);

            }
        });

        iconoDerecho.setVisibility(View.GONE);
        iconoDerecho.setImageResource(R.drawable.comunidad);


        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) customActionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
