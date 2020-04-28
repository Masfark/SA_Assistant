package com.example.sa_assistant.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

    public Cursor cursor, cursor1;
    public SQLiteDatabase database;
    public DBHelper dbHelper;
    public Context context;
    final static String TAG = "MyLogs";

    public BotReportsAdapter(Context context) {
        this.context = context;

        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        cursor = database.rawQuery("SELECT _id, report, report_check FROM Kbsa_reports", null);
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

                String report = cursor.getString(cursor.getColumnIndex("report"));

                textView.setText(report);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Integer idAd = getAdapterPosition() + 1;
                            Log.d(TAG, "Position adapter - " + idAd);
                            String id = idAd.toString();
                            Log.d(TAG, "Position cursor - " + id);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(dbHelper.KEY_REPORT_CHECK, "true");
                            database.update(dbHelper.TABLE_KBSA_REPORTS, contentValues, dbHelper.KEY_ID_REPORT + "= ?", new String[] {id});
                        }
                        else {
                            Integer idAd = getAdapterPosition() + 1;
                            Log.d(TAG, "Position adapter - " + idAd);
                            String id = idAd.toString();
                            Log.d(TAG, "Position cursor - " + id);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(dbHelper.KEY_REPORT_CHECK, "false");
                            database.update(dbHelper.TABLE_KBSA_REPORTS, contentValues, dbHelper.KEY_ID_REPORT + "= ?", new String[] {id});
                        }
                    }
                });

                cursor1 = database.rawQuery("SELECT _id, report, report_check FROM Kbsa_reports", null);
                cursor1.moveToPosition(position);
                String checked = cursor1.getString(cursor.getColumnIndex("report_check"));
                checkBox.setChecked(Boolean.parseBoolean(checked));
            }

        }
    }
}
