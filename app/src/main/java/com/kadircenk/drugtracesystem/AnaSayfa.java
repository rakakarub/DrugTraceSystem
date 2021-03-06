package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class AnaSayfa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        //qr_button definition+setOnClickListener iç içe.
        (findViewById(R.id.qr_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnaSayfa.this, eczaDolabi.class)); // yeni activity actik
            }
        });
//**************************************
//    SQLite kullanma taslagi asagida:
//        DBHelper myDB;
//        myDB=new DBHelper(this); //burada hep "this" yollayalim. context fark etmiyor nerede olursak olalım.
//        int asd = myDB.insertData("admin", "ilacIsmi1", "ilacSKT1", "ilacFiyat1");
//        Cursor cs = myDB.getData(asd);
//        cs.moveToFirst();
//        Toast.makeText(this, cs.getString(0)+cs.getString(1)+cs.getString(2)+cs.getString(3)+cs.getString(4), Toast.LENGTH_SHORT).show();
//        myDB.deleteData(asd);
//**************************************
    }

    public void kisiler(View view) {
        Intent intent = new Intent(this,kisiler.class);
        startActivity(intent);
    }

    public void ilacTest(View view) {
        if (!isConnected()) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        } else { // internet varsa devam et
            Intent intent = new Intent(this, QR_Scanner.class);
            intent.putExtra("query", "sorgu");
            startActivity(intent);
        }
    }

    public void credits(View view) {
        startActivity(new Intent(this, credits.class)); //credits activity'sini actik
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
