package com.example.assignment.FragmentThuChi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.CircularGauge;
import com.anychart.charts.Pie;
import com.anychart.core.axes.Circular;
import com.anychart.core.cartesian.series.Box;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.data.Mapping;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;
import com.example.assignment.AdapterThuChi.KhoanChiAdapter;
import com.example.assignment.AdapterThuChi.KhoanThuAdapter;
import com.example.assignment.ArrayListThuChi.GiaoDich;
import com.example.assignment.Capnhatthongtinnguoidung;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangthuchi;
import com.example.assignment.Dangnhap;
import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


public class ThongKe_Fragment extends Fragment {
//    private String linkdatabase = "https://huyassignment-default-rtdb.firebaseio.com/";
    private String linkdatabase;
    private TextView tungay, denngay, thu, chi, conlai, thongbaochar;
    private Button btnShow;
    BarChart barChart;
    PieChart anyChartView;
    private DatabaseReference reference;
    private RecyclerView rcv,rct;
    private ArrayList<GiaoDich> list = new ArrayList<>();
    private ArrayList<GiaoDich> listthu = new ArrayList<>();
    private ArrayList<GiaoDich> listchi = new ArrayList<>();
    private ArrayList<String> listchitheongay = new ArrayList<String>();
    private ArrayList<String> listthutheongay = new ArrayList<>();
    KhoanThuAdapter adapter;
    KhoanChiAdapter adapterchi;
    private ArrayList<Integer> thunhap = new ArrayList<>();
    private ArrayList<Integer> chitieu = new ArrayList<>();
    private ArrayList<String> ngaythu = new ArrayList<>();
    private ArrayList<String> ngaychi = new ArrayList<>();
    String[] loaikhoan = {"Khoản thu", "Khoản chi", "Còn lại"};
    private ArrayList<Integer> tienkhoan = new ArrayList<>();
    private chucnangthuchi chucnangthuchi;
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    final NumberFormat fmd = new DecimalFormat("#,###");
    public ThongKe_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_ke_, container, false);
        linkdatabase = getResources().getString(R.string.linkreutime);
        tungay = view.findViewById(R.id.tungay);
        denngay = view.findViewById(R.id.denngay);
        thu = view.findViewById(R.id.tienThu);
        thongbaochar = view.findViewById(R.id.thongbaochar);
        chi = view.findViewById(R.id.tienChi);
        conlai = view.findViewById(R.id.tienConLai);
        btnShow = view.findViewById(R.id.btnShow);
        barChart = view.findViewById(R.id.barchart);
        anyChartView = view.findViewById(R.id.anycharview);
        anyChartView.setVisibility(View.GONE);
        rcv = view.findViewById(R.id.listchi);
        rct = view.findViewById(R.id.listthu);

        chucnangthuchi = new chucnangthuchi(getActivity());
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.toggleButton);

        //set data thu
        adapter = new KhoanThuAdapter();
        GridLayoutManager gridLayoutManagerthu = new GridLayoutManager(getActivity(), 1);
        rct.setLayoutManager(gridLayoutManagerthu);
        adapter = new KhoanThuAdapter(getActivity(), R.layout.gridview_item, listthu);
        getLisviewDatabasefirebase();
        listthu = new ArrayList<>();
        adapter.setData(listthu);
        rct.setAdapter(adapter);

        //set data chi
        adapterchi = new KhoanChiAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rcv.setLayoutManager(gridLayoutManager);
        adapterchi = new KhoanChiAdapter(getActivity(), R.layout.gridview_item, listchi);
        listchi = new ArrayList<>();
        adapterchi.setData(listchi);
        rcv.setAdapter(adapterchi);


        //Format dạng tiền
        final NumberFormat fm = new DecimalFormat("#,###");

        int tongThu = 0, tongChi = 0, tong = 0;

        //kết nối firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int tongThu = 0, tongChi = 0, tong = 0;
                    for (DataSnapshot chilSnapshot : snapshot.getChildren()) {
                            String magd = chilSnapshot.child("maGd").getValue(String.class);
                        if (magd.contains("ThuNhap")) {
                            thunhap.clear();
                            int tien = chilSnapshot.child("soTien").getValue(Integer.class);
                            thunhap.add(tien);
                            for (int i = 0; i < thunhap.size(); i++) {
                                tongThu += thunhap.get(i);
                            }
                        }

                        if (magd.contains("ChiTieu")) {
                            chitieu.clear();
                            int tien = chilSnapshot.child("soTien").getValue(Integer.class);
                            chitieu.add(tien);
                            for (int i = 0; i < chitieu.size(); i++) {
                                tongChi += Math.abs(chitieu.get(i));
                            }
                        }
                        tong = tongThu - tongChi;
                        thu.setText(fm.format(tongThu)+ " VND");
                        chi.setText(fm.format(tongChi)+ " VND");
                        conlai.setText(fm.format(tong)+ " VND");

                        //Bieu đồ cột
                        bieudotron(tongThu, tongChi, tong);
                        Bieudocot(tongThu, tongChi, tong);
                    }
                    tienkhoan.clear();
                    tienkhoan.add(tongThu);
                    tienkhoan.add(tongChi);
                    tienkhoan.add(tong);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bieudotron(tienkhoan.get(0), tienkhoan.get(1), tienkhoan.get(2));
            }
        }, 4000);




        //Khu chọn vào ngày
        tungay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarkt = Calendar.getInstance();
                int ngay = calendarkt.get(Calendar.DAY_OF_MONTH);
                int thang = calendarkt.get(Calendar.MONTH);
                int nam = calendarkt.get(Calendar.YEAR);
                DatePickerDialog datePickerDialogkt = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarkt.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        tungay.setText(simpleDateFormat.format(calendarkt.getTime()));

                    }
                }, nam,thang,ngay);
                datePickerDialogkt.show();
            }
        });

        denngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarkt = Calendar.getInstance();
                int ngay = calendarkt.get(Calendar.DAY_OF_MONTH);
                int thang = calendarkt.get(Calendar.MONTH);
                int nam = calendarkt.get(Calendar.YEAR);
                DatePickerDialog datePickerDialogkt = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarkt.set(year,month,dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        denngay.setText(simpleDateFormat.format(calendarkt.getTime()));

                    }
                }, nam,thang,ngay);
                datePickerDialogkt.show();
                //Khi nhấn nút show lọc thu chi theo ngày
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tongThu = 0, tongChi = 0, tong = 0;
                        String bd = tungay.getText().toString();
                        String kt = denngay.getText().toString();
                        //Show danh sach thu chi
                        listthu.clear();
                        listchi.clear();

                        //Tính tiền theo ngày
                        //kết nối firebase
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
                                                 if (ngaythu.get(i).compareTo(bd) >= 0 && ngaythu.get(i).compareTo(kt) <= 0) {
                                                     thunhap.clear();
                                                     int tien = chilSnapshot.child("soTien").getValue(Integer.class);
                                                     thunhap.add(tien);
                                                     for (int j = 0; j < thunhap.size(); j++) {
                                                         tongThu += thunhap.get(j);
                                                     }
                                                     String listthutn = chilSnapshot.child("maGd").getValue(String.class);
                                                     listthutheongay.clear();
                                                     listthutheongay.add(listthutn);
                                                     GiaoDich giaoDich = snapshot.child(magd).getValue(GiaoDich.class);
                                                     listthu.add(giaoDich);
                                                     adapter.notifyDataSetChanged();
                                                    }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }


                                    }


                                    if (magd.contains("ChiTieu")) {
                                        ngaychi.clear();
                                        String ngay = chilSnapshot.child("ngayGd").getValue(String.class);
                                        ngaychi.add(ngay);
                                        for (int i = 0; i < ngaychi.size(); i++) {
                                            try {
                                                if (ngaychi.get(i).compareTo(bd) >= 0 && ngaychi.get(i).compareTo(kt) <= 0) {
                                                    chitieu.clear();
                                                    int tien = chilSnapshot.child("soTien").getValue(Integer.class);
                                                    chitieu.add(tien);
                                                    for (int j = 0; j < chitieu.size(); j++) {
                                                        tongChi += chitieu.get(j);
                                                    }
                                                    String listthuct = chilSnapshot.child("maGd").getValue(String.class);
                                                    listchitheongay.clear();
                                                    listchitheongay.add(listthuct);;
                                                    GiaoDich giaoDich = snapshot.child(magd).getValue(GiaoDich.class);
                                                    listchi.add(giaoDich);
                                                    adapterchi.notifyDataSetChanged();
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }

                                    tong = tongThu - tongChi;
                                    thu.setText(fm.format(tongThu) + " VND");
                                    chi.setText(fm.format(tongChi) + " VND");
                                    conlai.setText(fm.format(tong) + " VND");

                                    //Bieu đồ cột
                                    Bieudocot(tongThu, tongChi, tong);
                                    bieudotron(tongThu, tongChi, tong);
                                }
                                tienkhoan.clear();
                                tienkhoan.add(tongThu);
                                tienkhoan.add(tongChi);
                                tienkhoan.add(tong);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        tong = tongThu - tongChi;
                        thu.setText(fm.format(tongThu) + " VND");
                        chi.setText(fm.format(tongChi) + " VND");
                        conlai.setText(fm.format(tong) + " VND");

                    }

                });
            }
        });


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    barChart.setVisibility(View.GONE);
                    anyChartView.setVisibility(View.VISIBLE);
                    bieudotron(tienkhoan.get(0), tienkhoan.get(1), tienkhoan.get(2));
                } else {
                    Bieudocot(tienkhoan.get(0), tienkhoan.get(1), tienkhoan.get(2));
                    anyChartView.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    public void  bieudotron(int tongChi, int tongThu, int tong) {
        int time[] = {tongChi,tongThu,tong};
        String activity[] ={"Tổng thu","Tổng chi","Còn lại"};
        List<PieEntry> pieEntires = new ArrayList<>();
        for( int i = 0 ; i<time.length;i++){
            pieEntires.add(new PieEntry(time[i],activity[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntires,"");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        PieData data = new PieData(dataSet);
        //Get the chart
        data.setValueTextSize(15);
        dataSet.setValueTextColor(Color.rgb(255,255,255));
        anyChartView.setData(data);
//        anyChartView.invalidate();
        anyChartView.setCenterTextSize(25);
        anyChartView.setCenterTextColor(Color.rgb(62,68,103));
        anyChartView.setCenterText("CHI TIÊU");
//        anyChartView.setDrawEntryLabels(false);
//        anyChartView.setContentDescription("");
        anyChartView.setDrawHoleEnabled (true);
//        anyChartView.setDrawMarkers(true);
//        anyChartView.setMaxHighlightDistance(34);
//        anyChartView.setEntryLabelTextSize(15);
        anyChartView.setHoleRadius(43);
        anyChartView.getDescription().setEnabled(false);
        anyChartView.animateXY(1000, 1000);
//        Legend legend = anyChartView.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//        legend.setTextSize(12);
//        legend.setFormSize(20);
//        legend.setFormToTextSpace(2);
        anyChartView.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String sales = NumberFormat.getCurrencyInstance().format(e.getY());
                Toast.makeText(getContext(), "Số tiền: "+ sales, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }



    public void  Bieudocot(int tongChi, int tongThu, int tong) {
        if (tong == 0 && tongChi == 0 && tongThu == 0) {
            thongbaochar.setText("Không có dữ liệu để thiển thị...");
            barChart.clear();
            return;
        }
        thongbaochar.setText("");
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int[] tien ={tongThu , tongChi, tong};
        int[] loai = {1, 2, 3};
        for ( int i = 0; i < loai.length; i++) {
            barEntries.add(new BarEntry(loai[i], tien[i]));

        }
        BarDataSet dataSet = new BarDataSet(barEntries, "| Green: Khoản chi | Yellow: Khoản thu | Red: Còn lại |");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.rgb(255,154,141));
        dataSet.setValueTextSize(11f);

        BarData barData =  new BarData(dataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("");
        barChart.animateY(2500);
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
                        listthu.add(giaoDich);
                    }
                    if (giaoDich.getMaGd().contains("ChiTieu")) {
                        listchi.add(giaoDich);
                    }
                    adapter.notifyDataSetChanged();
                    adapterchi.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {
                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                if (giaoDich == null || listthu == null || listthu.isEmpty()) {
                    return;
                }
                if (giaoDich == null || listchi == null || listchi.isEmpty()) {
                    return;
                }
                for (int i = 0; i < listthu.size(); i++) {
                    if (giaoDich.getMaGd() == listthu.get(i).getMaGd()) {
                        listthu.set(i, giaoDich);
                        break;
                    }
                }
                for (int i = 0; i < listchi.size(); i++) {
                    if (giaoDich.getMaGd() == listchi.get(i).getMaGd()) {
                        listchi.set(i, giaoDich);
                        break;
                    }
                }
                adapterchi.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {
                GiaoDich giaoDich = snapshot.getValue(GiaoDich.class);
                if (giaoDich == null || listthu == null || listthu.isEmpty()) {
                    return;
                }
                if (giaoDich == null || listchi == null || listchi.isEmpty()) {
                    return;
                }
                for (int i = 0; i < listthu.size(); i++) {
                    if (giaoDich.getMaGd() == listthu.get(i).getMaGd()) {
                        listthu.remove(listthu.get(i));
                        break;
                    }
                }

                for (int i = 0; i < listchi.size(); i++) {
                    if (giaoDich.getMaGd() == listchi.get(i).getMaGd()) {
                        listchi.remove(listchi.get(i));
                        break;
                    }
                }
                adapterchi.notifyDataSetChanged();
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
