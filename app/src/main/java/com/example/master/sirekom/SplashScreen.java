package com.example.master.sirekom;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.master.sirekom.helper.PermissionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    PermissionHelper permissionHelper;
    Intent intent;

    //list permission
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
        permissionHelper = new PermissionHelper(this);

        //check permission pada app
        if(checkAndRequestPermissions()){
            startMain();
        }
    }

    public void startMain(){
        intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean checkAndRequestPermissions(){
        //check permission yg udah di acc
        List<String> listPermissionRequest = new ArrayList<>();
        for(String perm : reqPermission){
            if(ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionRequest.add(perm);

            }
        }
        //meminta permission yg belum acc
        if(!listPermissionRequest.isEmpty()){
            ActivityCompat.requestPermissions(this, listPermissionRequest.toArray(new String[listPermissionRequest.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;

            for(int i=0;  i<grantResults.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if(deniedCount == 0) {
                startMain();
            }else{
                for(Map.Entry<String, Integer> entry : permissionResult.entrySet()){
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    //jika aplikasi tidak diizinkan akan keluar keterangan
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,permName)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                });
                        builder.show();
                    }else{
                        //membuat dialog untuk pergi ke pengaturan
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Anda tidak mengizinkan aplikasi mengakses fitur android secara permanen. izinkan akses melalui [Pengaturan] > [Perizinan].").setTitle("SiRekom")
                                .setIcon(R.mipmap.ic_launcher_rd)
                                .setPositiveButton("pergi ke pengaturan", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //membuka jendela pengaturan
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",getPackageName(),null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("tidak, keluar saja", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                }
            }
        }
    }

    /*    private boolean checkAndRequestPermissions(){
        permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionCheckDone() {
                intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        permissionHelper.checkAndRequestPermissions();

        return true;
    }

    //@Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ){
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        permissionHelper.onRequestCallBack(requestCode,permissions, grantResults);
    }*/
}
