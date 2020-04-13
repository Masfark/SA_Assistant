package com.example.sa_assistant.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa_assistant.R;
import com.example.sa_assistant.interfaces.ItemClickListener;


public class ShopCursorAdapter extends RecyclerView.Adapter<ShopCursorAdapter.ViewHolder>{

    public Cursor cursor;
    private ItemClickListener clickListener;

    public ShopCursorAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_cursor_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv1, tv2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
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
    }

}

