package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class kisiler extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> mArrayAdapter;
    ArrayList<String> people = new ArrayList<>();
    ArrayList<Integer> people_id =  new ArrayList<>();
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiler);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        listView = (ListView) findViewById(R.id.people);

        myDB = new DBHelper(this);
        Cursor resultSet = myDB.getAllUsers();

        while (resultSet.moveToNext()) {
            people.add(" " + resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + Float.toString(resultSet.getFloat(3)));
            people_id.add(resultSet.getInt(0));
        }

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, people);
        listView.setAdapter(mArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // kişiye tıklayınca onunla ilgili ayrıntıları içeren bir activiy açacak(kullandığı ilaçlar vs)

            }
        });
    }

    public void ekle(View view) {

        Intent intent = new Intent(this, kisi_ekle.class);
        startActivityForResult(intent, 1);//kişi_ekleden yeni kişi gelip gelmediğine dair bilgi bekliyor
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            String personName = data.getStringExtra("name"),personGender = data.getStringExtra("gender");
            Integer personAge = data.getIntExtra("age", 0);//,id = data.getIntExtra("id",0);

            //people_id.add(id);
            people.add(personName+ ", " + personGender + ", "+ personAge);
            mArrayAdapter.notifyDataSetChanged();

        }
    }
}
