package com.tetraval.mochashi.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiCart;
import com.tetraval.mochashi.chashimodule.model.ChashiOrder;
import com.tetraval.mochashi.chashimodule.view.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChashiCartActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerCart;
    TextView tvEmptyCart;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    List<ChashiOrder> chashiCartList;
    CartAdapter cartAdapter;
    ProgressDialog progressDialog;
    TextView tvGTotal;
    MaterialButton btnCheckout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chashi_cart);

        Toolbar toolbarChashi = findViewById(R.id.toolbarChashiCart);
        setSupportActionBar(toolbarChashi);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chashi Cart");
        toolbarChashi.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarChashi.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        chashiCartList = new ArrayList<>();

        recyclerCart = findViewById(R.id.recyclerCart);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvGTotal = findViewById(R.id.tvGTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        progressDialog.show();
        fetchCart();

    }

    private void fetchCart() {

        CollectionReference customerColRef = db.collection("customer_profiles");
        DocumentReference documentReference = customerColRef.document(firebaseAuth.getCurrentUser().getUid());
        documentReference.collection("mo_cart")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        chashiCartList.clear();
                        double sub_total = 0.0;
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            ChashiOrder chashiOrder = new ChashiOrder();
                            chashiOrder.setOrder_id(snapshot.getString("order_id"));
                            chashiOrder.setOrder_product(snapshot.getString("order_product"));
                            chashiOrder.setOrder_product_image(snapshot.getString("order_product_image"));
                            chashiOrder.setOrder_quantity(snapshot.getString("order_quantity"));
                            chashiOrder.setOrder_rate(snapshot.getString("order_rate"));
                            chashiOrder.setOrder_shipping(snapshot.getString("order_shipping"));

                            chashiOrder.setOrder_pickup_address(snapshot.getString("order_pickup_address"));
                            chashiOrder.setOrder_delivery_address(snapshot.getString("order_delivery_address"));

                            chashiOrder.setOrder_chashi_name(snapshot.getString("order_chashi_name"));
                            chashiOrder.setOrder_chashi_id(snapshot.getString("order_chashi_id"));
                            chashiOrder.setOrder_chashi_amount(snapshot.getString("order_chashi_amount"));
                            chashiOrder.setOrder_customer_name(snapshot.getString("order_customer_name"));
                            chashiOrder.setOrder_customer_id(snapshot.getString("order_customer_id"));
                            chashiOrder.setOrder_chashi_amount(snapshot.getString("order_chashi_amount"));
                            chashiOrder.setOrder_status(snapshot.getString("order_status"));

                            String sub_total_amount = snapshot.getString("order_chashi_amount");
                            double gt_value = Double.parseDouble(sub_total_amount.replace("₹", ""));
                            sub_total = gt_value+sub_total;

                            chashiCartList.add(chashiOrder);
                        }
                        if (chashiCartList.size() == 0){
                            tvEmptyCart.setVisibility(View.VISIBLE);
                            recyclerCart.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            tvGTotal.setText("₹0.0");
                        } else {
                            cartAdapter = new CartAdapter(ChashiCartActivity.this, chashiCartList);
                            cartAdapter.notifyDataSetChanged();
                            recyclerCart.setAdapter(cartAdapter);
                            tvEmptyCart.setVisibility(View.GONE);
                            recyclerCart.setVisibility(View.VISIBLE);
                            tvGTotal.setText("₹"+sub_total);
                            progressDialog.dismiss();
                        }


                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == btnCheckout){
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setTitle("Checkout Alert!")
                    .setMessage("Do you sure want to checkout?, once you press process there will be no coming back.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            progressDialog.setMessage("Checking out...");
                            progressDialog.show();
                            CollectionReference customerColRef = db.collection("customer_profiles");
                            DocumentReference documentReference = customerColRef.document(firebaseAuth.getCurrentUser().getUid());
                            documentReference.collection("mo_cart")
                                    .addSnapshotListener(ChashiCartActivity.this, new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            chashiCartList.clear();
                                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                                ChashiOrder chashiOrder = new ChashiOrder();
                                                chashiOrder.setOrder_id(snapshot.getString("order_id"));
                                                chashiOrder.setOrder_product(snapshot.getString("order_product"));
                                                chashiOrder.setOrder_product_image(snapshot.getString("order_product_image"));
                                                chashiOrder.setOrder_quantity(snapshot.getString("order_quantity"));
                                                chashiOrder.setOrder_rate(snapshot.getString("order_rate"));
                                                chashiOrder.setOrder_shipping(snapshot.getString("order_shipping"));

                                                chashiOrder.setOrder_pickup_address(snapshot.getString("order_pickup_address"));
                                                chashiOrder.setOrder_delivery_address(snapshot.getString("order_delivery_address"));

                                                chashiOrder.setOrder_chashi_name(snapshot.getString("order_chashi_name"));
                                                chashiOrder.setOrder_chashi_id(snapshot.getString("order_chashi_id"));
                                                chashiOrder.setOrder_chashi_amount(snapshot.getString("order_chashi_amount"));
                                                chashiOrder.setOrder_customer_name(snapshot.getString("order_customer_name"));
                                                chashiOrder.setOrder_customer_id(snapshot.getString("order_customer_id"));
                                                chashiOrder.setOrder_chashi_amount(snapshot.getString("order_customer_amount"));
                                                chashiOrder.setOrder_status(snapshot.getString("order_status"));
                                                chashiCartList.add(chashiOrder);

                                                CollectionReference customerColRef = db.collection("mo_orders");
                                                DocumentReference customerDocRef = customerColRef.document();
                                                String order_id = customerDocRef.getId();
                                                customerColRef.document(order_id)
                                                        .set(chashiOrder)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    progressDialog.show();
                                                                    CollectionReference customerColRef = db.collection("customer_profiles");
                                                                    DocumentReference documentReference = customerColRef.document(firebaseAuth.getCurrentUser().getUid());
                                                                    documentReference.collection("mo_cart").document(snapshot.getString("order_id")).delete();
                                                                    cartAdapter.notifyDataSetChanged();
                                                                    progressDialog.dismiss();
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(ChashiCartActivity.this, "Order not created!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(ChashiCartActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                            }


                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
