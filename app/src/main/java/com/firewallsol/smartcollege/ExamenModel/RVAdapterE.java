package com.firewallsol.smartcollege.ExamenModel;

/**
 * Created by edfortis("eduardo loyo") on 02/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.ExamenDetalle;
import com.firewallsol.smartcollege.MainActivity;
import com.firewallsol.smartcollege.R;
import com.firewallsol.smartcollege.TareaDetalle;

import java.util.List;

public class RVAdapterE extends RecyclerView.Adapter<RVAdapterE.ExamenViewHolder> {

    Context context;
    List<ExamenModel> examenes;


    public RVAdapterE(List<ExamenModel> examenes, Context context) {
        this.examenes = examenes;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ExamenViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_examen, viewGroup, false);
        ExamenViewHolder pvh = new ExamenViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ExamenViewHolder examenViewHolder, int i) {


        examenViewHolder.txtNombre.setText(examenes.get(i).nombre);
        examenViewHolder.txtHora.setText(examenes.get(i).hora);
        examenViewHolder.txtFecha.setText(examenes.get(i).fecha);
        final int pox = i;
        examenViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent it = new Intent(context, ExamenDetalle.class);
                it.putExtra("id_examen",examenes.get(pox).id);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            }
        });
        examenViewHolder.imgCalendario.setColorFilter(Color.parseColor(MainActivity.color));
        examenViewHolder.imgReloj.setColorFilter(Color.parseColor(MainActivity.color));


    }

    @Override
    public int getItemCount() {
        return examenes.size();
    }


    public static class ExamenViewHolder extends RecyclerView.ViewHolder {
        //contenedores
        CardView cv;
        MaterialRippleLayout btn;
        //datos
        TextView txtNombre;
        TextView txtFecha;
        TextView txtHora;
        ImageView imgCalendario;
        ImageView imgReloj;





        ExamenViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            btn = (MaterialRippleLayout) itemView.findViewById(R.id.btn_examen);
            txtNombre = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            txtHora = (TextView) itemView.findViewById(R.id.txtHora);
            imgCalendario = (ImageView) itemView.findViewById(R.id.imgCalendario);
            imgReloj = (ImageView) itemView.findViewById(R.id.imgReloj);

        }
    }
}
