package com.kadircenk.drugtracesystem;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

public class AnaSayfa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    public void eczaDolabi(View view) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        v.vibrate(300);
        Toast.makeText(this, R.string.yapim_asamasinda, Toast.LENGTH_SHORT).show();
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
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
