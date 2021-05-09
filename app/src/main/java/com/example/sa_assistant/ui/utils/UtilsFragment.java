package com.example.sa_assistant.ui.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sa_assistant.R;
import com.example.sa_assistant.ui.shop_list.ActivityAddShop;

import static android.content.Context.CLIPBOARD_SERVICE;


public class UtilsFragment extends Fragment implements View.OnClickListener {

    private final int IMPORT_BUTTON = 100;
    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_utils, container, false);

        Button btn_import = root.findViewById(R.id.btn_import_db);
        btn_import.setOnClickListener(this);
        Button btn_export = root.findViewById(R.id.btn_export_db);
        btn_export.setOnClickListener(this);
        clipboardManager = (ClipboardManager)getContext().getSystemService(CLIPBOARD_SERVICE);

        return root;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_import_db) {
            Intent intent = new Intent(getActivity(), ImportActivity.class);
            startActivityForResult(intent, IMPORT_BUTTON);
        } else if (id == R.id.btn_export_db) {
            String text = Utils.getDataBaseList(getContext());
            clipData = ClipData.newPlainText("copied text", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getActivity(), "Скопировано", Toast.LENGTH_SHORT).show();
        }
    }
}