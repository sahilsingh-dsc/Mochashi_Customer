package com.tetraval.mochashi.ghmodule.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.tetraval.mochashi.R;

import java.util.Objects;

import me.himanshusoni.quantityview.QuantityView;

public class GHProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghproduct_details);

        Toolbar toolbarGHProductDetails = findViewById(R.id.toolbarGHProductDetails);
        setSupportActionBar(toolbarGHProductDetails);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Desi Chana");
        toolbarGHProductDetails.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarGHProductDetails.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        final MaterialButton btnATC = findViewById(R.id.btnATC);
        final LinearLayout lhSubTotal = findViewById(R.id.lhSubTotal);
        final TextView txtSubTotal = findViewById(R.id.txtSubTotal);

        final QuantityView quantityView_default = findViewById(R.id.quantityView_default);
        quantityView_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityView_default.requestFocus();
            }
        });
        quantityView_default.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                if (newQuantity == 0){
                    lhSubTotal.setVisibility(View.GONE);
                    btnATC.setText(R.string.atc);
                }else {
                    lhSubTotal.setVisibility(View.VISIBLE);
                    btnATC.setText("UPDATE CART");
                    txtSubTotal.setText("₹"+newQuantity*80);
                }
            }

            @Override
            public void onLimitReached() {

            }
        });

    }
}
