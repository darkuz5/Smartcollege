package com.firewallsol.smartcollege.Gaceta;

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
import com.firewallsol.smartcollege.Gacetas_Detalle;
import com.firewallsol.smartcollege.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.GacetaViewHolder> {
    public String bandera = "x";
    Context context;
    List<Gaceta> gacetas;

    public RVAdapter(List<Gaceta> gacetas, Context context) {
        this.gacetas = gacetas;
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
    public GacetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_gaceta, viewGroup, false);
        GacetaViewHolder pvh = new GacetaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final GacetaViewHolder personViewHolder, int i) {
        personViewHolder.txtNombre.setText(gacetas.get(i).tutor);
        personViewHolder.txtFecha.setText(calcularFechaFull(gacetas.get(i).fecha));
        personViewHolder.txtTitulo.setText(gacetas.get(i).titulo);
        personViewHolder.txtResumen.setText(gacetas.get(i).texto);
        Picasso.with(context).load(gacetas.get(i).avatar_tutor).into(personViewHolder.fotoperfil);
        if (gacetas.get(i).url.length() > 2)
            personViewHolder.imgAlerta.setVisibility(View.VISIBLE);
        else
            personViewHolder.imgAlerta.setVisibility(View.GONE);
        final int pox = i;
        personViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent it = new Intent(context, Gacetas_Detalle.class);
                it.putExtra("id", gacetas.get(pox).id);
                it.putExtra("tutor", gacetas.get(pox).tutor);
                it.putExtra("avatar_tutor", gacetas.get(pox).avatar_tutor);
                it.putExtra("titulo", gacetas.get(pox).titulo);
                it.putExtra("texto", gacetas.get(pox).texto);
                it.putExtra("fecha", gacetas.get(pox).fecha);
                it.putExtra("foto", gacetas.get(pox).foto);
                it.putExtra("url", gacetas.get(pox).url);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });


    }

    @Override
    public int getItemCount() {
        return gacetas.size();
    }

    public static class GacetaViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        MaterialRippleLayout btn;
        CircleImageView fotoperfil;
        TextView txtNombre;
        TextView txtFecha;
        ImageView imgAlerta;
        TextView txtTitulo;
        TextView txtResumen;


        GacetaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            btn = (MaterialRippleLayout) itemView.findViewById(R.id.btn_aviso);
            fotoperfil = (CircleImageView) itemView.findViewById(R.id.fotoperfil);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            imgAlerta = (ImageView) itemView.findViewById(R.id.imgAlerta);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtResumen = (TextView) itemView.findViewById(R.id.txtResumen);
        }
    }


}
