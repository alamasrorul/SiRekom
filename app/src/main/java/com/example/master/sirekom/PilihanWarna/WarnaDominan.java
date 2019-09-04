package com.example.master.sirekom.PilihanWarna;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.master.sirekom.R;
import com.example.master.sirekom.WarBaJilSer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class WarnaDominan extends AppCompatActivity {
    Bitmap bitmap, decoded;
    ImageView imageView;
    TextView textView;
    int bitmap_size = 40;
    int max_resolution_image = 500;
    String uriPath;
    String proses;

    int nilaiHue[][];
    float saturasi[][];
    float brightness[][];
    int indexMaxHue, indexMaxHue1, indexMaxHue2;
    float saturmaks, brigthmaks;
    int width;
    int height;
    int rentang = 0;
    int threshold = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warnadominan);
        try {
            Intent intent = getIntent();
            uriPath = intent.getStringExtra("uriPath");
            proses = intent.getStringExtra("proses");
            imageView = (ImageView) findViewById(R.id.imageView3);
            textView = (TextView) findViewById(R.id.textView4);
            bitmap = BitmapFactory.decodeFile(uriPath);

            if (proses.equals("pilihan")) {
                prosesGambar(getResizedBitmap(bitmap, max_resolution_image));
            } else if (proses.equals("cocok")) {
                prosesGambar2(getResizedBitmap(bitmap, max_resolution_image));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dominan(View view) {
        if (proses.equals("pilihan")) {

            Intent intent = new Intent(WarnaDominan.this, Hasilpilihanjilbab.class);
            intent.putExtra("huemaks", indexMaxHue);
            intent.putExtra("saturmaks", saturmaks);
            intent.putExtra("brightmaks", brigthmaks);
            startActivity(intent);
        } else if (proses.equals("cocok")) {
            Intent intent = new Intent(WarnaDominan.this, WarBaJilSer.class);
        }
    }



    private void prosesGambar2(Bitmap bitmap1) {
        height = bitmap1.getHeight();
        width = bitmap1.getWidth();
        nilaiHue = new int[width][height];
        saturasi = new float[width][height];
        brightness = new float[width][height];
        for (int i = 0; i < bitmap1.getWidth(); i++) {
            for (int j = 0; j < bitmap1.getHeight(); j++) {
                int p = bitmap1.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                float[] hsv = new float[3];
                Color.RGBToHSV(r, g, b, hsv);
                nilaiHue[i][j] = (int) hsv[0];
                saturasi[i][j] = hsv[1];
                brightness[i][j] = hsv[2];
            }
        }

        int nHue[] = new int[360];
        int nHue2[] = new int[360];

        int tengahW = Math.round(width / 2);
        int tengahH = Math.round(height / 2);
        rentang = Math.round(tengahW / 2);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < rentang; j++) {
                if(i<Math.round(tengahH/2)){
                    nHue[nilaiHue[tengahW - j][i]]++;
                    nHue[nilaiHue[tengahW + j][i]]++;
                }else{
                    nHue2[nilaiHue[tengahW - j][i]]++;
                    nHue2[nilaiHue[tengahW + j][i]]++;
                }

            }
        }

        int nMax = 0;
        int nMax2 = 0;
        indexMaxHue1 = -1;
        indexMaxHue2 = -1;
        for (int n = 0; n < 360; n++) {
            if (nHue[n] > nMax) {
                nMax = nHue[n];
                indexMaxHue1 = n;
            }
            if ((nHue2[n] > nMax2)) {
                nMax2 = nHue2[n];
                indexMaxHue2 = n;
            }
        }

        float tempS = 0f, tempV = 0f;
        float tempS1 = 0f, tempV1 = 0f;
        int jum = 0, jum1 = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < rentang; j++) {
                int hue = nilaiHue[j][i];
                if (hue == indexMaxHue1) {
                    tempS += saturasi[j][i];
                    tempV += brightness[j][i];
                    jum++;
                }
                if (hue == indexMaxHue2) {
                    tempS1 += saturasi[j][i];
                    tempV1 += brightness[j][i];
                    jum1++;
                }
            }
        }

        Bitmap operation = Bitmap.createBitmap(width, height, bitmap1.getConfig());
        float[] hsvtemp = new float[3];
        hsvtemp[0] = (float) indexMaxHue1;
        hsvtemp[1] = tempS/jum;
        hsvtemp[2] = tempV/jum;
        int warna = Color.HSVToColor(hsvtemp);
        float[] hsvtemp1 = new float[3];
        hsvtemp1[0] = (float) indexMaxHue1;
        hsvtemp1[1] = tempS1/jum1;
        hsvtemp1[2] = tempV1/jum1;
        int warna1 = Color.HSVToColor(hsvtemp1);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int hue = nilaiHue[j][i];
                if (hue > (indexMaxHue1 - threshold) && hue < (indexMaxHue1 + threshold)) {
                    operation.setPixel(j, i, warna);
                } else if (hue > (indexMaxHue2 - threshold) && hue < (indexMaxHue2 + threshold)) {
                    operation.setPixel(j, i, warna1);
                } else {
                    operation.setPixel(j, i, bitmap1.getPixel(j, i));
                }
            }
        }

        setToImageView(operation);
    }

    private void prosesGambar(Bitmap bitmap1) {
        height = bitmap1.getHeight();
        width = bitmap1.getWidth();
        nilaiHue = new int[width][height];
        saturasi = new float[width][height];
        brightness = new float[width][height];
        for (int i = 0; i < bitmap1.getWidth(); i++) {
            for (int j = 0; j < bitmap1.getHeight(); j++) {
                int p = bitmap1.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                float[] hsv = new float[3];
                Color.RGBToHSV(r, g, b, hsv);
                nilaiHue[i][j] = (int) hsv[0];
                saturasi[i][j] = hsv[1];
                brightness[i][j] = hsv[2];
            }
        }

        int nHue[] = new int[360];
        //       for(int n=0; n<360; n++){
//           System.out.println(nHue[n]);
//       }
        int tengahW = Math.round(width / 2);
        int tengahH = Math.round(height / 2);
        rentang = Math.round(tengahW / 2);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < (rentang); j++) {
                nHue[nilaiHue[tengahW - j][i]]++;
                nHue[nilaiHue[tengahW + j][i]]++;
            }
        }

        int nMax = 0;
        indexMaxHue = -1;
        for (int n = 0; n < 360; n++) {
            if (nHue[n] > nMax) {
                nMax = nHue[n];
                indexMaxHue = n;
            }
        }

        float tempS = 0f, tempV = 0f;
        int jum = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < rentang; j++) {
                int hue = nilaiHue[j][i];
                if (hue == indexMaxHue) {
                    tempS += saturasi[j][i];
                    tempV += brightness[j][i];
                    jum++;
                }
            }
        }
        saturmaks = tempS / jum;
        brigthmaks = tempV / jum;

        Bitmap operation = Bitmap.createBitmap(width, height, bitmap1.getConfig());
        float[] hsvtemp = new float[3];
        hsvtemp[0] = (float) indexMaxHue;
        hsvtemp[1] = saturmaks;
        hsvtemp[2] = brigthmaks;
        int warna = Color.HSVToColor(hsvtemp);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int hue = nilaiHue[j][i];
                if (hue > (indexMaxHue + threshold) || hue < (indexMaxHue - threshold)) {
                    operation.setPixel(j, i, bitmap1.getPixel(j, i));
                } else {
                    operation.setPixel(j, i, warna);
                    //operation.setPixel(j,i,Color.BLACK);
                }
            }
        }

        setToImageView(operation);
    }

    private void setToImageView(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imageView.setImageBitmap(decoded);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        Float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width * bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
