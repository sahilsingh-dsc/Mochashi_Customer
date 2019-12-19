package com.tetraval.mochashi.chashimodule.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiCategoryModel;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiActivity;

import java.util.ArrayList;
import java.util.List;

public class ChashiCategoryAdapter extends RecyclerView.Adapter<ChashiCategoryAdapter.ChashiCategoryViewHolder> {

    Context context;
    List<ChashiCategoryModel> chashiCategoryModelList;

    public ChashiCategoryAdapter(Context context, List<ChashiCategoryModel> chashiCategoryModelList) {
        this.context = context;
        this.chashiCategoryModelList = chashiCategoryModelList;
    }

    @NonNull
    @Override
    public ChashiCategoryAdapter.ChashiCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chashi_category_list_item, parent, false);
        return new ChashiCategoryViewHolder(view);
    }

    public void setfilter(List<ChashiCategoryModel> newlist) {
        chashiCategoryModelList = new ArrayList<>();
        chashiCategoryModelList.addAll(newlist);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ChashiCategoryAdapter.ChashiCategoryViewHolder holder, int position) {
        final ChashiCategoryModel chashiCategoryModel = chashiCategoryModelList.get(position);
        Glide.with(context).load(chashiCategoryModel.getC_image()).into(holder.imgCategoryImage);
        holder.txtCategoryName.setText(chashiCategoryModel.getC_name());
        holder.lvChashiCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("c_uid", chashiCategoryModel.getC_uid());
                bundle.putString("c_name", chashiCategoryModel.getC_name());
                Intent intent = new Intent(context, ChashiActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chashiCategoryModelList.size();
    }

    public class ChashiCategoryViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lvChashiCategory;
        ImageView imgCategoryImage;
        TextView txtCategoryName;

        public ChashiCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            lvChashiCategory = itemView.findViewById(R.id.lvChashiCategory);
            imgCategoryImage = itemView.findViewById(R.id.imgCategoryImage);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);

        }
    }
}
