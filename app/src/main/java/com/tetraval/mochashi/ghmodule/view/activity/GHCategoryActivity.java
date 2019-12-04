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

public class GHCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghcategory);

        Toolbar toolbarGhCategory = findViewById(R.id.toolbarGhCategory);
        setSupportActionBar(toolbarGhCategory);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ramu Grocery Shop");
        toolbarGhCategory.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarGhCategory.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        LinearLayout lvChashiCategory = findViewById(R.id.lvChashiCategory);
        lvChashiCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GHProductsActivity.class));
            }
        });

    }
}
