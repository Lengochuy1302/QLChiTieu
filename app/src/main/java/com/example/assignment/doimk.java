package com.example.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class doimk extends AppCompatActivity {
    EditText txtCTk, txtCpass, txtNewPass;
    Button btChangePass, btNhapLai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doimk);
        getSupportActionBar().hide();
        init();

        btNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(doimk.this,MainActivity.class));
            }
        });

        btChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }
                String email = user.getEmail();
                Boolean checkError = true;

                if (!email.equals(txtCTk.getText().toString().trim())) {
                    txtCTk.setError("Tài khoản Email không khớp");
                    checkError = false;
                }

                if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", txtCTk.getText().toString())){
                    txtCTk.setError("Email sai định dạng!");
                    checkError = false;
                }

                if(!txtNewPass.getText().toString().equals(txtCpass.getText().toString())){
                    txtNewPass.setError("Nhập lại mật khẩu không đúng!");
                    checkError = false;
                }
//
                if (txtNewPass.getText().toString().trim().isEmpty() || txtCpass.getText().toString().trim().isEmpty()) {
                    txtNewPass.setError("Không được để trống");
                    txtCpass.setError("Không được để trống");
                    checkError = false;
                }

                if (checkError) {
                    String newPassword = txtCpass.getText().toString().trim();
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        final ProgressDialog progressDialog = ProgressDialog.show(doimk.this,"Thông Báo","Đang kiểm tra...");
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(doimk.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(doimk.this, Dangnhap.class));
                                                finishAffinity();
                                            }
                                        },2500);

                                    } else {
                                        Toast.makeText(doimk.this, "Lỗi! Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }


    private void init(){
        txtCTk =  findViewById(R.id.edtCUser);
        txtCpass =  findViewById(R.id.edtCPass);
        txtNewPass =  findViewById(R.id.edtNewPass);
        btChangePass  = findViewById(R.id.btnChange);
        btNhapLai =  findViewById(R.id.btnRelay);
    }
}