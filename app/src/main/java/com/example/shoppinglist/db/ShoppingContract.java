package com.example.shoppinglist.db;

import android.provider.BaseColumns;

public class ShoppingContract {
    public static final String DB_NAME = "com.example.shoppinglist.db";
    public static final int DB_VERSION = 1;

    public class ShoppingEntry implements BaseColumns {
        public static final String TABLE = "shopping";

        public static final String COL_TASK_ID = "_id";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_PRICE = "price";
    }
}
