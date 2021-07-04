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

public class ServiceActivity extends AppCompatActivity {
    EditText edt_service, edt_price;
    Button btn_add, btn_cancel, btn_finish;
    ImageView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        this.initComponents();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finish = new Intent(ServiceActivity.this, PartActivity.class);
                startActivity(finish);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void insert() {
        try {
            String service = edt_service.getText().toString();
            String price = edt_price.getText().toString();
            String status = "0";
            if (service.isEmpty() || price.isEmpty() ) {
                Toast.makeText(this, "It's Empty",Toast.LENGTH_LONG).show();
            } else {
                SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS service(id INTEGER PRIMARY KEY AUTOINCREMENT, service VARCHAR, price VARCHAR, status VARCHAR)");
                String sql = "insert into service (service,price,status)values(?,?,?)";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1,service);
                statement.bindString(2,price);
                statement.bindString(3,status);
                statement.execute();
                Toast.makeText(this, "Service add successful",Toast.LENGTH_LONG).show();
                edt_service.setText("");
                edt_price.setText("");
                edt_service.requestFocus();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Service add failed",Toast.LENGTH_LONG).show();
        }
    }

    private void initComponents() {
        btn_add = (Button) findViewById(R.id.btn_add);
        edt_service = findViewById(R.id.edt_service);
        edt_price = findViewById(R.id.edt_price);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    public void listitem(View view) {
        Intent intent = new Intent(ServiceActivity.this, ServiceViewActivity.class);
        startActivity(intent);
    }
}