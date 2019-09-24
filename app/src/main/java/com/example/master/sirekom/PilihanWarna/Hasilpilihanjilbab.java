package com.example.master.sirekom.PilihanWarna;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.master.sirekom.MainActivity;
import com.example.master.sirekom.R;

public class Hasilpilihanjilbab extends AppCompatActivity {
    ImageView imageView;
    int huemaks;
    float saturmaks, brightmaks;
    Button warna1, warna2, warna3;
//fuzzy declaration

    //Rule ( Pertambahan Hue)
    float RJauh=20;
    float Rdekat =15;

    // Variabel Linguistik (Hue)
    float Jjauh=360;
    float Jdekat=0;
    float input =300;
    float nilai;
//fuzzy declaration


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasilpilihanjilbab);

//fuzzy algorithm
        float mt=MiyuJauh(input,Jjauh,Jdekat);
        float mr=MiyuDekat(input,Jjauh,Jdekat);
        nilai =(mt*RJauh)+(mr*Rdekat);
        int hs=Math.round(nilai);
        System.out.println(hs+"%");
//fuzzy algorithm


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


            //mencari warna

            int fuzzy1, fuzzy2,warnarekom1,warnarekom2;

            warna1 = (Button)findViewById(R.id.button5);
            warna2 = (Button)findViewById(R.id.button7);
            warna3 = (Button)findViewById(R.id.button8);
            warna1.setBackgroundColor(dominan);
            warna1.setText(Integer.toString(huemaks));


            //Pengaplikasian Nilai Hasil Fuzzy

            if(saturmaks>50){

                hsvtemp[1]=40;


            }
            else if(saturmaks>=20){

                hsvtemp[1]=70;


            }


            if(huemaks<340 &&huemaks>15) {
                fuzzy1 = (huemaks +hs) ;
                hsvtemp[0] = (float) (fuzzy1);
                warnarekom1 = Color.HSVToColor(hsvtemp);

                fuzzy2 = (huemaks - hs) ;
                hsvtemp[0] = (float) (fuzzy2);
                 warnarekom2 = Color.HSVToColor(hsvtemp);



                warna2.setBackgroundColor(warnarekom1);
                warna2.setText(Integer.toString(fuzzy1));
                warna3.setBackgroundColor(warnarekom2);
                warna3.setText(Integer.toString(fuzzy2));

            }
            else if(huemaks<5){

                fuzzy1 = (huemaks +10) ;
                hsvtemp[0] = (float) (fuzzy1);
                warnarekom1 = Color.HSVToColor(hsvtemp);

                fuzzy2 = (huemaks + 20) ;
                hsvtemp[0] = (float) (fuzzy2);
                warnarekom2 = Color.HSVToColor(hsvtemp);


                warna2.setBackgroundColor(warnarekom1);
                warna2.setText(Integer.toString(fuzzy1));
                warna3.setBackgroundColor(warnarekom2);
                warna3.setText(Integer.toString(fuzzy2));

            }

            else if (huemaks<=15) {

                fuzzy1 = (huemaks +10) ;
                hsvtemp[0] = (float) (fuzzy1);
                warnarekom1 = Color.HSVToColor(hsvtemp);

                fuzzy2 = (huemaks + 20) ;
                hsvtemp[0] = (float) (fuzzy2);
                warnarekom2 = Color.HSVToColor(hsvtemp);


                warna2.setBackgroundColor(warnarekom1);
                warna2.setText(Integer.toString(fuzzy1));
                warna3.setBackgroundColor(warnarekom2);
                warna3.setText(Integer.toString(fuzzy2));


            }
            else if (huemaks>355) {

                fuzzy1 = (huemaks -10) ;
                hsvtemp[0] = (float) (fuzzy1);
                warnarekom1 = Color.HSVToColor(hsvtemp);

                fuzzy2 = (huemaks - 15) ;
                hsvtemp[0] = (float) (fuzzy2);
                warnarekom2 = Color.HSVToColor(hsvtemp);


                warna2.setBackgroundColor(warnarekom1);
                warna2.setText(Integer.toString(fuzzy1));
                warna3.setBackgroundColor(warnarekom2);
                warna3.setText(Integer.toString(fuzzy2));


            }
            else if (huemaks>=340) {

                fuzzy1 = (huemaks -10) ;
                hsvtemp[0] = (float) (fuzzy1);
                warnarekom1 = Color.HSVToColor(hsvtemp);

                fuzzy2 = (huemaks - 15) ;
                hsvtemp[0] = (float) (fuzzy2);
                warnarekom2 = Color.HSVToColor(hsvtemp);


                warna2.setBackgroundColor(warnarekom1);
                warna2.setText(Integer.toString(fuzzy1));
                warna3.setBackgroundColor(warnarekom2);
                warna3.setText(Integer.toString(fuzzy2));


            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

// Fuzzy Component
    public static float MiyuJauh(float a, float jauh,float dekat){
        float hasil = (jauh-a) /(jauh-dekat);

        return hasil;

    }

    public static float MiyuDekat(float a,float jauh,float dekat)
    {
        float hasil = (a-dekat)/(jauh-dekat);
        return hasil;}
// Fuzzy Component



    public void awal(View view) {
        Intent intent = new Intent(Hasilpilihanjilbab.this, MainActivity.class);
        startActivity(intent);
    }
}
