package com.tetraval.mochashi.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiOrdersModel;
import com.tetraval.mochashi.chashimodule.utils.httpConnect;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class
ChashiProductDetailsActivity extends AppCompatActivity {

    ImageView imgMain, img1, img2, img3, img4, imgChashiPhoto;
    TextView txtChashiName, txtChashiLocation, txtChashiRate, txtChashiAvlQuantity, txtSubTotal, txtShippingCharge, txtGrandTotal, txtNoDelivery;
    RatingBar ratingBar;
    TextInputEditText txtQuantityRequired;
    RadioGroup rgDelivery;
    RadioButton rbYes, rbNo;
    MaterialButton btnBookOrder;
    double vendor_lat, vendor_long;
    double is_ship = 0.0;
    double shipping = 0.0, min = 0.0, m_credits = 0.0;

    String shipping_charge, min_shipping;
    String pickup_state = "No";
    SharedPreferences profile;
    double chashi_rate, quantity;
    double current_hqty, current_bqty;
    private String DistanceResult, DurationResult;
    private double customer_chashi_distance;
    String v_address;
    LinearLayout lhCredit;
    CheckBox cbCredit;
    TextView txtCreditAmount;
    String check_state = "No";

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chashi_product_details);

        Bundle bundle = getIntent().getExtras();
        final String image1 = bundle.getString("image1");
        final String image2 = bundle.getString("image2");
        final String image3 = bundle.getString("image3");
        final String image4 = bundle.getString("image4");
        final String chashi_photo = bundle.getString("chashi_photo");
        final String chashi_name = bundle.getString("chashi_name");
        final String chashi_uid = bundle.getString("chashi_uid");
        final String chashi_address = bundle.getString("chashi_address");
        v_address = chashi_address;
        final String chashi_unit = bundle.getString("chashi_unit");
        final String chashi_rating = bundle.getString("chashi_rating");
        final String rate = bundle.getString("rate");
        final String avl_quantity = bundle.getString("avl_quantity");
        final String homedelivery = bundle.getString("homedelivery");
        final String p_uid = bundle.getString("p_uid");
        final String p_category = bundle.getString("p_category");

        lhCredit = findViewById(R.id.lhCredit);
        cbCredit = findViewById(R.id.cbCredit);
        cbCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    check_state = "Yes";
                    doCalculations(homedelivery);
                }else {
                    check_state = "No";
                    doCalculations(homedelivery);
                }
            }
        });
        txtCreditAmount = findViewById(R.id.txtCreditAmount);

        profile = getSharedPreferences("USER_PROFILE", 0);

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
//        imgChashiPhoto = findViewById(R.id.imgChashiPhoto);
        txtChashiName = findViewById(R.id.txtChashiName);
        txtChashiLocation = findViewById(R.id.txtChashiLocation);
        txtChashiRate = findViewById(R.id.txtChashiRate);
        txtChashiAvlQuantity = findViewById(R.id.txtChashiAvlQuantity);
        txtSubTotal = findViewById(R.id.txtSubTotal);
        txtShippingCharge = findViewById(R.id.txtShippingCharge);
        txtGrandTotal = findViewById(R.id.txtGrandTotal);
        txtNoDelivery = findViewById(R.id.txtNoDelivery);
        ratingBar = findViewById(R.id.ratingBar);
        txtQuantityRequired = findViewById(R.id.txtQuantityRequired);
        rgDelivery = findViewById(R.id.rgDelivery);
        rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        btnBookOrder = findViewById(R.id.btnBookOrder);

        rbNo.setChecked(true);

        rbYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickup_state = "Yes";
                doCalculations(homedelivery);
            }
        });

        rbNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickup_state = "No";
                doCalculations(homedelivery);
            }
        });

        setProductDetails(
                image1,
                image2,
                image3,
                image4,
                chashi_photo,
                chashi_name,
                chashi_address,
                chashi_unit,
                chashi_rating,
                rate,
                avl_quantity,
                homedelivery
        );

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

        txtQuantityRequired.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtQuantityRequired.getText().toString().isEmpty()){
                    chashi_rate = 0.0;
                    quantity = 0.0;
                  //  shipping_charge = "0";
                    doCalculations(homedelivery);

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtQuantityRequired.getText().toString().isEmpty()){
                    chashi_rate = Double.parseDouble(rate);
                    quantity = Double.parseDouble("0"+txtQuantityRequired.getText().toString());
                    shipping = Double.parseDouble(shipping_charge);
                    min = Double.parseDouble(min_shipping);
                    doCalculations(homedelivery);
                } else {
                    chashi_rate = 0.0;
                    quantity = 0.0;
                   // shipping_charge = "0";
                    shipping = 0.0;
                    min = 0.0;
                    doCalculations(homedelivery);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (txtQuantityRequired.getText().toString().isEmpty()){
                    chashi_rate = 0.0;
                    quantity = 0.0;
                  //  shipping_charge = "0";
                    shipping = 0.0;
                    min = 0.0;
                    doCalculations(homedelivery);
                }
            }
        });

        CollectionReference shippingColRef = db.collection("system_constants");
        DocumentReference shippingDocRef = shippingColRef.document("shipping_charges");
        shippingDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    shipping_charge = snapshot.getString("chashi_shipping");
                    min_shipping = snapshot.getString("min_shipping");
                }else {
                    Toast.makeText(ChashiProductDetailsActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final String customer_uid = profile.getString("p_uid", "");
        final String customer_name = profile.getString("p_nickname", "");
        final String customer_address = profile.getString("p_address", "");
        final String customer_lat = profile.getString("p_lat", "");
        final String customer_long = profile.getString("p_long", "");


        btnBookOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity_req = txtQuantityRequired.getText().toString();
                String shipping_raw = txtShippingCharge.getText().toString();
                String shipping_plus = shipping_raw.replace("+  ", "");
                String shipping_final = shipping_plus.replace("₹", "");
                String total_raw = txtGrandTotal.getText().toString();
                String total_final = total_raw.replace("₹", "");
                if (TextUtils.isEmpty(quantity_req)){
                    txtQuantityRequired.setError("Please enter quantity");
                    return;
                }

                double req = Double.parseDouble(txtQuantityRequired.getText().toString());
                if (req <= 0){
                    txtQuantityRequired.setError("Quantity shuld not be 0");
                    return;
                }
                double c_avl = current_hqty - current_bqty;
                if (req > c_avl){
                    txtQuantityRequired.setError("Quantity cannot be more than available quantity "+c_avl+" "+chashi_unit);
                    return;
                }

                if (shipping_final.isEmpty() || total_final.isEmpty()){
                    Toast.makeText(ChashiProductDetailsActivity.this, "Something went wrong (cpd -> calculations)", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(ChashiProductDetailsActivity.this, "is ship :"+is_ship+"  min :"+min, Toast.LENGTH_LONG).show();

                if (is_ship > min  ||  is_ship == min){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    startActivity(new Intent(getApplicationContext(), ChashiDashboardActivity.class));
                                    finish();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:


                                    placeOrder(

                                            p_uid,
                                            p_category,
                                            customer_uid,
                                            customer_name,
                                            customer_address,
                                            chashi_uid,
                                            chashi_name,
                                            chashi_photo,
                                            chashi_address,
                                            chashi_rating,
                                            rate,
                                            quantity_req,
                                            shipping_final,
                                            total_final,
                                            homedelivery,
                                            pickup_state,
                                            chashi_unit,
                                            customer_lat,
                                            customer_long

                                    );


                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChashiProductDetailsActivity.this);
                    builder.setTitle("Want to buy more?");
                    builder.setMessage("You can buy more product at this low shipping charge").setPositiveButton("Yes Please", dialogClickListener)
                            .setNegativeButton("NO, JUST PLACE ORDER", dialogClickListener).show();
                }


                           }
        });

        SearchDistanceCommand();


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

        CollectionReference collectionReference = db.collection("customer_profiles");
        collectionReference.document(profile.getString("p_uid", "")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            m_credits = Double.parseDouble(task.getResult().getString("p_credits"));
                            txtCreditAmount.setText("-  "+"₹"+m_credits);
                            if (m_credits > 0){
                                lhCredit.setVisibility(View.VISIBLE);
                            }else {
                                m_credits = 0.0;
                                lhCredit.setVisibility(View.GONE);
                            }
                        }else
                        {
                            Toast.makeText(ChashiProductDetailsActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void setProductDetails(String image1, String image2, String image3, String image4, String chashi_photo, String chashi_name, String chashi_address, String chashi_unit, String chashi_rating, String rate, String avl_quantity, String homedelivery){
        Glide.with(getApplicationContext()).load(image1).into(imgMain);
        Glide.with(getApplicationContext()).load(image1).into(img1);
        Glide.with(getApplicationContext()).load(image2).into(img2);
        Glide.with(getApplicationContext()).load(image3).into(img3);
        Glide.with(getApplicationContext()).load(image4).into(img4);
//        Glide.with(getApplicationContext()).load(chashi_photo).into(imgChashiPhoto);
        txtChashiName.setText(chashi_name);
        txtChashiLocation.setText(chashi_address);
        ratingBar.setRating(Float.parseFloat(chashi_rating));
        txtChashiRate.setText("₹"+rate+"/"+chashi_unit);
        txtChashiAvlQuantity.setText(avl_quantity+" "+chashi_unit);

        if (homedelivery.equals("Yes")){
            txtNoDelivery.setVisibility(View.GONE);
            rgDelivery.setVisibility(View.GONE);
            txtShippingCharge.setText("+  "+"₹"+0.0);
        } else {
            if (homedelivery.equals("No")){
                txtNoDelivery.setVisibility(View.VISIBLE);
                rgDelivery.setVisibility(View.VISIBLE);
            }
        }


    }

    private void doCalculations(String homedelivery){
        if (check_state.equals("Yes")){

            double subtotal = (quantity*chashi_rate);
            double final_ship = shipping*customer_chashi_distance*quantity;
            if (final_ship < min){
                final_ship = min;
                Toast.makeText(this, ""+final_ship, Toast.LENGTH_SHORT).show();
            }
            is_ship = final_ship;

//            Toast.makeText(this, " :st: "+subtotal+" :ship: "+shipping+" :km: "+customer_chashi_distance+"  :qty:  "+quantity, Toast.LENGTH_SHORT).show();
            double grandtotal;
            if (homedelivery.equals("No") && pickup_state.equals("No")){
                txtSubTotal.setText("₹"+subtotal);
                txtShippingCharge.setText("+  "+"₹"+final_ship);
                grandtotal = subtotal+final_ship-m_credits;
                txtGrandTotal.setText("₹"+grandtotal);
            } else {
                txtSubTotal.setText("₹"+subtotal);
                txtShippingCharge.setText("+  "+"₹"+0.0);
                grandtotal = subtotal+0.0-m_credits;
                txtGrandTotal.setText("₹"+grandtotal);
            }

        } else if (check_state.equals("No")){

            double subtotal = (quantity*chashi_rate);
            double final_ship = shipping*customer_chashi_distance*quantity;
            if (final_ship < min){
                final_ship = min;
                Toast.makeText(this, ""+final_ship, Toast.LENGTH_SHORT).show();
            }
            is_ship = final_ship;
           // Toast.makeText(this, " :st: "+subtotal+" :ship: "+shipping+" :km: "+customer_chashi_distance+"  :qty:  "+quantity, Toast.LENGTH_SHORT).show();
            double grandtotal;
            if (homedelivery.equals("No") && pickup_state.equals("No")){
                txtSubTotal.setText("₹"+subtotal);
                txtShippingCharge.setText("+  "+"₹"+final_ship);
                grandtotal = subtotal+final_ship;
                txtGrandTotal.setText("₹"+grandtotal);
            } else {
                txtSubTotal.setText("₹"+subtotal);
                txtShippingCharge.setText("+  "+"₹"+0.0);
                grandtotal = subtotal+0.0;
                txtGrandTotal.setText("₹"+grandtotal);
            }

        }

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

        if (check_state.equals("Yes")){
            Map map = new HashMap();
            map.put("p_credits", "0");
            CollectionReference handqQtyCol = db.collection("customer_profiles");
            handqQtyCol.document(profile.getString("p_uid", "")).update(map);
        }
        Toast.makeText(this, "Order Placed!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ChashiProductDetailsActivity.this, ChashiDashboardActivity.class));
        finish();

    }

    private void updateBookedQuantity(String p_uid){
        double bqty = current_bqty+quantity;
        String final_b = String.valueOf(bqty);
        Map map = new HashMap();
        map.put("p_bquantity", final_b);
        CollectionReference handqQtyCol = db.collection("chashi_products");
        handqQtyCol.document(p_uid).update(map);
    }

    public void reset() {

        DistanceResult = "";
        DurationResult = "";

    }


    public void SearchDistanceCommand() {
            reset();
            new AsyncTaskParseJson().execute();

    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        String FormattedStartLocation = profile.getString("p_address", "").replaceAll(" ", "+");
        String FormattedGoalLocation = v_address.replaceAll(" ", "+");

        String yourServiceUrl = getString(R.string.api_url) + FormattedStartLocation + "&destinations=" + FormattedGoalLocation
                + getString(R.string.key_of_api);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                httpConnect jParser = new httpConnect();
                String json = jParser.getJSONFromUrl(yourServiceUrl);
                JSONObject object = new JSONObject(json);


                JSONArray array = object.getJSONArray("rows");


                JSONObject route = array.getJSONObject(0);


                JSONArray elements = route.getJSONArray("elements");


                JSONObject element = elements.getJSONObject(0);


                JSONObject durationObject = element.getJSONObject("duration");
                String duration = durationObject.getString("text");
                DurationResult = duration;


                JSONObject distanceObject = element.getJSONObject("distance");
                String distance = distanceObject.getString("text");
                DistanceResult = distance;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if (DistanceResult == null || DurationResult == null) {
                Toast ResultErrorHandle = Toast.makeText(ChashiProductDetailsActivity.this, "We could not find any results! Sorry!", Toast.LENGTH_SHORT);
                ResultErrorHandle.show();
            }

            if (DistanceResult.indexOf("km") != -1) {
                DistanceResult = DistanceResult.replaceAll("[^\\d.]", "");
                double km = Double.parseDouble(DistanceResult);
                customer_chashi_distance = km;
               // Toast.makeText(ChashiProductDetailsActivity.this, ""+km, Toast.LENGTH_SHORT).show();

            }
        }
    }




}
