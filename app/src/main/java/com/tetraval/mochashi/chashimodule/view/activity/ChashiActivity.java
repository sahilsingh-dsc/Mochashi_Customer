package com.tetraval.mochashi.chashimodule.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiModel;
import com.tetraval.mochashi.chashimodule.view.adapter.ChashiAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.security.AccessController.getContext;

public class ChashiActivity extends AppCompatActivity {

    TextView textCartItemCount, txtNoProducts;
    int mCartItemCount = 5;
    RecyclerView recyclerChashi;
    List<ChashiModel> chashiModelList;
    ChashiAdapter chashiAdapter;
    AutoCompleteTextView txtACSortBy;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chashi);

        Bundle bundle = getIntent().getExtras();
        String c_uid = bundle.getString("c_uid");
        String c_name = bundle.getString("c_name");

        Toolbar toolbarChashi = findViewById(R.id.toolbarChashi);
        setSupportActionBar(toolbarChashi);
        Objects.requireNonNull(getSupportActionBar()).setTitle(c_name);
        toolbarChashi.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        Objects.requireNonNull(toolbarChashi.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        txtACSortBy = findViewById(R.id.txtACSortBy);
        String[] SORTS = new String[] {"Low To High Price", "High To Low Price"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(ChashiActivity.this, R.layout.support_simple_spinner_dropdown_item, SORTS);
        txtACSortBy.setAdapter(sortAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        db = FirebaseFirestore.getInstance();

        recyclerChashi = findViewById(R.id.recyclerChashi);
        recyclerChashi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chashiModelList = new ArrayList<>();
        chashiModelList.clear();

        txtNoProducts = findViewById(R.id.txtNoProducts);

        txtACSortBy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sort_type = adapterView.getItemAtPosition(i).toString();
                if (sort_type.equals("Low To High Price")){
                    fetchChashi(c_name);
                    sortByPriceLH();
                    chashiAdapter.notifyDataSetChanged();
                } else if (sort_type.equals("High To Low Price")){
                    fetchChashi(c_name);
                    sortByPriceHL();
                    chashiAdapter.notifyDataSetChanged();
                }
            }
        });

        progressDialog.show();
        fetchChashi(c_name);

    }

    private void sortByPriceHL() {
        Collections.sort(chashiModelList, (l1, l2) -> {
            if (Double.parseDouble(l1.getChashi_rate()) < Double.parseDouble(l2.getChashi_rate())) {
                return 1;
            } else if (Double.parseDouble(l1.getChashi_rate()) > Double.parseDouble(l2.getChashi_rate())) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    private void sortByPriceLH() {
        Collections.sort(chashiModelList, (l1, l2) -> {
            if (Double.parseDouble(l1.getChashi_rate()) > Double.parseDouble(l2.getChashi_rate())) {
                return 1;
            } else if (Double.parseDouble(l1.getChashi_rate()) < Double.parseDouble(l2.getChashi_rate())) {
                return -1;
            } else {
                return 0;
            }
        });
    }


    private void fetchChashi(String c_name){
        Query queryProducts = db.collection("chashi_products");
        queryProducts.whereEqualTo("p_category",c_name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        chashiModelList.clear();
                        for (DocumentSnapshot document : task.getResult()){
                            double c_h_qty = Double.parseDouble(document.getString("p_hquantity"));
                            double c_b_qty = Double.parseDouble(document.getString("p_bquantity"));
                            double c_a_qty = c_h_qty - c_b_qty;
                            if (c_a_qty != 0.0) {
                                ChashiModel chashiModel = new ChashiModel(
                                        document.getString("p_uid"),
                                        document.getString("p_category"),
                                        document.getString("p_chashi_uid"),
                                        document.getString("p_chashi_photo"),
                                        document.getString("p_chashi_name"),
                                        document.getString("p_chashi_address"),
                                        document.getString("p_chashi_rating"),
                                        document.getString("p_rate"),
                                        document.getString("p_hquantity"),
                                        document.getString("p_bquantity"),
                                        document.getString("p_unit"),
                                        document.getString("p_image1"),
                                        document.getString("p_image2"),
                                        document.getString("p_image3"),
                                        document.getString("p_image4"),
                                        document.getString("p_homedelivery")

                                );
                                chashiModelList.add(chashiModel);
                            }
                        }
                        chashiAdapter = new ChashiAdapter(ChashiActivity.this, chashiModelList);
                        recyclerChashi.setAdapter(chashiAdapter);
                        chashiAdapter.notifyDataSetChanged();
                        if (chashiModelList.isEmpty()){
                            txtNoProducts.setVisibility(View.VISIBLE);
                        }else {
                            txtNoProducts.setVisibility(View.GONE);
                        }
                        progressDialog.dismiss();
                    }
                }else {
                    Toast.makeText(ChashiActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.header_menu, menu);
//
//        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
//
//        View actionView = MenuItemCompat.getActionView(menuItem);
//        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
//
//        setupBadge();
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.menu_cart:

            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

}
