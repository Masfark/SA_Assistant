package com.example.sa_assistant.ui.shop_list;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.Adapters;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class ShopListFragment extends Fragment {

    public static RecyclerView shopList;
    private DBHelper dbHelper;
    private FloatingActionButton fab;

    final static String TAG = "MyLogs";
    final int ADD_BUTTON = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_shop_list, container, false);

        shopList = root.findViewById(R.id.shop_list_view);
        fab = root.findViewById(R.id.floatingActionButton);
        dbHelper = new DBHelper(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        shopList.setLayoutManager(linearLayoutManager);

        shopListCreate(getContext(), dbHelper, shopList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAddShop.class);
                startActivityForResult(intent, ADD_BUTTON);
            }
        });

        return root;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        shopListCreate(getContext(), dbHelper, shopList);
        return super.onContextItemSelected(item);
    }

    public static void okClick(Context context, DBHelper dbHelper, RecyclerView shopList) {
        shopListCreate(context, dbHelper, shopList);
    }


    public static void shopListCreate(Context context, DBHelper dbHelper, RecyclerView shopList) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Shops ORDER BY number ASC", null);
        Adapters.ShopCursorAdapter adapter = new Adapters.ShopCursorAdapter(context, cursor);
        shopList.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_BUTTON) {

                ContentValues contentValues = new ContentValues();

                String num = data.getStringExtra("shopNum");
                String city = data.getStringExtra("shopCity");
                String add = data.getStringExtra("shopAddress");
                String ooo = data.getStringExtra("shopOOO");

                if (data.getStringExtra("reserve") != null) {
                    String reserve = data.getStringExtra("reserve");
                    contentValues.put(DBHelper.KEY_RES_NUM, reserve);
                }


                contentValues.put(DBHelper.KEY_NUMBER, num);
                contentValues.put(DBHelper.KEY_CITY, city);
                contentValues.put(DBHelper.KEY_ADDRESS, add);
                contentValues.put(DBHelper.KEY_OOO, ooo);

                database.insert(DBHelper.TABLE_SHOPS, null, contentValues);
                database.close();

                shopListCreate(getContext(), dbHelper, shopList);
            }
        } else {
            return;
        }

        database.close();
    }

    @Override
    public void onResume() {
        shopListCreate(getContext(), dbHelper, shopList);
        super.onResume();
    }
}
