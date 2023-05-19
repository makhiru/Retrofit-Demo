package com.example.retrofitdemo;

import android.view.View;

import com.example.retrofitdemo.model.ProductData;

public interface    RecyclerClickListener {
    void onLongClick(View view, ProductData.Productdatas data);
}
