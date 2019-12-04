package com.tetraval.mochashi.chashimodule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiOrdersModel;

import java.util.List;

public class ChashiOrderAdapter extends RecyclerView.Adapter<ChashiOrderAdapter.ChashiOrderViewHolder> {

    Context context;
    List<ChashiOrdersModel> chashiOrdersModelList;
    String expand_state;

    public ChashiOrderAdapter(Context context, List<ChashiOrdersModel> chashiOrdersModelList) {
        this.context = context;
        this.chashiOrdersModelList = chashiOrdersModelList;
    }

    @NonNull
    @Override
    public ChashiOrderAdapter.ChashiOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_item, parent, false);
        return new ChashiOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChashiOrderAdapter.ChashiOrderViewHolder holder, int position) {
        final ChashiOrdersModel chashiOrdersModel = chashiOrdersModelList.get(position);
        holder.txtOrderStatus.setText(chashiOrdersModel.getO_status());
        holder.txtChashiName.setText(chashiOrdersModel.getO_chashi_name());
        holder.txtChashiLocation.setText(chashiOrdersModel.getO_chashi_address());
        holder.txtOrderId.setText(chashiOrdersModel.getO_uid());
        holder.txtOrderDate.setText((CharSequence) chashiOrdersModel.getO_timestamp());
        holder.txtOrderCategory.setText(chashiOrdersModel.getO_p_category());
        holder.txtOrderQuantity.setText(chashiOrdersModel.getO_quantity()+" "+chashiOrdersModel.getO_unit());
        holder.txtOrderRate.setText(chashiOrdersModel.getO_rate()+"/"+chashiOrdersModel.getO_unit());
        holder.txtOrderShipping.setText("₹"+chashiOrdersModel.getO_shipping());
        holder.txtOrderTotal.setText("₹"+chashiOrdersModel.getO_total());
        holder.txtProductCategory.setText(chashiOrdersModel.getO_p_category());
        holder.txtOrderProductTotal.setText("₹"+chashiOrdersModel.getO_total());
        holder.ratingBar.setRating(Float.parseFloat(chashiOrdersModel.getO_chashi_rating()));
        Glide.with(context).load(chashiOrdersModel.getO_chashi_photo()).into(holder.imgChashiPhoto);
        expand_state = "unexpanded";

        holder.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expand_state.equals("expanded")){
                    holder.lvExpandBar.setVisibility(View.VISIBLE);
                    holder.txtCancelOrder.setVisibility(View.GONE);
                    holder.imgExpand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                    holder.cardView.setVisibility(View.GONE);
                    expand_state = "unexpanded";
                } else if (expand_state.equals("unexpanded")){
                    if (chashiOrdersModel.getO_status().equals("Pending")){
                        holder.txtCancelOrder.setVisibility(View.VISIBLE);
                    }else if (chashiOrdersModel.getO_status().equals("Confirmed")){
                        holder.txtCancelOrder.setVisibility(View.INVISIBLE);
                    }
                    holder.lvExpandBar.setVisibility(View.GONE);
                    holder.imgExpand.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    holder.cardView.setVisibility(View.VISIBLE);
                    expand_state = "expanded";
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return chashiOrdersModelList.size();
    }

    public class ChashiOrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderStatus, txtChashiName, txtChashiLocation, txtOrderId, txtOrderDate, txtOrderCategory, txtCancelOrder;
        TextView txtOrderQuantity, txtOrderRate, txtOrderShipping, txtOrderTotal, txtProductCategory, txtOrderProductTotal;
        RatingBar ratingBar;
        ImageView imgChashiPhoto, imgExpand;
        CardView cardView;
        LinearLayout lvExpandBar;

        public ChashiOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtChashiName = itemView.findViewById(R.id.txtChashiName);
            txtChashiLocation = itemView.findViewById(R.id.txtChashiLocation);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderCategory = itemView.findViewById(R.id.txtOrderCategory);
            txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity);
            txtOrderRate = itemView.findViewById(R.id.txtOrderRate);
            txtOrderShipping = itemView.findViewById(R.id.txtOrderShipping);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtProductCategory = itemView.findViewById(R.id.txtProductCategory);
            txtOrderProductTotal = itemView.findViewById(R.id.txtOrderProductTotal);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgChashiPhoto = itemView.findViewById(R.id.imgChashiPhoto);
            imgExpand = itemView.findViewById(R.id.imgExpand);
            cardView = itemView.findViewById(R.id.cardView);
            lvExpandBar = itemView.findViewById(R.id.lvExpandBar);
            txtCancelOrder = itemView.findViewById(R.id.txtCancelOrder);

        }
    }
}