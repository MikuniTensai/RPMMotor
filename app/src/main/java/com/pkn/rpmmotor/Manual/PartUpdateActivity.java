package com.pkn.rpmmotor.Manual;

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

import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.R;

public class PartUpdateActivity extends AppCompatActivity {

    EditText edt_sparepartid, edt_sparepart, edt_count, edt_price;
    Button btn_update, btn_cancel;
    ImageView btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_update);

        this.initComponents();

        Intent data = getIntent();
        String id = data.getStringExtra("id").toString();
        String sparepart = data.getStringExtra("sparepart").toString();
        String count = data.getStringExtra("count").toString();
        String price = data.getStringExtra("price").toString();

        edt_sparepartid.setText(id);
        edt_sparepart.setText(sparepart);
        edt_count.setText(count);
        edt_price.setText(price);

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

    private void initComponents() {
        edt_sparepartid = findViewById(R.id.edt_sparepartid);
        edt_sparepart = findViewById(R.id.edt_sparepart);
        edt_count = findViewById(R.id.edt_count);
        edt_price = findViewById(R.id.edt_price);
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.img_delete);
    }

    public void update(){
        try {
            String ids = edt_sparepartid.getText().toString();
            String spareparts = edt_sparepart.getText().toString();
            String counts = edt_count.getText().toString();
            String prices = edt_price.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "update sparepart set sparepart = ?, count = ?, price = ? where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,spareparts);
            statement.bindString(2,counts);
            statement.bindString(3,prices);
            statement.bindString(4,ids);
            statement.execute();
            Toast.makeText(this, "Spart Part update successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), PartViewActivity.class);
            startActivity(view);
        } catch (Exception ex) {
            Toast.makeText(this, "Spart Part update failed",Toast.LENGTH_LONG).show();
        }
    }

    public void delete(){
        try {
            String ids = edt_sparepartid.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "delete from sparepart where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,ids);
            statement.execute();
            Toast.makeText(this, "Spart Part delete successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), PartViewActivity.class);
            startActivity(view);
        } catch (Exception ex){
            Toast.makeText(this, "Spart Part delete failed",Toast.LENGTH_LONG).show();
        }
    }
}