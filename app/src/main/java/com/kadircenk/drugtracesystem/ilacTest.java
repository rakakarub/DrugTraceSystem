package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ilacTest extends AppCompatActivity {

  /*  private static final String NAMESPACE = "http://its.titck.gov.tr/net/check/productstatus/";
    private static final String URL = "http://its.saglik.gov.tr/ControlProduct/CheckProductStatusService";
    private static final String SOAP_ACTION = "http://its.titck.gov.tr/net/check/productstatus/CheckProductStatusRequest";
    private static final String METHOD_NAME = "CheckProductStatusRequest";

    TextView ilacIsmi, fiyat, skt;

    String hash, hashSon;
    String barkodNumarası, seriNumarası;

    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aResult : result) {
            sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }*/

    TextView ilacIsmi, fiyat, skt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_test);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        ilacIsmi = (TextView) findViewById(R.id.ilacIsmi);
        fiyat = (TextView) findViewById(R.id.fiyat);
        skt = (TextView) findViewById(R.id.skt);

        Intent data = getIntent();

        ilacIsmi.setText("İsim: " + data.getStringExtra("drugName"));
        fiyat.setText("Fiyat: " + data.getStringExtra("price"));
        skt.setText("SKT: " + data.getStringExtra("SKT"));

       /* setContentView(R.layout.activity_ilac_test);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide(); // NullPointerException atabilir

        ilacIsmi = (TextView) findViewById(R.id.ilacIsmi);
        fiyat = (TextView) findViewById(R.id.fiyat);
        skt = (TextView) findViewById(R.id.skt);

        Intent intent = getIntent();

        String gelenVeri = intent.getStringExtra("veri");

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

        hash = "";
        try {
            hash = sha1(barkodNumarası + seriNumarası);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        hashSon = "";
        try {
            hashSon = sha1(hash.substring(5, 21) + hash.substring(3, 12));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
        }).start();*/
    }
}
