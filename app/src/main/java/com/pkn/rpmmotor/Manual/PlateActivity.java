package com.pkn.rpmmotor.Manual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.R;

public class PlateActivity extends AppCompatActivity {

    EditText edt_noplat, edt_brandcar, edt_typecar;
    Button btn_cancel, btn_finish;
    ImageView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        this.initComponents();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this.finish();
            }

            private void finish() {
                Intent intent = new Intent(PlateActivity.this, ManualPOSViewActivity.class);
                intent.putExtra("noplat", edt_noplat.getText().toString());
                intent.putExtra("brandcar", edt_brandcar.getText().toString());
                intent.putExtra("typecar", edt_typecar.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        edt_noplat = findViewById(R.id.edt_noplat);
        edt_brandcar = findViewById(R.id.edt_brandcar);
        edt_typecar = findViewById(R.id.edt_typecar);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }
}