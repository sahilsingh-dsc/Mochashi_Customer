package com.tetraval.mochashi.ghmodule.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tetraval.mochashi.R;

import java.util.Objects;

public class GHVendorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghvendor);

        Toolbar toolbarGHVendor = findViewById(R.id.toolbarGHVendor);
        setSupportActionBar(toolbarGHVendor);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Grocery Vendors");
        toolbarGHVendor.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarGHVendor.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        LinearLayout lvChashiCategory = findViewById(R.id.lvChashiCategory);
        lvChashiCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GHCategoryActivity.class));
            }
        });
    }
}
