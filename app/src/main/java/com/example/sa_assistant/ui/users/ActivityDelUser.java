package com.example.sa_assistant.ui.users;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.SaCursorAdapter;

public class ActivityDelUser extends AppCompatActivity implements View.OnClickListener {

    Spinner spinDelUser;
    Button btnDelUser, btnClose;
    DBHelper dbHelper;

    final String TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_user);

        spinDelUser = (Spinner) findViewById(R.id.spinDelUser);

        btnDelUser = (Button)findViewById(R.id.btnDelUser);
        btnDelUser.setOnClickListener(this);
        btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, username FROM Users", null);
        SaCursorAdapter adapter = new SaCursorAdapter(this, cursor);
        spinDelUser.setAdapter(adapter);
        database.close();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnDelUser) {
            int spinId = spinDelUser.getSelectedItemPosition();
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = database.rawQuery("SELECT _id, username FROM Users", null);

            if (cursor.getCount() != 0) {
                cursor.moveToPosition(spinId);
                String idUser = cursor.getString(cursor.getColumnIndex("_id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                database.delete(dbHelper.TABLE_USERS, dbHelper.KEY_ID_USER + "= ?", new String[]{idUser});

                Toast.makeText(this, "Пользователь - " + username + " удален.", Toast.LENGTH_SHORT).show();

                database.close();
                finish();
            }
            else {
                finish();
            }
        }

        else if (id == R.id.btnClose) {
            finish();
        }
    }

}
