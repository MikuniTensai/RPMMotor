package com.pkn.rpmmotor.Manual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.Model.Mobil;
import com.pkn.rpmmotor.Model.Spearpart;
import com.pkn.rpmmotor.R;
import com.pkn.rpmmotor.Remote.APIUtils;
import com.pkn.rpmmotor.Remote.DataService;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlateActivity extends AppCompatActivity {

    EditText edt_noplat, edt_brandcar, edt_typecar;
    Button btn_cancel, btn_finish;
    ImageView list_item;

    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        this.initComponents();

        ButterKnife.bind(this);
        dataService = APIUtils.getDataService();

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


                Mobil m = new Mobil();
                m.setNo_plat(edt_noplat.getText().toString());
                m.setBrand_mobil(edt_brandcar.getText().toString());
                m.setJenis_mobil(edt_brandcar.getText().toString());
                addDataMobil(m);
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

    public void addDataMobil(Mobil m){
        Call<Mobil> call = dataService.addDataMobil(m);
        call.enqueue(new Callback<Mobil>() {
            @Override
            public void onResponse(Call<Mobil> call, Response<Mobil> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PlateActivity.this, "Data created successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mobil> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
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