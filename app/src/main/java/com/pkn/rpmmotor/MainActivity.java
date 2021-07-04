package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pkn.rpmmotor.Manual.ServiceActivity;
import com.pkn.rpmmotor.Model.Data;
import com.pkn.rpmmotor.Remote.APIUtils;
import com.pkn.rpmmotor.Remote.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button category, brand, product, category_view, brand_view, product_view, pos, pos_view, pos_print, manual;
    ImageView refresh;

    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initComponents();
        dataService = APIUtils.getDataService();

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent category = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(category);
            }
        });

        category_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent category_view = new Intent(MainActivity.this, CategoryViewActivity.class);
                startActivity(category_view);
            }
        });

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent brand = new Intent(MainActivity.this, BrandActivity.class);
                startActivity(brand);
            }
        });

        brand_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent brand_view = new Intent(MainActivity.this, BrandViewActivity.class);
                startActivity(brand_view);
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent product = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(product);
            }
        });

        product_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent product_view = new Intent(MainActivity.this, ProductViewActivity.class);
                startActivity(product_view);
            }
        });

        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pos = new Intent(MainActivity.this, POSActivity.class);
                startActivity(pos);
            }
        });

        pos_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pos_view = new Intent(MainActivity.this, POSViewActivity.class);
                startActivity(pos_view);
            }
        });

        pos_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pos_print = new Intent(MainActivity.this, POSPrintActivity.class);
                startActivity(pos_print);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data u = new Data();
                deleteData(u);

                SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);
                String sql = "delete from pos where status = 0";
                SQLiteStatement statement = db.compileStatement(sql);
                statement.execute();
                finish();
                startActivity(getIntent());
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manual = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(manual);
            }
        });
    }

    public void deleteData(Data u){
        Call<Data> call = dataService.deleteData(u);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Update data successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    private void initComponents(){
        category = findViewById(R.id.category);
        category_view = findViewById(R.id.category_view);
        brand = findViewById(R.id.brand);
        brand_view = findViewById(R.id.brand_view);
        product = findViewById(R.id.product);
        product_view = findViewById(R.id.product_view);
        pos = findViewById(R.id.pos);
        pos_view = findViewById(R.id.post_view);
        pos_print = findViewById(R.id.post_print);
        refresh = findViewById(R.id.refresh);
        manual = (Button) findViewById(R.id.manual);
    }

}