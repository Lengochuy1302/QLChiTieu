package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class BlankFragment4 extends Fragment {
    private Button btnGetStart;
    private View mView;
    private SharedPreferences prefs;
    public BlankFragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_intro_four, container, false);
        btnGetStart = mView.findViewById(R.id.btnStart);
        btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(getActivity(), MainActivity.class);
             getActivity().startActivity(intent);
            }
        });


        return mView;
    }
}