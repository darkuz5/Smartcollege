package com.firewallsol.smartcollege.TareaModel;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.R;

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
        tareaViewHolder.txtFecha.setText(calcularFecha(tareas.get(i).fecha));
        tareaViewHolder.txtTitulo.setText(tareas.get(i).descripcion);



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



        TareaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            btn = (MaterialRippleLayout) itemView.findViewById(R.id.btn_aviso);

            txtMateria = (TextView) itemView.findViewById(R.id.txtMateria);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtTexto = (TextView) itemView.findViewById(R.id.txtTexto);
        }
    }


    private static String calcularFecha(String fecha) {
        String fec = "";

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

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

            if(fecDateParse.equals(fecActual)) {
                fec = horaFormat.format(fecDateHoraParse);
            } else if ((fecDateHoraParse.after(ayerInicio) || fecDateHoraParse.equals(ayerInicio)) && (fecDateHoraParse.before(ayerFinal) || fecDateHoraParse.equals(ayerFinal))){
                fec = "AYER";
            } else {
                fec = fechaFormat.format(fecDateHoraParse);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
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

            if(fecDateParse.equals(fecActual)) {
                fec = "HOY";
            } else if ((fecDateHoraParse.after(ayerInicio) || fecDateHoraParse.equals(ayerInicio)) && (fecDateHoraParse.before(ayerFinal) || fecDateHoraParse.equals(ayerFinal))){
                fec = "AYER";
            } else {
                fec = fechaFormat.format(fecDateHoraParse);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return fec;
    }



}
