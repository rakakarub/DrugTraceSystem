package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class kisiler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiler);
    }

    public void ekle(View view) {

        Intent intent = new Intent(this,kisi_ekle.class);
        startActivity(intent);
    }

}
