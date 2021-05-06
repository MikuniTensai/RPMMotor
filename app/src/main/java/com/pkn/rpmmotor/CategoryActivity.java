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

public class CategoryActivity extends AppCompatActivity {

    EditText edt_category, edt_desc;
    Button btn_add, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

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
                Intent cancel = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(cancel);
            }
        });
    }

    private void initComponents(){
        edt_category = findViewById(R.id.edt_category);
        edt_desc = findViewById(R.id.edt_desc);
        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
    }

    public void insert(){
        try {
            String category = edt_category.getText().toString();
            String description = edt_desc.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS category(id INTEGER PRIMARY KEY AUTOINCREMENT, category VARCHAR, catdesc VARCHAR)");

            String sql = "insert into category (category,catdesc)values(?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,category);
            statement.bindString(2,description);
            statement.execute();
            Toast.makeText(this, "Category add successful",Toast.LENGTH_LONG).show();
            edt_category.setText("");
            edt_desc.setText("");
            edt_category.requestFocus();

        } catch (Exception ex) {
            Toast.makeText(this, "Category add failed",Toast.LENGTH_LONG).show();
        }
    }
}