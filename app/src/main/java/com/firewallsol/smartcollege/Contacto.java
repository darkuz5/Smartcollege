package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Contacto extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<ItemsInicio> arrayInicio;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoScrollViewPager pager;
    private Activity activity;
    private View root;
    String url;
    LinearLayout padre;


    public Contacto() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Contacto newInstance() {
        Contacto fragment = new Contacto();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_contacto, container, false);
        activity = getActivity();
        ImageView logo = (ImageView) root.findViewById(R.id.logo);
        Picasso.with(activity).load(MainActivity.urlImgPrincipal).placeholder(R.drawable.logosc).into(logo);
        padre = (LinearLayout) root.findViewById(R.id.padre);


        SQLiteDatabase db = MainActivity.db_sqlite.getWritableDatabase();
        Cursor config = db.rawQuery("select * from colegio", null);
        if(config.moveToFirst()){
            ((TextView) root.findViewById(R.id.textTel)).setText(config.getString(3));
            final String phone = config.getString(3);
            root.findViewById(R.id.textTel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    Intent chooser = Intent.createChooser(intent, "Seleccione");
                    if (intent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivity(chooser);
                    }
                    activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                }
            });
            ((TextView) root.findViewById(R.id.txtEmail)).setText(config.getString(6));
            final String emaildir = config.getString(6);
            root.findViewById(R.id.txtEmail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"+emaildir)); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, emaildir);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent chooser = Intent.createChooser(intent, "Seleccione");
                    if (intent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivity(chooser);
                    }
                    activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                }
            });
            ((TextView) root.findViewById(R.id.txtLink)).setText(config.getString(8));
            url = config.getString(8);
            if (!url.contains("http"))
                    url = "http://" + url;

            root.findViewById(R.id.txtLink).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Intent chooser = Intent.createChooser(browserIntent, "Seleccione");
                    if (browserIntent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivity(chooser);
                    }
                    activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                }
            });

            ((TextView) root.findViewById(R.id.txtDirecion)).setText(config.getString(7));

        }
        config.close();


        Cursor directorio = db.rawQuery("select * from directorio",null);
        if (directorio.moveToFirst()){
         do{
             View evento = inflater.inflate(R.layout.adapter_item_directorio, null);
             TextView puesto = (TextView) evento.findViewById(R.id.puesto);
             TextView nombre = (TextView) evento.findViewById(R.id.nombre);
             TextView txtCorreo = (TextView) evento.findViewById(R.id.txtCorreo);

             puesto.setText(directorio.getString(2));
             nombre.setText(directorio.getString(1));
             txtCorreo.setText(directorio.getString(3));
             final String xCorreo = directorio.getString(3);
             txtCorreo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Intent intent = new Intent(Intent.ACTION_SENDTO);
                     intent.setData(Uri.parse("mailto:"+xCorreo)); // only email apps should handle this
                     intent.putExtra(Intent.EXTRA_EMAIL, xCorreo);
                     activity.startActivity(intent);
                     activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                 }
             });

             padre.addView(evento);;
         }while(directorio.moveToNext());
        }
        db.close();
        return root;
    }


}
