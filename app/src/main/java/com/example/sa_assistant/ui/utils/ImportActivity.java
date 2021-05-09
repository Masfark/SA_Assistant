package com.example.sa_assistant.ui.utils;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sa_assistant.R;

public class ImportActivity extends AppCompatActivity {

    private EditText editText_import;
    private Button btn_import, btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        editText_import = findViewById(R.id.ed_import);

        btn_import = findViewById(R.id.btn_import);
        btn_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_string = editText_import.getText().toString();
                Utils.setDataBaseList(getBaseContext(), input_string);
                editText_import.setText("");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }
        });
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}