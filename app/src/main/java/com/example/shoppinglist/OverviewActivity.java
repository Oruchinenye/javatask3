package com.example.shoppinglist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.shoppinglist.db.ShoppingContract;
import com.example.shoppinglist.db.ShoppingDbHelper;
import com.example.shoppinglist.db.ShoppingItem;
import com.example.shoppinglist.db.ShoppingItemList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {
    private static final String TAG = "OverviewActivity";
    private ShoppingDbHelper mHelper;
    private ListView mTaskListView;
    private TextView totalValue;
    private ShoppingItemList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);
        mHelper = new ShoppingDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        totalValue = (TextView) findViewById(R.id.total_value);
        updateUI();
    }

    private void updateUI() {
        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
        Double total_price = 0.0;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ShoppingContract.ShoppingEntry.TABLE,
                new String[]{ShoppingContract.ShoppingEntry._ID, ShoppingContract.ShoppingEntry.COL_TASK_PRICE, ShoppingContract.ShoppingEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(ShoppingContract.ShoppingEntry.COL_TASK_ID);
            int itemIndex = cursor.getColumnIndex(ShoppingContract.ShoppingEntry.COL_TASK_TITLE);
            int priceIndex = cursor.getColumnIndex(ShoppingContract.ShoppingEntry.COL_TASK_PRICE);
            total_price += cursor.getDouble(priceIndex);
            shoppingItems.add(
                    new ShoppingItem(cursor.getInt(idIndex),
                            cursor.getString(priceIndex),
                            cursor.getString(itemIndex)
                    )
            );
        }

        if (mAdapter == null) {
            mAdapter = new ShoppingItemList(this,shoppingItems,true);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(shoppingItems);
            mAdapter.notifyDataSetChanged();
        }
        totalValue.setText(String.valueOf(total_price));
        cursor.close();
        db.close();
    }

}
