package com.example.master.sirekom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.content.Intent;

import com.example.master.sirekom.CekCocok.CamKecBaju;
import com.example.master.sirekom.PilihanWarna.Kamera;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Masuk Menu Rekom Warna
    public void pilihan(View view){
    Intent intent = new Intent(MainActivity.this, Kamera.class);
        intent.putExtra("proses","pilihan");
        startActivity(intent);
    }
    //Masuk Menu Cek Kecocokan
    public void kecocokan(View view){
        Intent intent = new Intent(MainActivity.this, CamKecBaju.class);
        intent.putExtra("proses","Baju");
        startActivity(intent);
    }
}

