package com.example.sa_assistant.ui.shop_list;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sa_assistant.R;

public class ActivityAddShop extends AppCompatActivity implements View.OnClickListener {

    private EditText etAddShopNumber, etAddShopAddress, etAddShopCity;
    private Spinner spinAddOOO;
    private Button btnAddShop, btnClose;
    private CheckBox checkReserve;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        etAddShopNumber = findViewById(R.id.etAddShopNumber);
        etAddShopAddress = findViewById(R.id.etAddShopAddress);
        etAddShopCity = findViewById(R.id.etAddShopCity);
        spinAddOOO = findViewById(R.id.spinAddOOO);
        checkReserve = findViewById(R.id.checkReserve);
        linearLayout = findViewById(R.id.lineRes);

        btnAddShop = findViewById(R.id.btnAddShop);
        btnAddShop.setOnClickListener(this);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.OOO, R.layout.simple_item_list);
        spinAddOOO.setAdapter(adapter);

        checkReserve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkReserve.isChecked()) {
                    EditText edRes = new EditText(getApplicationContext());
                    edRes.setHint("Номер резерва");
                    edRes.setId(1);
                    edRes.setTextSize(16);
                    edRes.setInputType(InputType.TYPE_CLASS_PHONE);
                    edRes.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    linearLayout.setGravity(Gravity.LEFT|Gravity.CENTER);
                    linearLayout.addView(edRes);
                }
                else {
                    linearLayout.removeViewAt(1);
                }
            }
        });

    }


    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnAddShop) {
            Intent intent = new Intent();
            intent.putExtra("shopNum", etAddShopNumber.getText().toString());
            intent.putExtra("shopCity", etAddShopCity.getText().toString());
            intent.putExtra("shopAddress", etAddShopAddress.getText().toString());
            intent.putExtra("shopOOO", spinAddOOO.getSelectedItem().toString());
            if (checkReserve.isChecked()) {
                EditText editText = linearLayout.findViewById(1);
                intent.putExtra("reserve", editText.getText().toString());
            }
            setResult(RESULT_OK, intent);
            finish();
        }

        else if (id == R.id.btnClose) {
            finish();
        }

    }
}
