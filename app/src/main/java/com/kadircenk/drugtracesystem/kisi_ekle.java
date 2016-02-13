package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class kisi_ekle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_ekle);
    }

    //EditText name = (EditText) findViewById(R.id.isim);
    //bunun commentini kaldırınca hata veriyor, nedenini anlamadım
    //amacım buraya değer girilince katıt butonunu aktifleştimekti
    //note that kaydet is not enabled at the moment

    public void kaydet(View view) {

        Toast.makeText(this,"Kişi Kaydedildi",Toast.LENGTH_SHORT).show();

        finish();
    }
}
