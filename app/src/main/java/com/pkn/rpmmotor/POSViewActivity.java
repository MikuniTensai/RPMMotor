package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class POSViewActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_o_s_view);

        list_item = findViewById(R.id.list_item);
        SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("select * from pos", null);
        int id = c.getColumnIndex("id");
        int product = c.getColumnIndex("product");
        int qty = c.getColumnIndex("qty");
        int price = c.getColumnIndex("price");
        int total = c.getColumnIndex("total");
        titles.clear();

        arrayAdapter = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles);
        list_item.setAdapter(arrayAdapter);

        final ArrayList<Product> Products = new ArrayList<Product>();
        if (c.moveToFirst()){
            do {
                Product prd = new Product();
                prd.id = c.getString(id);
                prd.product = c.getString(product);
                prd.qty = c.getString(qty);
                prd.price = c.getString(price);
                prd.total = c.getString(total);
                Products.add(prd);

                titles.add(c.getString(id) + "\n" + c.getString(product) + "\n" + c.getString(qty) + "\t" + c.getString(price) + "\t" + c.getString(total) );

            } while (c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            list_item.invalidateViews();
        }
    }
}