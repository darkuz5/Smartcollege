package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Database.Database;

public class SeleccionAlumno extends AppCompatActivity {
    public static Database db_sqlite;
    public static InputMethodManager inputManager;
    public static Activity activity;
    public static String color;
    public static ActionBar mActionBar;
    public static ImageView iconoDerecho;
    public static ImageView iconoIzquierdo;
    public static ImageView imagenPrincipal;
    public static TextView textoPrincipal;
    public static TextView textoSecundario;
    LinearLayout padre;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_alumno);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        activity = this;

        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        mActionBar = getSupportActionBar();

        db_sqlite = new Database(activity);
        SQLiteDatabase db = db_sqlite.getWritableDatabase();
        /*if(db != null) {
            db.close();
        }*/

        Cursor config = db.rawQuery("select * from colegio", null);
        if (config.moveToFirst()) {
            color = "#" + config.getString(5);
        } else {
            color = "#A01027";
        }

        if (color.length() < 6) {
            color = "#A01027";
        }

        Log.i("Color", color);

        CustomActionBar();

        padre = (LinearLayout) findViewById(R.id.padre);
        Cursor hijos = db.rawQuery("select * from hijos order by id asc", null);
        if (hijos.moveToFirst()) {
            do {
                View adp_alumno = inflater.inflate(R.layout.adapter_alumno, null);
                ((TextView) adp_alumno.findViewById(R.id.nombre)).setText(hijos.getString(1));
                final String id_alumno = hijos.getString(0);
                adp_alumno.findViewById(R.id.btn_alumno).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            MainActivity.activity.finish();
                        } catch (NullPointerException e){
                            e.printStackTrace();
                        }

                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        it.putExtra("color", color);
                        it.putExtra("alumno", id_alumno);
                        startActivity(it);
                        finish();

                    }
                });


                padre.addView(adp_alumno);
            } while (hijos.moveToNext());
        }


        db.close();
        db_sqlite.close();


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

        imagenPrincipal.setVisibility(View.GONE);
        textoPrincipal.setVisibility(View.VISIBLE);
        textoPrincipal.setText("ALUMNOS");

        iconoIzquierdo.setVisibility(View.GONE);
        iconoIzquierdo.setImageResource(R.drawable.atras);
        iconoIzquierdo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        mActionBar.setCustomView(customActionBarView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

}
