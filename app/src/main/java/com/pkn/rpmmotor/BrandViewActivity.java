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
import android.widget.Toast;

import java.util.ArrayList;

public class BrandViewActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_view);

        try {
            list_item = findViewById(R.id.list_item);
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            final Cursor c = db.rawQuery("select * from brand", null);
            int id = c.getColumnIndex("id");
            int brand = c.getColumnIndex("brand");
            int branddesc = c.getColumnIndex("branddesc");

            titles.clear();

            arrayAdapter = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles);
            list_item.setAdapter(arrayAdapter);

            final ArrayList<Brand> Brands = new ArrayList<Brand>();
            if (c.moveToFirst()){
                do {
                    Brand brd = new Brand();
                    brd.id = c.getString(id);
                    brd.brand = c.getString(brand);
                    brd.description = c.getString(branddesc);
                    Brands.add(brd);

                    titles.add(c.getString(id) + "\n" + c.getString(brand) );

                } while (c.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                list_item.invalidateViews();
            }

            list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = titles.get(position).toString();
                    Brand brand = Brands.get((position));
                    Intent data = new Intent(getApplicationContext(),BrandUpdateActivity.class);
                    data.putExtra("id",brand.id);
                    data.putExtra("brand",brand.brand);
                    data.putExtra("branddesc",brand.description);
                    startActivity(data);
                }
            });
        } catch (Exception e) {
            Intent brand = new Intent(BrandViewActivity.this, BrandActivity.class);
            brand.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(brand);
            Toast.makeText(this, "Data Empty, Add Brand First",Toast.LENGTH_LONG).show();
        }

    }
}