package com.pkn.rpmmotor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class POSActivity extends AppCompatActivity {

    EditText edt_productid, edt_product, edt_qty, edt_price, edt_total, edt_list;
    Button btn_search, btn_add, btn_print, btn_cancel;
    Switch sw_tax;
    ImageView img_refresh;

    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_o_s);

        this.initComponents();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int qty = Integer.parseInt(edt_qty.getText().toString());
                int price = Integer.parseInt(edt_price.getText().toString());
                int total = qty * price;

                edt_total.setText(String.valueOf(total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(POSActivity.this, MainActivity.class);
                startActivity(cancel);
            }
        });
    }

    private void initComponents() {
        edt_productid = findViewById(R.id.edt_productid);
        edt_product = findViewById(R.id.edt_product);
        edt_qty = findViewById(R.id.edt_qty);
        edt_price = findViewById(R.id.edt_price);
        edt_total = findViewById(R.id.edt_total);
        btn_search = findViewById(R.id.btn_search);
        btn_add = findViewById(R.id.btn_add);
        btn_print = findViewById(R.id.btn_print);
        btn_cancel = findViewById(R.id.btn_cancel);
        img_refresh = findViewById(R.id.img_refresh);
    }

    public void search(){
        try {
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
            String id = edt_productid.getText().toString();
            final Cursor c = db.rawQuery("select * from product where id = '"+id+"'", null);
            int product = c.getColumnIndex("product");
            int qty = c.getColumnIndex("qty");
            int price = c.getColumnIndex("price");

            titles.clear();

            final ArrayList<ProductView> products = new ArrayList<ProductView>();
            if (c.moveToFirst()){
                do {
                    ProductView stu = new ProductView();
                    stu.product = c.getString(product);
                    stu.qty = c.getString(qty);
                    stu.price = c.getString(price);
                    products.add(stu);

                    edt_product.setText(c.getString(product));
                    edt_price.setText(c.getString(price));
                } while (c.moveToNext());
            }
        } catch (Exception e){
            Intent product = new Intent(POSActivity.this, ProductActivity.class);
            product.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(product);
            Toast.makeText(this, "Data Empty, add Product First",Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint({"SetTextI18n"})
    public void add(){
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        String id = edt_productid.getText().toString();
        final Cursor c = db.rawQuery("select * from product where id = '"+id+"'", null);
        int product = c.getColumnIndex("product");
        int qty = c.getColumnIndex("qty");
        int price = c.getColumnIndex("price");

        titles.clear();

        final ArrayList<ProductView> products = new ArrayList<ProductView>();
        if (c.moveToFirst()){
            do {
                ProductView stu = new ProductView();
                stu.product = c.getString(product);
                stu.qty = c.getString(qty);
                stu.price = c.getString(price);
                products.add(stu);

                //condition here
            } while (c.moveToNext());
        }
        insert();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void insert(){
        try {
            String product = edt_product.getText().toString();
            String qty = edt_qty.getText().toString();
            String price = edt_price.getText().toString();
            String total = edt_total.getText().toString();
            String created_at = getDateTime();
            String status = "0";

            SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS pos(id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR, qty VARCHAR, price VARCHAR, total VARCHAR, status VARCHAR, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

            String sql = "insert into pos (product,qty,price,total,status,created_at)values(?,?,?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,product);
            statement.bindString(2,qty);
            statement.bindString(3,price);
            statement.bindString(4,total);
            statement.bindString(5,status);
            statement.bindString(6,created_at);
            statement.execute();
            Toast.makeText(this, "POS add successful",Toast.LENGTH_LONG).show();
            edt_product.requestFocus();
        } catch (Exception ex) {
            Toast.makeText(this, "Category add failed",Toast.LENGTH_LONG).show();
        }
    }
}