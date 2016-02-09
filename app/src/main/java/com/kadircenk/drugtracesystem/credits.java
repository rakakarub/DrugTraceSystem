package com.kadircenk.drugtracesystem;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class credits extends AppCompatActivity {
    TextView txt_kes;
    Button surprise_button;
    MediaPlayer mp;
    int kadir;
    int kadir2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); // NullPointerException atabilir
        }

        txt_kes = (TextView) findViewById(R.id.kes_txt);
        surprise_button = (Button) findViewById(R.id.surprise_button);
        mp = MediaPlayer.create(credits.this, R.raw.ksc);

        txt_kes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                vib.vibrate(500);
                Toast.makeText(credits.this, R.string.kesici_pop_up, Toast.LENGTH_LONG).show();
                return true;
            }
        });

        txt_kes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


        surprise_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                } else {
                    mp.start();
                    Toast.makeText(credits.this, R.string.surprise, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mp.isPlaying()) {
            mp.stop();
        }
    }
}
