package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AnaSayfa extends AppCompatActivity {

    Button qr_btn; // LONG PRESS vs. SHORT PRESS for different QR_SCAN modes.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide(); // NullPointerException atabilir

        qr_btn = (Button) findViewById(R.id.qr_btn);

        //SHORT CLICK OPENS QR_SCANNER IN NORMAL MODE
        qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaSayfa.this, eczaDolabi.class);
                startActivity(intent);
            }
        });
    }

    public void kisiler(View view) { // SONRA YAPILACAK
        //((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100); // Vibrate for 100 milliseconds
        //Toast.makeText(this, R.string.yapim_asamasinda, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, credits.class);
        startActivity(intent);
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
