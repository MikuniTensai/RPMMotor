package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoryViewActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        list_item = findViewById(R.id.list_item);
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("select * from category", null);
        int id = c.getColumnIndex("id");
        int category = c.getColumnIndex("category");
        int catdesc = c.getColumnIndex("catdesc");

        titles.clear();

        arrayAdapter = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles);
        list_item.setAdapter(arrayAdapter);

        final ArrayList<Cate> Cates = new ArrayList<Cate>();
        if (c.moveToFirst()){
            do {
                Cate cate = new Cate();
                cate.id = c.getString(id);
                cate.category = c.getString(category);
                cate.desc = c.getString(catdesc);
                Cates.add(cate);

                titles.add(c.getString(id) + "\n" + c.getString(category) + "\n" + c.getString(catdesc) );

            } while (c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            list_item.invalidateViews();
        }

        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = titles.get(position).toString();
                Cate cate = Cates.get((position));
                Intent data = new Intent(getApplicationContext(),CategoryUpdateActivity.class);
                data.putExtra("id",cate.id);
                data.putExtra("category",cate.category);
                data.putExtra("catdesc",cate.desc);
                startActivity(data);
            }
        });
    }
}