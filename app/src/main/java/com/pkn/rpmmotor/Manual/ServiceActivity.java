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
import com.pkn.rpmmotor.Model.Data;
import com.pkn.rpmmotor.Model.Jasa;
import com.pkn.rpmmotor.R;
import com.pkn.rpmmotor.Remote.APIUtils;
import com.pkn.rpmmotor.Remote.DataService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {
    EditText edt_idservice, edt_service, edt_price;
    Button btn_add, btn_cancel, btn_finish;
    ImageView list_item;
    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

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
            String id = edt_idservice.getText().toString();
            String service = edt_service.getText().toString();
            String price = edt_price.getText().toString();
            @Override
            public void onClick(View v) {
                if (service.isEmpty() || price.isEmpty()) {
                    Intent finish = new Intent(ServiceActivity.this, PartActivity.class);
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
                Intent intent = new Intent(ServiceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void insert() {
        try {
            String id = edt_idservice.getText().toString();
            String service = edt_service.getText().toString();
            String price = edt_price.getText().toString();
            String status = "0";
            if (service.isEmpty() || price.isEmpty() ) {
                Toast.makeText(this, "It's Empty",Toast.LENGTH_LONG).show();
            } else {
                SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS service(id INTEGER PRIMARY KEY AUTOINCREMENT, idservice VARCHAR, service VARCHAR, price VARCHAR, status VARCHAR)");
                String sql = "insert into service (idservice,service,price,status)values(?,?,?,?)";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.bindString(1,id);
                statement.bindString(2,service);
                statement.bindString(3,price);
                statement.bindString(4,status);
                statement.execute();
                Toast.makeText(this, "Service add successful",Toast.LENGTH_LONG).show();
                edt_idservice.setText("");
                edt_service.setText("");
                edt_price.setText("");
                edt_service.requestFocus();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Service add failed",Toast.LENGTH_LONG).show();
        }
        String idservice = edt_idservice.getText().toString();

        Jasa j = new Jasa();
        j.setId_jasa(Integer.parseInt(idservice));
        j.setJasa(edt_service.getText().toString());
        j.setHarga(edt_price.getText().toString());
        j.setTanggal(getDateTime());
        addDataJasa(j);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void addDataJasa(Jasa j){
        Call<Jasa> call = dataService.addDataJasa(j);
        call.enqueue(new Callback<Jasa>() {
            @Override
            public void onResponse(Call<Jasa> call, Response<Jasa> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ServiceActivity.this, "Data created successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Jasa> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    private void initComponents() {
        edt_idservice = findViewById(R.id.edt_idservice);
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