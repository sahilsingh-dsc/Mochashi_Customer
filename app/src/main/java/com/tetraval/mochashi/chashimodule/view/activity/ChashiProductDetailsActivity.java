package com.tetraval.mochashi.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.tetraval.mochashi.chashimodule.model.ChashiOrdersModel;
import com.tetraval.mochashi.chashimodule.utils.httpConnect;
import com.tetraval.mochashi.genericmodule.view.activity.authmodule.MobileActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class
ChashiProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgMain, img1, img2, img3, img4, imgChashiPhoto;
    TextView txtChashiName, txtChashiLocation, txtChashiRate, txtChashiAvlQuantity, txtSubTotal, txtShippingCharge, txtGrandTotal, txtNoDelivery;
    RatingBar ratingBar;
    TextInputEditText txtQuantityRequired;
    MaterialButton btnBookOrder;
    private FirebaseAuth firebaseAuth;
    double chashi_rate, quantity;
    double current_hqty, current_bqty;
    private ProgressDialog progressDialog;

    TextView textCartItemCount;
    int mCartItemCount = 0;

    double dmSubTotal;
    Bundle bundle;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chashi_product_details);

        bundle = getIntent().getExtras();
        final String image1 = bundle.getString("image1");
        final String image2 = bundle.getString("image2");
        final String image3 = bundle.getString("image3");
        final String image4 = bundle.getString("image4");
        final String chashi_photo = bundle.getString("chashi_photo");
        final String chashi_name = bundle.getString("chashi_name");
        final String chashi_uid = bundle.getString("chashi_uid");
        final String chashi_unit = bundle.getString("chashi_unit");
        final String chashi_rating = bundle.getString("chashi_rating");
        final String rate = bundle.getString("rate");
        final String avl_quantity = bundle.getString("avl_quantity");
        final String p_uid = bundle.getString("p_uid");
        final String p_category = bundle.getString("p_category");

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        db = FirebaseFirestore.getInstance();
        fetchCurrentQty(p_uid);

        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout.setSuffixText(chashi_unit);

        Toolbar toolbarChashiProductDetails = findViewById(R.id.toolbarChashiProductDetails);
        setSupportActionBar(toolbarChashiProductDetails);
        Objects.requireNonNull(getSupportActionBar()).setTitle(chashi_name);
        toolbarChashiProductDetails.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarChashiProductDetails.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        imgMain = findViewById(R.id.imgMain);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        txtChashiName = findViewById(R.id.txtChashiName);
        txtChashiLocation = findViewById(R.id.txtChashiLocation);
        txtChashiRate = findViewById(R.id.txtChashiRate);
        txtChashiAvlQuantity = findViewById(R.id.txtChashiAvlQuantity);
        txtSubTotal = findViewById(R.id.txtSubTotal);
        ratingBar = findViewById(R.id.ratingBar);
        txtQuantityRequired = findViewById(R.id.txtQuantityRequired);
        btnBookOrder = findViewById(R.id.btnBookOrder);
        btnBookOrder.setOnClickListener(this);

        setProductDetails(
                image1,
                image2,
                image3,
                image4,
                chashi_name,
                chashi_unit,
                chashi_rating,
                rate,
                avl_quantity
        );



        txtQuantityRequired.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mQty = txtQuantityRequired.getText().toString();
                double mRate = Double.parseDouble(rate);
                if (!mQty.isEmpty()){
                    double dmQty = Double.parseDouble(mQty);
                    doCalculation(dmQty, mRate);
                } else {
                    doCalculation(0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(image1).into(imgMain);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(image2).into(imgMain);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(image3).into(imgMain);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext()).load(image4).into(imgMain);
            }
        });

        if (firebaseAuth.getCurrentUser() != null){
            getCartItemCount();
        }


    }

    private void doCalculation(double dmQty, double mRate) {
        double dmSubTotal = dmQty*mRate;
        txtSubTotal.setText("₹"+dmSubTotal);
    }

    private void fetchCurrentQty(String p_uid){
        CollectionReference handqQtyCol = db.collection("chashi_products");
        handqQtyCol.document(p_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    current_hqty = Double.parseDouble(snapshot.getString("p_hquantity"));
                    current_bqty = Double.parseDouble(snapshot.getString("p_bquantity"));
                }
            }
        });

    }

    private void setProductDetails(String image1, String image2, String image3, String image4, String chashi_name, String chashi_unit, String chashi_rating, String rate, String avl_quantity){
        Glide.with(getApplicationContext()).load(image1).into(imgMain);
        Glide.with(getApplicationContext()).load(image1).into(img1);
        Glide.with(getApplicationContext()).load(image2).into(img2);
        Glide.with(getApplicationContext()).load(image3).into(img3);
        Glide.with(getApplicationContext()).load(image4).into(img4);
        txtChashiName.setText(chashi_name);
        ratingBar.setRating(Float.parseFloat(chashi_rating));
        txtChashiRate.setText("₹"+rate+"/"+chashi_unit);
        txtChashiAvlQuantity.setText(avl_quantity+" "+chashi_unit);

    }

    private void placeOrder(String p_uid, String p_category, String customer_uid, String customer_name, String customer_address, String chashi_uid, String chashi_name, String chashi_photo, String chashi_address, String chashi_rating, String rate, String quantity_req, String shipping_final, String total_final, String homedelivery, String pickup_state, String chashi_unit, String customer_lat, String customer_long){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        CollectionReference orderColRef = db.collection("chashi_orders");
        DocumentReference orderDocRef = orderColRef.document();
        String o_uid = orderDocRef.getId();
        ChashiOrdersModel chashiOrdersModel = new ChashiOrdersModel();
        chashiOrdersModel.setO_timestamp(formatter.format(date).toString());
        chashiOrdersModel.setO_uid(o_uid);
        chashiOrdersModel.setO_p_uid(p_uid);
        chashiOrdersModel.setO_p_category(p_category);
        chashiOrdersModel.setO_customer_uid(customer_uid);
        chashiOrdersModel.setO_customer_name(customer_name);
        chashiOrdersModel.setO_customer_address(customer_address);
        chashiOrdersModel.setO_chashi_uid(chashi_uid);
        chashiOrdersModel.setO_chashi_name(chashi_name);
        chashiOrdersModel.setO_chashi_photo(chashi_photo);
        chashiOrdersModel.setO_chashi_address(chashi_address);
        chashiOrdersModel.setO_chashi_rating(chashi_rating);
        chashiOrdersModel.setO_rate(rate);
        chashiOrdersModel.setO_quantity(String.valueOf(quantity));
        chashiOrdersModel.setO_shipping(shipping_final);
        chashiOrdersModel.setO_total(total_final);
        chashiOrdersModel.setO_homedelivery(homedelivery);
        chashiOrdersModel.setO_pickup(pickup_state);
        chashiOrdersModel.setO_status("Pending");
        chashiOrdersModel.setO_unit(chashi_unit);
        chashiOrdersModel.setO_lat(customer_lat);
        chashiOrdersModel.setO_long(customer_long);
        chashiOrdersModel.setP_delivery_status("0");
        chashiOrdersModel.setP_received_qty("0");
        orderColRef.document(o_uid).set(chashiOrdersModel);
        updateBookedQuantity(p_uid);

        Toast.makeText(this, "Added To Cart!", Toast.LENGTH_SHORT).show();

    }

    private void updateBookedQuantity(String p_uid){
        double bqty = current_bqty+quantity;
        String final_b = String.valueOf(bqty);
        Map map = new HashMap();
        map.put("p_bquantity", final_b);
        CollectionReference handqQtyCol = db.collection("chashi_products");
        handqQtyCol.document(p_uid).update(map);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBookOrder){
            progressDialog.show();
            doAddToCart();
        }
    }

    private void doAddToCart() {
        if (firebaseAuth.getCurrentUser() != null){
            CollectionReference customerColRef = db.collection("customer_profiles");
            DocumentReference customerDocRef = customerColRef.document();
            String order_id = customerDocRef.getId();
            Toast.makeText(this, ""+order_id, Toast.LENGTH_SHORT).show();
            ChashiOrder chashiOrder = new ChashiOrder();

            chashiOrder.setOrder_id(order_id);
            chashiOrder.setOrder_product(bundle.getString("p_category"));
            chashiOrder.setOrder_product_image(bundle.getString("image1"));
            chashiOrder.setOrder_quantity(txtQuantityRequired.getText().toString());
            chashiOrder.setOrder_rate(bundle.getString("rate"));
            chashiOrder.setOrder_shipping("10");
            chashiOrder.setOrder_pickup_address("no address");
            chashiOrder.setOrder_delivery_address("no address");
            chashiOrder.setOrder_chashi_name(bundle.getString("chashi_name"));
            chashiOrder.setOrder_chashi_id(bundle.getString("chashi_uid"));
            chashiOrder.setOrder_chashi_amount(txtSubTotal.getText().toString());
            chashiOrder.setOrder_customer_name("Sahil Singh");
            chashiOrder.setOrder_customer_id(firebaseAuth.getCurrentUser().getUid());
            chashiOrder.setOrder_chashi_amount(txtSubTotal.getText().toString());
            chashiOrder.setOrder_status("pending");

            customerDocRef = customerColRef.document(firebaseAuth.getCurrentUser().getUid());

            customerDocRef.collection("mo_cart")
                    .document(order_id)
                    .set(chashiOrder)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                btnBookOrder.setText("Added to Cart");
                                getCartItemCount();
                                setupBadge();
                                new MaterialAlertDialogBuilder(ChashiProductDetailsActivity.this)
                                        .setTitle("Added To Cart")
                                        .setMessage("Your product added to cart now you can either checkout or continue to shop more.")
                                        .setPositiveButton("Continue Shopping", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(ChashiProductDetailsActivity.this, ChashiDashboardActivity.class));
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("Checkout", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(ChashiProductDetailsActivity.this, ChashiCartActivity.class));
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ChashiProductDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ChashiProductDetailsActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressDialog.dismiss();
            startActivity(new Intent(ChashiProductDetailsActivity.this, MobileActivity.class));
        }
    }

    private void getCartItemCount() {
        CollectionReference customerColRef = db.collection("customer_profiles");
        DocumentReference documentReference = customerColRef.document(firebaseAuth.getCurrentUser().getUid());
        documentReference.collection("mo_cart")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        mCartItemCount = queryDocumentSnapshots.getDocuments().size();
                        setupBadge();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth.getCurrentUser() != null){
            getCartItemCount();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            getCartItemCount();
        }
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
        if (firebaseAuth.getCurrentUser() != null){

            setupBadge();

            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(menuItem);
                    startActivity(new Intent(ChashiProductDetailsActivity.this, ChashiCartActivity.class));
                }
            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.menu_cart:

            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

}
