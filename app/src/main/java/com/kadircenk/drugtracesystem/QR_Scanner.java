package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.zxing.Result;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private String NAMESPACE;
    private String URL;
    private String SOAP_ACTION;
    private String METHOD_NAME;
    private ZXingScannerView mScannerView;
    private String barkodNumarası, seriNumarası, gelenVeri, hash, hashSon;
    private SoapObject response;
    private LinearLayout llid;
    private Switch sw;

    public static String sha1(String input) throws NoSuchAlgorithmException
    {
        byte[] result = MessageDigest.getInstance("SHA1").digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aResult : result)
            sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }

    public void parser()
    {
        char ilkKarakter = gelenVeri.charAt(0);
        int index = 19;
        barkodNumarası = gelenVeri.substring(3, 17);
        seriNumarası = "";
        hashSon = "";
        hash = "";

        while (true) {
            if (gelenVeri.charAt(index) == ilkKarakter)
                break;
            else
                seriNumarası += gelenVeri.charAt(index);
            ++index;
        }

        try {
            hash = sha1(barkodNumarası + seriNumarası);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            hashSon = sha1(hash.substring(5, 21) + hash.substring(3, 12));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle state)
    {
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(state);
        setContentView(R.layout.activity_qr__scanner);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        Resources rs = getResources();
        NAMESPACE = rs.getString(R.string.ilac_check_namespace);
        URL = rs.getString(R.string.ilac_check_url);
        SOAP_ACTION = rs.getString(R.string.ilac_check_soapaction);
        METHOD_NAME = rs.getString(R.string.ilac_check_methodname);

        llid = (LinearLayout) findViewById(R.id.llid);
        sw = (Switch) findViewById(R.id.switch1);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view

        llid.addView(mScannerView);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    mScannerView.stopCamera();
                    llid.removeAllViews();

                    mScannerView = new ZXingScannerView(QR_Scanner.this) {
                        @Override
                        public void onPreviewFrame(byte[] data, Camera camera) {
                            Camera.Parameters params = camera.getParameters();
                            params.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE); // negatif effect icin
                            camera.setParameters(params);
                            super.onPreviewFrame(data, camera);
                        }
                    };   // Programmatically initialize the scanner view

                    mScannerView.setResultHandler(QR_Scanner.this);
                    llid.addView(mScannerView);
                    mScannerView.startCamera();
                } else {
                    // The toggle is disabled
                    mScannerView.stopCamera();
                    llid.removeAllViews();

                    mScannerView = new ZXingScannerView(QR_Scanner.this);
                    mScannerView.setResultHandler(QR_Scanner.this);
                    llid.addView(mScannerView);
                    mScannerView.startCamera();
                }
            }
        });
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
//         If you would like to resume scanning after all, call this method below:
//        mScannerView.resumeCameraPreview(this);

        mScannerView.stopCamera();

        if (!rawResult.getBarcodeFormat().toString().equalsIgnoreCase("DATA_MATRIX"))
        {
            setResult(RESULT_CANCELED,null);
            this.finish();
        } else {
            //dogru sekilde aldik datayi, simdi biraz vibrasyon, bu benim misyon
            ((Vibrator) this.getSystemService(VIBRATOR_SERVICE)).vibrate(50);

            gelenVeri = rawResult.getText();
            parser();
            sorgu();
        }
    }

    public void sorgu()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    PropertyInfo GTIN = new PropertyInfo();
                    PropertyInfo SN = new PropertyInfo();
                    PropertyInfo CHECK = new PropertyInfo();
                    PropertyInfo ISMANUEL = new PropertyInfo();
                    PropertyInfo USERKEY = new PropertyInfo();
                    PropertyInfo DEVICE = new PropertyInfo();

                    GTIN.setName("GTIN");
                    GTIN.setValue(barkodNumarası);
                    GTIN.setType(String.class);

                    SN.setName("SN");
                    SN.setValue(seriNumarası);
                    SN.setType(String.class);

                    CHECK.setName("CHECK");
                    CHECK.setValue(hashSon);
                    CHECK.setType(String.class);

                    ISMANUEL.setName("ISMANUEL");
                    ISMANUEL.setValue("1");
                    ISMANUEL.setType(String.class);

                    USERKEY.setName("USERKEY");
                    USERKEY.setValue("13FBC250945CE210");
                    USERKEY.setType(String.class);

                    DEVICE.setName("DEVICE");
                    DEVICE.setValue("1");
                    DEVICE.setType(String.class);

                    request.addProperty(GTIN);
                    request.addProperty(SN);
                    request.addProperty(CHECK);
                    request.addProperty(ISMANUEL);
                    request.addProperty(USERKEY);
                    request.addProperty(DEVICE);

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = false;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    androidHttpTransport.debug = true;

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    response = (SoapObject) envelope.bodyIn;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ilacTest.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
                        }
                    });*/
                    finish();
                }
                finally
                {
                    Intent temp_intent = getIntent();

                    String query = temp_intent.getStringExtra("query");
                    if (query.compareTo("sorgu") == 0) {
                        Intent SBA = new Intent(getApplicationContext(), ilacTest.class);

                        SBA.putExtra("drugName", String.valueOf(response.getProperty("DRUGNAME")));
                        SBA.putExtra("price", String.valueOf(response.getProperty("PRICE") + " ₺"));
                        SBA.putExtra("SKT", String.valueOf(response.getProperty("PRODUCTEXPIREDATE")));
                        startActivity(SBA);
                    } else {
                        Intent data = new Intent();
                        data.putExtra("drugName", String.valueOf(response.getProperty("DRUGNAME")));
                        data.putExtra("drugSKT", String.valueOf(response.getProperty("PRODUCTEXPIREDATE")));
                        data.putExtra("drugPrice", String.valueOf(response.getProperty("PRICE")));
                        setResult(RESULT_OK, data);
                    }
                    QR_Scanner.this.finish();
                }
            }
        }).start();
    }
}
