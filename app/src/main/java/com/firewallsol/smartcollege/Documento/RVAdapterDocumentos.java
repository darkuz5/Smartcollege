package com.firewallsol.smartcollege.Documento;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firewallsol.smartcollege.R;

import java.util.List;


public class RVAdapterDocumentos extends RecyclerView.Adapter<RVAdapterDocumentos.DocumentosViewHolder> {
    public String bandera = "x";
    Context context;
    List<Documento> documento;

    public RVAdapterDocumentos(List<Documento> documento, Context context) {
        this.documento = documento;
        this.context = context;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public DocumentosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_documento, viewGroup, false);
        DocumentosViewHolder pvh = new DocumentosViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final DocumentosViewHolder personViewHolder, int i) {
        personViewHolder.txtTitulo.setText(documento.get(i).titulo);
        final int pox = i;
        personViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                String url = documento.get(pox).url;
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setData(Uri.parse(url));
                activity.startActivity(it);
                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

            }
        });


    }

    @Override
    public int getItemCount() {
        return documento.size();
    }

    public static class DocumentosViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        MaterialRippleLayout btn;
        TextView txtTitulo;


        DocumentosViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            btn = (MaterialRippleLayout) itemView.findViewById(R.id.btn_gal);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
        }
    }


}
