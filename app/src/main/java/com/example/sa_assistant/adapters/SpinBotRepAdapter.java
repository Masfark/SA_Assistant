package com.example.sa_assistant.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sa_assistant.R;

public class SpinBotRepAdapter extends CursorAdapter {
    public SpinBotRepAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.simple_item_list, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        if (cursor.getCount() == 0){
            TextView tv3 = (TextView) view.findViewById(R.id.tv3);
            tv3.setText("Список пуст");
        }
        else {
            TextView tv3 = (TextView) view.findViewById(R.id.tv3);
            String name_sa = cursor.getString(cursor.getColumnIndex("report"));
            tv3.setText(name_sa);
        }
    }
}

