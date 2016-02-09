package com.kadircenk.drugtracesystem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ilacTest extends AppCompatActivity {

    private static final String NAMESPACE = "http://its.titck.gov.tr/net/check/productstatus/";
    private static final String URL = "http://its.saglik.gov.tr/ControlProduct/CheckProductStatusService";
    private static final String SOAP_ACTION = "http://its.titck.gov.tr/net/check/productstatus/CheckProductStatusRequest";
    private static final String METHOD_NAME = "CheckProductStatusRequest";

    TextView ilacIsmi, fiyat, skt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_test);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ilacIsmi = (TextView) findViewById(R.id.ilacIsmi);
        fiyat = (TextView) findViewById(R.id.fiyat);
        skt = (TextView) findViewById(R.id.skt);

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
                    GTIN.setValue("08699514129110");
                    GTIN.setType(String.class);

                    SN.setName("SN");
                    SN.setValue("291942006945105");
                    SN.setType(String.class);

                    CHECK.setName("CHECK");
                    CHECK.setValue("333ad1d6c931f7ea9b2098a504dc6c76513c6511");
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
//YES WE DID
