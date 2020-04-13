package com.example.sa_assistant.ui.reports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;

public class ActivityAddBotReport extends AppCompatActivity implements View.OnClickListener {

    Button btnAddBotRep, btnClose;
    EditText etBotRepText;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bot_report);

        etBotRepText = findViewById(R.id.etRepText);

        btnAddBotRep = findViewById(R.id.btn_add_bot_rep);
        btnAddBotRep.setOnClickListener(this);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        dbHelper = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_add_bot_rep) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(dbHelper.KEY_REPORT, etBotRepText.getText().toString());
            contentValues.put(dbHelper.KEY_REPORT_CHECK, "false");

            database.insert(dbHelper.TABLE_KBSA_REPORTS, null, contentValues);
            database.close();
            finish();
        }
        else if (id == R.id.btnClose) {
            finish();
        }
    }
}
