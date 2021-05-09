package com.example.sa_assistant.ui.reports;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.Adapters;

public class ActivityDelBotReport extends AppCompatActivity implements View.OnClickListener {

    Button btnDelBotRep, btnClose;
    Spinner spinReps;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_bot_report);

        btnDelBotRep = findViewById(R.id.btn_del_bot_rep);
        btnDelBotRep.setOnClickListener(this);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        spinReps = findViewById(R.id.spinDelBotRep);

        ArrayAdapter<CharSequence> emptyChoose = ArrayAdapter.createFromResource(this, R.array.emptyChoose, R.layout.simple_item_list);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, report FROM Kbsa_reports", null);

        if (cursor.getCount() == 0) {
            spinReps.setAdapter(emptyChoose);
        }
        else {
            Adapters.SpinBotRepAdapter adapter = new Adapters.SpinBotRepAdapter(this, cursor);
            spinReps.setAdapter(adapter);
        }

    }

    public void delReport() {
        int spinId = spinReps.getSelectedItemPosition();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, report FROM Kbsa_reports", null);
        cursor.moveToPosition(spinId);
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        database.delete(dbHelper.TABLE_KBSA_REPORTS, dbHelper.KEY_ID_REPORT + "= ?", new String[] {id});
        database.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_del_bot_rep) {
            delReport();
            finish();
        }
        else if (id == R.id.btnClose) {
            finish();
        }
    }
}
