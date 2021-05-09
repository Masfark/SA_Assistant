package com.example.sa_assistant.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.interfaces.ItemClickListener;
import com.example.sa_assistant.ui.shop_list.ActivityAddShop;
import com.example.sa_assistant.ui.shop_list.ChangeShopActivity;
import com.example.sa_assistant.ui.shop_list.ShopListFragment;

public class Adapters {

    public static class BotReportsAdapter extends RecyclerView.Adapter<BotReportsAdapter.ViewHolder> {

        public Cursor cursor, cursor1, cursor2;
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bot_reports, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                                Integer idAd = getAdapterPosition();
                                Log.d(TAG, "Position adapter - " + idAd);
                                cursor2 = database.rawQuery("SELECT _id, report, report_check FROM Kbsa_reports", null);
                                cursor2.moveToPosition(idAd);
                                String id = cursor2.getString(cursor2.getColumnIndex("_id"));
                                Log.d(TAG, "Position cursor - " + id);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DBHelper.KEY_REPORT_CHECK, "true");
                                database.update(DBHelper.TABLE_KBSA_REPORTS, contentValues, DBHelper.KEY_ID_REPORT + "= ?", new String[] {id});
                            }
                            else {
                                Integer idAd = getAdapterPosition();
                                Log.d(TAG, "Position adapter - " + idAd);
                                cursor2 = database.rawQuery("SELECT _id, report, report_check FROM Kbsa_reports", null);
                                cursor2.moveToPosition(idAd);
                                String id = cursor2.getString(cursor2.getColumnIndex("_id"));
                                Log.d(TAG, "Position cursor - " + id);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(DBHelper.KEY_REPORT_CHECK, "false");
                                database.update(DBHelper.TABLE_KBSA_REPORTS, contentValues, DBHelper.KEY_ID_REPORT + "= ?", new String[] {id});
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


    public static class SpinBotRepAdapter extends CursorAdapter {
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


    public static class ShopCursorAdapter extends RecyclerView.Adapter<ShopCursorAdapter.ViewHolder> {

        public Cursor cursor;
        private Context context;
        public DBHelper dbHelper;
        private ItemClickListener clickListener;

        public ShopCursorAdapter(Context context, Cursor cursor) {
            this.cursor = cursor;
            this.context = context;

            dbHelper = new DBHelper(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shop_cursor, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {


            TextView tv1, tv2;
            final int IDM_DEL = 1;
            final int IDM_CHANGE = 2;
            final int IDM_DEL_ALL = 3;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                itemView.setOnCreateContextMenuListener(this);
                itemView.setOnClickListener(this);

            }

            void Bind(Cursor cursor, int position) {

                if (cursor.getCount() == 0) {
                    tv2.setText("Список магазинов пуст");
                }
                else {
                    cursor.moveToPosition(position);

                    String num = cursor.getString(cursor.getColumnIndex("number"));
                    String address = cursor.getString(cursor.getColumnIndex("address"));

                    tv1.setText(num);
                    tv2.setText(address);
                }

            }

            @Override
            public void onClick(View view) {
                if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                if (cursor.getCount() == 0) {
                    return;
                }
                else {
                    contextMenu.add(Menu.NONE, IDM_DEL, Menu.NONE, "Удалить магазин").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            cursor.moveToPosition(getAdapterPosition());
                            String id = cursor.getString(cursor.getColumnIndex("_id"));
                            SQLiteDatabase database = dbHelper.getWritableDatabase();
                            database.delete(DBHelper.TABLE_SHOPS, DBHelper.KEY_ID + "= ?", new String[]{id});
                            database.close();
                            return false;
                        }
                    });
                    contextMenu.add(Menu.NONE, IDM_CHANGE, Menu.NONE, "Изменить").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            cursor.moveToPosition(getAdapterPosition());
                            Intent intent = new Intent(context, ChangeShopActivity.class);
                            intent.putExtra("shopID", cursor.getString(cursor.getColumnIndex("_id")));
                            intent.putExtra("shopNum", cursor.getString(cursor.getColumnIndex("number")));
                            intent.putExtra("shopCity", cursor.getString(cursor.getColumnIndex("city")));
                            intent.putExtra("shopAddress", cursor.getString(cursor.getColumnIndex("address")));
                            intent.putExtra("shopOOO", cursor.getString(cursor.getColumnIndex("ooo")));
                            if (cursor.getString(cursor.getColumnIndex("reserve_number")) != null) {
                                intent.putExtra("reserve", cursor.getString(cursor.getColumnIndex("reserve_number")));
                            }
                            context.startActivity(intent);
                            return false;
                        }
                    });
                    contextMenu.add(Menu.NONE, IDM_DEL_ALL, Menu.NONE, "Очистить список").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            AlertDialog.Builder checkBuilder = new AlertDialog.Builder(context);
                            checkBuilder.setMessage("Вы действительно хотите удалить список магазинов ?")
                                    .setTitle("Внимание !")
                                    .setCancelable(false)
                                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            SQLiteDatabase database = dbHelper.getWritableDatabase();
                                            database.delete(DBHelper.TABLE_SHOPS, null, null);
                                            database.close();
                                            ShopListFragment.okClick(context, dbHelper, ShopListFragment.shopList);
                                        }
                                    })
                                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert = checkBuilder.create();
                            alert.show();
                            return false;
                        }
                    });
                }
            }

        }

    }


    public static class SaCursorAdapter extends CursorAdapter {
        public SaCursorAdapter (Context context, Cursor cursor) {
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
                String name_sa = cursor.getString(cursor.getColumnIndex("username"));
                tv3.setText(name_sa);
            }
        }
    }


    public static class SpinCursorAdapter extends CursorAdapter {

        public SpinCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.adapter_shop_cursor, parent, false);
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

}