package com.pkn.rpmmotor.Manual;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pkn.rpmmotor.BluetoothHandler;
import com.pkn.rpmmotor.DeviceActivity;
import com.pkn.rpmmotor.MainActivity;
import com.pkn.rpmmotor.Model.Data;
import com.pkn.rpmmotor.POSViewActivity;
import com.pkn.rpmmotor.PrinterCommands;
import com.pkn.rpmmotor.Product;
import com.pkn.rpmmotor.R;
import com.zj.btsdk.BluetoothService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ManualPOSViewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface{
    TextView textview,textview1,textview2, textview3, textview4, textview5,textview6;
    ListView list_item,list_item1;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> titles1 = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ArrayAdapter arrayAdapter1;
    Button btn_print;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;
    Switch sw_tax;

    private final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_p_o_s_view);

        textview = findViewById(R.id.textview);
        textview1 = findViewById(R.id.textview1);
        list_item = findViewById(R.id.list_item);
        list_item1 = findViewById(R.id.list_item1);
        btn_print = findViewById(R.id.btn_print);
        tvStatus = findViewById(R.id.tv_status);
        sw_tax = findViewById(R.id.sw_tax);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        textview4 = findViewById(R.id.textview4);
        textview5 = findViewById(R.id.textview5);
        textview6 = findViewById(R.id.textview6);

        ButterKnife.bind(this);
        setupBluetooth();

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printText();
            }
        });

        String msg = "================================";
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        final Cursor c = db.rawQuery("select * from service where status = 0", null);

        int id = c.getColumnIndex("id");
        int service = c.getColumnIndex("service");
        int price = c.getColumnIndex("price");
        int status = c.getColumnIndex("status");

        titles.clear();

        arrayAdapter = new ArrayAdapter(this, R.layout.custom_list, R.id.text, titles);
        list_item.setAdapter(arrayAdapter);

        textview1.setText("RPM Motor \n Perum Bumi Perkasa Regency \n Karangploso Malang \n Telp. 082139119990 \n================================");
        textview3.setText("No Plat : " + getIntent().getStringExtra("noplat"));
        textview2.setText("Brand   : " + getIntent().getStringExtra("brandcar"));
        textview4.setText("Jenis   : " + getIntent().getStringExtra("typecar"));
        titles.add(msg);

        final ArrayList<Service> Services = new ArrayList<Service>();
        if (c.moveToFirst()) {
            do {
                Service srvc = new Service();
                srvc.id = c.getString(id);
                srvc.service = c.getString(service);
                srvc.price = c.getString(price);
                srvc.status = c.getString(status);
                Services.add(srvc);

                titles.add(c.getString(service) + " \t\t " + " Rp." + c.getString(price));

            } while (c.moveToNext());
            titles.add(msg);
            arrayAdapter.notifyDataSetChanged();
            list_item.invalidateViews();
        }

        SQLiteDatabase dbs = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        final Cursor d = dbs.rawQuery("select * from sparepart where status = 0", null);

        int idd = d.getColumnIndex("id");
        int sparepart = d.getColumnIndex("sparepart");
        int count = d.getColumnIndex("count");
        int priced = d.getColumnIndex("price");
        int statusd = d.getColumnIndex("status");

        titles1.clear();

        arrayAdapter1 = new ArrayAdapter(this, R.layout.custom_list, R.id.text, titles1);
        list_item1.setAdapter(arrayAdapter1);

        titles1.add(msg);
        final ArrayList<Part> Parts = new ArrayList<Part>();
        if (d.moveToFirst()) {
            do {
                Part prt = new Part();
                prt.id = d.getString(idd);
                prt.sparepart = d.getString(sparepart);
                prt.count = d.getString(count);
                prt.price = d.getString(priced);
                prt.status = d.getString(statusd);
                Parts.add(prt);

                titles1.add(d.getString(sparepart) + "\t" + d.getString(count) + " QTY\t= " + "Rp." + d.getString(priced));

            } while (d.moveToNext());
            titles1.add(msg);
            int addAllValues = addAllValues1() + addAllValues2();
            textview.setText("Total : Rp. "+Integer.toString(addAllValues)+"\n\n");
            arrayAdapter1.notifyDataSetChanged();
            list_item1.invalidateViews();
        }

        sw_tax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                int addAllValues = addAllValues1() + addAllValues2();
                textview.setText("Total : Rp. "+Integer.toString(addAllValues)+"\n");
                if(checked){
                    int total = addAllValues + (10 * addAllValues  / 100);;
                    textview.setText("Total : Rp. "+Integer.toString(total)+"\n Sudah termasuk ppn 10% \n");
                    Toast.makeText(getApplicationContext(),"True", Toast.LENGTH_SHORT).show();
                }else {
                    textview.setText("Total : Rp. "+Integer.toString(addAllValues)+"\n");
                    Toast.makeText(getApplicationContext(),"False", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void printText() {
        if (!mService.isAvailable()) {
            Log.i(TAG, "printText: perangkat tidak support bluetooth");
            return;
        }
        if (isPrinterReady) {
            if (textview1.getText().toString().isEmpty()) {
                Toast.makeText(this, "Cant print null text", Toast.LENGTH_SHORT).show();
                return;
            }

            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(textview1.getText().toString(), "");
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(textview3.getText().toString(),"");
            mService.sendMessage(textview2.getText().toString(),"");
            mService.sendMessage(textview4.getText().toString(), "");
            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(textview5.getText().toString(), "");

            for (int position = 0;position<list_item.getCount();position++){
                mService.sendMessage(list_item.getItemAtPosition(position).toString(),"");
            }
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage(textview6.getText().toString(), "");
            for (int position1 = 0;position1<list_item1.getCount();position1++){
                mService.sendMessage(list_item1.getItemAtPosition(position1).toString(),"");
            }
            mService.write(PrinterCommands.ESC_ALIGN_RIGHT);
            mService.sendMessage(textview.getText().toString(), "");
            mService.write(PrinterCommands.ESC_ALIGN_LEFT);
            mService.sendMessage("Tanggal : " + getDateTime(),"");
            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage("Terima Kasih Sudah Berkunjung \n RPM Motor Spesialis Chevrolet \n Akses rpmmotor.web.id","");
            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.ESC_ENTER);
            mService.write(PrinterCommands.ESC_ENTER);

            delete();

            Intent back = new Intent(ManualPOSViewActivity.this, MainActivity.class);
            startActivity(back);

        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceActivity.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
        }
    }

    private void delete() {
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        String sql = "delete from service where status = 0";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.execute();

        SQLiteDatabase dbc = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        String sql1 = "delete from sparepart where status = 0";
        SQLiteStatement statement1 = dbc.compileStatement(sql1);
        statement1.execute();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void requestBluetooth() {
        if (mService != null) {
            if (!mService.isBTopen()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, RC_ENABLE_BLUETOOTH);
            }
        }
    }

    @AfterPermissionGranted(RC_BLUETOOTH)
    private void setupBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "You need bluetooth permission",
                    RC_BLUETOOTH, params);
            return;
        }
        mService = new BluetoothService(this, new BluetoothHandler(this));
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // TODO: 10/11/17 do something
    }

    @Override
    public void onDeviceConnected() {
        isPrinterReady = true;
        tvStatus.setText("Terhubung dengan perangkat");
    }

    @Override
    public void onDeviceConnecting() {
        tvStatus.setText("Sedang menghubungkan...");
    }

    @Override
    public void onDeviceConnectionLost() {
        isPrinterReady = false;
        tvStatus.setText("Koneksi perangkat terputus");
    }

    @Override
    public void onDeviceUnableToConnect() {
        tvStatus.setText("Tidak dapat terhubung ke perangkat");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_ENABLE_BLUETOOTH:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult: bluetooth aktif");
                } else
                    Log.i(TAG, "onActivityResult: bluetooth harus aktif untuk menggunakan fitur ini");
                break;
            case RC_CONNECT_DEVICE:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString(DeviceActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice mDevice = mService.getDevByMac(address);
                    mService.connect(mDevice);
                }
                break;
        }
    }

    public int addAllValues1(){
        int count = 0;
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        final Cursor c = db.rawQuery("select sum(price) from service where status = 0", null);
        if(c.moveToFirst()){
            count = c.getInt(0);
        }
        return count;
    }

    public int addAllValues2(){
        int count = 0;
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        final Cursor c = db.rawQuery("select sum(price) from sparepart where status = 0", null);
        if(c.moveToFirst()){
            count = c.getInt(0);
        }
        return count;
    }

}