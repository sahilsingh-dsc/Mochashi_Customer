package com.tetraval.mochashi.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    double min_charge;
    double charge_weight;
    double charge_amount;
    double total_delivery_charge;

    TextView tvProductTotal, tvDeliveryCharge, tvTotalWeight, tvWeightNotice;

    LinearLayout lhWeight, lhProduct, lhDeivery, lhGrand;




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

        tvProductTotal = findViewById(R.id.tvProductTotal);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalWeight = findViewById(R.id.tvTotalWeight);

        tvWeightNotice = findViewById(R.id.tvWeightNotice);
        lhWeight = findViewById(R.id.lhWeight);
        lhProduct = findViewById(R.id.lhProduct);
        lhDeivery = findViewById(R.id.lhDeivery);
        lhGrand = findViewById(R.id.lhGrand);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        progressDialog.show();
        fetchShipping();

    }

    private void fetchShipping() {
        db.collection("system_constants")
                .document("shipping_charges")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String min = task.getResult().getString("min_shipping");
                    String weight = task.getResult().getString("haat_shipping");
                    String amount = task.getResult().getString("grocery_shipping");
                    min_charge = Double.parseDouble(min);
                    charge_weight = Double.parseDouble(weight);
                    charge_amount = Double.parseDouble(amount);

                    fetchCart();

                }
            }
        });
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
                        double total_weight = 0.0;
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            ChashiOrder chashiOrder = new ChashiOrder();
                            chashiOrder.setOrder_date_time(snapshot.getString("order_date_time"));
                            chashiOrder.setOrder_id(snapshot.getString("order_id"));
                            chashiOrder.setOrder_product_id(snapshot.getString("order_product_id"));
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

                            String weight = snapshot.getString("order_quantity");
                            double t_weight = Double.parseDouble(weight);
                            total_weight = t_weight+total_weight;

                            chashiCartList.add(chashiOrder);
                        }

                        if (chashiCartList.size() == 0){
                            tvEmptyCart.setVisibility(View.VISIBLE);
                            recyclerCart.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            tvProductTotal.setText("₹0.0");
                            tvDeliveryCharge.setText("₹0.0");
                            tvGTotal.setText("₹0.0");
                            tvWeightNotice.setVisibility(View.GONE);
                            tvTotalWeight.setText("0.0");
                            hide();


                        } else {
                            cartAdapter = new CartAdapter(ChashiCartActivity.this, chashiCartList);
                            cartAdapter.notifyDataSetChanged();
                            recyclerCart.setAdapter(cartAdapter);
                            tvEmptyCart.setVisibility(View.GONE);
                            recyclerCart.setVisibility(View.VISIBLE);
                            show();
                            if (total_weight >= charge_weight){
                                total_delivery_charge = total_weight*charge_amount;
                                Toast.makeText(ChashiCartActivity.this, ""+total_delivery_charge, Toast.LENGTH_SHORT).show();
                               tvProductTotal.setText("₹"+sub_total);
                                tvDeliveryCharge.setText("₹"+total_delivery_charge);
                                double grand = sub_total+total_delivery_charge;
                                tvGTotal.setText("₹"+grand);
                                tvTotalWeight.setText(total_weight+"");
                                tvWeightNotice.setVisibility(View.VISIBLE);
                                tvWeightNotice.setText("As the weight is more then "+charge_weight+"kg"+ " so the delivery charge will be calculated according to weight with the rate of ₹"+charge_amount+"/kg");
                            } else {
                                total_delivery_charge = min_charge;
                                Toast.makeText(ChashiCartActivity.this, ""+total_delivery_charge, Toast.LENGTH_SHORT).show();
                                tvProductTotal.setText("₹"+sub_total);
                                tvDeliveryCharge.setText("₹"+total_delivery_charge);
                                double grand = sub_total+total_delivery_charge;
                                tvGTotal.setText("₹"+grand);
                                tvWeightNotice.setVisibility(View.GONE);
                                tvTotalWeight.setText(total_weight+"");
                            }

                         //   Toast.makeText(ChashiCartActivity.this, ""+charge_weight, Toast.LENGTH_LONG).show();
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
                                                chashiOrder.setOrder_date_time(snapshot.getString("order_date_time"));
                                                chashiOrder.setOrder_id(snapshot.getString("order_id"));
                                                chashiOrder.setOrder_product_id(snapshot.getString("order_product_id"));
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
                                                chashiCartList.add(chashiOrder);

                                                db.collection("chashi_products")
                                                        .document(snapshot.getString("order_product_id"))
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                String b_booked = task.getResult().getString("p_bquantity");
                                                                double booked_qty = Double.parseDouble(b_booked);
                                                                String update_qty = snapshot.getString("order_quantity");
                                                                double u_qty = Double.parseDouble(update_qty);

                                                                double final_qty = booked_qty+u_qty;

                                                                Map map = new HashMap();
                                                                map.put("p_bquantity", final_qty+"");

                                                                db.collection("chashi_products")
                                                                        .document(snapshot.getString("order_product_id")).update(map)
                                                                        .addOnCompleteListener(new OnCompleteListener() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task task) {


                                                                                CollectionReference customerColRef = db.collection("mo_orders");
                                                                                DocumentReference customerDocRef = customerColRef.document();
                                                                                customerColRef.document(snapshot.getString("order_id"))
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
                                                                        });

                                                            }
                                                        });

                                            }

                                            if (chashiCartList.isEmpty()){
                                                new MaterialAlertDialogBuilder(ChashiCartActivity.this)
                                                        .setTitle("Checkout Successful!")
                                                        .setMessage("Thanks for using Mochashi as you have ordered stuff at the comfort of a click.")
                                                        .setPositiveButton("Go To Home", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                                startActivity(new Intent(ChashiCartActivity.this, ChashiDashboardActivity.class));
                                                                finish();
                                                            }
                                                        }).show();
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

    private void show(){
        btnCheckout.setVisibility(View.VISIBLE);
        lhGrand.setVisibility(View.VISIBLE);
        lhDeivery.setVisibility(View.VISIBLE);
        lhProduct.setVisibility(View.VISIBLE);
        lhWeight.setVisibility(View.VISIBLE);
    }

    private void hide(){
        btnCheckout.setVisibility(View.GONE);
        lhGrand.setVisibility(View.GONE);
        lhDeivery.setVisibility(View.GONE);
        lhProduct.setVisibility(View.GONE);
        lhWeight.setVisibility(View.GONE);
    }

}
