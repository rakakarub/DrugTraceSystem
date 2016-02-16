package com.kadircenk.drugtracesystem;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.System.*;

public class kisi_ekle extends AppCompatActivity {

    EditText name;
    Button saveButton;
    ImageButton imageButton;
    private static final int CAM_REQUEST = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_ekle);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        name = (EditText) findViewById(R.id.isim);
        saveButton = (Button) findViewById(R.id.kaydet);
        imageButton = (ImageButton) findViewById(R.id.imageButton);


        name.addTextChangedListener(new TextWatcher() { //Bu text e yazılanları kontrol ediyor
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setEnabled(!name.getText().toString().trim().isEmpty()); // eğer text boş değilse kaydet butonunu aktifleştiriyor
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void newImage(View view){

        // http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
        // bu siteden aldım kodları, isteyen sitye bakabilir, ayrıntılı açıklamaları var

        final CharSequence[] options = { "Yeni Fotoğraf Çek", "Galeriden Seç", "İptal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fotoğraf Ekle");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yeni Fotoğraf Çek")) // kameratı açıyor
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAM_REQUEST);
                }
                else if (options[item].equals("Galeriden Seç")) // galeriyi açıyor
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
                }
                else if (options[item].equals("İptal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // kamera ve galeriden gelen sonuçlar
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CAM_REQUEST) { //Yeni fotoğraf için kamerayı açıyor ama çektiği fotoğrafı alıp activity ye koyamadım bir türlü

                Bitmap foto = (Bitmap) data.getExtras().get("data");
                /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if (foto != null)
                    foto.compress(Bitmap.CompressFormat.JPEG,  90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                imageButton.setImageBitmap(foto);
            }
            else if (requestCode == 2) { // galeriden fotoğraf seçiyor
                // kodu çok incelemedim, daha sonra düzenleme yapmak gerekecek

                Uri selectedImageUri = data.getData();

                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);

                final int REQUIRED_SIZE = 200;
                int scale = 1;

                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;

                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;

                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                imageButton.setImageBitmap(bm);
            }
        }
    }

    public void kaydet(View view) {

        Toast.makeText(this, R.string.kisi_kaydedildi, Toast.LENGTH_SHORT).show();

        Intent data = new Intent();
        setResult(RESULT_OK, data);
        //burada bilgileri file a kaydetmesi lazım
        //bu activity diğerine cevap veriyor çünkü sadece burada file a yazma işlemi olursa diğeri file okusun

        finish();
    }
}

