package com.example.assignment.AdapterThuChi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.ArrayListThuChi.GiaoDich;
import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class KhoanThuAdapter extends RecyclerView.Adapter<KhoanThuAdapter.ViewHolder> {
//    private String linkdatabase = "https://huyassignment-default-rtdb.firebaseio.com/";
private String linkdatabase;
    private Context context;
    private DatabaseReference reference;
    public  List<GiaoDich> list;
    private ArrayList<String> listTC = new ArrayList<String>();
    private  int layout;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private NumberFormat fm = new DecimalFormat("#,###");
    private DatePickerDialog datePickerDialog;
    boolean isDark = false;

    public KhoanThuAdapter() {
    }

    public void setData(List<GiaoDich> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public KhoanThuAdapter(Context context, ArrayList<GiaoDich> list, Boolean isDark) {
        this.context = context;
        this.list = list;
        this.isDark = isDark;
    }


    public KhoanThuAdapter(Context context, ArrayList<GiaoDich> list) {
        this.context = context;
        this.list = list;

    }
    public KhoanThuAdapter(Context context,int layout, ArrayList<GiaoDich> list) {
        this.context = context;
        this.list = list;
        this.layout=layout;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text, giatext, ngaytext;
        private ImageView img_avataitem;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textList);
            giatext = itemView.findViewById(R.id.gialist);
            ngaytext = itemView.findViewById(R.id.ngaylist);
            img_avataitem = itemView.findViewById(R.id.img_avataitem);
            relativeLayout = itemView.findViewById(R.id.relative_item);

        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        linkdatabase = context.getString(R.string.linkreutime);
        final GiaoDich giaoDich = list.get(position);
        if (giaoDich == null) {
            return;
        }
        holder.text.setText(giaoDich.getMoTaGd());
        holder.giatext.setText("+"+fm.format(giaoDich.getSoTien()) +"đ ");
        holder.ngaytext.setText(""+giaoDich.getNgayGd()+" ");


        //Khi nhấn nút sửa
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        context, R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(context).inflate(
                        R.layout.layout_sua_xoa_xemchitiet_item,
                        (LinearLayout) bottomSheetDialog.findViewById(R.id.bottomSheetContainer)
                );
                TextView txtXemchiTiet=bottomSheetView.findViewById(R.id.txt_XemChiTiet);
                TextView txtSuaKhoanChi=bottomSheetView.findViewById(R.id.txt_SuaThuChi);
                TextView txtXoa=bottomSheetView.findViewById(R.id.txt_XoaThuChi);
                txtSuaKhoanChi.setText("Sửa khoản thu");

                //Xem chi tiết
                txtXemchiTiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                        if (position == RecyclerView.NO_POSITION) return;
                        GiaoDich giaoDich = list.get(position);
                        if (giaoDich == null) {
                            return;
                        }
                        //Hiện thông tin giao dịch khi click vào item
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.thong_tin_gd);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        TextView mota, ngay, tien, loai, title, ghichu;
                        mota = dialog.findViewById(R.id.mota_gd);
                        ngay = dialog.findViewById(R.id.ngay_gd);
                        tien = dialog.findViewById(R.id.tien_gd);
                        loai = dialog.findViewById(R.id.loai_gd);
                        ghichu = dialog.findViewById(R.id.ghichu_gd);
                        title = dialog.findViewById(R.id.thongtinGD);
                        title.setText("THÔNG TIN THU NHẬP");
                        mota.setText("Mô tả: "+ giaoDich.getMoTaGd());
                        ngay.setText("Ngày "+ giaoDich.getNgayGd());
                        tien.setText("Tăng +"+ fm.format(giaoDich.getSoTien()) +" VND");
                        loai.setText("Loại thu: "+ giaoDich.getMaKhoan());
                        ghichu.setText("Ghi chú: "+ giaoDich.getGhiChuGd());
                        dialog.show();

                    }
                });



                txtSuaKhoanChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();

                        final Dialog dialog = new Dialog(context);
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
                        final EditText ghichuGd = dialog.findViewById(R.id.them_ghichu_gd);
                        final TextView title = dialog.findViewById(R.id.titleThemKhoan);
                        final Button xoa = dialog.findViewById(R.id.xoaTextGD);
                        final Button them = dialog.findViewById(R.id.btnThemGD);

                        //Set tiêu đề, text
                        title.setText("SỬA KHOẢN THU");
                        them.setText("SỬA");
                        moTaGd.setText(giaoDich.getMoTaGd());
                        ngayGd.setText(giaoDich.getNgayGd());
                        tienGd.setText(String.valueOf(giaoDich.getSoTien()));
                        //Đổ dữ liệu vào spinner
                        listTC = new ArrayList<String>();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            return;
                        }
                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("ThuChi");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot chilSnapshot:snapshot.getChildren()) {
                                    String namethuchi = chilSnapshot.child("tenKhoan").getValue(String.class);
                                    int maloai = chilSnapshot.child("loaiKhoan").getValue(Integer.class);
                                    if ( maloai == 0) {
                                        listTC.add(namethuchi);
                                    }

                                }

                                final ArrayAdapter sp = new ArrayAdapter(context, R.layout.spiner_loaithuchi, listTC);
                                spLoaiGd.setAdapter(sp);
                                int vitri = -1;
                        for (int i = 0; i < listTC.size(); i++) {
                            if (listTC.get(i).equalsIgnoreCase(giaoDich.getMaKhoan())) {
                                vitri = i;
                                break;
                            }
                        }
                                spLoaiGd.setSelection(vitri);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        ghichuGd.setText(giaoDich.getGhiChuGd());

                        //Khi nhấn ngày hiện lên lựa chọ ngày
                        ngayGd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar calendar = Calendar.getInstance();
                                int ngay = calendar.get(Calendar.DAY_OF_MONTH);
                                int thang = calendar.get(Calendar.MONTH);
                                int nam = calendar.get(Calendar.YEAR);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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


                        //Khi nhấn nút xóa
                        xoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        //Khi nhấn nút sửa
                        them.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mota = moTaGd.getText().toString();
                                String ngay = ngayGd.getText().toString();
                                String tien = tienGd.getText().toString();
                                String ghichu = ghichuGd.getText().toString();
                                String ma = spLoaiGd.getSelectedItem().toString();
                                //Check lỗi
                                if (mota.isEmpty() && ngay.isEmpty() && tien.isEmpty()) {
                                    Toast.makeText(context, "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user == null) {
                                        return;
                                    }
                                    try {
                                        giaoDich.setMoTaGd(mota);
                                        giaoDich.setNgayGd(ngay);
                                        giaoDich.setSoTien(Integer.parseInt(tien));
                                        giaoDich.setMaKhoan(ma);
                                        giaoDich.setGhiChuGd(ghichu);
                                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                                        reference.child(giaoDich.getMaGd()).updateChildren(giaoDich.toMap(), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                dialog.dismiss();
                                            }
                                        });
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            }
                        });

                        dialog.show();


                    }
                });

                txtXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);

                        dialog.setContentView(R.layout.popup_xoa_item);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final TextView txt_Massage = dialog.findViewById(R.id.txt_Titleconfirm);
                        Button xoa = dialog.findViewById(R.id.xoaTextLT);
                        final Button them = dialog.findViewById(R.id.btnThemLT);
                        final Button btn_Yes = dialog.findViewById(R.id.btn_yes);
                        final Button btn_No = dialog.findViewById(R.id.btn_no);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progress_loadconfirm);
                        progressBar.setVisibility(View.INVISIBLE);
                        txt_Massage.setText("Bạn có muốn xóa " + list.get(position).getMoTaGd() + " hay không ? ");
                        btn_Yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    txt_Massage.setText("");
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (user == null) {
                                                return;
                                            }
                                            reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                                            reference.child(""+ list.get(position).getMaGd()).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull  DatabaseReference ref) {

                                                }
                                            });
                                            dialog.dismiss();

                                        }
                                    }, 2000);



                            }
                        });
                        btn_No.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });


                        dialog.show();

                    }
                });
                bottomSheetView.findViewById(R.id.txt_Huy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });


        holder.img_avataitem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


}