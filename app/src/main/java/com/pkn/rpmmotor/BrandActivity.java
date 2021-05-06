package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BrandActivity extends AppCompatActivity {

    EditText edt_brand, edt_desc;
    Button btn_add, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);

        this.initComponents();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(BrandActivity.this, MainActivity.class);
                startActivity(cancel);
            }
        });
    }

    private void initComponents(){
        edt_brand = findViewById(R.id.edt_brand);
        edt_desc = findViewById(R.id.edt_desc);
        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    public void insert(){
        try {
            String brand = edt_brand.getText().toString();
            String description = edt_desc.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS brand(id INTEGER PRIMARY KEY AUTOINCREMENT, brand VARCHAR, branddesc VARCHAR)");

            String sql = "insert into brand (brand,branddesc)values(?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,brand);
            statement.bindString(2,description);
            statement.execute();
            Toast.makeText(this, "Brand add successful",Toast.LENGTH_LONG).show();
            edt_brand.setText("");
            edt_desc.setText("");
            edt_brand.requestFocus();

        } catch (Exception ex) {
            Toast.makeText(this, "Brand add failed",Toast.LENGTH_LONG).show();
        }
    }
}