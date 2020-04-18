package com.example.shoppinglist.db;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.shoppinglist.R;

import java.util.ArrayList;

public class ShoppingItemList extends ArrayAdapter<ShoppingItem> {
    private final Context context;
    private final ArrayList<ShoppingItem> data;
    private final int layoutResourceId;
    private final boolean viewMode;

    public ShoppingItemList(@NonNull Context context, @NonNull ArrayList<ShoppingItem> objects) {
        this(context,objects , false);
    }

    public ShoppingItemList(@NonNull Context context, @NonNull ArrayList<ShoppingItem> objects, boolean viewMode) {
        super(context,
                R.layout.lists,
                objects
        );
        this.context = context;
        this.data = objects;
        this.layoutResourceId = viewMode? R.layout.list_view: R.layout.lists;
        this.viewMode = viewMode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId,parent, false);
            holder = new ViewHolder();
            holder.id = (TextView)row.findViewById(R.id.item_id);
            holder.price = (TextView)row.findViewById(R.id.item_price);
            holder.title = (TextView)row.findViewById(R.id.item_title);
            row.setTag(holder);
            if (viewMode){
              holder.deleteBtn =   (Button)  row.findViewById(R.id.button);
            }
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }



        ShoppingItem item = data.get(position);

        holder.id.setText(String.valueOf(item.item__id));
        holder.price.setText(item.item_price);
        holder.title.setText(item.item_title);

        return row;
    }

    static class ViewHolder
    {
        TextView id;
        TextView price;
        TextView title;
        Button deleteBtn;
    }
}
