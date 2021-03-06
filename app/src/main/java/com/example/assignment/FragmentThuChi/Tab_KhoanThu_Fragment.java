package com.example.assignment.FragmentThuChi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.AdapterThuChi.KhoanThuAdapter;
import com.example.assignment.AdapterThuChi.LoaiThuAdapter;
import com.example.assignment.ArrayListThuChi.GiaoDich;
import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangloaithuchi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangthuchi;
import com.example.assignment.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class Tab_KhoanThu_Fragment extends Fragment {
    private static final int RESULT_OK = 12;
//    private String linkdatabase = "https://huyassignment-default-rtdb.firebaseio.com/";
private String linkdatabase;
    private DatabaseReference reference;
    View view;
    private RecyclerView rcv;
    private ArrayList<GiaoDich> list = new ArrayList<>();
    private ArrayList<String> listTC = new ArrayList<>();
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private chucnangthuchi chucnangthuchi;
    private SpeechRecognizer speechRecognizer;
    Intent mSpeechRecordIntent;
    KhoanThuAdapter adapter;
    LoaiThuAdapter adapterlt;
    TextView btnlocloai, locngaybatdau;
    private ArrayList<String> ngaythu = new ArrayList<>();
    private ArrayList<Integer> thunhap = new ArrayList<>();
    private ArrayList<String> listthutheongay = new ArrayList<>();


    FloatingActionButton girdBtn,danhsachBtn,addBtn, searchLoaiTC, locTC;
    protected static final int RESULT_SPEECH = 10;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(list, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    private int RecordAudioRequestCode = 123;

    public Tab_KhoanThu_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listview_khoanthu, container, false);

        linkdatabase = getResources().getString(R.string.linkreutime);
        rcv = view.findViewById(R.id.rcv_KhoanThu);
        addBtn = view.findViewById(R.id.addBtn);
        girdBtn = view.findViewById(R.id.girdBtn);
        danhsachBtn = view.findViewById(R.id.danhsachBtn);
        searchLoaiTC = view.findViewById(R.id.searchLoaiTC);
        btnlocloai = view.findViewById(R.id.them_danshach);
        locngaybatdau = view.findViewById(R.id.loc_ngay_bat_dau);
        chucnangthuchi = new chucnangthuchi(getActivity());

        adapter = new KhoanThuAdapter();
        adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(linearLayoutManager);
        getLisviewDatabasefirebase();
        list = new ArrayList<>();
        adapter.setData(list);
        rcv.setAdapter(adapter);


        girdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                rcv.setLayoutManager(gridLayoutManager);
                adapter = new KhoanThuAdapter(getActivity(), R.layout.gridview_item, list);
                rcv.setAdapter(adapter);
            }
        });
        danhsachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rcv.setLayoutManager(layoutManager);
                adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item, list);
                rcv.setAdapter(adapter);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcv);

        //L???c theo ng??y
        locngaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Ch???n ng??y b???t ?????u!", Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                int ngaydb = calendar.get(Calendar.DAY_OF_MONTH);
                int thang = calendar.get(Calendar.MONTH);
                int nam = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String ngayw =  simpleDateFormat.format(calendar.getTime());
                        Toast.makeText(getActivity(), "Ch???n ng??y k???t th??c!", Toast.LENGTH_SHORT).show();
                        Calendar calendarkt = Calendar.getInstance();
                        int ngay = calendarkt.get(Calendar.DAY_OF_MONTH);
                        int thang = calendarkt.get(Calendar.MONTH);
                        int nam = calendarkt.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialogkt = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendarkt.set(year,month,dayOfMonth);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String ngaysau =  simpleDateFormat.format(calendarkt.getTime());
                                locngaybatdau.setText(ngayw+" - "+simpleDateFormat.format(calendarkt.getTime()));
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                reference = FirebaseDatabase.getInstance(linkdatabase)
                                        .getReference("users").child(user.getUid()).child("GiaoDich");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int tongThu = 0, tongChi = 0, tong = 0;
                                        for (DataSnapshot chilSnapshot : snapshot.getChildren()) {
                                            String magd = chilSnapshot.child("maGd").getValue(String.class);
                                            if (magd.contains("ThuNhap")) {
                                                ngaythu.clear();
                                                String ngay = chilSnapshot.child("ngayGd").getValue(String.class);
                                                ngaythu.add(ngay);
                                                for (int i = 0; i < ngaythu.size(); i++) {
                                                    try {
                                                        if (ngaythu.get(i).compareTo(ngayw) >= 0 && ngaythu.get(i).compareTo(ngaysau) <= 0) {
                                                            String listthutn = chilSnapshot.child("maGd").getValue(String.class);
                                                            listthutheongay.clear();
                                                            listthutheongay.add(listthutn);
                                                            GiaoDich giaoDich = snapshot.child(magd).getValue(GiaoDich.class);
                                                            list.add(giaoDich);
                                                            adapter.notifyDataSetChanged();

                                                            if (list.size() == 0) {
                                                                Toast.makeText(getActivity(), "Kh??ng c?? d??? li???u!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(getActivity(), "L???c th??nh c??ng!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }


                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }, nam,thang,ngay);
                        datePickerDialogkt.show();
                    }
                }, nam,thang,ngaydb);
                datePickerDialog.show();

            }
        });


        //L???c theo lo???i
        btnlocloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.loctc);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final Spinner locloai = dialog.findViewById(R.id.locLoai);


                //????? d??? li???u v??o spinner
                final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.listview_item_loaitc, listTC);
                listTC = new ArrayList<>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }
                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("ThuChi");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for (DataSnapshot chilSnapshot:snapshot.getChildren()) {
                            String namethuchi = chilSnapshot.child("tenKhoan").getValue(String.class);
                            int maloai = chilSnapshot.child("loaiKhoan").getValue(Integer.class);
                            if ( maloai == 0) {
                                listTC.add(namethuchi);
                            }

                        }

                        final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.spiner_loaithuchi, listTC);
                        locloai.setAdapter(sp);
                    }


                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });



                //L???c
                final Button btnloc = dialog.findViewById(R.id.loc);
                btnloc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String spinner_data = locloai.getSelectedItem().toString();
                        btnlocloai.setText(spinner_data);
                        list.clear();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            return;
                        }
                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                        reference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                                if (giaoDich != null) {
                                    if (giaoDich.getMaGd().contains("ThuNhap")) {
                                        if (giaoDich.getMaKhoan().contains(spinner_data)) {
                                            list.add(giaoDich);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        dialog.dismiss();
                    }
                });

                //H???y l???c
                final Button btnhuy = dialog.findViewById(R.id.huyloc);
                btnhuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        searchLoaiTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timkiem_thuchi);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText edt_TimLoaiChi = dialog.findViewById(R.id.tim_loai_thu);
                Button xoa = dialog.findViewById(R.id.xoaTextLT);
                final Button them = dialog.findViewById(R.id.btnThemLT);
                final TextView title = dialog.findViewById(R.id.titleTim);
                title.setText("T??M KHO???N THU NH???P");
                edt_TimLoaiChi.setHint("Nh???p kho???n thu nh???p c???n t??m...");

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                rcv.addItemDecoration(dividerItemDecoration);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(rcv);

                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String themText = edt_TimLoaiChi.getText().toString();
                        if (themText.equals("")) {
                            edt_TimLoaiChi.setError("Ch??a nh???p g?? c???!");
                            return;
                        }

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            return;
                        }
                        list.clear();
                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                        reference.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                                if (giaoDich != null) {
                                    if (giaoDich.getMaGd().contains("ThuNhap")) {
                                        if (giaoDich.getMoTaGd().contains(themText)) {
                                            list.add(giaoDich);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {
                            }

                            @Override
                            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        dialog.dismiss();
                    }
                });

                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.clear();
                        getLisviewDatabasefirebase();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.them_khoan_thuchi);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                final EditText moTaGd = dialog.findViewById(R.id.them_mota_gd);
                final TextView ngayGd = dialog.findViewById(R.id.them_ngay_gd);
                final EditText tienGd = dialog.findViewById(R.id.them_tien_gd);
                final Spinner spLoaiGd = dialog.findViewById(R.id.spLoaiGd);
                final TextView title = dialog.findViewById(R.id.titleThemKhoan);
                final TextView ghichuGd = dialog.findViewById(R.id.them_ghichu_gd);
                final Button xoa = dialog.findViewById(R.id.xoaTextGD);
                final Button them = dialog.findViewById(R.id.btnThemGD);
                them.setEnabled(true);
                final Button voice = dialog.findViewById(R.id.voice_to_text);



                //Set ti??u ?????
                title.setText("TH??M KHO???N THU");
                String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                ngayGd.setText(currentDate);

                //Khi nh???n mic hi???n m???c thu ??m gi???ng n??i
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
                            ghichuGd.setText(text.get(0));
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {

                    }
                });


                voice.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_UP:
                                speechRecognizer.stopListening();
                                ghichuGd.setHint("Nh???n gi??? bi???u t?????ng mic ????? n??i...");
                                break;

                            case MotionEvent.ACTION_DOWN:
                                ghichuGd.setText("");
                                ghichuGd.setHint("Khi n??i xong h??y th??? tay! ??ang nghe...");
                                speechRecognizer.startListening(mSpeechRecordIntent);
                                break;
                        }
                        return false;
                    }
                });

                //Khi nh???n ng??y hi???n l??n l???a ch??? ng??y
                ngayGd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
                        int thang = calendar.get(Calendar.MONTH);
                        int nam = calendar.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year,month,dayOfMonth);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                ngayGd.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, nam,thang,ngay);
                        datePickerDialog.show();
                    }
                });

                //????? d??? li???u v??o spinner
                listTC = new ArrayList<>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }
                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("ThuChi");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for (DataSnapshot chilSnapshot:snapshot.getChildren()) {
                            String namethuchi = chilSnapshot.child("tenKhoan").getValue(String.class);
                            int maloai = chilSnapshot.child("loaiKhoan").getValue(Integer.class);
                            if ( maloai == 0) {
                                listTC.add(namethuchi);
                            }

                        }

                        final ArrayAdapter sp = new ArrayAdapter(getActivity(), R.layout.spiner_loaithuchi, listTC);
                        spLoaiGd.setAdapter(sp);
                    }


                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });


                //Khi nh???n n??t x??a
                xoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        rcv.setLayoutManager(layoutManager);
                        adapter = new KhoanThuAdapter(getActivity(), R.layout.listview_item,list);
                        rcv.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });
                //Khi nh???n n??t Th??m
                them.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listTC.size() == 0) {
                            dialog.dismiss();
                            TextView tieude;
                            Button themltc;
                            Dialog dialoga = new Dialog(getActivity());
                            dialoga.setContentView(R.layout.popupchuacodullieu);
                            dialoga.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            tieude = dialoga.findViewById(R.id.txttieude);
                            themltc = dialoga.findViewById(R.id.btn_yes);
                            tieude.setText("C???n ??t nh???t m???t lo???i thu nh???p! H??y th??m ngay!");
                            themltc.setOnClickListener(v1 -> {
                                dialoga.dismiss();
                            });
                            dialoga.show();
                            return;
                        }

                        String mota = moTaGd.getText().toString();
                        String ngay = ngayGd.getText().toString();
                        String tien = tienGd.getText().toString();
                        String ghichu = ghichuGd.getText().toString();
                        String ma = spLoaiGd.getSelectedItem().toString();

                        //Check l???i
                        if (mota.isEmpty() || tien.isEmpty()) {
                            if (mota.isEmpty()) {
                                moTaGd.setError("Kh??ng ???????c ????? tr???ng!");
                            }
                            if (tien.isEmpty()) {
                                tienGd.setError("Kh??ng ???????c ????? tr???ng!");
                            }
                        } else {
                            try {
                                them.setEnabled(false);
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid());
                                Calendar calendar = Calendar.getInstance();
                                GiaoDich giaoDich = new GiaoDich();
                                giaoDich.setMaGd("ThuNhap "+ calendar.getTimeInMillis());
                                giaoDich.setMoTaGd(mota);
                                giaoDich.setNgayGd(ngay);
                                giaoDich.setSoTien(Integer.parseInt(tien));
                                giaoDich.setMaKhoan(ma);
                                giaoDich.setGhiChuGd(ghichu);
                                reference.child("GiaoDich").child("ThuNhap "+ calendar.getTimeInMillis()).setValue(giaoDich);

                                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Th??ng B??o","??ang th??m...");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            dialog.dismiss();
                                        }
                                    },1000);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                    }
                });

                dialog.show();
            }
        });
        return view;


    }

    private void getLisviewDatabasefirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                if (giaoDich != null) {
                    if (giaoDich.getMaGd().contains("ThuNhap")) {
                        list.add(giaoDich);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {
                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                if (giaoDich == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (giaoDich.getMaGd() == list.get(i).getMaGd()) {
                        list.set(i, giaoDich);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {
                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                if (giaoDich == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (giaoDich.getMaGd() == list.get(i).getMaGd()) {
                        list.remove(list.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
