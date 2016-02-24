package com.kadircenk.drugtracesystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonAdaptor extends BaseAdapter {

    LayoutInflater layoutInflater;
    ArrayList<Person> persons;
    Activity activity;

    public PersonAdaptor(Activity activity, ArrayList<Person> mpersons){

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        persons = mpersons;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View personView;

        personView = layoutInflater.inflate(R.layout.person_listview,null);
        ImageView iv = (ImageView) personView.findViewById(R.id.pic);
        TextView isim = (TextView) personView.findViewById(R.id.isim_listview);
        TextView yas = (TextView) personView.findViewById(R.id.yas_listview);
        TextView cinsiyet = (TextView) personView.findViewById(R.id.cinsiyet_listview);

        if(persons.size()!=0) {
            Person kisi = persons.get(position);
            isim.setText(kisi.getName().toString());
            yas.setText(kisi.getAge().toString());
            cinsiyet.setText(kisi.getGender().toString());
            iv.setImageBitmap(kisi.getPic());
        }

        return null;
    }
}
