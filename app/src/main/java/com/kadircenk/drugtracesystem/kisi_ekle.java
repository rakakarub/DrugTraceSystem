package com.kadircenk.drugtracesystem;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class kisi_ekle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_ekle);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    //EditText name = (EditText) findViewById(R.id.isim);
    //bunun commentini kaldırınca hata veriyor, nedenini anlamadım
    //amacım buraya değer girilince katıt butonunu aktifleştimekti
    //note that kaydet is not enabled at the moment

    public void kaydet(View view) {
        Toast.makeText(this, R.string.kisi_kaydedildi, Toast.LENGTH_SHORT).show();
        finish();
    }
}
