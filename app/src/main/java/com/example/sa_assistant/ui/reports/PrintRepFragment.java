package com.example.sa_assistant.ui.reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.Adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrintRepFragment extends Fragment implements View.OnClickListener{


    public String date1, date2, serial;

    public Button buttonSend, buttonForm, buttonClear;
    public ImageButton btn_first_date, btn_second_date;
    public EditText reportForm, ed_sn;
    public TextView tv_fist_date, tv_second_date;
    public Spinner spinShops;
    public DBHelper dbHelper;
    public Calendar dateAndTime1 = Calendar.getInstance(), dateAndTime2 = Calendar.getInstance();

    final static String TAG = "MyLogs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_print_rep, container, false);

        buttonForm = root.findViewById(R.id.buttonForm);
        buttonForm.setOnClickListener(this);
        buttonSend = root.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        buttonClear = root.findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(this);
        btn_first_date = root.findViewById(R.id.btn_first_date);
        btn_first_date.setOnClickListener(this);
        btn_second_date = root.findViewById(R.id.btn_second_date);
        btn_second_date.setOnClickListener(this);

        tv_fist_date = root.findViewById(R.id.tv_first_date);
        tv_second_date = root.findViewById(R.id.tv_second_date);

        spinShops = root.findViewById(R.id.spin_shops);
        ed_sn = root.findViewById(R.id.ed_sn);
        reportForm = root.findViewById(R.id.reportForm);

        dbHelper = new DBHelper(getActivity());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address FROM Shops ORDER BY number ASC", null);

        ArrayAdapter<CharSequence> emptyChoose = ArrayAdapter.createFromResource(getActivity(), R.array.emptyChoose, R.layout.simple_item_list);

        if (cursor.getCount() == 0) {
            spinShops.setAdapter(emptyChoose);
        } else {
            Adapters.SpinCursorAdapter adapter = new Adapters.SpinCursorAdapter(getActivity(), cursor);
            spinShops.setAdapter(adapter);
        }

        return root;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonClear) {
            reportForm.setText("");
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
        else if (id == R.id.buttonForm) {
            String check = "Список пуст";

            if (check.equals(spinShops.getSelectedItem().toString())) {
                Toast.makeText(getActivity(), "Заполните список магазинов", Toast.LENGTH_SHORT).show();
            }
            else {
                reportForm.requestFocus();
                Report();
            }
        }
        else if (id == R.id.btn_first_date) {
            setDate1();
        }
        else if (id == R.id.btn_second_date) {
            setDate2();
        }
    }

    public void setDate1() {

        DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dateAndTime1.set(Calendar.YEAR, year);
                dateAndTime1.set(Calendar.MONTH, monthOfYear);
                dateAndTime1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                date1 = df.format(dateAndTime1.getTime());
                tv_fist_date.setText(df.format(dateAndTime1.getTime()));
            }
        };

        new DatePickerDialog(getActivity(), myCallBack,
                dateAndTime1.get(Calendar.YEAR),
                dateAndTime1.get(Calendar.MONTH),
                dateAndTime1.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    public void setDate2() {

        DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                dateAndTime2.set(Calendar.YEAR, year);
                dateAndTime2.set(Calendar.MONTH, monthOfYear);
                dateAndTime2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                date2 = df.format(dateAndTime2.getTime());
                tv_second_date.setText(df.format(dateAndTime2.getTime()));
            }
        };

        new DatePickerDialog(getActivity(), myCallBack,
                dateAndTime2.get(Calendar.YEAR),
                dateAndTime2.get(Calendar.MONTH),
                dateAndTime2.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    public void Report () {

        String work_time = ColumnDays();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, city, address FROM Shops ORDER BY number ASC", null);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        int idShop = spinShops.getSelectedItemPosition();

        cursor.moveToPosition(idShop);

        String shopNum = cursor.getString(cursor.getColumnIndex("number"));
        String shopCity = cursor.getString(cursor.getColumnIndex("city"));
        String shopAddr = cursor.getString(cursor.getColumnIndex("address"));

        String rep = ("№" + shopNum + ", " + shopCity + ", " + shopAddr + "\n"
                        + "Принтер: SN:" + ed_sn.getText() + "\n"
                        + "Количество страниц: \n"
                        + "Время наработки: " + work_time + "\n"
                        + "Проблема: \n\n"
                        + "Перемещение: Пт" + shopNum + "-0 от " + df.format(dateAndTime2.getTime()) + "\n"
                        + "РЦ Подольск");

        reportForm.setText(rep);

    }

    public String ColumnDays () {

        long difference = dateAndTime2.getTimeInMillis()  - dateAndTime1.getTimeInMillis();
        int allDays = (int) (difference/(24*60*60*1000));
        int weeks = allDays / 7;
        int days = allDays % 7;

        String part1, part2, work_time;

        if (weeks <= 20 && weeks >= 10){
            part1 = weeks + " недель ";
        }
        else if (weeks % 10 == 1) {
            part1 = weeks + " неделя ";
        }
        else if (weeks % 10 <= 4 && weeks % 10 != 0){
            part1 = weeks + " недели ";
        }
        else {
            part1 = weeks + " недель ";
        }

        if (days == 0) {
                part2 = "";
            }
        else if (days == 1) {
            part2 = days + " день";
        }
        else if (days <= 4) {
            part2 = days + " дня";
        }
        else {
            part2 = days + " дней";
        }

        work_time = part1 + part2;
        return work_time;
    }
}
