package com.firewallsol.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;
import com.firewallsol.smartcollege.Funciones.jSONFunciones;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Eventos_Calendar extends Fragment {

    final static Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    public static CompactCalendarView compactCalendarView;
    public static int azul;
    private static Map<Date, List<Booking>> bookings = new HashMap<>();
    private Activity activity;
    private View root;
    String eventos;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());


    public Eventos_Calendar() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Eventos_Calendar newInstance() {
        Eventos_Calendar fragment = new Eventos_Calendar();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private static void addEvents2(CompactCalendarView compactCalendarView, Date fecha) {
        Log.i("XXX", Calendar.DATE + "");
        // currentCalender.add(fecha,1);

        compactCalendarView.addEvent(new CalendarDayEvent(fecha.getTime(), azul), false);
        bookings.put(fecha, createBookings());


    }




    private static List<Booking> createBookings() {
        return Arrays.asList();
    }

    private static Date ConvertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_eventoscalendar, container, false);
        activity = getActivity();


        compactCalendarView = (CompactCalendarView) root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        final TextView mes = (TextView) root.findViewById(R.id.mes);
        mes.setText(capitalize(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth())));

        final ImageButton showPreviousMonthBut = (ImageButton) root.findViewById(R.id.prev_button);
        final ImageButton showNextMonthBut = (ImageButton) root.findViewById(R.id.next_button);


        eventos = MainActivity.eventos;
        //minflater = inflater;
        azul = Color.parseColor(MainActivity.color);
        if (TextUtils.isEmpty(eventos)){
            /** Descargar Eventos **/
            new DescargarEventos().execute();
        } else {
            try {
                llenado(eventos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Booking> bookingsFromMap = bookings.get(dateClicked);
                Log.d("MainActivity", "inside onclick " + dateClicked);
                if (bookingsFromMap != null) {
                    Log.d("MainActivity", bookingsFromMap.toString());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    System.out.format("%30s %s\n", "yyyy-MM-dd", sdf.format(dateClicked));

                    Intent it = new Intent(getActivity(),Eventos_Lista_Calendar.class);
                    it.putExtra("fecha",sdf.format(dateClicked));
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_left, android.R.anim.fade_out);

                   /* mutableBookings.clear();
                    for(Booking booking : bookingsFromMap){
                        mutableBookings.add(booking.title);
                    }*/


                    // below will remove events
                    // compactCalendarView.removeEvent(new CalendarDayEvent(dateClicked.getTime(), Color.argb(255, 169, 68, 65)), true);
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mes.setText(capitalize(dateFormatForMonth.format(firstDayOfNewMonth)));
            }
        });


        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showPreviousMonth();
            }
        });

        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showNextMonth();
            }
        });

        return root;
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public class Booking {
        private String title;
        private Date date;

        public Booking(String title, Date date) {
            this.title = title;
            this.date = date;
        }


        @Override
        public String toString() {
            return "Booking{" +
                    "title='" + title + '\'' +
                    ", date=" + date +
                    '}';
        }

    }
    class DescargarEventos extends AsyncTask<Void, Void, String> {

        String url;
        ArrayList<ItemsInicio> arrayInicio;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = activity.getString(R.string.getEventos);
            arrayInicio = new ArrayList<>();

        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonRead = "";
            try {
                List<NameValuePair> paramsSend = new ArrayList<>();
                paramsSend.add(new BasicNameValuePair("id_escuela", MainActivity.idEscuela));
                jSONFunciones json = new jSONFunciones();
                jsonRead = json.jSONRead(url, jSONFunciones.POST, paramsSend);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonRead;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                llenado(aVoid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    public  static void llenado(String datos) throws JSONException {
        Log.i("Eventos", datos);

        JSONObject jsonObj = new JSONObject(datos);
        if (jsonObj.has("eventos")) {
            JSONArray evento = jsonObj.getJSONArray("eventos");
            for (int i = 0; i < evento.length(); i++) {
                JSONObject c = evento.getJSONObject(i);
                Date fecha = ConvertToDate(c.getString("fecha"));
                addEvents2(compactCalendarView, fecha);
            }
        }
    }


}
