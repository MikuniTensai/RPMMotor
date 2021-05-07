package com.pkn.rpmmotor;

import androidx.annotation.Nullable;
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

public class POSActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface {

    EditText edt_productid, edt_product, edt_qty, edt_price, edt_total, edt_list;
    Button btn_search, btn_add, btn_print, btn_cancel;
    Switch sw_tax;
    ImageView img_refresh;

    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

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
        setContentView(R.layout.activity_p_o_s);

        this.initComponents();
        ButterKnife.bind(this);
        setupBluetooth();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        edt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int qty = Integer.parseInt(edt_qty.getText().toString());
                int price = Integer.parseInt(edt_price.getText().toString());
                int total = qty * price;

                edt_total.setText(String.valueOf(total));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printText();
            }
        });
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

    private void initComponents() {
        edt_productid = findViewById(R.id.edt_productid);
        edt_product = findViewById(R.id.edt_product);
        edt_qty = findViewById(R.id.edt_qty);
        edt_price = findViewById(R.id.edt_price);
        edt_total = findViewById(R.id.edt_total);
        edt_list = findViewById(R.id.edt_list);
        btn_search = findViewById(R.id.btn_search);
        btn_add = findViewById(R.id.btn_add);
        btn_print = findViewById(R.id.btn_print);
        btn_cancel = findViewById(R.id.btn_cancel);
        img_refresh = findViewById(R.id.img_refresh);
    }

    public void search(){
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        String id = edt_productid.getText().toString();
        final Cursor c = db.rawQuery("select * from product where id = '"+id+"'", null);
        int product = c.getColumnIndex("product");
        int qty = c.getColumnIndex("qty");
        int price = c.getColumnIndex("price");

        titles.clear();

        final ArrayList<ProductView> products = new ArrayList<ProductView>();
        if (c.moveToFirst()){
            do {
                ProductView stu = new ProductView();
                stu.product = c.getString(product);
                stu.qty = c.getString(qty);
                stu.price = c.getString(price);
                products.add(stu);

                edt_product.setText(c.getString(product));
                edt_price.setText(c.getString(price));
            } while (c.moveToNext());
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void add(){
        SQLiteDatabase db = openOrCreateDatabase("rpmmotor", Context.MODE_PRIVATE, null);
        String id = edt_productid.getText().toString();
        final Cursor c = db.rawQuery("select * from product where id = '"+id+"'", null);
        int product = c.getColumnIndex("product");
        int qty = c.getColumnIndex("qty");
        int price = c.getColumnIndex("price");

        titles.clear();

        final ArrayList<ProductView> products = new ArrayList<ProductView>();
        if (c.moveToFirst()){
            do {
                ProductView stu = new ProductView();
                stu.product = c.getString(product);
                stu.qty = c.getString(qty);
                stu.price = c.getString(price);
                products.add(stu);

                edt_list.setText("RPM Motor" + "\n" + "Telp : 08988509765" + "\n" + "Selasa. 12/04/2021 13:44:29" + "\n" + c.getString(product) + "\t" + c.getString(qty));
            } while (c.moveToNext());
        }
        insert();
    }

    private void printText() {
        if (!mService.isAvailable()) {
            Log.i(TAG, "printText: perangkat tidak support bluetooth");
            return;
        }
        if (isPrinterReady) {
            if (edt_list.getText().toString().isEmpty()) {
                Toast.makeText(this, "Cant print null text", Toast.LENGTH_SHORT).show();
                return;
            }
            mService.write(PrinterCommands.ESC_ALIGN_CENTER);
            mService.sendMessage(edt_list.getText().toString(), "");
            mService.write(PrinterCommands.ESC_ENTER);
        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceActivity.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
//                insert();
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

    public void insert(){
        try {
            String product = edt_product.getText().toString();
            String qty = edt_qty.getText().toString();
            String price = edt_price.getText().toString();
            String total = edt_total.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS pos(id INTEGER PRIMARY KEY AUTOINCREMENT, product VARCHAR, qty VARCHAR, price VARCHAR, total VARCHAR)");

            String sql = "insert into pos (product,qty,price,total)values(?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,product);
            statement.bindString(2,qty);
            statement.bindString(3,price);
            statement.bindString(4,total);
            statement.execute();
            Toast.makeText(this, "POS add successful",Toast.LENGTH_LONG).show();
            edt_product.requestFocus();
        } catch (Exception ex) {
            Toast.makeText(this, "Category add failed",Toast.LENGTH_LONG).show();
        }
    }
}