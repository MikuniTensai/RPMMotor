package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class POSPrintActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_o_s_print);

        try {
            SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);

            final Cursor c = db.rawQuery("select * from pos where status = 1", null);

            int id = c.getColumnIndex("id");
            int product = c.getColumnIndex("product");
            int qty = c.getColumnIndex("qty");
            int price = c.getColumnIndex("price");
            int total = c.getColumnIndex("total");
            int status = c.getColumnIndex("status");
            int created_at = c.getColumnIndex("created_at");
            titles.clear();

            list_item = findViewById(R.id.list_item);

            arrayAdapter = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles);
            list_item.setAdapter(arrayAdapter);
            final ArrayList<Product> Products = new ArrayList<Product>();
            if (c.moveToFirst()) {
                do {
                    Product prd = new Product();
                    prd.id = c.getString(id);
                    prd.product = c.getString(product);
                    prd.qty = c.getString(qty);
                    prd.price = c.getString(price);
                    prd.total = c.getString(total);
                    prd.status = c.getString(status);
                    prd.created_at = c.getString(created_at);
                    Products.add(prd);

                    titles.add(c.getString(product) + "\n" + c.getString(qty) + " QTY \t X \t " + "Rp." + c.getString(price) + "\t \t = \t" + " Rp." + c.getString(total) + "\n" + c.getString(created_at));

                } while (c.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                list_item.invalidateViews();
            }
        } catch (Exception e) {
            Intent pos = new Intent(POSPrintActivity.this, POSActivity.class);
            pos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(pos);
            Toast.makeText(this, "Data Empty, add POS First",Toast.LENGTH_LONG).show();
        }
    }
}