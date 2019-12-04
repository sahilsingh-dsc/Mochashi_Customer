package com.tetraval.mochashi.chashimodule.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiModel;
import com.tetraval.mochashi.chashimodule.view.activity.ChashiProductDetailsActivity;

import java.util.List;

public class ChashiAdapter extends RecyclerView.Adapter<ChashiAdapter.ChashiViewHolder> {

    Context context;
    List<ChashiModel> chashiModelList;

    public ChashiAdapter(Context context, List<ChashiModel> chashiModelList) {
        this.context = context;
        this.chashiModelList = chashiModelList;
    }

    @NonNull
    @Override
    public ChashiAdapter.ChashiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chashi_list_item, parent, false);
        return new ChashiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChashiAdapter.ChashiViewHolder holder, int position) {
        final ChashiModel chashiModel = chashiModelList.get(position);

        Glide.with(context).load(chashiModel.getChashi_product_photo1()).into(holder.imgChashiPhoto);
        holder.txtChashiName.setText(chashiModel.getChashi_name());
        holder.txtChashiLocation.setText(chashiModel.getChashi_location());
        holder.ratingBar.setRating(Float.parseFloat(chashiModel.getChashi_rating()));
        holder.txtChashiRate.setText(String.format("₹%s/%s", chashiModel.getChashi_rate(), chashiModel.getChashi_unit()));
        double current_hq = Double.parseDouble(chashiModel.getChashi_hqty());
        double current_bq = Double.parseDouble(chashiModel.getChashi_bqty());
        final double current_aq = current_hq-current_bq;
        holder.txtChashiAvlQuantity.setText(String.format("%s %s", current_aq, chashiModel.getChashi_unit()));
        holder.lvChashi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("p_uid", chashiModel.getChashi_p_uid());
                bundle.putString("p_category", chashiModel.getChashi_p_category());
                bundle.putString("image1", chashiModel.getChashi_product_photo1());
                bundle.putString("image2", chashiModel.getChashi_product_photo2());
                bundle.putString("image3", chashiModel.getChashi_product_photo3());
                bundle.putString("image4", chashiModel.getChashi_product_photo4());
                bundle.putString("chashi_photo", chashiModel.getChashi_photo());
                bundle.putString("chashi_name", chashiModel.getChashi_name());
                bundle.putString("chashi_uid", chashiModel.getChashi_id());
                bundle.putString("chashi_address", chashiModel.getChashi_location());
                bundle.putString("chashi_rating", chashiModel.getChashi_rating());
                bundle.putString("rate", chashiModel.getChashi_rate());
                bundle.putString("avl_quantity", String.valueOf(current_aq));
                bundle.putString("chashi_unit", chashiModel.getChashi_unit());
                bundle.putString("homedelivery", chashiModel.getChashi_homedelivery());
                Intent intent = new Intent(context, ChashiProductDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chashiModelList.size();
    }

    public class ChashiViewHolder extends RecyclerView.ViewHolder {

        ImageView imgChashiPhoto;
        TextView txtChashiName, txtChashiLocation;
        RatingBar ratingBar;
        TextView txtChashiRate, txtChashiAvlQuantity;
        LinearLayout lvChashi;

        public ChashiViewHolder(@NonNull View itemView) {
            super(itemView);

            imgChashiPhoto = itemView.findViewById(R.id.imgChashiPhoto);
            txtChashiName = itemView.findViewById(R.id.txtChashiName);
            txtChashiLocation = itemView.findViewById(R.id.txtChashiLocation);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtChashiRate = itemView.findViewById(R.id.txtChashiRate);
            txtChashiAvlQuantity = itemView.findViewById(R.id.txtChashiAvlQuantity);
            lvChashi = itemView.findViewById(R.id.lvChashi);
        }
    }
}
