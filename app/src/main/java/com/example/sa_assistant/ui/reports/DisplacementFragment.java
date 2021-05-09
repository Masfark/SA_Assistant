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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.Adapters;

public class DisplacementFragment extends Fragment implements View.OnClickListener{


    private Button buttonSend, buttonForm, buttonClear;
    private CheckBox checkRes;
    private EditText reportForm;
    private Spinner spinFrom, spinTo;
    private DBHelper dbHelper;

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
        checkRes = root.findViewById(R.id.checkReserve);

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
            Adapters.SpinCursorAdapter adapter = new Adapters.SpinCursorAdapter(getActivity(), cursor);
            spinFrom.setAdapter(adapter);
            spinTo.setAdapter(adapter);
        }

        return root;
    }

    public String generateReport() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, city, address, reserve_number FROM Shops ORDER BY number ASC", null);

        if (cursor.getCount() != 0)
        {
            int idShopFrom = spinFrom.getSelectedItemPosition();
            int idShopTo = spinTo.getSelectedItemPosition();

            cursor.moveToPosition(idShopFrom);

            String shopNum = cursor.getString(cursor.getColumnIndex("number"));
            String shopCity = cursor.getString(cursor.getColumnIndex("city"));
            String shopAddr = cursor.getString(cursor.getColumnIndex("address"));
            String part1 = "";

            if (checkRes.isChecked()) {
                String shopReserve = cursor.getString(cursor.getColumnIndex("reserve_number"));
                if (shopReserve != null) {
                    part1 = ("Прошу сделать перемещение из резерва №" + shopReserve + " магазина №" + shopNum + "(" + shopCity + ", " + shopAddr + ")\nна ");
                } else {
                    part1 = ("Прошу сделать перемещение из магазина №" + shopNum + "(" + shopCity + ", " + shopAddr + ")\nна ");
                }
            } else {
                part1 = ("Прошу сделать перемещение из магазина №" + shopNum + "(" + shopCity + ", " + shopAddr + ")\nна ");
            }

            cursor.moveToPosition(idShopTo);
            shopNum = cursor.getString(cursor.getColumnIndex("number"));
            shopCity = cursor.getString(cursor.getColumnIndex("city"));
            shopAddr = cursor.getString(cursor.getColumnIndex("address"));
            String part2 = "";

            if (checkRes.isChecked()) {
                String shopReserve = cursor.getString(cursor.getColumnIndex("reserve_number"));
                if (shopReserve != null) {
                    part2 = ("резерв \n№" + shopReserve + " магазина №" + shopNum + "(" + shopCity + ", " + shopAddr + ")\n\n");
                } else {
                    part2 = ("магазин \n№" + shopNum + "(" + shopCity + ", " + shopAddr + ")\n\n");
                }
            } else {
                part2 = ("магазин \n№" + shopNum + "(" + shopCity + ", " + shopAddr + ")\n\n");
            }


            String report = (part1 + part2 + "код товара ");

            return report;
        }
        return "В списке нет магазинов";
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
