package com.tetraval.mochashi.chashimodule.view.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiDashboardActivity;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.MobileActivity;

public class ProfileFragment extends Fragment {

    TextView txtName, txtAddress, txtMobileNumber, txtEmail, txtCurrentCredits, txtLogout;
    SharedPreferences profile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    public ProfileFragment() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtMobileNumber = view.findViewById(R.id.txtMobileNumber);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtCurrentCredits = view.findViewById(R.id.txtCurrentCredits);



        profile = getContext().getSharedPreferences("USER_PROFILE", 0);
        firebaseAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("customer_profiles");
        DocumentReference documentReference = collectionReference.document(profile.getString("p_uid", ""));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String m_credits = task.getResult().getString("p_credits");
                    txtCurrentCredits.setText(m_credits);
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


                txtLogout = view.findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                ((ChashiDashboardActivity)getActivity()).userLogout();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure, want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        setProfileData();

        return view;
    }

    private void setProfileData(){
        txtName.setText(profile.getString("p_nickname", ""));
        txtMobileNumber.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
        txtAddress.setText(profile.getString("p_address", ""));
        txtEmail.setText(profile.getString("p_email", ""));

    }

}
