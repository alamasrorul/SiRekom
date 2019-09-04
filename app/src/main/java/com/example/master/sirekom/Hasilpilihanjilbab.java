package com.example.master.sirekom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Hasilpilihanjilbab extends AppCompatActivity {
    ImageView imageView;
    int huemaks;
    float saturmaks, brightmaks;
    Button warna1, warna2, warna3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasilpilihanjilbab);
        try {
            Intent intent = getIntent();
            huemaks = intent.getIntExtra("huemaks",0);
            saturmaks = intent.getFloatExtra("saturmaks",0f);
            brightmaks = intent.getFloatExtra("brightmaks",0f);

            float[] hsvtemp = new float[3];
            hsvtemp[0]=(float)huemaks;
            hsvtemp[1]=saturmaks;
            hsvtemp[2]=brightmaks;
            int dominan = Color.HSVToColor(hsvtemp);

            int fuzzy1 = (huemaks-180>0)?(huemaks-180):(360+huemaks-180);
            hsvtemp[0]=(float)(fuzzy1);
            int warnarekom1 = Color.HSVToColor(hsvtemp);

            int fuzzy2 = (huemaks-90>0)?(huemaks-90):(huemaks-90+360);
            hsvtemp[0]=(float)(fuzzy2);
            int warnarekom2 = Color.HSVToColor(hsvtemp);

            warna1 = (Button)findViewById(R.id.button5);
            warna2 = (Button)findViewById(R.id.button7);
            warna3 = (Button)findViewById(R.id.button8);

            warna1.setBackgroundColor(dominan);
            warna1.setText(Integer.toString(huemaks));
            warna2.setBackgroundColor(warnarekom1);
            warna2.setText(Integer.toString(fuzzy1));
            warna3.setBackgroundColor(warnarekom2);
            warna3.setText(Integer.toString(fuzzy2));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void awal(View view) {
        Intent intent = new Intent(Hasilpilihanjilbab.this, MainActivity.class);
        startActivity(intent);
    }
}
