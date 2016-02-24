package com.kadircenk.drugtracesystem;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class kisiler extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<Person> mArrayAdapter;
    PersonAdaptor personAdaptor;
    ArrayList<Person> people = new ArrayList<>();//Person tipinde bir objeyi arrayliste attım
    ArrayList<Integer> people_id =  new ArrayList<>();//sadece id'leri tutuyor
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisiler);

        //ActionBar varsa yok ettik
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        //yeni listView
        listView = (ListView) findViewById(R.id.people);

        myDB = new DBHelper(this);
        Cursor resultSet = myDB.getAllUsers();

        while (resultSet.moveToNext()) {
            //Person class ında bir objem var, database ten okurken bu objeyi çağırıyorum, arada char[] ve bitmap çevirisi var
            byte[] ba = resultSet.getBlob(4);
            Bitmap bmp = BitmapFactory.decodeByteArray(ba, 0, ba.length);
            people.add(new Person(resultSet.getString(1),resultSet.getInt(2),resultSet.getString(3),bmp));
            people_id.add(resultSet.getInt(0));
        }

        //mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, people);
        //listView.setAdapter(mArrayAdapter);

        //yeni adaptor oluşturuyorum ki listview ı tutsun
        personAdaptor = new PersonAdaptor(this, people);
        System.out.println(people.size());
        if(people.size()!=0)
            listView.setAdapter(personAdaptor);

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
            Integer personAge = data.getIntExtra("age", 0),id = data.getIntExtra("id", 0);

            //kisi_ekle den gelen byte array i tekrar bitmap yapıyor
            byte[] byteArray = data.getExtras().getByteArray("pic");
            Bitmap bmp = null;
            if (byteArray != null) {
                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }


            people_id.add(id);
            people.add(new Person(personName, personAge, personGender, bmp));
            //mArrayAdapter.notifyDataSetChanged();
            personAdaptor.notifyDataSetChanged();
        }
    }
}
