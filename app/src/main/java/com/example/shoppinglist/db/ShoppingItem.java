package com.example.shoppinglist.db;

public class ShoppingItem {
    public int item__id;
    public String item_price;
    public String item_title;

    public ShoppingItem(int _id, String _price , String _title) {
        item__id = _id;
        item_price = _price;
        item_title = _title;
    }
}
