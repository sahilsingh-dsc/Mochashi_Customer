package com.tetraval.mochashi.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.view.fragment.HomeFragment;
import com.tetraval.mochashi.chashimodule.view.fragment.OrdersFragment;
import com.tetraval.mochashi.chashimodule.view.fragment.ProfileFragment;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.MobileActivity;

import java.util.Objects;

public class ChashiDashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    TextView textCartItemCount;
    int mCartItemCount = 5;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chashi_dashboard);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(ChashiDashboardActivity.this);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        Toolbar toolbarChashiDashboard = findViewById(R.id.toolbarChashiDashboard);
        setSupportActionBar(toolbarChashiDashboard);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.chashi_online));
        toolbarChashiDashboard.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarChashiDashboard.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();
            setProfileData();
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to Mochashi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.menu_cart:

            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){

            case R.id.menu_home :
                fragment = new HomeFragment();
                break;

            case R.id.menu_orders :
                fragment = new OrdersFragment();
                break;

            case R.id.menu_profile :
                fragment = new ProfileFragment();
                break;

        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    public void userLogout(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(ChashiDashboardActivity.this, MobileActivity.class));
        finish();
    }


    private void setProfileData(){
        CollectionReference profileCol = db.collection("customer_profiles");
        profileCol.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        DocumentSnapshot document = task.getResult();
                        SharedPreferences profile = getSharedPreferences("USER_PROFILE", 0);
                        SharedPreferences.Editor editor = profile.edit();
                        editor.putString("p_active", document.getString("p_active"));
                        editor.putString("p_address", document.getString("p_address"));
                        editor.putString("p_email", document.getString("p_email"));
                        editor.putString("p_lat", document.getString("p_lat"));
                        editor.putString("p_long", document.getString("p_long"));
                        editor.putString("p_nickname", document.getString("p_nickname"));
                        editor.putString("p_uid", document.getString("p_uid"));
                        editor.apply();
                    } else {
                        Toast.makeText(ChashiDashboardActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChashiDashboardActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

