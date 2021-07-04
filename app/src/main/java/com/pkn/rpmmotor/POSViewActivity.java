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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pkn.rpmmotor.Model.Data;
import com.pkn.rpmmotor.Remote.APIUtils;
import com.pkn.rpmmotor.Remote.DataService;
import com.zj.btsdk.BluetoothService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSViewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, BluetoothHandler.HandlerInterface{
    ListView list_item;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    TextView textview, textview1;
    Button btn_print;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_status)
    TextView tvStatus;
    Switch sw_tax;

    private final String TAG = MainActivity.class.getSimpleName();
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;

    private static final String DB_URL = "jdbc:mysql://103.146.63.70:2083/rpmmotor_rpm-motor";
    private static final String USER = "rpmmotor";
    private static final String PASS = "@Rofiq312";

    private BluetoothService mService = null;
    private boolean isPrinterReady = false;

    DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_o_s_view);

        list_item = findViewById(R.id.list_item);
        textview = findViewById(R.id.textview);
        textview1 = findViewById(R.id.textview1);
        btn_print = findViewById(R.id.btn_print);
        tvStatus = findViewById(R.id.tv_status);
        sw_tax = findViewById(R.id.sw_tax);

        ButterKnife.bind(this);
        setupBluetooth();

        dataService = APIUtils.getDataService();

        try {
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

            String msg = "================================";

            textview1.setText("RPM Motor \n Perum Bumi Perkasa Regency \n Karangploso Malang \n Telp. 082139119990");
            titles.add(msg);
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

                    titles.add(c.getString(product) + "\t" + c.getString(qty) + " QTY X " + "Rp." + c.getString(price) + " \t \t = " + "   Rp. " + c.getString(total));

                } while (c.moveToNext());
                titles.add(msg);
                textview.setText("Total : Rp. "+Integer.toString(addAllValues())+"\n\n");
                arrayAdapter.notifyDataSetChanged();
                list_item.invalidateViews();
            }

            this.addAllValues();
            sw_tax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    textview.setText("Total : Rp. "+Integer.toString(addAllValues())+"\n\n");
                    if(checked){
                        Integer total = addAllValues() + (10 * addAllValues()  / 100);;
                        textview.setText("Total : Rp. "+Integer.toString(total)+"\n Sudah termasuk ppn 10% \n");
                        Toast.makeText(getApplicationContext(),"True", Toast.LENGTH_SHORT).show();
                    }else {
                        textview.setText("Total : Rp. "+Integer.toString(addAllValues())+"\n\n");
                        Toast.makeText(getApplicationContext(),"False", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e){
            Intent pos = new Intent(POSViewActivity.this, POSActivity.class);
            pos.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(pos);
            Toast.makeText(this, "Data Empty, Add Product first",Toast.LENGTH_LONG).show();
        }
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
            for (int position = 0;position<list_item.getCount();position++){
                mService.sendMessage(list_item.getItemAtPosition(position).toString(),"");
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

            update();

            Data u = new Data();
            u.setStatus("1");
            u.setCreated_at(getDateTime());
            updateData(u);

            POSViewActivity.Send objSend = new POSViewActivity.Send();
            objSend.execute("");
        } else {
            if (mService.isBTopen())
                startActivityForResult(new Intent(this, DeviceActivity.class), RC_CONNECT_DEVICE);
            else
                requestBluetooth();
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void update() {
        try {
            SQLiteDatabase db = openOrCreateDatabase("pos", Context.MODE_PRIVATE, null);

            String date = getDateTime();

            String sql = "update pos set status = ?, created_at = ? where status = 0";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,"1");
            statement.bindString(2, date);
            statement.execute();

            Toast.makeText(this, "Update successful",Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
        }
    }

    private class Send extends AsyncTask<String, String, String> {
        String msg = "";
        String created_at = getDateTime();

        @Override
        protected void onPreExecute() {
            textview.setText("Please wait inserting data");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                if (conn == null) {
                    msg = "Connection goes wrong";
                } else {
                    String query = "update data set status = '1', created_at = '"+created_at+"' where status = '0'";
                    Statement stmt = conn.createStatement();
                    stmt.execute(query);
                    msg = "Update Successfull!";
                }
                conn.close();
            } catch (Exception e) {
                msg = "Connection goes wrong";
                e.printStackTrace();
            }
            return msg;
        }
    }

    public void updateData(Data u){
        Call<Data> call = dataService.updateData(u);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    Toast.makeText(POSViewActivity.this, "Update data successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
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