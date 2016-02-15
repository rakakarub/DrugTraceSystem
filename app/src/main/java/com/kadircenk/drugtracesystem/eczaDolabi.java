package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class eczaDolabi extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mDrugList = new ArrayList();

    String gelenVeri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecza_dolabi);

        mainListView = (ListView) findViewById(R.id.dolap_list);
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDrugList);
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
