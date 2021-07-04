package com.pkn.rpmmotor.Manual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pkn.rpmmotor.Brand;
import com.pkn.rpmmotor.BrandActivity;
import com.pkn.rpmmotor.BrandUpdateActivity;
import com.pkn.rpmmotor.BrandViewActivity;
import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.R;

import java.util.ArrayList;

public class ServiceViewActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_view);

        try {
            list_item = findViewById(R.id.list_item);
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            final Cursor c = db.rawQuery("select * from service", null);
            int id = c.getColumnIndex("id");
            int service = c.getColumnIndex("service");
            int price = c.getColumnIndex("price");

            titles.clear();

            arrayAdapter = new ArrayAdapter(this, R.layout.custom_list, R.id.text, titles);
            list_item.setAdapter(arrayAdapter);

            final ArrayList<Service> Services = new ArrayList<Service>();
            if (c.moveToFirst()) {
                do {
                    Service srvc = new Service();
                    srvc.id = c.getString(id);
                    srvc.service = c.getString(service);
                    srvc.price = c.getString(price);
                    Services.add(srvc);

                    titles.add(c.getString(id) + "\n" + c.getString(service) + "\n" + c.getString(price));

                } while (c.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                list_item.invalidateViews();
            }

            list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = titles.get(position).toString();
                    Service services = Services.get((position));
                    Intent data = new Intent(getApplicationContext(),ServiceUpdateActivity.class);
                    data.putExtra("id",services.id);
                    data.putExtra("service",services.service);
                    data.putExtra("price",services.price);
                    startActivity(data);
                }
            });
        } catch (Exception e) {
            Intent brand = new Intent(ServiceViewActivity.this, MainActivity.class);
            brand.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(brand);
            Toast.makeText(this, "Data Empty, Add Service First",Toast.LENGTH_LONG).show();
        }

    }
}