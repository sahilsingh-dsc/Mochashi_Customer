package com.tetraval.mochashi.ghmodule.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tetraval.mochashi.R;

import java.util.Objects;

import me.himanshusoni.quantityview.QuantityView;

public class GHProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghproducts);

        Toolbar toolbarGHProducts = findViewById(R.id.toolbarGHProducts);
        setSupportActionBar(toolbarGHProducts);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chickpea");
        toolbarGHProducts.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarGHProducts.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        final MaterialButton btnATC = findViewById(R.id.btnATC);
        final LinearLayout lhSubTotal = findViewById(R.id.lhSubTotal);
        final TextView txtSubTotal = findViewById(R.id.txtSubTotal);
        final MaterialButton btnATC1 = findViewById(R.id.btnATC1);
        final LinearLayout lhSubTotal1 = findViewById(R.id.lhSubTotal1);
        final TextView txtSubTotal1 = findViewById(R.id.txtSubTotal1);
        final EditText txtQInput = findViewById(R.id.txtQInput);
        final LinearLayout lvpdl = findViewById(R.id.lvpdl);
        lvpdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GHProductDetailsActivity.class));
            }
        });

        txtQInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String q_input = txtQInput.getText().toString();
                if (!q_input.isEmpty()) {
                    double qty = 0;
                    qty = Double.parseDouble(q_input);
                    if (qty == 0) {
                        lhSubTotal1.setVisibility(View.GONE);
                        btnATC1.setText(R.string.atc);
                    } else {
                        lhSubTotal1.setVisibility(View.VISIBLE);
                        btnATC1.setText("UPDATE CART");
                        int result = (int) (qty * 100);
                        txtSubTotal1.setText("₹" +result);
                    }
                }else {
                    lhSubTotal1.setVisibility(View.GONE);
                    btnATC1.setText(R.string.atc);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
