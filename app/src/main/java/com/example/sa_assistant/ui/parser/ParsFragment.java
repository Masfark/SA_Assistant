package com.example.sa_assistant.ui.parser;

import androidx.appcompat.app.AlertDialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ParsFragment extends Fragment implements View.OnClickListener{

    EditText shopsNeedCheckedField;
    TextView shopsCheckedField;
    Button checkButton, clearButton, copyButton;
    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pars, container, false);

        shopsNeedCheckedField = root.findViewById(R.id.editText);
        shopsCheckedField = root.findViewById(R.id.textView);

        checkButton = root.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
        clearButton = root.findViewById(R.id.clear_btn);
        clearButton.setOnClickListener(this);
        copyButton = root.findViewById(R.id.buttonCopy);
        copyButton.setOnClickListener(this);

        clipboardManager = (ClipboardManager)getContext().getSystemService(CLIPBOARD_SERVICE);

        return root;
    }

    public void shopCheck () {

        DBHelper dbHelper = new DBHelper(getActivity());

        String[] shopsNeedChecked;
        String total = "";
        Editable text;
        text = shopsNeedCheckedField.getText();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_SHOPS, null, null, null, null, null, null);
        cursor.moveToFirst();

        if (text.length() == 0) {
            checkField();
            total = " ";
        }
        else if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "Список магазинов пуст.", Toast.LENGTH_SHORT).show();
        }
        else {

            int idNumber = cursor.getColumnIndex(DBHelper.KEY_NUMBER);
            int idAddress = cursor.getColumnIndex(DBHelper.KEY_ADDRESS);
            int idCity = cursor.getColumnIndex(DBHelper.KEY_CITY);

            while (matcher.find()) {
                shopsNeedChecked = matcher.group().split("\n");

                if (cursor.moveToFirst()) {

                    do {

                        if (Long.parseLong(shopsNeedChecked[0]) == cursor.getInt(idNumber)) {
                            total += "Магазин " + cursor.getInt(idNumber) + " - " + cursor.getString(idCity) + ", " +  cursor.getString(idAddress) + "\n";
                        }

                    } while (cursor.moveToNext());

                }
            }
        }

        if (total == "") {
            shopsCheckedField.setText("Моих магазинов в списке нет ...");
        }
        else {
            shopsCheckedField.setText(total);
        }
    }


    public void checkField () {
        AlertDialog.Builder checkBuilder = new AlertDialog.Builder(getActivity());
        checkBuilder.setMessage("Заполните магазины для проверки ...")
                .setTitle("Ошибка")
                .setCancelable(false)
                .setNegativeButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = checkBuilder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.checkButton) {
            shopCheck();
        }
        else if (id == R.id.clear_btn) {
            shopsNeedCheckedField.setText("");
            shopsCheckedField.setText("");
        }
        else if (id == R.id.buttonCopy) {
            String text = shopsCheckedField.getText().toString();
            clipData = ClipData.newPlainText("copied text", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getActivity(), "Скопировано", Toast.LENGTH_SHORT).show();
        }
    }
}
