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

import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.R;

import java.util.ArrayList;

public class PartViewActivity extends AppCompatActivity {

    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_view);

        try {
            list_item = findViewById(R.id.list_item);
            SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);

            final Cursor c = db.rawQuery("select * from sparepart", null);
            int id = c.getColumnIndex("id");
            int sparepart = c.getColumnIndex("sparepart");
            int count = c.getColumnIndex("count");
            int price = c.getColumnIndex("price");

            titles.clear();

            arrayAdapter = new ArrayAdapter(this, R.layout.custom_list, R.id.text, titles);
            list_item.setAdapter(arrayAdapter);

            final ArrayList<Part> Parts = new ArrayList<Part>();
            if (c.moveToFirst()) {
                do {
                    Part prt = new Part();
                    prt.id = c.getString(id);
                    prt.sparepart = c.getString(sparepart);
                    prt.count = c.getString(count);
                    prt.price = c.getString(price);
                    Parts.add(prt);

                    titles.add(c.getString(id) + "\n" + c.getString(sparepart) + "\n" + c.getString(count) + "\n" + c.getString(price));

                } while (c.moveToNext());
                arrayAdapter.notifyDataSetChanged();
                list_item.invalidateViews();
            }

            list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = titles.get(position).toString();
                    Part parts = Parts.get((position));
                    Intent data = new Intent(getApplicationContext(),PartUpdateActivity.class);
                    data.putExtra("id",parts.id);
                    data.putExtra("sparepart",parts.sparepart);
                    data.putExtra("count",parts.count);
                    data.putExtra("price",parts.price);
                    startActivity(data);
                }
            });
        } catch (Exception e) {
            Intent brand = new Intent(PartViewActivity.this, MainActivity.class);
            brand.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(brand);
            Toast.makeText(this, "Data Empty, Add Spare Part First",Toast.LENGTH_LONG).show();
        }

    }
}