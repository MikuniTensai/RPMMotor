package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    EditText edt_product, edt_desc,edt_qty, edt_price;
    Spinner spinner1, spinner2;
    ArrayList<String> titles1 = new ArrayList<String>();
    ArrayList<String> titles2 = new ArrayList<String>();
    ArrayAdapter arrayAdapter1;
    ArrayAdapter arrayAdapter2;
    Button btn_add, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        this.initComponents();
        try {
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            final Cursor c = db.rawQuery("select * from category", null);
            int category = c.getColumnIndex("category");

            titles1.clear();

            arrayAdapter1 = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles1);
            spinner1.setAdapter(arrayAdapter1);

            final ArrayList<Cate> Cates = new ArrayList<Cate>();
            if (c.moveToFirst()){
                do {
                    Cate cate = new Cate();
                    cate.category = c.getString(category);
                    Cates.add(cate);
                    titles1.add(c.getString(category));
                } while (c.moveToNext());
                arrayAdapter1.notifyDataSetChanged();
            }

            final Cursor b = db.rawQuery("select * from brand", null);
            int brand = b.getColumnIndex("brand");

            titles2.clear();

            arrayAdapter2 = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles2);
            spinner2.setAdapter(arrayAdapter2);

            final ArrayList<Brand> Brands = new ArrayList<Brand>();
            if (b.moveToFirst()){
                do {
                    Brand brd = new Brand();
                    brd.brand = b.getString(brand);
                    Brands.add(brd);
                    titles2.add(b.getString(brand));
                } while (b.moveToNext());
                arrayAdapter2.notifyDataSetChanged();
            }

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insert();
                }
            });
        } catch (Exception e){
            Toast.makeText(this, "Add Category and Brand first",Toast.LENGTH_LONG).show();
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(ProductActivity.this, MainActivity.class);
                startActivity(cancel);
            }
        });
    }

    private void initComponents() {
        edt_product = findViewById(R.id.edt_product);
        edt_desc = findViewById(R.id.edt_desc);
        spinner1 = findViewById(R.id.catid);
        spinner2 = findViewById(R.id.brandid);
        edt_qty = findViewById(R.id.edt_qty);
        edt_price = findViewById(R.id.edt_price);
        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    public void insert(){
        try {
            String product = edt_product.getText().toString();
            String description = edt_desc.getText().toString();
            String category = spinner1.getSelectedItem().toString();
            String brand = spinner2.getSelectedItem().toString();
            String qty = edt_qty.getText().toString();
            String price = edt_price.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS product(id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR, prodesc VARCHAR, category VARCHAR, brand VARCHAR, qty VARCHAR, price VARCHAR)");

            String sql = "insert into product (product,prodesc,category,brand,qty,price)values(?,?,?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,product);
            statement.bindString(2,description);
            statement.bindString(3,category);
            statement.bindString(4,brand);
            statement.bindString(5,qty);
            statement.bindString(6,price);
            statement.execute();
            Toast.makeText(this, "Product add successful",Toast.LENGTH_LONG).show();
            edt_product.setText("");
            edt_desc.setText("");
            edt_qty.setText("");
            edt_price.setText("");
            edt_product.requestFocus();

        } catch (Exception ex) {
            Toast.makeText(this, "Product add failed",Toast.LENGTH_LONG).show();
        }
    }
}