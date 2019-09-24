package com.example.master.sirekom.CekCocok;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.master.sirekom.MainActivity;
import com.example.master.sirekom.PilihanWarna.Hasilpilihanjilbab;
import com.example.master.sirekom.R;

public class CekCocok extends AppCompatActivity {
    // HSV Baju
    int huemaks;
    float saturmaks, brightmaks;

    // HSV Krudung
    int huemaksKd;
    float saturmaksKd, brightmaksKd;
    int a=0;
    Button warna1, warna2 ;


    int cocok =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        // HSV Krudung
        huemaksKd = intent.getIntExtra("huemaksKd", 0);
        saturmaksKd = intent.getFloatExtra("saturmaksKd", 0f);
        brightmaksKd = intent.getFloatExtra("brightmaksKd", 0f);
        System.out.println("Krudung");
        System.out.println(huemaksKd);
        System.out.println(saturmaksKd);
        System.out.println(brightmaksKd);

        // HSV Baju
        huemaks = intent.getIntExtra("huemaks", 0);
        saturmaks = intent.getFloatExtra("saturmaks", 0f);
        brightmaks = intent.getFloatExtra("brightmaks", 0f);


        System.out.println("Baju");
        System.out.println(huemaks);
        System.out.println(saturmaks);
        System.out.println(brightmaks);






        //Hitam
        if(brightmaks<0.20){
            if(brightmaksKd<0.20){
                setContentView(R.layout.activity_cek_cocok);

            }
            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }


        }
        //Putih
        else if(brightmaks>0.95){
            if(brightmaksKd>0.95){
                setContentView(R.layout.activity_cek_cocok);
            }

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }

        }
        //abu"
        else if(saturmaks<0.20){
            if(saturmaksKd<0.20){
                setContentView(R.layout.activity_cek_cocok);
            }

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }

        }
        //RED
        else if (((huemaks <= 45) || (huemaks <= 360 && huemaks >= 330))) {

            //Pencocokan
            if(((huemaksKd <= 45) || (huemaksKd <= 360 && huemaksKd >= 330)) ){
            setContentView(R.layout.activity_cek_cocok);}

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }
        }
        //Kuning
        else if((huemaks<=60 && huemaks>45)){

            if((huemaksKd <= 60 &&huemaks>45) ){
                setContentView(R.layout.activity_cek_cocok);}

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }
        }
        //Hijau
        else if((huemaks<=165 && huemaks>60)) {

            if((huemaksKd<=165 && huemaksKd>60)) {
                setContentView(R.layout.activity_cek_cocok);
            }

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }
        }
        //Biru
        else if((huemaks<=255 && huemaks>165)) {

            if((huemaksKd<=255 && huemaksKd>=165)) {
                setContentView(R.layout.activity_cek_cocok);
            }

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }
        }

        //Ungu
        else if((huemaks<=285 && huemaks>255)) {

            if((huemaksKd<=285 && huemaksKd>255)) {
                setContentView(R.layout.activity_cek_cocok);
            }

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }
        }
        //Pink
        else if((huemaks<345 && huemaks>285)) {

            if((huemaksKd<345 && huemaksKd>285)) {
                setContentView(R.layout.activity_cek_cocok);
            }

            else{
                setContentView(R.layout.activity_war_jil_ba_tik_ser);
                a=1;
            }
        }
        else{
            setContentView(R.layout.activity_war_jil_ba_tik_ser);
            a=1;
        }

        if (a==0){
            //Hsv Button Bj
            float[] hsvtemp = new float[3];
            hsvtemp[0]=(float)huemaks;
            hsvtemp[1]=saturmaks;
            hsvtemp[2]=brightmaks;
            int dominan = Color.HSVToColor(hsvtemp);

            //Hsv Button Kd
            float[] hsvtemp2 = new float[3];
            hsvtemp2[0]=(float)huemaksKd;
            hsvtemp2[1]=saturmaksKd;
            hsvtemp2[2]=brightmaksKd;
            int dominan2 = Color.HSVToColor(hsvtemp2);


            warna1 = (Button)findViewById(R.id.buttonbj);
            warna2 = (Button)findViewById(R.id.buttonkd);
            warna1.setBackgroundColor(dominan);
            warna2.setBackgroundColor(dominan2);
            warna1.setText(Integer.toString(huemaks));
            warna2.setText(Integer.toString(huemaksKd));

        }
        else{
            //Hsv Button Bj
            float[] hsvtemp = new float[3];
            hsvtemp[0]=(float)huemaks;
            hsvtemp[1]=saturmaks;
            hsvtemp[2]=brightmaks;
            int dominan = Color.HSVToColor(hsvtemp);

            //Hsv Button Kd
            float[] hsvtemp2 = new float[3];
            hsvtemp2[0]=(float)huemaksKd;
            hsvtemp2[1]=saturmaksKd;
            hsvtemp2[2]=brightmaksKd;
            int dominan2 = Color.HSVToColor(hsvtemp2);


            warna1 = (Button)findViewById(R.id.button1);
            warna2 = (Button)findViewById(R.id.button2);
            warna1.setBackgroundColor(dominan);
            warna2.setBackgroundColor(dominan2);
            warna1.setText(Integer.toString(huemaks));
            warna2.setText(Integer.toString(huemaksKd));


        }

        }

        //Methode kembali ke main menu
    public void mainmenu(View view){
        Intent intent = new Intent(CekCocok.this, MainActivity.class);
        startActivity(intent);
    }

    //methode lanjut ke rekom warna
    public void lanjut(View view) {

            Intent intent = new Intent(CekCocok.this, Hasilpilihanjilbab.class);
            intent.putExtra("huemaks", huemaks);
            intent.putExtra("saturmaks", saturmaks);
            intent.putExtra("brightmaks", brightmaks);

            startActivity(intent);

    }


}
