package com.muvi.apisdksampleapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by BISHAL on 08-09-2017.
 */

public class FilterActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(FilterActivity.this,"Filter activity",Toast.LENGTH_SHORT).show();
    }
}
