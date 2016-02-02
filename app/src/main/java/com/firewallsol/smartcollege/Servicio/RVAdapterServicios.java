package com.firewallsol.smartcollege.Servicio;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.R;
import com.firewallsol.smartcollege.Servicios_Detalle;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RVAdapterServicios extends RecyclerView.Adapter<RVAdapterServicios.ServiciosViewHolder> {
    public String bandera = "x";
    Context context;
    List<Servicio> servicio;

    public RVAdapterServicios(List<Servicio> servicio, Context context) {
        this.servicio = servicio;
        this.context = context;
    }

    private static String calcularFechaFull(String fecha) {
        String fec = "";

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd ' de ' MMMM");
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date fecDateHoraParse = parseFormat.parse(fecha);

            Calendar today = Calendar.getInstance();
            Date fecActual = dateFormat.parse(dateFormat.format(today.getTime()));
            Date fecDateParse = dateFormat.parse(fecha);

            Calendar ayer = Calendar.getInstance();
            ayer.add(Calendar.DATE, -1);
            ayer.set(Calendar.HOUR_OF_DAY, 0);
            ayer.set(Calendar.MINUTE, 0);
            ayer.set(Calendar.SECOND, 0);

            Date ayerInicio = parseFormat.parse(parseFormat.format(ayer.getTime()));

            ayer = Calendar.getInstance();
            ayer.add(Calendar.DATE, -1);
            ayer.set(Calendar.HOUR_OF_DAY, 23);
            ayer.set(Calendar.MINUTE, 59);
            ayer.set(Calendar.SECOND, 59);

            Date ayerFinal = parseFormat.parse(parseFormat.format(ayer.getTime()));

            if (fecDateParse.equals(fecActual)) {
                fec = "HOY";
            } else if ((fecDateHoraParse.after(ayerInicio) || fecDateHoraParse.equals(ayerInicio)) && (fecDateHoraParse.before(ayerFinal) || fecDateHoraParse.equals(ayerFinal))) {
                fec = "AYER";
            } else {
                fec = fechaFormat.format(fecDateHoraParse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ServiciosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_servicio, viewGroup, false);
        ServiciosViewHolder pvh = new ServiciosViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ServiciosViewHolder personViewHolder, int i) {
        personViewHolder.txtTitulo.setText(servicio.get(i).nombre);
        if (servicio.get(i).foto.length() > 0)
            Picasso.with(context).load(servicio.get(i).foto).into(personViewHolder.fotoServicio);
        final int pox = i;
        personViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent it = new Intent(context, Servicios_Detalle.class);
                it.putExtra("data", servicio.get(pox).data);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });


    }

    @Override
    public int getItemCount() {
        return servicio.size();
    }

    public static class ServiciosViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        MaterialRippleLayout btn;
        ImageView fotoServicio;
        TextView txtTitulo;


        ServiciosViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            fotoServicio = (ImageView) itemView.findViewById(R.id.fotoServicio);
            btn = (MaterialRippleLayout) itemView.findViewById(R.id.btn_gal);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
        }
    }


}
