package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result rawResult)
    {
        // Do something with the result here

        //Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG);

        Log.v("solitaire", rawResult.getText()); // Prints scan results
        Log.v("solitaire", String.valueOf( rawResult.getText().length() ) ); // Prints scan results length
        if(rawResult.getText().charAt(0) == rawResult.getText().charAt(26))
            Log.d("solitaire", "adbadnsfmsfm");

        Log.v("solitaire", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

        mScannerView.stopCamera();

        Intent intent = new Intent(this, ilacTest.class);
        intent.putExtra("veri", rawResult.getText());
        startActivity(intent);

        this.finish();
    }
}
