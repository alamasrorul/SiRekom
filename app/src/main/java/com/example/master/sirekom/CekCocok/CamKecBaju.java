package com.example.master.sirekom.CekCocok;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.master.sirekom.PilihanWarna.Hasilpilihanjilbab;
import com.example.master.sirekom.PilihanWarna.Kamera;
import com.example.master.sirekom.R;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CamKecBaju extends AppCompatActivity {

    String proses;
    int huemaksBj;
    float saturmaksBj, brightmaksBj;

    String uriPath;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    Uri fileUri;
    Intent intent;
    ImageView imageView;
    Button btn_choose_image;
    TextView txt;
    int bitmap_size = 40;
    int max_resolution_image = 800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_kec_baju);

        Intent intent= getIntent();

        imageView = findViewById(R.id.image_view);
        btn_choose_image = (Button) findViewById(R.id.btn_choose_image);
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        proses = intent.getStringExtra("proses");


        System.out.println(proses);


        //Ubah Text
       if(proses.equals("Krudung")){
            txt = (TextView) findViewById(R.id.textcam);
            txt.setText("Pilih Hijab");
           huemaksBj = intent.getIntExtra("huemaksBj",0);
           saturmaksBj = intent.getFloatExtra("saturmaksBj",0f);
           brightmaksBj = intent.getFloatExtra("brightmaksBj",0f);
           System.out.println(huemaksBj);
           System.out.println(saturmaksBj);
           System.out.println(brightmaksBj);
       }



    }

    public void lanjut(View view) {
        Intent intent = new Intent(CamKecBaju.this, GetNilaiHsvBaju.class);
        intent.putExtra("uriPath", uriPath);
        if (proses.equals("Baju")) {
            intent.putExtra("proses","Baju");
        } else if (proses.equals("Krudung")) {
            intent.putExtra("proses","Krudung");
            intent.putExtra("huemaksBj", huemaksBj);
            intent.putExtra("saturmaksBj", saturmaksBj);
            intent.putExtra("brightmaksBj", brightmaksBj);
        }

        startActivity(intent);

    }
    //Select Image from gallery or Camera
    private void selectImage() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Ambil Foto", "Ambil dari Galeri", "Batal"};

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(CamKecBaju.this);
        builder.setTitle("Tambah Foto");
        builder.setIcon(R.mipmap.ic_launcher_rd);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil Foto")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Ambil dari Galeri")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + " resultCode " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());
                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    uriPath = fileUri.getPath();
                    //btn_choose_image.setText(uriPath);
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    uriPath = getRealPath(this, data.getData());
                    // btn_choose_image.setText(uriPath);
                    bitmap = MediaStore.Images.Media.getBitmap(CamKecBaju.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private String getRealPath(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
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

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Tike");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monotoring", "Oops ! Failed create Monotoring directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_TIKE_" + timeStamp + ".jpg");

        return mediaFile;
    }

}
