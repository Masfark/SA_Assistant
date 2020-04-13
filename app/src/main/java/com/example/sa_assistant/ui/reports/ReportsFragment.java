package com.example.sa_assistant.ui.reports;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.SaCursorAdapter;
import com.example.sa_assistant.adapters.ShopCursorAdapter;
import com.example.sa_assistant.adapters.SpinCursorAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.Duration;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.provider.Telephony.Mms.Part.FILENAME;

public class ReportsFragment extends Fragment implements View.OnClickListener {

    ReportsViewModel reportsViewModel;
    TextView txtShops, txtSa, txtReports;
    EditText reportForm;
    Spinner spinShops, spinSa, spinReports;
    Button buttonForm, buttonSend, buttonClear;
    ClipboardManager clipboardManager;
    DBHelper dbHelper;
    String currentDate, time;

    final static String TAG = "MyLogs";

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        reportsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        txtShops = root.findViewById(R.id.txtShops);
        txtSa = root.findViewById(R.id.txtSa);
        txtReports = root.findViewById(R.id.txtReports);

        reportForm = root.findViewById(R.id.reportForm);

        spinShops = root.findViewById(R.id.spinShops);
        spinSa = root.findViewById(R.id.spinSa);
        spinReports = root.findViewById(R.id.spinReports);

        buttonClear = root.findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(this);
        buttonSend = root.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        buttonForm = root.findViewById(R.id.buttonForm);
        buttonForm.setOnClickListener(this);

        clipboardManager = (ClipboardManager)getContext().getSystemService(CLIPBOARD_SERVICE);


        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = date.format(System.currentTimeMillis());


