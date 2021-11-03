package com.example.assignment.AdapterThuChi;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.ArrayListThuChi.ThuChi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangloaithuchi;
import com.example.assignment.ChucNangThemSuaXoaThuChiTaiKhoan.chucnangthuchi;
import com.example.assignment.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoaiChiAdapter extends RecyclerView.Adapter<LoaiChiAdapter.ViewHolder> {
//    private String linkdatabase = "https://huyassignment-default-rtdb.firebaseio.com/";
private String linkdatabase;
    private Context context;
    private DatabaseReference reference;
    private List<ThuChi> list;
    private  int layout;

    public LoaiChiAdapter() {
    }

    public void setData(List<ThuChi> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public LoaiChiAdapter(Context context, ArrayList<ThuChi> list) {
        this.context = context;
        this.list = list;
    }

    public LoaiChiAdapter(Context context,int layout, ArrayList<ThuChi> list) {
        this.context = context;
        this.list = list;
        this.layout=layout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView img_avataitemtc;
        RelativeLayout relativeLayouttc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textListtc);
            img_avataitemtc = itemView.findViewById(R.id.img_avataitemtc);
            relativeLayouttc = itemView.findViewById(R.id.relative_item_loaitc);

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
        ThuChi thuChi = list.get(position);
        if (thuChi == null) {
            return;
        }
        holder.text.setText(thuChi.getTenKhoan());

        final ThuChi tc = list.get(position);
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
                txtXemchiTiet.setVisibility(View.GONE);
                TextView txtSuaKhoanChi=bottomSheetView.findViewById(R.id.txt_SuaThuChi);
                TextView txtXoa=bottomSheetView.findViewById(R.id.txt_XoaThuChi);
                txtSuaKhoanChi.setText("Sửa loại chi");

                txtXemchiTiet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();

                    }
                });
                txtSuaKhoanChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.them_loai_thuchi);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (dialog != null && dialog.getWindow() != null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        final TextView text = dialog.findViewById(R.id.them_loai_thu);
                        Button xoa = dialog.findViewById(R.id.xoaTextLT);
                        final Button them = dialog.findViewById(R.id.btnThemLT);
                        final TextView title = dialog.findViewById(R.id.titleThemLoai);
                        title.setText("SỬA LOẠI CHI");
                        text.setText(tc.getTenKhoan());
                        them.setText("SỬA");

                        them.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tenkhoan = list.get(position).getTenKhoan();
                                String themText = text.getText().toString();
                                if (themText.isEmpty()) {
                                    text.setError("Không được để trống!");
                                    return;
                                }
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user == null) {
                                    return;
                                }
                                thuChi.setTenKhoan(themText);

                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("ThuChi");
                                reference.child(tc.getMaKhoan()).updateChildren(thuChi.toMap(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot chilSnapshot : snapshot.getChildren()) {
                                                    String namethuchi = chilSnapshot.child("maKhoan").getValue(String.class);
                                                    if ( tenkhoan.equals(namethuchi)) {
                                                        String magd = chilSnapshot.child("maGd").getValue(String.class);
                                                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich").child(magd).child("maKhoan");
                                                        reference.setValue(themText);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });


                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                        xoa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
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
                        txt_Massage.setText("Bạn có muốn xóa " + list.get(position).getTenKhoan() + " hay không ? ");
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
                                        String tenkhoan = list.get(position).getTenKhoan();
                                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("ThuChi");
                                        reference.child(""+ list.get(position).getMaKhoan()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull  DatabaseReference ref) {

                                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                                                reference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot chilSnapshot : snapshot.getChildren()) {
                                                            String namethuchi = chilSnapshot.child("maKhoan").getValue(String.class);
                                                            if ( tenkhoan.equals(namethuchi)) {
                                                                String magd = chilSnapshot.child("maGd").getValue(String.class);
                                                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("GiaoDich");
                                                                reference.child(magd).removeValue(new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(@Nullable DatabaseError error, @NonNull  DatabaseReference ref) {

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
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
        holder.img_avataitemtc.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        holder.relativeLayouttc.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}