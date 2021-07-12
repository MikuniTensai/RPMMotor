package com.pkn.rpmmotor.Manual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.Model.Jasa;
import com.pkn.rpmmotor.Model.Spearpart;
import com.pkn.rpmmotor.R;
import com.pkn.rpmmotor.Remote.APIUtils;
import com.pkn.rpmmotor.Remote.DataService;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartActivity extends AppCompatActivity {

    EditText edt_idsparepart,edt_sparepart, edt_count, edt_price;
    Button btn_add, btn_cancel, btn_finish;
    ImageView list_item;

    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part);

        this.initComponents();

        ButterKnife.bind(this);
        dataService = APIUtils.getDataService();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sparepart = edt_sparepart.getText().toString();
                String count = edt_count.getText().toString();
                String price = edt_price.getText().toString();
                if (sparepart.isEmpty() || count.isEmpty() || price.isEmpty()) {
                    Intent finish = new Intent(PartActivity.this, PlateActivity.class);
                    startActivity(finish);
                    insert();
                } else {
                    insert();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insert() {
        try {
            String idsparepart = edt_idsparepart.getText().toString();
            String sparepart = edt_sparepart.getText().toString();
            String count = edt_count.getText().toString();
            String price = edt_price.getText().toString();
            String status = "0";
            if (sparepart.isEmpty() || price.isEmpty() || count.isEmpty()) {
                Toast.makeText(this, "It's Empty",Toast.LENGTH_LONG).show();
            } else {
                SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS sparepart(id INTEGER PRIMARY KEY AUTOINCREMENT, sparepart VARCHAR, count VARCHAR, price VARCHAR, status VARCHAR)");
                String sql = "insert into sparepart (sparepart,count,price,status)values(?,?,?,?)";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1,sparepart);
                statement.bindString(2,count);
                statement.bindString(3,price);
                statement.bindString(4,status);
                statement.execute();
                Toast.makeText(this, "Sparepart add successful",Toast.LENGTH_LONG).show();
                edt_sparepart.setText("");
                edt_count.setText("");
                edt_price.setText("");
                edt_sparepart.requestFocus();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Sparepart add failed",Toast.LENGTH_LONG).show();
        }
        String idpart = edt_idsparepart.getText().toString();

        Spearpart s = new Spearpart();
        s.setId_spearpart(Integer.parseInt(idpart));
        s.setSpearpart(edt_sparepart.getText().toString());
        s.setHarga_s(edt_price.getText().toString());
        addDataSpearpart(s);
    }

    public void addDataSpearpart(Spearpart s){
        Call<Spearpart> call = dataService.addDataSpearpart(s);
        call.enqueue(new Callback<Spearpart>() {
            @Override
            public void onResponse(Call<Spearpart> call, Response<Spearpart> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PartActivity.this, "Data created successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Spearpart> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    private void initComponents() {
        edt_idsparepart = findViewById(R.id.edt_idsparepart);
        btn_add = (Button) findViewById(R.id.btn_add);
        edt_sparepart = findViewById(R.id.edt_sparepart);
        edt_count = findViewById(R.id.edt_count);
        edt_price = findViewById(R.id.edt_price);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    public void listitem(View view) {
        Intent intent = new Intent(PartActivity.this, PartViewActivity.class);
        startActivity(intent);
    }
}