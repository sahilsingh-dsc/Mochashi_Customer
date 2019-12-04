package com.tetraval.mochashi.genericmodule.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiDashboardActivity;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.MobileActivity;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.OTPActivity;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.UserActivity;

import static com.tetraval.mochashi.chashimodule.utils.Constants.SPLASH_DEALY;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String p_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null){
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference profileRef = db.collection("customer_profiles").document(uid);
                    profileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()){
                                    startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                                    finish();
                                }else {
                                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(SplashActivity.this, "database error (otp->profile)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    startActivity(new Intent(getApplicationContext(), UserActivity.class));
//                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), MobileActivity.class));
                    finish();
                }

            }
        }, SPLASH_DEALY);

    }
}
