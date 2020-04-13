package com.example.sa_assistant.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;

public class BotReportsAdapter extends RecyclerView.Adapter<BotReportsAdapter.ViewHolder>{

    public Cursor cursor;
    public SQLiteDatabase database;

    public BotReportsAdapter(SQLiteDatabase database, Cursor cursor) {
        this.cursor = cursor;
        this.database = database;
    }

    @NonNull
    @Override
    public BotReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_reports_adapter, parent, false);
        BotReportsAdapter.ViewHolder viewHolder = new BotReportsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BotReportsAdapter.ViewHolder holder, int position) {
        holder.Bind(cursor, position);
    }

    @Override
    public int getItemCount() {
        if (cursor.getCount() != 0) {
            return cursor.getCount();
        }
        else {
            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;
        DBHelper dbHelper;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_adapter);
            checkBox = itemView.findViewById(R.id.cb_adapter);
        }

        void Bind(final Cursor cursor, int position) {

            if (cursor.getCount() == 0) {
                checkBox.setVisibility(View.INVISIBLE);
                textView.setText("Добавьте отчеты.");


            }
            else {
                cursor.moveToPosition(position);

                final String id = cursor.getString(cursor.getColumnIndex("_id"));
                String report = cursor.getString(cursor.getColumnIndex("report"));
                String checked = cursor.getString(cursor.getColumnIndex("report_check"));

                textView.setText(report);
                checkBox.setChecked(Boolean.parseBoolean(checked));

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(dbHelper.KEY_REPORT_CHECK, "true");
                            database.update(dbHelper.TABLE_KBSA_REPORTS, contentValues, dbHelper.KEY_ID_REPORT + "= ?", new String[] {id});
                        }
                        else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(dbHelper.KEY_REPORT_CHECK, "false");
                            database.update(dbHelper.TABLE_KBSA_REPORTS, contentValues, dbHelper.KEY_ID_REPORT + "= ?", new String[] {id});
                        }
                    }
                });
            }

        }
    }
}
