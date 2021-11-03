package com.example.assignment.FragmentThuChi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.chichu.GhiChuEditer;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashSet;

public class Tab_GhiChu_Fragment extends Fragment {
    FloatingActionMenu addfolde;
    View view;
    private String linkdatabase;
    public static ArrayList<String> notes = new ArrayList<String>();
    public static ArrayAdapter<String> arrayAdapter;
    public Tab_GhiChu_Fragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab__ghi_chu_, container, false);
        addfolde = view.findViewById(R.id.addnote);
        addfolde.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GhiChuEditer.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView)view.findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.tanay.thunderbird.deathnote", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>)sharedPreferences.getStringSet("notes", null);

        if(set == null || set.size() == 0)
        {

        }

        else
        {
            notes = new ArrayList<>(set);         // to bring all the already stored data in the set to the notes ArrayList
        }



        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i=new Intent(getActivity(),GhiChuEditer.class);
                i.putExtra("noteID", position);
                getActivity().startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                new AlertDialog.Builder(getContext())                   // we can't use getApplicationContext() here as we want the activity to be the context, not the application
                        .setTitle("Thông báo")
                        .setMessage("Có chắc bạn muốn xóa nó không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)                        // to remove the selected note once "Yes" is pressed
                            {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.tanay.thunderbird.deathnote", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })

                        .setNegativeButton("Hủy", null)
                        .show();

                return true;               // this was initially false but we change it to true as if false, the method assumes that we want to do a short click after the long click as well
            }
        });


        return view;
    }
}