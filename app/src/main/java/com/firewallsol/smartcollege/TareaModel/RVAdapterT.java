package com.firewallsol.smartcollege.TareaModel;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.Alumno;
import com.firewallsol.smartcollege.Gacetas_Detalle;
import com.firewallsol.smartcollege.MainActivity;
import com.firewallsol.smartcollege.R;
import com.firewallsol.smartcollege.ReporteDetalle;
import com.firewallsol.smartcollege.Tarea;
import com.firewallsol.smartcollege.TareaDetalle;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RVAdapterT extends RecyclerView.Adapter<RVAdapterT.TareaViewHolder> {

    Context context;
    List<TareaModel> tareas;


    public RVAdapterT(List<TareaModel> tareas, Context context) {
        this.tareas = tareas;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TareaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_tarea, viewGroup, false);
        TareaViewHolder pvh = new TareaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TareaViewHolder tareaViewHolder, int i) {


        tareaViewHolder.txtMateria.setText(tareas.get(i).materia);
        tareaViewHolder.txtTitulo.setText(tareas.get(i).titulo);
        tareaViewHolder.txtFecha.setText(tareas.get(i).fecha);
        tareaViewHolder.txtTexto.setText(tareas.get(i).descripcion);
        tareaViewHolder.img.setColorFilter(Color.parseColor(MainActivity.color));
        final int pox = i;
        tareaViewHolder.btn_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent it = new Intent(context, TareaDetalle.class);
                it.putExtra("materia", tareas.get(pox).materia);
                it.putExtra("titulo", tareas.get(pox).titulo);
                it.putExtra("fecha", tareas.get(pox).fecha);
                it.putExtra("texto",tareas.get(pox).descripcion);
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
            }
        });



    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }


    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        //contenedores
        CardView cv;
        MaterialRippleLayout btn;
        //datos
        TextView txtMateria;
        TextView txtTitulo;
        TextView txtFecha;
        TextView txtTexto;
        ImageView img;
        MaterialRippleLayout btn_tarea;



        TareaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            btn = (MaterialRippleLayout) itemView.findViewById(R.id.btn_aviso);
            txtMateria = (TextView) itemView.findViewById(R.id.txtMateria);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtTexto = (TextView) itemView.findViewById(R.id.txtTexto);
            img = (ImageView) itemView.findViewById(R.id.imgAlerta);
            btn_tarea = (MaterialRippleLayout) itemView.findViewById(R.id.btn_aviso);
        }
    }
}
