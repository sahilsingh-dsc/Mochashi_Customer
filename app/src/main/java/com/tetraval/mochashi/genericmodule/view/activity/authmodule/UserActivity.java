package com.tetraval.mochashi.genericmodule.view.activity.authmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rtchagas.pingplacepicker.PingPlacePicker;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiDashboardActivity;
import com.tetraval.mochashi.genericmodule.model.ProfileModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    TextInputEditText tiNickName, tiProfileEmail, tiProfileAddress;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    String l_lat, l_long, l_address;

    private static final int PLACE_PICKER_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbarUser = findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbarUser);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Profile");
        toolbarUser.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarUser.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp));
        toolbarUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Objects.requireNonNull(toolbarUser.getOverflowIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final String p_uid = firebaseAuth.getCurrentUser().getUid();

        tiNickName = findViewById(R.id.tiNickName);
        tiProfileEmail = findViewById(R.id.tiProfileEmail);
        tiProfileAddress = findViewById(R.id.tiProfileAddress);

        tiProfileAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });

        MaterialButton btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String p_nickname = tiNickName.getText().toString();
                String p_email = tiProfileEmail.getText().toString();
                String p_address = tiProfileAddress.getText().toString();
                String p_lat = "0.0";
                String p_long = "0.0";

                if (TextUtils.isEmpty(p_nickname)){
                    Toast.makeText(UserActivity.this, "Nick Name must not be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(p_email)){
                    Toast.makeText(UserActivity.this, "Email must not be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(p_address)){
                    Toast.makeText(UserActivity.this, "Address must not be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(p_lat)){
                    Toast.makeText(UserActivity.this, "Something went wrong (profile->lat)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(p_long)){
                    Toast.makeText(UserActivity.this, "Something went wrong (profile->long)", Toast.LENGTH_SHORT).show();
                    return;
                }

                setProfile(p_uid, p_nickname, p_email, p_address, p_lat, p_long);

            }
        });
    }


    private void setProfile(String p_uid, String p_nickname, String p_email, String p_address, String p_lat, String p_long){

        ProfileModel profileModel = new ProfileModel();
        profileModel.setP_uid(p_uid);
        profileModel.setP_nickname(p_nickname);
        profileModel.setP_email(p_email);
        profileModel.setP_address(p_address);
        profileModel.setP_lat(l_lat);
        profileModel.setP_long(l_long);
        profileModel.setP_credits("0");
        profileModel.setP_active("active");

        db.collection("customer_profiles").document(p_uid)
                .set(profileModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserActivity.this, "Database Error (profile->db): "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyAsSY0Kk-PHj8BYktZY74V2MlyEyTVmc_Y")
                .setMapsApiKey("AIzaSyAsSY0Kk-PHj8BYktZY74V2MlyEyTVmc_Y");

        try {
            Intent placeIntent = builder.build(UserActivity.this);
            startActivityForResult(placeIntent, PLACE_PICKER_REQ_CODE);
        }
        catch (Exception ex) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PLACE_PICKER_REQ_CODE) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                Toast.makeText(this, ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                tiProfileAddress.setText(place.getAddress());
                l_lat = String.valueOf(place.getLatLng().latitude);
                l_long = String.valueOf(place.getLatLng().longitude);
                l_address = place.getAddress();
            }
        }
    }
}
