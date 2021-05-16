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

public class ProductViewActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        try {
            list_item = findViewById(R.id.list_item);
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            final Cursor c = db.rawQuery("select * from product", null);
            int id = c.getColumnIndex("id");
            int product = c.getColumnIndex("product");
            int prodesc = c.getColumnIndex("prodesc");
            int category = c.getColumnIndex("category");
            int brand = c.getColumnIndex("brand");
            int qty = c.getColumnIndex("qty");
            int price = c.getColumnIndex("price");

            titles.clear();

            arrayAdapter = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles);
            list_item.setAdapter(arrayAdapter);

            final ArrayList<Product> Products = new ArrayList<Product>();
            if (c.moveToFirst()){
                do {
                    Product prd = new Product();
                    prd.id = c.getString(id);
                    prd.product = c.getString(product);
                    prd.description = c.getString(prodesc);
                    prd.category = c.getString(category);
                    prd.brand = c.getString(brand);
                    prd.qty = c.getString(qty);
                    prd.price = c.getString(price);
                    Products.add(prd);

                    titles.add(c.getString(id) + "\n" + c.getString(product) + "\n" + c.getString(prodesc)
                            + "\n" + c.getString(category)+ "\n" + c.getString(brand) + "\n" + c.getString(qty)+ "\n" + c.getString(price));

                } while (c.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                list_item.invalidateViews();
            }
        } catch (Exception e){
            Intent product = new Intent(ProductViewActivity.this, ProductActivity.class);
            product.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(product);
            Toast.makeText(this, "Data Empty, add Product First",Toast.LENGTH_LONG).show();
        }

    }
}