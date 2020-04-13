package com.example.sa_assistant.ui.shop_list;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.ShopCursorAdapter;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;

public class ShopListFragment extends Fragment implements View.OnClickListener {

    private ShopListViewModel shopListViewModel;
    private RecyclerView shopList;
    private DBHelper dbHelper;

    final static String TAG = "MyLogs";
    final int ADD_BUTTON = 1;
    final int DEL_BUTTON = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        shopListViewModel = ViewModelProviders.of(this).get(ShopListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shop_list, container, false);

        shopList = root.findViewById(R.id.shop_list_view);

        Button add_shop_btn = root.findViewById(R.id.add_shop_btn);
        add_shop_btn.setOnClickListener(this);

        Button del_shop_btn = root.findViewById(R.id.del_shop_btn);
        del_shop_btn.setOnClickListener(this);

        Button clear_btn = root.findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(this);

        dbHelper = new DBHelper(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        shopList.setLayoutManager(linearLayoutManager);


        shopListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //shops.setText(s);

            }
        });

        shopListCreate();

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.add_shop_btn) {

            Intent intent = new Intent(getActivity(), ActivityAddShop.class);
            startActivityForResult(intent, ADD_BUTTON);

        } else if (id == R.id.del_shop_btn) {
            Intent intent = new Intent(getActivity(), ActivityDelShop.class);
            startActivityForResult(intent, DEL_BUTTON);
        } else if (id == R.id.clear_btn) {
            Dialog();
            shopListCreate();
        }

    }


    public void shopListCreate() {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address, ooo FROM Shops ORDER BY number ASC", null);
        ShopCursorAdapter adapter = new ShopCursorAdapter(cursor);
        shopList.setAdapter(adapter);

    }

    public void Dialog() {
        AlertDialog.Builder checkBuilder = new AlertDialog.Builder(getActivity());
        checkBuilder.setMessage("Вы действительно хотите удалить список магазинов ?")
                .setTitle("Внимание !")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(dbHelper.TABLE_SHOPS, null, null);
                        dbHelper.close();
                        shopListCreate();
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_BUTTON) {

                String num = data.getStringExtra("shopNum");
                String city = data.getStringExtra("shopCity");
                String add = data.getStringExtra("shopAddress");
                String ooo = data.getStringExtra("shopOOO");


                ContentValues contentValues = new ContentValues();

                contentValues.put(dbHelper.KEY_NUMBER, num);
                contentValues.put(dbHelper.KEY_CITY, city);
                contentValues.put(dbHelper.KEY_ADDRESS, add);
                contentValues.put(dbHelper.KEY_OOO, ooo);

                database.insert(dbHelper.TABLE_SHOPS, null, contentValues);
                database.close();

                shopListCreate();
            } else if (requestCode == DEL_BUTTON) {
                String num = data.getStringExtra("shopNum");

                database.delete(dbHelper.TABLE_SHOPS, dbHelper.KEY_NUMBER + "= ?", new String[]{num});
                database.close();

                shopListCreate();
            }
        } else {
            return;
        }

        database.close();
    }
}
