package com.example.sa_assistant.ui.reports;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.SpinCursorAdapter;

public class DisplacementFragment extends Fragment implements View.OnClickListener{


    public Button buttonSend, buttonForm, buttonClear;
    public EditText reportForm;
    public Spinner spinFrom, spinTo;
    public DBHelper dbHelper;

    final static String TAG = "MyLogs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_displacement, container, false);

        buttonForm = root.findViewById(R.id.buttonForm);
        buttonForm.setOnClickListener(this);
        buttonSend = root.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        buttonClear = root.findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(this);

        reportForm = root.findViewById(R.id.reportForm);

        spinFrom = root.findViewById(R.id.spinFrom);
        spinTo = root.findViewById(R.id.spinTo);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address FROM Shops ORDER BY number ASC", null);

        ArrayAdapter<CharSequence> emptyChoose = ArrayAdapter.createFromResource(getActivity(), R.array.emptyChoose, R.layout.simple_item_list);

        if (cursor.getCount() == 0) {
            spinFrom.setAdapter(emptyChoose);
            spinTo.setAdapter(emptyChoose);
        } else {
            SpinCursorAdapter adapter = new SpinCursorAdapter(getActivity(), cursor);
            spinFrom.setAdapter(adapter);
            spinTo.setAdapter(adapter);
        }

        return root;
    }

    public String generateReport() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, city, address FROM Shops ORDER BY number ASC", null);

        int idShopFrom = spinFrom.getSelectedItemPosition();
        int idShopTo = spinTo.getSelectedItemPosition();

        cursor.moveToPosition(idShopFrom);

        String shopNum = cursor.getString(cursor.getColumnIndex("number"));
        String shopCity = cursor.getString(cursor.getColumnIndex("city"));
        String shopAddr = cursor.getString(cursor.getColumnIndex("address"));

        String part1 = ("Прошу сделать перемещение с магазина №" + shopNum + "(" + shopCity + ", " + shopAddr + ")\nна магазин\n");

        cursor.moveToPosition(idShopTo);
        shopNum = cursor.getString(cursor.getColumnIndex("number"));
        shopCity = cursor.getString(cursor.getColumnIndex("city"));
        shopAddr = cursor.getString(cursor.getColumnIndex("address"));

        String part2 = ("№" + shopNum + "(" + shopCity + ", " + shopAddr + ")\n\n");
        String report = (part1 + part2 + "код товара ");

        return report;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.buttonForm) {
            String report = generateReport();
            reportForm.setText(report);
        }
        else if (id == R.id.buttonSend) {
            String text = reportForm.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
        else if (id == R.id.buttonClear) {
            reportForm.setText("");
        }
    }
}
