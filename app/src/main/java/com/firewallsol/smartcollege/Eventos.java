package com.firewallsol.smartcollege;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firewallsol.smartcollege.Adaptadores.Items.ItemsInicio;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class Eventos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<ItemsInicio> arrayInicio;
    public static View root;
    public static FragmentManager supportFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoScrollViewPager pager;
    private Activity activity;


    public Eventos() {
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
    public static Eventos newInstance(String param1, String param2) {
        Eventos fragment = new Eventos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void showFragmentView(int fragmentId) {
        Fragment f = null;
        switch (fragmentId) {
            case 0:
                f = Eventos_Calendar.newInstance();
                break;
            case 20:
                f = Eventos_Lista.newInstance();
                break;

        }

        //f = Loader.newInstance("", "");

        if (f != null) {
            supportFragment.beginTransaction().replace(R.id.content_frame_eventos, f).commit();
        }
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
        activity = getActivity();
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_eventos, container, false);
        supportFragment = getChildFragmentManager();
        showFragmentView(0);

        root.findViewById(R.id.proxEventos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentView(20);
            }
        });
        root.findViewById(R.id.calendario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentView(0);
            }
        });


        return root;
    }


}
