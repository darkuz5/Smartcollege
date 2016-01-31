package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Avisos extends Fragment   implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AutoScrollViewPager pager;
    public static ArrayList<ItemsInicio> arrayInicio;

    private Activity activity;
    private View root;
    LinearLayout slider;
    LinearLayout otros;
    private SliderLayout mDemoSlider;
    HashMap<String,String> url_maps;
    ArrayList<String> datax;




    public Avisos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Avisos.
     */
    // TODO: Rename and change types and number of parameters
    public static Avisos newInstance(String param1, String param2) {
        Avisos fragment = new Avisos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_avisos, container, false);
       // pager = (AutoScrollViewPager) root.findViewById(R.id.viewPager);

        mDemoSlider = (SliderLayout) root.findViewById(R.id.slider);
        otros = (LinearLayout) root.findViewById(R.id.otros);
        String jsonRead = MainActivity.contenido_avisos;
        url_maps = new HashMap<String, String>();
        datax = new ArrayList<String>();

        Log.e("json", jsonRead);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonRead);


            if (jsonObject.has("avisos")) {
                JSONArray array = jsonObject.getJSONArray("avisos");
                for (int i=1; i<array.length(); i++){
                    final JSONObject c = array.getJSONObject(i);
                    if (i < 4){

                        url_maps.put(c.getString("titulo"), c.getString("foto"));
                        datax.add(c.toString());

                        /*View aviso1 = inflater.inflate(R.layout.inicio_portada_item_adapter,null);
                        ((TextView) aviso1.findViewById(R.id.tvTitulo)).setText(c.getString("titulo"));
                        ImageView fotogde = (ImageView) aviso1.findViewById(R.id.imgImagen);
                        Picasso.with(activity).load(c.getString("foto")).placeholder(R.drawable.logosc).into(fotogde);

                        aviso1.findViewById(R.id.btn_aviso).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(getContext(), DetalleAviso.class);
                                it.putExtra("datos",c.toString());
                                startActivity(it);
                            }
                        });


                        slider.addView(aviso1);*/

                    } else {

                        View aviso2 = inflater.inflate(R.layout.adapter_item_avisos,null);
                        ((TextView) aviso2.findViewById(R.id.txtTitulo)).setText(c.getString("titulo"));
                        ((TextView) aviso2.findViewById(R.id.txtFecha)).setText(calcularFechaFull(c.getString("fecha")));
                        ((TextView) aviso2.findViewById(R.id.txtHace)).setText(diferenciaDias(c.getString("fecha") + " " + c.getString("hora")));
                        aviso2.findViewById(R.id.btn_aviso).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(getContext(), DetalleAviso.class);
                                it.putExtra("datos", c.toString());
                                startActivity(it);
                                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                            }
                        });
                        String texto = c.getString("texto");
                        if (texto.length()>100){
                            texto = texto.substring(100)+"...";
                        }
                                ((TextView) aviso2.findViewById(R.id.txtResumen)).setText(texto);
                        ImageView fotogde = (ImageView) aviso2.findViewById(R.id.imgAlerta);
                        switch (c.getString("tipo")){
                            case "1":
                                Picasso.with(activity).load(R.drawable.alertaamarilla).into(fotogde);
                                break;
                            case "2":
                                Picasso.with(activity).load(R.drawable.alertanaranja).into(fotogde);
                                break;
                            case "3":
                                Picasso.with(activity).load(R.drawable.alertaroja).into(fotogde);
                                break;
                            default:
                                Picasso.with(activity).load(R.drawable.alertagris).into(fotogde);
                                break;
                        }



                        otros.addView(aviso2);
                    }
                }
            }




            Integer c = datax.size();
            for(String name : url_maps.keySet()){
                c--;
                TextSliderView textSliderView = new TextSliderView(getActivity().getApplicationContext());
                // initialize a SliderLayout
                final int xc = c;
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {

                                Intent it = new Intent(getContext(), DetalleAviso.class);
                                it.putExtra("datos",datax.get(xc));
                                startActivity(it);
                                activity.overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);
                            }
                        });

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                mDemoSlider.addSlider(textSliderView);
            }


            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(5000);
            mDemoSlider.setCustomIndicator((PagerIndicator) root.findViewById(R.id.custom_indicator));
            mDemoSlider.addOnPageChangeListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return root;
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

        SimpleDateFormat fechaFormat = new SimpleDateFormat("dd ' de ' MMMM ' de ' yyyy");
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

    public static String diferenciaDias(String fecha) {
        String fec = "";
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date fecDateHoraParse = parseFormat.parse(fecha);
            Calendar today = Calendar.getInstance();
            Date fecActual = parseFormat.parse(parseFormat.format(today.getTime()));
            Date fecDateParse = parseFormat.parse(fecha);

            Date fpast = fecDateHoraParse; // June 20th, 2010
            Date ftoday = fecActual; // July 24th
            int days = Days.daysBetween(new DateTime(fpast), new DateTime(ftoday)).getDays(); // => 34
            int horas = Hours.hoursBetween(new DateTime(fpast), new DateTime(ftoday)).getHours();

            if (horas < 24){
                fec = "Hace "+horas+" horas";
            } else {
                fec = "Hace "+days+" días";
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fec;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
