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
import android.widget.ImageView;
import android.widget.Toast;

public class BrandUpdateActivity extends AppCompatActivity {

    EditText edt_brandid, edt_brand, edt_desc;
    Button btn_update, btn_cancel;
    ImageView btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_update);

        this.initComponents();

        Intent data = getIntent();
        String id = data.getStringExtra("id").toString();
        String brand = data.getStringExtra("brand").toString();
        String branddesc = data.getStringExtra("branddesc").toString();

        edt_brandid.setText(id);
        edt_brand.setText(brand);
        edt_desc.setText(branddesc);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(view);
            }
        });
    }

    private void initComponents(){
        edt_brandid = findViewById(R.id.edt_brandid);
        edt_brand = findViewById(R.id.edt_brand);
        edt_desc = findViewById(R.id.edt_desc);
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.img_delete);
    }

    public void update(){
        try {
            String ids = edt_brandid.getText().toString();
            String brands = edt_brand.getText().toString();
            String branddescs = edt_desc.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "update brand set brand = ?, branddesc = ? where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,brands);
            statement.bindString(2,branddescs);
            statement.bindString(3,ids);
            statement.execute();
            Toast.makeText(this, "Brand update successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(view);
        } catch (Exception ex) {
            Toast.makeText(this, "Brand update failed",Toast.LENGTH_LONG).show();
        }
    }

    public void delete(){
        try {
            String ids = edt_brandid.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "delete from brand where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,ids);
            statement.execute();
            Toast.makeText(this, "Brand delete successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(view);
        } catch (Exception ex){
            Toast.makeText(this, "Brand delete failed",Toast.LENGTH_LONG).show();
        }
    }
}