package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shoppinglist.db.ShoppingContract;
import com.example.shoppinglist.db.ShoppingDbHelper;
import com.example.shoppinglist.db.ShoppingItem;
import com.example.shoppinglist.db.ShoppingItemList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ShoppingDbHelper mHelper;
    private ListView mTaskListView;
    private TextView totalValue;
    private ShoppingItemList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
//           // shoppingItems.add(new String[]{cursor.getString(price),cursor.getString(id)});
            shoppingItems.add(
                    new ShoppingItem(cursor.getInt(idIndex),
                    cursor.getString(priceIndex),
                    cursor.getString(itemIndex)
                    )
            );
        }

        if (mAdapter == null) {
            mAdapter = new ShoppingItemList(this,shoppingItems);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(shoppingItems);
            mAdapter.notifyDataSetChanged();
        }

        View totalContainer = (View) findViewById(R.id.total);
        View buttonContainer = (View) findViewById(R.id.button_container);
        if(shoppingItems.size() > 0){
            totalContainer.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);
        }else{
            totalContainer.setVisibility(View.GONE);
            buttonContainer.setVisibility(View.GONE);
        }
        totalValue.setText(String.valueOf(total_price));
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final View formView = getLayoutInflater().inflate(R.layout.dialog,null);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_title)
                        .setMessage(R.string.dialog_info)
                        .setView(formView)
                        //.setView(dialogView)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText task = (EditText) formView.findViewById(R.id.item);
                                EditText priceText = (EditText) formView.findViewById(R.id.price);
                                Log.d(TAG, "Task to add: " + task.getText().toString() + " Price: " + priceText.getText().toString() );
                                String item = String.valueOf(task.getText());
                                Double price = Double.valueOf(priceText.getText().toString());

                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(ShoppingContract.ShoppingEntry.COL_TASK_TITLE, item);
                                values.put(ShoppingContract.ShoppingEntry.COL_TASK_PRICE, price);
                                db.insertWithOnConflict(ShoppingContract.ShoppingEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.item_id);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ShoppingContract.ShoppingEntry.TABLE,
                ShoppingContract.ShoppingEntry.COL_TASK_ID + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    public void showOverview(View View) {
        Intent intent = new Intent(this,OverviewActivity.class);
        startActivity(intent);
    }
}