        reportForm.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (reportForm.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                }
                return false;
            }
        });


        ArrayAdapter<CharSequence> emptyChoose = ArrayAdapter.createFromResource(getActivity(), R.array.emptyChoose, R.layout.simple_item_list);

        dbHelper = new DBHelper(getActivity());

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address FROM Shops ORDER BY number ASC", null);

        if (cursor.getCount() == 0) {
            spinShops.setAdapter(emptyChoose);
        }
        else {
            SpinCursorAdapter adapter = new SpinCursorAdapter(getActivity(), cursor);
            spinShops.setAdapter(adapter);
        }

        Cursor cursor1 = database.rawQuery("SELECT _id, username FROM Users", null);

        if (cursor1.getCount() == 0) {
            spinSa.setAdapter(emptyChoose);
        }
        else {
            SaCursorAdapter adapter2 = new SaCursorAdapter(getActivity(), cursor1);
            spinSa.setAdapter(adapter2);
        }

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.ChooseReports, R.layout.simple_item_list);
        spinReports.setAdapter(adapter3);

        return root;
    }


    private String differenceTime(String time, String currentTime) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date t1 = timeFormat.parse(time);
        Date t2 = timeFormat.parse(currentTime);
        long total = t2.getTime() - t1.getTime();
        int hours = (int) (total / (60 * 60 * 1000));
        int minutes = (int) (total / (60 * 1000)) % 60;
        String differenceTime = "";

        if (hours == 0) {
        }
        else if (hours == 1) {
            differenceTime += hours + " час ";
        }
        else if ((hours > 1) && (hours <= 4)) {
            differenceTime += hours + " часа ";
        }
        else {
            differenceTime += hours + " часов ";
        }

        if (minutes > 0) {
            differenceTime += minutes + " минут.";
        }

        return differenceTime;
    }

    private String roundTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String[] timeSplit = timeFormat.format(System.currentTimeMillis()).split(":");

        int n = Integer.parseInt(timeSplit[1]);
        int mod = n % 10;
        Log.d(TAG, "Mod - " + mod);
        int res = 0;
        if (mod <= 5) {
            res = n - mod;
        }
        else {
            res = n - (mod - 5);
        }
        if (res == 0) {
            timeSplit[1] = "00";
        }
        else if (res == 5) {
            timeSplit[1] = "05";
        }
        else {
            timeSplit[1] = Integer.toString(res);
        }

        String time = timeSplit[0] + ":" + timeSplit[1];
        return time;
    }

    private void saveTime() {

        String time = roundTime();

        SQLiteDatabase timeBase = dbHelper.getWritableDatabase();
        Cursor cursor = timeBase.rawQuery("SELECT _id, time FROM Time", null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TIME, time);

        if (cursor.getCount() == 0) {
            timeBase.insert(dbHelper.TABLE_DATE_TIME, null, contentValues);
            timeBase.close();
        }
        else {
            cursor.moveToLast();
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            timeBase.update(dbHelper.TABLE_DATE_TIME, contentValues, dbHelper.KEY_ID_TIME + "= ?", new String[]{id});
        }

    }

    private void loadTime() {

        SQLiteDatabase timeBase = dbHelper.getWritableDatabase();
        Cursor cursor = timeBase.rawQuery("SELECT time FROM Time", null);

        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            time = cursor.getString(cursor.getColumnIndex("time"));
            Log.d(TAG, "Count - " + cursor.getCount());
            Log.d(TAG, "Column - " + time);
        }
        else {
            time = "";
        }

    }

    private void generateReport() throws ParseException {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address, city, ooo FROM Shops ORDER BY number ASC", null);
        Cursor cursor1 = database.rawQuery("SELECT _id, username FROM Users", null);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Reports, R.layout.simple_item_list);

        int idShop = spinShops.getSelectedItemPosition();
        int idSa = spinSa.getSelectedItemPosition();
        int idRep = spinReports.getSelectedItemPosition();
        cursor.moveToPosition(idShop);
        cursor1.moveToPosition(idSa);

        String shopNum = cursor.getString(cursor.getColumnIndex("number"));
        String shopCity = cursor.getString(cursor.getColumnIndex("city"));
        String shopAddr = cursor.getString(cursor.getColumnIndex("address"));
        String shopOOO = cursor.getString(cursor.getColumnIndex("ooo"));
        String saName = cursor1.getString(cursor1.getColumnIndex("username"));

        String currentTime = roundTime();
        String differenceTime = "";
        loadTime();
        if (time.equals("")) {

        }
        else {
            differenceTime = differenceTime(time, currentTime);
            Log.d(TAG, "Difference Time - " + differenceTime);
        }


        String baseReport = (shopNum + " " + shopCity + ", " + shopAddr + "\n\n" + adapter.getItem(idRep)
                + "\n\n" + "СА " + saName);

        String reportBT = ("Магазин " + shopNum + " " + shopCity + ", " + shopAddr + ", юр.лицо ООО " + shopOOO
                + "\n\n" + adapter.getItem(idRep) + "\n\n СА " + saName);

        String firstReport = (shopNum + " " + shopCity + ", " + shopAddr + "\n\n" + adapter.getItem(idRep) + " "
                + currentTime + "\n\n" + "СА " + saName);

        String secondReport = "";

        if (differenceTime.equals("")) {
             secondReport = (shopNum + " " + shopCity + ", " + shopAddr + "\n\n" + adapter.getItem(idRep) + " "
                    + time + " до " + currentTime + "\n\n" + "СА " + saName);
        }
        else {
             secondReport = (shopNum + " " + shopCity + ", " + shopAddr + "\n\n" + adapter.getItem(idRep) + " "
                    + time + " до " + currentTime + " (" + differenceTime + ")" + "\n\n" + "СА " + saName);
        }

        if (idRep == 0) {
            reportForm.setText(baseReport);
        }
        else if (idRep == 1) {
            reportForm.setText(reportBT);
        }
        else if (idRep == 2 || idRep == 4 || idRep == 6 ){
            reportForm.setText(firstReport);
            saveTime();
        }
        else if (idRep == 3 || idRep == 5 || idRep == 7 || idRep == 8 ){
            reportForm.setText(secondReport);
        }


        cursor.close();
        cursor1.close();
        database.close();

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
            else if (check.equals(spinSa.getSelectedItem().toString())) {
                Toast.makeText(getActivity(), "Заполните имя пользователя", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    reportForm.requestFocus();
                    generateReport();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
