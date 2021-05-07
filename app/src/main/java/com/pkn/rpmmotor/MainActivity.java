package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button category, brand, product, category_view, brand_view, product_view, pos, pos_view, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initComponents();

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
                Intent post_view = new Intent(MainActivity.this, POSViewActivity.class);
                startActivity(post_view);
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
    }

}