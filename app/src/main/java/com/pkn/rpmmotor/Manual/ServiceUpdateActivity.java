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

public class ServiceUpdateActivity extends AppCompatActivity {

    EditText edt_serviceid, edt_service, edt_price;
    Button btn_update, btn_cancel;
    ImageView btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_update);

        this.initComponents();

        Intent data = getIntent();
        String id = data.getStringExtra("id").toString();
        String service = data.getStringExtra("service").toString();
        String price = data.getStringExtra("price").toString();

        edt_serviceid.setText(id);
        edt_service.setText(service);
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
        edt_serviceid = findViewById(R.id.edt_serviceid);
        edt_service = findViewById(R.id.edt_service);
        edt_price = findViewById(R.id.edt_price);
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_delete = findViewById(R.id.img_delete);
    }

    public void update(){
        try {
            String ids = edt_serviceid.getText().toString();
            String services = edt_service.getText().toString();
            String prices = edt_price.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "update service set service = ?, price = ? where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,services);
            statement.bindString(2,prices);
            statement.bindString(3,ids);
            statement.execute();
            Toast.makeText(this, "Service update successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), ServiceViewActivity.class);
            startActivity(view);
        } catch (Exception ex) {
            Toast.makeText(this, "Service update failed",Toast.LENGTH_LONG).show();
        }
    }

    public void delete(){
        try {
            String ids = edt_serviceid.getText().toString();
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            String sql = "delete from service where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,ids);
            statement.execute();
            Toast.makeText(this, "Service delete successful",Toast.LENGTH_LONG).show();
            Intent view = new Intent(getApplicationContext(), ServiceViewActivity.class);
            startActivity(view);
        } catch (Exception ex){
            Toast.makeText(this, "Service delete failed",Toast.LENGTH_LONG).show();
        }
    }
}