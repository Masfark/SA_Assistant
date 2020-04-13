package com.example.sa_assistant.ui.shop_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sa_assistant.R;

public class ActivityAddShop extends AppCompatActivity implements View.OnClickListener {

    EditText etAddShopNumber, etAddShopAddress, etAddShopCity;
    Spinner spinAddOOO;
    Button btnAddShop, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        etAddShopNumber = (EditText)findViewById(R.id.etAddShopNumber);
        etAddShopAddress = (EditText)findViewById(R.id.etAddShopAddress);
        etAddShopCity = (EditText)findViewById(R.id.etAddShopCity);
        spinAddOOO = (Spinner)findViewById(R.id.spinAddOOO);

        btnAddShop = (Button)findViewById(R.id.btnAddShop);
        btnAddShop.setOnClickListener(this);
        btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.OOO, R.layout.simple_item_list);
        spinAddOOO.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnAddShop) {
            Intent intent = new Intent();
            intent.putExtra("shopNum", etAddShopNumber.getText().toString());
            intent.putExtra("shopCity", etAddShopCity.getText().toString());
            intent.putExtra("shopAddress", etAddShopAddress.getText().toString());
            intent.putExtra("shopOOO", spinAddOOO.getSelectedItem().toString());
            setResult(RESULT_OK, intent);
            finish();
        }

        else if (id == R.id.btnClose) {
            finish();
        }

    }
}
