package com.kadircenk.drugtracesystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Ilac_arama extends AppCompatActivity {
    ArrayAdapter<String> mArrayAdapter;
    ListView mainListView;
    ArrayList<String> druglist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_arama);

        //DBHelper mydb=new DBHelper(this);


        EditText DRUG_NAME = (EditText) findViewById(R.id.ilac_ismi_header);

        mainListView = (ListView) findViewById(R.id.dolap_list);

        druglist.clear();

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, druglist);
        mainListView.setAdapter(mArrayAdapter);


        DRUG_NAME.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                TextView myOutputBox = (TextView) findViewById(R.id.ilac_skt_header);
                druglist.clear();


                int t = 0, k, i = eczaDolabi.mDrugnameList.size();

                String temp = s.toString();
                for (k = 0; k < i; k++) {
                    if (eczaDolabi.mDrugnameList.get(k).contains(temp)) {
                        druglist.add(eczaDolabi.mDrugnameList.get(k));
                        t++;
                    }
                }
                String res = String.valueOf(t) + " İlaç Bulundu";
                myOutputBox.setText(res);
            }
        });


    }
}
