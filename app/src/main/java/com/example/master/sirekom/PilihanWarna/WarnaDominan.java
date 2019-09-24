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
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(indexMaxHue);
    }

    //methode masuk Hasil Rekom Warna

    public void dominan(View view) {
        if (proses.equals("pilihan")) {

            Intent intent = new Intent(WarnaDominan.this, Hasilpilihanjilbab.class);
            intent.putExtra("huemaks", indexMaxHue);
            intent.putExtra("saturmaks", saturmaks);
            intent.putExtra("brightmaks", brigthmaks);
            startActivity(intent);
        }
    }


    //Proses Pencarian RGB HSV gambar
    private void prosesGambar(Bitmap bitmap1) {
        height = bitmap1.getHeight();
        width = bitmap1.getWidth();
        nilaiHue = new int[width][height];
        saturasi = new float[width][height];
        brightness = new float[width][height];

        // Pencarian Nilai RGB

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
        // RGB
        Bitmap operation = Bitmap.createBitmap(width, height, bitmap1.getConfig());
        float[] hsvtemp = new float[3];
        hsvtemp[0] = (float) indexMaxHue;
        hsvtemp[1] = saturmaks;
        hsvtemp[2] = brigthmaks;

        // Convert Rgb --> Hsv
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

    //Methode menampilkan gambar

    private void setToImageView(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imageView.setImageBitmap(decoded);
    }

    //Methode Mengatur ukuran gambar
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
