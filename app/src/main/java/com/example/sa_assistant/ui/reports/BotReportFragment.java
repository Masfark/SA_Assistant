package com.example.sa_assistant.ui.reports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.BotReportsAdapter;
import com.example.sa_assistant.adapters.SpinCursorAdapter;
import com.example.sa_assistant.ui.shop_list.ActivityAddShop;

public class BotReportFragment extends Fragment implements View.OnClickListener {

    Button btnAddRep, btnDelRep, btnClear, btnForm, btnSend;
    EditText edTxtBotReport;
    Spinner spinShops;
    RecyclerView recyclerViewReports;
    DBHelper dbHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bot_report, container, false);

        edTxtBotReport = root.findViewById(R.id.ed_txt_bot_report);
        spinShops = root.findViewById(R.id.spin_shops);
        recyclerViewReports = root.findViewById(R.id.rv_kbsa_reports);

        btnAddRep = root.findViewById(R.id.btn_add_rep);
        btnAddRep.setOnClickListener(this);
        btnDelRep = root.findViewById(R.id.btn_del_rep);
        btnDelRep.setOnClickListener(this);
        btnForm = root.findViewById(R.id.btn_kbsa_rep);
        btnForm.setOnClickListener(this);
        btnClear = root.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
        btnSend = root.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReports.setLayoutManager(linearLayoutManager);

        ArrayAdapter<CharSequence> emptyChoose = ArrayAdapter.createFromResource(getActivity(), R.array.emptyChoose, R.layout.simple_item_list);

        dbHelper = new DBHelper(getActivity());

        edTxtBotReport.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (edTxtBotReport.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                }
                return false;
            }
        });

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address FROM Shops ORDER BY number ASC", null);

        if (cursor.getCount() == 0) {
            spinShops.setAdapter(emptyChoose);
        } else {
            SpinCursorAdapter adapter = new SpinCursorAdapter(getActivity(), cursor);
            spinShops.setAdapter(adapter);
        }

        return root;
    }

    public void reportsListCreate() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, report, report_check FROM Kbsa_reports", null);
        BotReportsAdapter adapter = new BotReportsAdapter(database, cursor);
        recyclerViewReports.setAdapter(adapter);
    }

    public void generateBotReport() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, report, report_check FROM Kbsa_reports", null);
        Cursor cursorShops = database.rawQuery("SELECT _id, number, address, city FROM Shops ORDER BY number ASC", null);

        if (cursorShops.getCount() == 0) {
            Toast.makeText(getActivity(), "Добавьте магазины.", Toast.LENGTH_SHORT).show();
        } else if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Добавьте отчеты.", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            String report = "";
            do {
                String checkStatus = cursor.getString(cursor.getColumnIndex("report_check"));
                if (checkStatus.equals("true")) {
                    String reportText = cursor.getString(cursor.getColumnIndex("report"));

                    int num = (int) (Math.random() * 101);

                    if (num > 51) {
                        report += reportText + ", ";
                    }
                    else {
                        report = reportText + ", " + report;
                    }

                }
            } while (cursor.moveToNext());

            if (report.equals("")) {
                Toast.makeText(getActivity(), "Выберите нужные отчеты.", Toast.LENGTH_SHORT).show();
            } else {
                int idShopSelected = spinShops.getSelectedItemPosition();
                cursorShops.moveToPosition(idShopSelected);
                String shopNum = cursorShops.getString(cursorShops.getColumnIndex("number"));
                String shopCity = cursorShops.getString(cursorShops.getColumnIndex("city"));
                String shopAddr = cursorShops.getString(cursorShops.getColumnIndex("address"));
                report = shopNum + " " + shopCity + ", " + shopAddr + " - " + report.substring(0, report.length() - 2) + ".";
                edTxtBotReport.setText(report);
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_add_rep) {
            Intent intent = new Intent(getActivity(), ActivityAddBotReport.class);
            startActivity(intent);
        } else if (id == R.id.btn_del_rep) {
            Intent intent = new Intent(getActivity(), ActivityDelBotReport.class);
            startActivity(intent);
        } else if (id == R.id.btn_kbsa_rep) {
            generateBotReport();
            edTxtBotReport.requestFocus();
        } else if (id == R.id.btn_clear) {
            edTxtBotReport.setText("");
        } else if (id == R.id.btn_send) {
            String text = edTxtBotReport.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reportsListCreate();
    }
}
