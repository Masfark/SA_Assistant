package com.example.sa_assistant.ui.notes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.DBHelper;
import com.example.sa_assistant.R;
import com.example.sa_assistant.adapters.ShopCursorAdapter;
import com.example.sa_assistant.interfaces.ItemClickListener;


public class NotesChooseShopFragment extends Fragment implements ItemClickListener {

    RecyclerView rc_note_shop_choose;
    DBHelper dbHelper;
    FragmentTransaction fTrans;
    Fragment fragmentNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes_choose_shop, container, false);

        rc_note_shop_choose = root.findViewById(R.id.rc_note_shop_choose);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rc_note_shop_choose.setLayoutManager(linearLayoutManager);

        dbHelper = new DBHelper(getActivity());

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, address, ooo, note FROM Shops ORDER BY number ASC", null);
        ShopCursorAdapter adapter = new ShopCursorAdapter(cursor);
        rc_note_shop_choose.setAdapter(adapter);
        adapter.setClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view, int position) {
        createNote(position);
    }

    public void createNote(int position) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id, number, city, address, ooo, note FROM Shops ORDER BY number ASC", null);
        cursor.moveToPosition(position);
        String num = cursor.getString(cursor.getColumnIndex("number"));
        String city = cursor.getString(cursor.getColumnIndex("city"));
        String address = cursor.getString(cursor.getColumnIndex("address"));
        String note = cursor.getString(cursor.getColumnIndex("note"));
        String shop = num + " " + city + ", " + address;

        fragmentNote = new ShopNoteFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("address", shop);
        args.putString("note", note);
        fragmentNote.setArguments(args);
        fTrans = getChildFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment_notes_choose_shop, fragmentNote);
        fTrans.addToBackStack("Notes list");
        fTrans.commit();

    }
}
