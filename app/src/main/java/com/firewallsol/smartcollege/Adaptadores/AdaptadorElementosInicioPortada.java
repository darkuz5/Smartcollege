package com.firewallsol.smartcollege.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsPromociones;
import com.firewallsol.smartcollege.Adaptadores.Items.ItemsSlider;
import com.firewallsol.smartcollege.R;
import com.firewallsol.smartcollege.SplashScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import com.firewallsol.android.EventosDetalle;

/**
 * Created on 05/10/15.
 */
public class AdaptadorElementosInicioPortada extends PagerAdapter {

    Activity activity;
    LayoutInflater inflater;
    private ArrayList<ItemsSlider> listData;

    public AdaptadorElementosInicioPortada(Activity activity, ArrayList<ItemsSlider> listData) {
        this.activity = activity;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.inicio_portada_item_adapter, container, false);

        final ItemsSlider item = listData.get(position);

        // Locate the TextViews in viewpager_item.xml
        ImageView imgImagen = (ImageView) itemView.findViewById(R.id.imgImagen);
        TextView tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);

        if(item.slide_image_url != null && !item.slide_image_url.equals("null") && !TextUtils.isEmpty(item.slide_image_url))
            Picasso.with(activity).load(item.slide_image_url).into(imgImagen);

        tvTitulo.setText(item.slide_title);

        imgImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemsPromociones itm = item.evento;
                Intent it = new Intent(activity, SplashScreen.class);
                it.putExtra("Item", itm);
                activity.startActivity(it);
            }
        });

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
