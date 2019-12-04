package com.tetraval.mochashi.genericmodule.view.activity.authmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiDashboardActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbarLogin = findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbarLogin);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.login);
        toolbarLogin.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarLogin.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp));
        toolbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
        Objects.requireNonNull(toolbarLogin.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        MaterialButton mbtnLoginUser = findViewById(R.id.mbtnLoginUser);
        mbtnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
