package com.example.master.sirekom;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    Intent intent;

    //permission variable
    String[] reqPermission = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int PERMISSION_REQUEST_CODE = 1240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        //cek izin pada app
        if(checkAndRequestPermissions()){
            startMain(1500);
        }
    }

    public void startMain(int timer){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },timer);
    }

    /*
    *Mulai----
    * Area kode permission request {camera, read and write external storage}
    * dan mengatasi jika user mencentang checkbox never ask again
    * */
    public boolean checkAndRequestPermissions(){
        //penambahan permission request
        List<String> listPermissionRequest = new ArrayList<>();
        for(String perm : reqPermission){
            if(ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionRequest.add(perm);

            }
        }
        //meminta izin aplikasi untuk pertama kali dibuka
        if(!listPermissionRequest.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    //disini tanggapan user diolah, mulai dari diberi atau tidaknya izin hingga penanganan checkbox never ask again dicentang
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (requestCode == PERMISSION_REQUEST_CODE){
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;

            for(int i=0;  i<grantResults.length; i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if(deniedCount == 0) {
                //start immediately, because something
                startMain(0);
            }else{
                for(Map.Entry<String, Integer> entry : permissionResult.entrySet()){
                    String permName = entry.getKey();

                    //jika aplikasi tidak diberi izin maka akan keluar keterangan diperlakannya izin tersebut
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,permName)){

                        //edit pesan disini
                        builder.setMessage("Aplikasi ini memerlukan akses kamera dan penyimpanan untuk bisa berjalan dengan baik").setTitle("SiRekom")
                                .setIcon(R.mipmap.ic_launcher_rd)
                                .setPositiveButton("ya, beri izin", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        checkAndRequestPermissions();
                                    }
                                })
                                .setNegativeButton("tidak, saya tidak mau", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                }).show();
                    }else{

                        //membuat dialog untuk pergi ke pengaturan jika user menekan never ask again
                        TextView messageView = new TextView(this);
                        messageView.setText(R.string.reqPermManually);
                        messageView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                        messageView.setPadding(20, 20, 20, 10);

                        builder.setView(messageView).setTitle("SiRekom")
                                .setIcon(R.mipmap.ic_launcher_rd)
                                .setPositiveButton("pergi ke pengaturan", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //membuka jendela pengaturan
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getPackageName(),null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("tidak, keluar saja", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                }).show();
                    }
                }
            }
        }
    }
    /*
    *----Berakhir
     */
}
