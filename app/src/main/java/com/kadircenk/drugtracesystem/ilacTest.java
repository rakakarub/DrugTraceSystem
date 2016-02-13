package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ilacTest extends AppCompatActivity {

    private static final String NAMESPACE = "http://its.titck.gov.tr/net/check/productstatus/";
    private static final String URL = "http://its.saglik.gov.tr/ControlProduct/CheckProductStatusService";
    private static final String SOAP_ACTION = "http://its.titck.gov.tr/net/check/productstatus/CheckProductStatusRequest";
    private static final String METHOD_NAME = "CheckProductStatusRequest";

    TextView ilacIsmi, fiyat, skt;

    String hash, hashSon;
    String barkodNumarası, seriNumarası;

    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_test);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide(); // NullPointerException atabilir

        Intent intent = getIntent();

        if (intent.getStringExtra("veri") != null) {
            String gelenVeri = intent.getStringExtra("veri");

            TextView veriText = (TextView) findViewById(R.id.veri);
            veriText.setText(gelenVeri);

            char ilkKarakter = gelenVeri.charAt(0);

            barkodNumarası = gelenVeri.substring(3, 17);
            seriNumarası = "";
            int index = 19;

            while (true) {
                if (gelenVeri.charAt(index) == ilkKarakter)
                    break;

                else
                    seriNumarası += gelenVeri.charAt(index);

                index++;
            }

            TextView barkodText = (TextView) findViewById(R.id.barkod);
            barkodText.setText(barkodNumarası);

            TextView seriText = (TextView) findViewById(R.id.seri);
            seriText.setText(seriNumarası);

            hash = "";
            try {
                hash = sha1(barkodNumarası + seriNumarası);
            } catch (NoSuchAlgorithmException e) {

            }

            hashSon = "";
            try {
                hashSon = sha1(hash.substring(5, 21) + hash.substring(3, 12));
            } catch (NoSuchAlgorithmException e) {

            }

            Log.d("solitaire", hashSon);


            ilacIsmi = (TextView) findViewById(R.id.ilacIsmi);
            fiyat = (TextView) findViewById(R.id.fiyat);
            skt = (TextView) findViewById(R.id.skt);

        } else { // getStringExtra("veri")==null durumu
            //geri dondur simdilik
            finish();
        }

        new Thread(new Runnable() {
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
                    androidHttpTransport.debug = true; // sonra kaldırılacak. debug icin var.

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    final SoapObject response = (SoapObject) envelope.bodyIn;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ilacIsmi.setText("İsim: " + String.valueOf(response.getProperty("DRUGNAME")));
                            fiyat.setText("Fiyat: " + String.valueOf(response.getProperty("PRICE") + " ₺"));
                            skt.setText("SKT: " + String.valueOf(response.getProperty("PRODUCTEXPIREDATE")));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ilacTest.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
                        }
                    });
                    finish();
                }

            }
        }).start();
    }
}
