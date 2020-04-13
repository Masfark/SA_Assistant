package com.example.sa_assistant.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sa_assistant.R;

public class SpinCursorAdapter extends CursorAdapter {

    public SpinCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.shop_cursor_adapter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv1 = (TextView) view.findViewById(R.id.tv1);
        TextView tv2 = (TextView) view.findViewById(R.id.tv2);

        String num = cursor.getString(cursor.getColumnIndex("number"));
        String address = cursor.getString(cursor.getColumnIndex("address"));

        tv1.setText(num);
        tv2.setText(address);

    }
}
