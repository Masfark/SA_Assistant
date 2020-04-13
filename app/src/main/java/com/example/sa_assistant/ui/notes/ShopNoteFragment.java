package com.example.sa_assistant.ui.notes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;

public class ShopNoteFragment extends Fragment {

    private int position;
    private String address, note;
    EditText et_shop_note;
    TextView tv_shop_note_address;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position",0);
            address = getArguments().getString("address", "");
            note = getArguments().getString("note", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_note, container, false);

        et_shop_note = root.findViewById(R.id.et_shop_note);
        tv_shop_note_address = root.findViewById(R.id.tv_shop_note_address);
        tv_shop_note_address.setText(address);

        dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        cursor = database.rawQuery("SELECT _id, note FROM Shops", null);

        if (note.equals("")) {
            et_shop_note.requestFocus();
        }
        else {
            et_shop_note.setText(note);
            et_shop_note.requestFocus();
        }

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        cursor.moveToPosition(position);
        String note = et_shop_note.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NOTE, note);
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        database.update(dbHelper.TABLE_SHOPS, contentValues, dbHelper.KEY_ID + "= ?", new String[]{id});
        database.close();
    }
}
