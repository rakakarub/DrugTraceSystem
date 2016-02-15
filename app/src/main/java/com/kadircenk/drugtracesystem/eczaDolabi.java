package com.kadircenk.drugtracesystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class eczaDolabi extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView mainListView;
    ArrayAdapter<String> mArrayAdapter;
    ArrayList<String> mDrugList = new ArrayList<>();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    int ilac_sayisi;

    String gelenVeri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecza_dolabi);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        ilac_sayisi = sharedPref.getInt("ilac_sayisi", 0);

        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String tmp = entry.getValue().toString();
            if (tmp.startsWith("ilac+"))
                mDrugList.add(tmp.substring(5));

        }

        mainListView = (ListView) findViewById(R.id.dolap_list);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDrugList);
        mainListView.setAdapter(mArrayAdapter);

        mainListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }

    public void yeniİlaç(View v)
    {
        Intent intent = new Intent(this, QR_Scanner.class);
        startActivityForResult(intent,1);
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
                    ilac_sayisi++;

                    sharedPref = eczaDolabi.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("ilac" + ilac_sayisi + "+" + gelenVeri, "ilac+" + gelenVeri);
                    editor.putInt("ilac_sayisi", ilac_sayisi);
                    editor.commit();

                    mDrugList.add(gelenVeri);
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
