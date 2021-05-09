package com.example.sa_assistant.ui.reference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.view.InputDeviceCompat;
import androidx.fragment.app.Fragment;

import com.example.sa_assistant.R;
import com.example.sa_assistant.ui.utils.Utils;

public class ReferenceFragment extends Fragment {

    private Button btnTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reference, container, false);

        TextView textView = root.findViewById(R.id.tv1_ref);
        btnTest = root.findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.Notify(getContext(), "Notification", "Hello World !");
            }
        });


        return root;
    }

    public static byte[] getByteArray(String hex) {
        int length = hex.length() / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex.charAt(i * 2), 16);
            int value = (high << 4) | Character.digit(hex.charAt((i * 2) + 1), 16);
            if (value > 127) {
                value += InputDeviceCompat.SOURCE_ANY;
            }
            raw[i] = (byte) value;
        }
        return raw;
    }

}