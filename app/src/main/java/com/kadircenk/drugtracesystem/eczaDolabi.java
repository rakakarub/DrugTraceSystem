package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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
    ArrayList<String> mDrugList_id = new ArrayList<>();
    TextView ilac_ismi_header, ilac_skt_header, ilac_fiyat_header;
    String gelenDrugName, gelenDrugSKT, gelenDrugPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        SQLiteDatabase myDB = openOrCreateDatabase("DrugTraceSystem", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,ilac_ismi VARCHAR,ilac_skt VARCHAR,ilac_fiyat VARCHAR);");

        Cursor resultSet = myDB.rawQuery("Select * from Users", null);

        while (resultSet.moveToNext()) {
            mDrugList.add(" " + resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4) + "₺"); // bu while loop, resultSet boş degilse komple tum itemlerini tarıyor, 2. field'ı (ilac_name) alıyor.
            mDrugList_id.add(resultSet.getString(0)); // get id of this loop item, sonradan DB'den silebilmek icin kullanicaz.
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

//                Toast.makeText(getApplicationContext(),mainListView.getItemAtPosition(position).toString().split(",")[0],Toast.LENGTH_LONG).show();

                ilac_ismi_header.setText(ilac_ismi);
                ilac_skt_header.setText(ilac_skt);
                ilac_fiyat_header.setText(ilac_fiyat);
            }
        });

        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //önce DB'den sil
                SQLiteDatabase myDB = openOrCreateDatabase("DrugTraceSystem", MODE_PRIVATE, null);
                myDB.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,ilac_ismi VARCHAR,ilac_skt VARCHAR,ilac_fiyat VARCHAR);");
                myDB.execSQL("DELETE FROM Users WHERE id=" + mDrugList_id.get(position) + ";"); //id'sine gore bulup silicez. id primary key, so no duplicate ids exist.

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

    public void yeniIlac(View v)
    {
        Intent intent = new Intent(this, QR_Scanner.class);
        intent.putExtra("query", "ecza_dolabi");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) //switch daha hizliymis bu durum icin.
        {
            case 1:
                if (resultCode == RESULT_OK)
                {
                    if (data.hasExtra("drugName")) {
                        gelenDrugName = data.getStringExtra("drugName");
                        gelenDrugSKT = data.getStringExtra("drugSKT");
                        gelenDrugPrice = data.getStringExtra("drugPrice");

//                        Toast.makeText(getApplicationContext(), gelenDrugName+gelenDrugSKT+gelenDrugPrice, Toast.LENGTH_LONG).show();

                        Resources rs = getResources();
                        SQLiteDatabase myDB = eczaDolabi.this.openOrCreateDatabase(rs.getString(R.string.db_name), MODE_PRIVATE, null);
                        myDB.execSQL(rs.getString(R.string.create_table_query));

//                        Toast.makeText(getApplicationContext(), rs.getString(R.string.insert_header) + "('admin'," + DatabaseUtils.sqlEscapeString(gelenDrugName) + "," + DatabaseUtils.sqlEscapeString(gelenDrugSKT) + "," + DatabaseUtils.sqlEscapeString(gelenDrugPrice) + ");",Toast.LENGTH_LONG).show();
                        myDB.execSQL(rs.getString(R.string.insert_header) + "('admin'," + DatabaseUtils.sqlEscapeString(gelenDrugName) + "," + DatabaseUtils.sqlEscapeString(gelenDrugSKT) + "," + DatabaseUtils.sqlEscapeString(gelenDrugPrice) + ");");

                        //get the "id" of the last inserted row, which is the query written in above line, look up!
                        Cursor resultSet = myDB.rawQuery(rs.getString(R.string.last_row_id_select), null);
                        resultSet.moveToFirst();

                        mDrugList.add(" " + gelenDrugName + ", " + gelenDrugSKT + ", " + gelenDrugPrice + "₺"); //listView'a ilac adini yazdik.
                        mDrugList_id.add(resultSet.getString(0)); //id listemize ilacin id'sini yazdik
                        mArrayAdapter.notifyDataSetChanged();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, R.string.qr_kod_okutunuz, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
