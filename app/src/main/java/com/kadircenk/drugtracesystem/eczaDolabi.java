package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class eczaDolabi extends AppCompatActivity {
    ListView mainListView;
    ArrayAdapter<String> mArrayAdapter;
    ArrayList<String> mDrugList = new ArrayList<>();
    ArrayList<Integer> mDrugList_id = new ArrayList<>(); //database entrylerinin idlerini tutacak, daha sonra id kullanarak bu entryleri silebilmemiz icin
    TextView ilac_ismi_header, ilac_skt_header, ilac_fiyat_header;
    String gelenDrugName, gelenDrugSKT, gelenDrugPrice;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecza_dolabi);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        ilac_ismi_header = (TextView) findViewById(R.id.ilac_ismi_header);
        ilac_skt_header = (TextView) findViewById(R.id.ilac_skt_header);
        ilac_fiyat_header = (TextView) findViewById(R.id.ilac_fiyat_header);
        mainListView = (ListView) findViewById(R.id.dolap_list);

        myDB = new DBHelper(this);
        Cursor resultSet = myDB.getAllData();

        while (resultSet.moveToNext()) {
            mDrugList.add(" " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4) + "₺"); // bu while loop, resultSet boş degilse komple tum itemlerini tarıyor, 2. field'ı (ilac_name) alıyor.
            mDrugList_id.add(resultSet.getInt(0)); // get id of this loop item, sonradan DB'den silebilmek icin kullanicaz.
        }

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDrugList);
        mainListView.setAdapter(mArrayAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] listItemdeYazanlar = mainListView.getItemAtPosition(position).toString().split(","); // ilac_ismi,|ilac_skt|ilac_fiyat ayırdık.
                String ilac_ismi = listItemdeYazanlar[0];
                String ilac_skt = listItemdeYazanlar[1];
                String ilac_fiyat = listItemdeYazanlar[2];

                ilac_ismi_header.setText(ilac_ismi);
                ilac_skt_header.setText(ilac_skt);
                ilac_fiyat_header.setText(ilac_fiyat);
            }
        });

        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //önce DB'den sil
                myDB.deleteData(mDrugList_id.get(position)); //database id field 1'den başlıyor. listView'da ise 0'dan başlıyor. +1 o yüzden.

                //sonra ListView'dan sil
                mDrugList.remove(position);
                mArrayAdapter.notifyDataSetChanged();

                //silindi diye Toast yolla ekrana
                Toast.makeText(getApplicationContext(), "İlaç silindi!", Toast.LENGTH_SHORT).show();

                //biraz da titret
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);

                return false;
            }
        });
    }

    public void yeniIlac(View v) {
        if (!isConnected()) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        } else { // internet varsa devam et
            Intent intent = new Intent(this, QR_Scanner.class);
            intent.putExtra("query", "ecza_dolabi");
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) //switch daha hizliymis bu durum icin.
        {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("drugName")) {
                        gelenDrugName = data.getStringExtra("drugName");
                        gelenDrugSKT = data.getStringExtra("drugSKT");
                        gelenDrugPrice = data.getStringExtra("drugPrice");

//                        Toast.makeText(getApplicationContext(), gelenDrugName+gelenDrugSKT+gelenDrugPrice, Toast.LENGTH_LONG).show();

//                        Resources rs = getResources();
//                        SQLiteDatabase myDB = eczaDolabi.this.openOrCreateDatabase(rs.getString(R.string.db_name), MODE_PRIVATE, null);
//
//                        myDB.execSQL(rs.getString(R.string.create_table_query));
//
////                        Toast.makeText(getApplicationContext(), rs.getString(R.string.insert_header) + "('admin'," + DatabaseUtils.sqlEscapeString(gelenDrugName) + "," + DatabaseUtils.sqlEscapeString(gelenDrugSKT) + "," + DatabaseUtils.sqlEscapeString(gelenDrugPrice) + ");",Toast.LENGTH_LONG).show();
//                        myDB.execSQL(rs.getString(R.string.insert_header) + "('admin'," + DatabaseUtils.sqlEscapeString(gelenDrugName) + "," + DatabaseUtils.sqlEscapeString(gelenDrugSKT) + "," + DatabaseUtils.sqlEscapeString(gelenDrugPrice) + ");");
//
//                        //get the "id" of the last inserted row, which is the query written in above line, look up!
//                        Cursor resultSet = myDB.rawQuery(rs.getString(R.string.last_row_id_select), null);

                        //yukarıda yorumdakiler eski database sistemi. alttakiler yeni sistemimiz. kca
                        int last_inserted_id = myDB.insertData("admin", gelenDrugName, gelenDrugSKT, gelenDrugPrice);

                        mDrugList.add(" " + gelenDrugName + ", " + gelenDrugSKT + ", " + gelenDrugPrice + "₺"); //listView'a ilac adini yazdik.
                        mDrugList_id.add(last_inserted_id); //id listemize ilacin id'sini yazdik
                        mArrayAdapter.notifyDataSetChanged();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, R.string.sadece_karekod_okutunuz, Toast.LENGTH_LONG).show();
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                }
                break;
        }
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
