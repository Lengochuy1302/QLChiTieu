package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Datlaimatkhaubanggmail extends AppCompatActivity {
private TextView regmail;
private Button regmailbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datlaimatkhaubanggmail);
        regmail = findViewById(R.id.regmail);
        regmailbtn = findViewById(R.id.regmailbtn);
        getSupportActionBar().hide();
        requesgmail();
    }

    private void requesgmail() {
        regmailbtn.setOnClickListener(v -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = regmail.getText().toString().trim();

            if (emailAddress.isEmpty()) {
                regmail.setError("Không được để trống!");
                return;
            }
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Datlaimatkhaubanggmail.this, "Email đặt lại password đã được gửi!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Datlaimatkhaubanggmail.this, Dangnhap.class));
                            }
                        }
                    });

        });


    }


}