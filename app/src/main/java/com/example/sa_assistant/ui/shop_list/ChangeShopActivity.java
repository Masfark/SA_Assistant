package com.example.sa_assistant.ui.shop_list;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;

public class ChangeShopActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAddShopNumber, etAddShopAddress, etAddShopCity;
    private Spinner spinAddOOO;
    private Button btnChange, btnClose;
    private CheckBox checkReserve;
    private LinearLayout linearLayout;
    private DBHelper dbHelper;
    private String shopNum, shopCity, shopAddress, shopOOO, shopReserve, shopID;
    final static String TAG = "MyLogs";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_shop);

        etAddShopNumber = findViewById(R.id.etAddShopNumber);
        etAddShopAddress = findViewById(R.id.etAddShopAddress);
        etAddShopCity = findViewById(R.id.etAddShopCity);
        spinAddOOO = findViewById(R.id.spinAddOOO);
        checkReserve = findViewById(R.id.checkReserve);
        linearLayout = findViewById(R.id.lineRes);

        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.OOO, R.layout.simple_item_list);
        spinAddOOO.setAdapter(adapter);

        Intent intent = getIntent();
        shopID = intent.getStringExtra("shopID");
        shopNum = intent.getStringExtra("shopNum");
        etAddShopNumber.setText(shopNum);
        shopCity = intent.getStringExtra("shopCity");
        etAddShopCity.setText(shopCity);
        shopAddress = intent.getStringExtra("shopAddress");
        etAddShopAddress.setText(shopAddress);
        shopOOO = intent.getStringExtra("shopOOO");
        Log.d(TAG, "Shop - " + R.array.OOO);

        for (int i = 0; i < 5; i++) {
            spinAddOOO.setSelection(i);
            if (spinAddOOO.getSelectedItem().toString().equals(shopOOO)) {
                break;
            }
            if (i == 4) {
                spinAddOOO.setSelection(0);
                Toast.makeText(this, "Вашего ООО нет в списке. Выберите ООО из списка.", Toast.LENGTH_SHORT).show();
            }
        }

        if (intent.getStringExtra("reserve") != null) {
            shopReserve = intent.getStringExtra("reserve");
            checkReserve.setChecked(true);
            EditText edRes = new EditText(getApplicationContext());
            edRes.setHint("Номер резерва");
            if (shopReserve != null) {
                edRes.setText(shopReserve);
            }
            edRes.setId(2);
            edRes.setTextSize(16);
            edRes.setInputType(InputType.TYPE_CLASS_PHONE);
            edRes.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            linearLayout.addView(edRes);
        }
        Log.d(TAG, "Reserve - " + intent.getStringExtra("reserve"));

        checkReserve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkReserve.isChecked()) {
                    EditText edRes = new EditText(getApplicationContext());
                    edRes.setHint("Номер резерва");
                    if (shopReserve != null) {
                        edRes.setText(shopReserve);
                    }
                    edRes.setId(2);
                    edRes.setTextSize(16);
                    edRes.setInputType(InputType.TYPE_CLASS_PHONE);
                    edRes.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    linearLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
                    linearLayout.addView(edRes);
                    shopReserve = edRes.getText().toString();
                } else {
                    linearLayout.removeViewAt(1);
                    shopReserve = null;
                }
            }
        });


    }


    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnChange) {

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            shopNum = etAddShopNumber.getText().toString();
            shopCity = etAddShopCity.getText().toString();
            shopAddress = etAddShopAddress.getText().toString();
            shopOOO = spinAddOOO.getSelectedItem().toString();

            if (checkReserve.isChecked()) {
                EditText editText = linearLayout.findViewById(2);
                shopReserve = editText.getText().toString();
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_NUMBER, shopNum);
            contentValues.put(DBHelper.KEY_CITY, shopCity);
            contentValues.put(DBHelper.KEY_ADDRESS, shopAddress);
            contentValues.put(DBHelper.KEY_OOO, shopOOO);
            contentValues.put(DBHelper.KEY_RES_NUM, shopReserve);

            database.update(DBHelper.TABLE_SHOPS, contentValues, DBHelper.KEY_ID + "= ?", new String[]{shopID});
            database.close();
            finish();
        } else if (id == R.id.btnClose) {
            finish();
        }

    }
}