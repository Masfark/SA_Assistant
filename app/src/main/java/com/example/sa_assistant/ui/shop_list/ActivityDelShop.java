package com.example.sa_assistant.ui.shop_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sa_assistant.R;

public class ActivityDelShop extends AppCompatActivity implements View.OnClickListener {

    EditText etDelShop;
    Button btnDelShop, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_shop);

        etDelShop = (EditText)findViewById(R.id.etDelShop);

        btnDelShop = (Button)findViewById(R.id.btnDelShop);
        btnDelShop.setOnClickListener(this);
        btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnDelShop) {
            Intent intent = new Intent();
            intent.putExtra("shopNum", etDelShop.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }

        else if (id == R.id.btnClose) {
            finish();
        }
    }
}
