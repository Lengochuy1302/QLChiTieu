package com.example.assignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Dangnhap extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private SharedPreferences prefs;
    TextInputLayout inputLayout;
    private String linkdatabase;
    private FirebaseAuth mAuth;
    private EditText edUsername, edPass;
    private TextView qmk;
    private DatabaseReference reference;
    private Button dktk;
    public static final String FULLNAME = "FULLNAME";
    public static final String LEVEL = "LEVEL";
    public static final String USERNAME = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangnhap);
        linkdatabase = getResources().getString(R.string.linkreutime);
        getSupportActionBar().hide();
        mappingView();
        dktk.setEnabled(true);
        setupListener();

    }

   final private void mappingView() {
        edUsername = findViewById(R.id.edtUserName);
        edPass = findViewById(R.id.edtPassword);
        qmk = findViewById(R.id.quenmatkhau);
       dktk = findViewById(R.id.btnLogin);
    }

    private void setupListener(){
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            startActivity(new Intent(Dangnhap.this, DangKiActivity.class));
        });

        qmk.setOnClickListener(v -> {
            startActivity(new Intent(Dangnhap.this, Datlaimatkhaubanggmail.class));
        });

        dktk.setOnClickListener(v -> {

            String userNameInput = edUsername.getText().toString().trim();
            String passwordInput = edPass.getText().toString().trim();


            Boolean checkError = true;
            if(userNameInput.isEmpty()){
                edUsername.setError("Email kh??ng ???????c ????? tr???ng!");
                checkError = false;
            }

            if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", userNameInput)){
                edUsername.setError("Email sai ?????nh d???ng!");
                checkError = false;
            }

            if(passwordInput.isEmpty()){
                edPass.setError("M???t kh???u kh??ng ???????c ????? tr???ng!");
                checkError = false;
            }
            if(passwordInput.length()<6){
                edPass.setError("M???t kh???u qu?? y???u ph???i tr??n 6 k?? t???!");

            }
            if (checkError == true) {
                dktk.setEnabled(false);
            mAuth.signInWithEmailAndPassword(userNameInput, passwordInput)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // Sign in success, update UI with the signed-in user's information
                        final ProgressDialog progressDialog = ProgressDialog.show(Dangnhap.this, "Th??ng B??o", "??ang ????ng nh???p...");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                sendNotification();
                                String name = user.getDisplayName();
                                if (name == null) {
                                    Device device = new Device();
                                    String iddevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    device.setID("ThietBi");
                                    device.setTenDevice(iddevice);
                                    reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("DeviceID");
                                    reference.child("ThietBi").setValue(device);
                                    Intent introIntent = new Intent(Dangnhap.this, Capnhatthongtinnguoidung.class);
                                    startActivity(introIntent);
                                    finishAffinity();
                                } else {
                                    Device device = new Device();
                                    String iddevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    device.setID("ThietBi");
                                    device.setTenDevice(iddevice);
                                    reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("DeviceID");
                                    reference.child("ThietBi").setValue(device);
                                    startActivity(new Intent(Dangnhap.this, MainActivity.class));
                                    finishAffinity();
                                }
                            }
                        }, 2000);

                    } else {
                        // If sign in fails, display a message to the user.
                        edUsername.setError("Sai t??i kho???n Email");
                        edPass.setError("Sai m???t kh???u");

                    }
                }
            });
}
        });
    }

    private void  sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_hello);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(Dangnhap.this, NotificationApplication.CHANNEL_ID)
                .setContentTitle("Ch??o b???n nh??!")
                .setContentText("H??m nay th???t l?? tuy???t v???i khi ???????c g???p b???n! Ch??c b???n c?? m???t ng??y th???t vui v???!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("H??m nay th???t l?? tuy???t v???i khi ???????c g???p b???n! Ch??c b???n c?? m???t ng??y th???t vui v???!"))
                .setSmallIcon(R.drawable.iconnho)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }

}