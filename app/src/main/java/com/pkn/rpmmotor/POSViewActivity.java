package com.pkn.rpmmotor;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class POSViewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface{
    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    TextView textview, textview1;
    Button btn_print;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;

    private final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_o_s_view);

        list_item = findViewById(R.id.list_item);
        textview = findViewById(R.id.textview);
        textview1 = findViewById(R.id.textview1);
        btn_print = findViewById(R.id.btn_print);
        tvStatus = findViewById(R.id.tv_status);

        ButterKnife.bind(this);
        setupBluetooth();

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printText();
            }
        });

        SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("select * from pos where status = 0", null);

        int id = c.getColumnIndex("id");
        int product = c.getColumnIndex("product");
        int qty = c.getColumnIndex("qty");
        int price = c.getColumnIndex("price");
        int total = c.getColumnIndex("total");
        int status = c.getColumnIndex("status");
        titles.clear();

        arrayAdapter = new ArrayAdapter(this, R.layout.custom_list,R.id.text, titles);
        list_item.setAdapter(arrayAdapter);

        textview1.setText("RPM Motor \n Jln Sudirman \n Telp. 0821");
        final ArrayList<Product> Products = new ArrayList<Product>();
        if (c.moveToFirst()){
            do {
                Product prd = new Product();
                prd.id = c.getString(id);
                prd.product = c.getString(product);
                prd.qty = c.getString(qty);
                prd.price = c.getString(price);
                prd.total = c.getString(total);
                prd.status = c.getString(status);
                Products.add(prd);

                titles.add(c.getString(product) + "\n" + c.getString(qty) + " QTY \t X \t " + "Rp." + c.getString(price) + "\t \t = \t" + " Rp." + c.getString(total));

            } while (c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            list_item.invalidateViews();
        }

        this.addAllValues();
        textview.setText("Total : Rp. "+Integer.toString(addAllValues()));
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
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(textview1.getText().toString(), "");
            for (int position = 0;position<list_item.getCount();position++){
                mService.sendMessage(list_item.getItemAtPosition(position).toString(),"");
            }
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(textview.getText().toString(), "");
            mService.write(PrinterCommands.ESC_ENTER);

            update();
        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceActivity.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
        }
    }

    private void update() {
        try {
            SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);

            String sql = "update pos set status = 1 where status = 0";
            SQLiteStatement statement = db.compileStatement(sql);
            Toast.makeText(this, "Update successful",Toast.LENGTH_LONG).show();
            statement.execute();
        } catch (Exception ex) {
            Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
        }
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

    public int addAllValues(){
        int count = 0;
        SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);
        final Cursor c = db.rawQuery("select sum(total) from pos where status = 0", null);
        if(c.moveToFirst()){
            count = c.getInt(0);
        }
        return count;
    }
}