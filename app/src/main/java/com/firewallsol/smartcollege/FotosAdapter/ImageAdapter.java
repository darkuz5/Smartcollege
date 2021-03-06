package com.firewallsol.smartcollege.FotosAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.firewallsol.smartcollege.Galerias_Slider;
import com.firewallsol.smartcollege.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DARKUZ5 on 25/01/2016.
 */
public class ImageAdapter extends BaseAdapter {
    ArrayList<String> mylist;
    ArrayList<String> titulo;
    // Contexto de la aplicación
    private Context mContext;
    // Array de identificadores
    private String[] mThumbIds;

    public ImageAdapter(Context c, ArrayList<String> urls, ArrayList<String> titulo) {
        this.mContext = c;
        this.mylist = urls;
        this.titulo = titulo;
    }

    public int getCount() {
        return mylist.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, final View convertView, ViewGroup parent) {
        //ImageView a retornar
        SquareImageView imageView;

        if (convertView == null) {
            /*
            Crear un nuevo Image View de 90x90
            y con recorte alrededor del centro
             */
            imageView = new SquareImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (SquareImageView) convertView;
        }

        final String urlFoto = mylist.get(position);
        final String titul = titulo.get(position);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /**/

                Activity activity = (Activity) mContext;

                Intent it = new Intent(mContext, Galerias_Slider.class);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

                /**/
            }
        });
        //Setear la imagen desde el recurso drawable
        if (urlFoto.length() > 5)
            Picasso.with(mContext).load(urlFoto).into(imageView);
        // imageView.setImageResource();
        return imageView;
    }

}