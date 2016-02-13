package com.kadircenk.drugtracesystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AnaSayfa extends AppCompatActivity {

    Button qr_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        qr_btn = (Button) findViewById(R.id.qr_btn);

        //LONG CLICK OPENS QR_SCANNER IN NEGATIVE MODE
        qr_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(AnaSayfa.this, QR_Scanner.class);
                intent.putExtra("negatif", true);
                startActivity(intent);
                return true;
            }
        });

        //SHORT CLICK OPENS QR_SCANNER IN NORMAL MODE
        qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaSayfa.this, QR_Scanner.class);
                intent.putExtra("negatif", false);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // NullPointerException atabilir
        }

    }

    public void kisiler(View view) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        v.vibrate(300);
        Toast.makeText(this, R.string.yapim_asamasinda, Toast.LENGTH_SHORT).show();
    }

    public void ilacTest(View view) {
        if (!isConnected()) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        } else { // internet varsa
            Intent intent = new Intent(this, ilacTest.class);
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
