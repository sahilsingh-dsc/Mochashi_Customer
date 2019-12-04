package com.tetraval.mochashi.chashimodule.view.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiCategoryModel;
import com.tetraval.mochashi.chashimodule.model.ChashiModel;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiActivity;
import com.tetraval.mochashi.chashimodule.view.adapter.ChashiAdapter;
import com.tetraval.mochashi.chashimodule.view.adapter.ChashiCategoryAdapter;
import com.tetraval.mochashi.genericmodule.view.adapter.SliderAdapterExample;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerChashiCategory;
    private ChashiCategoryAdapter chashiCategoryAdapter;
    private List<ChashiCategoryModel> chashiCategoryModelList;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapterExample adapter = new SliderAdapterExample(getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.NONE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.colorPrimary));
        sliderView.setIndicatorUnselectedColor(Color.WHITE);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        db = FirebaseFirestore.getInstance();

        recyclerChashiCategory = view.findViewById(R.id.recyclerChashiCategory);
        recyclerChashiCategory.setHasFixedSize(true);
        recyclerChashiCategory.setNestedScrollingEnabled(false);
        recyclerChashiCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        chashiCategoryModelList = new ArrayList<>();
        chashiCategoryModelList.clear();

        progressDialog.show();
        fetchChashiCategory();

        return view;
    }

    private void fetchChashiCategory(){
        Query queryCategories = db.collection("chashi_categories");
        queryCategories.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() != null){
                        chashiCategoryModelList.clear();
                        for (DocumentSnapshot c_document : task.getResult()){
                            String c_name = c_document.getString("c_name");

                            Query queryProducts = db.collection("chashi_products");
                            queryProducts.whereEqualTo("p_category",c_name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult() != null){
                                            for (DocumentSnapshot document : task.getResult()){
                                        //        Toast.makeText(getContext(), ""+document, Toast.LENGTH_SHORT).show();
                                                if (document.exists()){
                                                    ChashiCategoryModel chashiCategoryModel = new ChashiCategoryModel(
                                                            c_document.getString("c_uid"),
                                                            c_document.getString("c_name"),
                                                            c_document.getString("c_image")
                                                    );
                                                    chashiCategoryModelList.add(chashiCategoryModel);
                                                //    Toast.makeText(getContext(), "exist", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            chashiCategoryAdapter = new ChashiCategoryAdapter(getContext(), chashiCategoryModelList);
                                            recyclerChashiCategory.setAdapter(chashiCategoryAdapter);
                                            progressDialog.dismiss();

                                        }
                                    }
                                }
                            });
                        }

                    }
                }else {
                    Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

}
