package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.database.Cursor;
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
    TextView ilac_ismi_header;
    TextView ilac_skt_header;

    String gelenVeri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecza_dolabi);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide(); // NullPointerException atabilir

        ilac_ismi_header = (TextView) findViewById(R.id.ilac_ismi_header);
        ilac_skt_header = (TextView) findViewById(R.id.ilac_skt_header);

        SQLiteDatabase myDB = openOrCreateDatabase("DrugTraceSystem", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,ilac_ismi VARCHAR,ilac_skt VARCHAR,ilac_fiyat VARCHAR);");

        Cursor resultSet = myDB.rawQuery("Select * from Users", null);

        while (resultSet.moveToNext()) {
            mDrugList.add(resultSet.getString(2)); // bu while loop, resultSet boş degilse komple tum itemlerini tarıyor, 2. field'ı (ilac_name) alıyor.
            mDrugList_id.add(resultSet.getString(0)); // get id of the element, sonradan DB'den silebilmek icin kullanicaz.
        }

        mainListView = (ListView) findViewById(R.id.dolap_list);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDrugList);
        mainListView.setAdapter(mArrayAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mainListView.getItemAtPosition(position);
                String ilac_ismi = o.toString();

                ilac_ismi_header.setText(ilac_ismi);
                ilac_skt_header.setText("Boş");
            }
        });

        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //DB'den sil
                SQLiteDatabase myDB = openOrCreateDatabase("DrugTraceSystem", MODE_PRIVATE, null);
                myDB.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,ilac_ismi VARCHAR,ilac_skt VARCHAR,ilac_fiyat VARCHAR);");
                myDB.execSQL("DELETE FROM Users WHERE id=" + mDrugList_id.get(position) + ";");

                //ListView'dan sil
                mDrugList.remove(position);
                mArrayAdapter.notifyDataSetChanged();

                //silindi diye Toast yolla
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
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                if(data.hasExtra("drugName"))
                {
                    gelenVeri = data.getStringExtra("drugName");

                    SQLiteDatabase myDB = eczaDolabi.this.openOrCreateDatabase("DrugTraceSystem", MODE_PRIVATE, null);
                    myDB.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR,ilac_ismi VARCHAR,ilac_skt VARCHAR,ilac_fiyat VARCHAR);");
                    myDB.execSQL("INSERT INTO Users(user,ilac_ismi,ilac_skt,ilac_fiyat) VALUES('admin','" + gelenVeri + "','test_skt','test_fiyat');");

                    //get the id column of the last row, which is the upper query, look up!
                    Cursor resultSet2 = myDB.rawQuery("Select id from Users", null);
                    resultSet2.moveToLast();

                    mDrugList.add(gelenVeri);
                    mDrugList_id.add(resultSet2.getString(0));
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Lütfen QR Kod okutunuz!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
