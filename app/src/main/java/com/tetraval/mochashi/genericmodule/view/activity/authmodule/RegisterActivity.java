package com.tetraval.mochashi.genericmodule.view.activity.authmodule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.LoginActivity;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // support action and toolbar setup here
        Toolbar toolbarRegister = findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbarRegister);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.register);
        toolbarRegister.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarRegister.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp));
        toolbarRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        Objects.requireNonNull(toolbarRegister.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        // user type exposed dropdown setup here
        String[] account_type = {getString(R.string.customer), getString(R.string.chashi_one), getString(R.string.daily_haat_vendor), getString(R.string.grocery_haat_vendor)};
        ArrayAdapter<String> accountTypeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, account_type);
        AutoCompleteTextView txtACAccountType = findViewById(R.id.txtACAccountType);
        txtACAccountType.setAdapter(accountTypeAdapter);

        // salute type exposed dropdown setup here
        String[] salute_type = {getString(R.string.sir), getString(R.string.madam), getString(R.string.babu), getString(R.string.dada), getString(R.string.didi), getString(R.string.bhai), getString(R.string.mausa), getString(R.string.mausi), getString(R.string.other)};
        ArrayAdapter<String> saluteTypeAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, salute_type);
        AutoCompleteTextView txtCASalute = findViewById(R.id.txtCASalute);
        txtCASalute.setAdapter(saluteTypeAdapter);

        MaterialButton mbtnRegisterUser = findViewById(R.id.mbtnRegisterUser);
        mbtnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });
    }

    private boolean customerFieldValidator(){
        return true;
    }

    private boolean chashiFieldValidator(){
        return true;
    }

    private boolean groceryFieldValidator(){
        return true;
    }

    private void registerUser(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
