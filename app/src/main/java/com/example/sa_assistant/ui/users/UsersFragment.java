package com.example.sa_assistant.ui.users;

import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;

public class UsersFragment extends Fragment implements View.OnClickListener {

    EditText editSaName;
    Button addSaButton, delSaButton;
    DBHelper dbHelper;

    final static String TAG = "MyLogs";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);

        editSaName = root.findViewById(R.id.editSaName);

        addSaButton = root.findViewById(R.id.addSaButton);
        addSaButton.setOnClickListener(this);
        delSaButton = root.findViewById(R.id.delSaButton);
        delSaButton.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());

        return root;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.addSaButton) {

            String name = editSaName.getText().toString();

            if (name.equals("")) {
                Toast.makeText(getActivity(), "Введите имя пользователя.", Toast.LENGTH_SHORT).show();
            }
            else {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put(dbHelper.KEY_USERNAME, name);
                database.insert(dbHelper.TABLE_USERS, null, contentValues);

                Toast.makeText(getActivity(), "Пользователь - " + name + " добавлен.", Toast.LENGTH_SHORT).show();
                editSaName.setText("");
            }
        }
        else if (id == R.id.delSaButton) {
            Intent intent = new Intent(getActivity(), ActivityDelUser.class);
            startActivity(intent);
        }
    }
}
