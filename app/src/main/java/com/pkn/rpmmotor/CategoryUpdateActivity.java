package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CategoryUpdateActivity extends AppCompatActivity {

    EditText edt_categoryid, edt_category, edt_desc;
    Button btn_update, btn_cancel;
    ImageView btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_update);

        this.initComponents();

        Intent data = getIntent();
        String id = data.getStringExtra("id").toString();
        String category = data.getStringExtra("category").toString();
        String catdesc = data.getStringExtra("catdesc").toString();

        edt_categoryid.setText(id);
        edt_category.setText(category);
        edt_desc.setText(catdesc);

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
        edt_categoryid = findViewById(R.id.edt_categoryid);
        edt_category = findViewById(R.id.edt_category);
        edt_desc = findViewById(R.id.edt_desc);
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.img_delete);
    }

    public void update(){
        try {
            String ids = edt_categoryid.getText().toString();
            String categorys = edt_category.getText().toString();
            String catdescs = edt_desc.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "update category set category = ?, catdesc = ? where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,categorys);
            statement.bindString(2,catdescs);
            statement.bindString(3,ids);
            statement.execute();
            Toast.makeText(this, "Category update successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(view);
        } catch (Exception ex) {
            Toast.makeText(this, "Category update failed",Toast.LENGTH_LONG).show();
        }
    }

    public void delete(){
        try {
            String ids = edt_categoryid.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "delete from category where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,ids);
            statement.execute();
            Toast.makeText(this, "Category delete successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(view);
        } catch (Exception ex){
            Toast.makeText(this, "Category delete failed",Toast.LENGTH_LONG).show();
        }
    }

}