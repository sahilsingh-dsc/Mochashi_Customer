package com.tetraval.mochashi.chashimodule.view.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiOrdersModel;
import com.tetraval.mochashi.chashimodule.view.adapter.ChashiOrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    RecyclerView recyclerOrders;
    List<ChashiOrdersModel> chashiOrdersModelList;
    ChashiOrderAdapter chashiOrderAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    TextView txtNoOrders;
    SharedPreferences profile;
    FirebaseAuth firebaseAuth;

    private int limit = 10;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;

    public OrdersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        txtNoOrders = view.findViewById(R.id.txtNoOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        chashiOrdersModelList = new ArrayList<>();
        chashiOrdersModelList.clear();

        profile = getActivity().getSharedPreferences("USER_PROFILE", 0);

        progressDialog.show();
        fetchOrders();

        return view;
    }

    private void fetchOrders() {

        CollectionReference orderRef = db.collection("mo_orders");
        Query query = orderRef.whereEqualTo("order_customer_id", firebaseAuth.getCurrentUser().getUid());
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().getDocuments().isEmpty()) {
                                for (DocumentSnapshot snapshot : task.getResult()) {
                                    ChashiOrdersModel chashiOrdersModel = new ChashiOrdersModel();
                                    chashiOrdersModel.setO_uid(snapshot.getString("order_id"));
                                    chashiOrdersModel.setO_p_category(snapshot.getString("order_product"));
                                    chashiOrdersModel.setO_customer_uid(snapshot.getString("order_customer_id"));
                                    chashiOrdersModel.setO_chashi_uid(snapshot.getString("order_chashi_id"));
                                    chashiOrdersModel.setO_chashi_name(snapshot.getString("order_chashi_name"));
                                    chashiOrdersModel.setO_chashi_photo(snapshot.getString("order_product_image"));
                                    chashiOrdersModel.setO_rate(snapshot.getString("order_rate"));
                                    chashiOrdersModel.setO_quantity(snapshot.getString("order_quantity"));
                                    chashiOrdersModel.setO_total(snapshot.getString("order_chashi_amount"));
                                    chashiOrdersModel.setO_status(snapshot.getString("order_status"));
                                    chashiOrdersModelList.add(chashiOrdersModel);
                                }
                                progressDialog.dismiss();
                                chashiOrderAdapter = new ChashiOrderAdapter(getContext(), chashiOrdersModelList);
                                recyclerOrders.setAdapter(chashiOrderAdapter);

                            } else {
                                progressDialog.dismiss();
                                txtNoOrders.setVisibility(View.VISIBLE);
                                recyclerOrders.setVisibility(View.INVISIBLE);

                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    });

                }


    }