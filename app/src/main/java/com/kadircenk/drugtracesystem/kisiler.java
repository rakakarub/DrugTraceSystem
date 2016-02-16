package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class kisiler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiler);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }

    public void ekle(View view) {
        Intent intent = new Intent(this,kisi_ekle.class);
        startActivity(intent);
    }

}
