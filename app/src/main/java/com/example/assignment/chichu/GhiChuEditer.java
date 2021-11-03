package com.example.assignment.chichu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment.FragmentThuChi.Tab_GhiChu_Fragment;
import com.example.assignment.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class GhiChuEditer extends AppCompatActivity {
    int noteID;
    private ImageButton btnSpeak;
    private EditText editText;
    protected static final int RESULT_SPEECH = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghi_chu_editer);
        getSupportActionBar().setTitle("GHI CHÚ THU CHI");

        btnSpeak = findViewById(R.id.micghichu);

        editText = findViewById(R.id.editText);
        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteID", -1);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    editText.setText("");
                }catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if(noteID != -1)
        {
            editText.setText(Tab_GhiChu_Fragment.notes.get(noteID));
        }

        else
        {
            Tab_GhiChu_Fragment.notes.add("Chưa có tiêu đề, xin mời nhập tại đây...");                // as initially, the note is empty
            noteID = Tab_GhiChu_Fragment.notes.size() - 1;
            Tab_GhiChu_Fragment.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Tab_GhiChu_Fragment.notes.set(noteID, "Ngày: "+ day + "/" + month + "/" + year+" Nội dung: "+s);
                Tab_GhiChu_Fragment.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.tanay.thunderbird.deathnote", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<>(Tab_GhiChu_Fragment.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> text =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(text.get(0));
                }
                break;
        }
    }
}