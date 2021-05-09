package com.example.sa_assistant.ui.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.MainActivity;
import com.example.sa_assistant.R;

public class Utils {

    public static void setDataBaseList(Context context, String str) {
        SQLiteDatabase database = new DBHelper(context).getWritableDatabase();
        String[] rows = str.split("\n");
        if (!rows[0].equals("")) {
            for (String row : rows) {
                String[] shop = row.split(",");

                if (shop.length == 4 | shop.length == 5) {

                    ContentValues contentValues = new ContentValues();

                    String num = shop[0];
                    String city = shop[1];
                    String add = shop[2];
                    String ooo = shop[3];

                    contentValues.put(DBHelper.KEY_NUMBER, num);
                    contentValues.put(DBHelper.KEY_CITY, city);
                    contentValues.put(DBHelper.KEY_ADDRESS, add);
                    contentValues.put(DBHelper.KEY_OOO, ooo);

                    if (shop.length == 5) {
                        contentValues.put(DBHelper.KEY_RES_NUM, shop[4]);
                    }
                    database.insert(DBHelper.TABLE_SHOPS, null, contentValues);
                } else {
                    Toast.makeText(context, "Магазин - " + row + " не добавлен", Toast.LENGTH_SHORT).show();
                }
            }
            Toast.makeText(context, "Список загружен", Toast.LENGTH_SHORT).show();
            database.close();
        } else {
            Toast.makeText(context, "Список пуст", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getDataBaseList(Context context) {
        String result, str1 = "";
        SQLiteDatabase database = new DBHelper(context).getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Shops ORDER BY number ASC", null);
        if (cursor.getCount() == 0) {
            return "В базе нет магазинов";
        }
        cursor.moveToFirst();
        do {
            for (int i = 1; i < 6; i++) {
                if (cursor.getString(i) != null) {
                    str1 = str1 + cursor.getString(i) + ",";
                }
            }
            str1 = str1 + "\n";
            str1 = str1.replaceAll("^,|,$", "");

        } while (cursor.moveToNext());
        result = str1;
        database.close();
        return result;
    }

    public static void Notify(Context context, String title, String text) {
        String CHANNEL_ID = "Test notification";
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_announcement_24)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        createNotificationChannel(context, CHANNEL_ID);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }

    private static void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Основные";
            String description = "Основные уведомления";
            int importance = NotificationManager.IMPORTANCE_MAX;
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
