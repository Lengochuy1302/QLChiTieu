package com.example.assignment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhanHoiNguoiDung#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhanHoiNguoiDung extends Fragment {
//    private String linkdatabase = "https://huyassignment-default-rtdb.firebaseio.com/";
    private String linkdatabase;
    private static final int RESULT_OK = 2;
    private TextView tennguoidung, gmailnguoidung;
    protected static final int RESULT_SPEECH = 1;
    private Button btngui;
    private EditText feedbacknguoidung;
    private Button micfeed;
    private FirebaseDatabase database;
    DatabaseReference ref;
    private SpeechRecognizer speechRecognizer;
    Intent mSpeechRecordIntent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhanHoiNguoiDung() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhanHoiNguoiDung.
     */
    // TODO: Rename and change types and number of parameters
    public static PhanHoiNguoiDung newInstance(String param1, String param2) {
        PhanHoiNguoiDung fragment = new PhanHoiNguoiDung();
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
        View view = inflater.inflate(R.layout.fragment_phan_hoi_nguoi_dung, container, false);
        linkdatabase = getResources().getString(R.string.linkreutime);
        tennguoidung = view.findViewById(R.id.getuser);
        gmailnguoidung = view.findViewById(R.id.emailget);
        feedbacknguoidung = view.findViewById(R.id.feedback);
        btngui = view.findViewById(R.id.btnguiykien);
        micfeed = view.findViewById(R.id.micfeedback);
        showAllUserData();

        //Khi nhấn mic hiện mục thu âm giọng nói
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        mSpeechRecordIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecordIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecordIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (text!=null){
                    feedbacknguoidung.setText(text.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        micfeed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        feedbacknguoidung.setHint("Nhấn giữ biểu tượng mic để nói...");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        feedbacknguoidung.setText("");
                        feedbacknguoidung.setHint("Khi nói xong hãy thả tay! Đang nghe...");
                        speechRecognizer.startListening(mSpeechRecordIntent);
                        break;
                }
                return false;
            }
        });



        btngui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser usera = FirebaseAuth.getInstance().getCurrentUser();
                if (usera == null) {
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat ngay = new SimpleDateFormat("dd");
                String strDate = ngay.format(calendar.getTime());
                SimpleDateFormat thang = new SimpleDateFormat("MM");
                String strMonth = thang.format(calendar.getTime());
                SimpleDateFormat nam = new SimpleDateFormat("yyyy");
                String strYear = nam.format(calendar.getTime());
                String noidung = feedbacknguoidung.getText().toString().trim();
                String gmailnguoi = gmailnguoidung.getText().toString().trim();
                String tennguoifeedback = tennguoidung.getText().toString().trim();

                if (noidung.equals("")) {
                    feedbacknguoidung.setError("Không được để trống!");
                    return;
                }
                Users user = new Users();
                user.setUserName(tennguoifeedback+" id "+calendar.getTimeInMillis());
                user.setEmail(gmailnguoi);
                user.setFeedback(noidung);


                database = FirebaseDatabase.getInstance(linkdatabase);
                ref = database.getReference("users").child(usera.getUid()).child("Feedback");
                ref.child(tennguoifeedback+" id "+calendar.getTimeInMillis()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Thông báo", "Đang gủi góp ý...");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                ref.child("Tên "+ tennguoifeedback+"    Ngày " + strDate + " tháng "+ strMonth +" năm "+ strYear +"    Mã "+calendar.getTimeInMillis()).setValue(user);
                                feedbacknguoidung.setText("");
                                feedbacknguoidung.setHint("Phần mềm quản lý thu chi xin chào, rất vui khi gặp bạn ở đây. Hãy đóng góp ý kiến cho mình ở đây nhé...");

                            }
                        }, 2000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            ;
        });
        return view;
    }


    private void showAllUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        tennguoidung.setText(name);
        gmailnguoidung.setText(email);

    }
}