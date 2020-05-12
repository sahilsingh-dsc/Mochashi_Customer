package com.tetraval.mochashi.chashimodule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiOrdersModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChashiOrderAdapter extends RecyclerView.Adapter<ChashiOrderAdapter.ChashiOrderViewHolder> {

    Context context;
    List<ChashiOrdersModel> chashiOrdersModelList;
    String expand_state;
    FirebaseFirestore db;

    public ChashiOrderAdapter(Context context, List<ChashiOrdersModel> chashiOrdersModelList) {
        this.context = context;
        this.chashiOrdersModelList = chashiOrdersModelList;
    }

    @NonNull
    @Override
    public ChashiOrderAdapter.ChashiOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_item, parent, false);
        db = FirebaseFirestore.getInstance();
        return new ChashiOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChashiOrderAdapter.ChashiOrderViewHolder holder, int position) {
        final ChashiOrdersModel chashiOrdersModel = chashiOrdersModelList.get(position);
        holder.txtOrderStatus.setText(chashiOrdersModel.getO_status());
        holder.txtChashiName.setText(chashiOrdersModel.getO_chashi_name());
//        holder.txtOrderId.setText(chashiOrdersModel.getO_uid());
        holder.txtOrderCategory.setText(chashiOrdersModel.getO_p_category());
        holder.txtOrderQuantity.setText(chashiOrdersModel.getO_quantity()+"kg");
        holder.txtOrderRate.setText(chashiOrdersModel.getO_rate()+"/kg");
        holder.txtProductCategory.setText(chashiOrdersModel.getO_p_category());
        holder.txtOrderProductTotal.setText(chashiOrdersModel.getO_total());
        holder.txtOrderTotal.setText(chashiOrdersModel.getO_total());
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
                        holder.txtCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map cancelMap = new HashMap();
                                cancelMap.put("order_status", "Cancelled");
                                CollectionReference cancelQuery = db.collection("mo_orders");
                                        cancelQuery.document(chashiOrdersModel.getO_uid())
                                        .update(cancelMap)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                holder.txtOrderStatus.setText("Cancelled");
                                                Toast.makeText(context, "Order Cancelled!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Something went wrong..."+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
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

        TextView txtOrderStatus, txtChashiName, txtOrderId, txtOrderDate, txtOrderCategory, txtCancelOrder, txtOrderTotal;
        TextView txtOrderQuantity, txtOrderRate, txtProductCategory, txtOrderProductTotal;
        ImageView imgChashiPhoto, imgExpand;
        CardView cardView;
        LinearLayout lvExpandBar;

        public ChashiOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtChashiName = itemView.findViewById(R.id.txtChashiName);
//            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderCategory = itemView.findViewById(R.id.txtOrderCategory);
            txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity);
            txtOrderRate = itemView.findViewById(R.id.txtOrderRate);
            txtProductCategory = itemView.findViewById(R.id.txtProductCategory);
            txtOrderProductTotal = itemView.findViewById(R.id.txtOrderProductTotal);
            imgChashiPhoto = itemView.findViewById(R.id.imgChashiPhoto);
            imgExpand = itemView.findViewById(R.id.imgExpand);
            cardView = itemView.findViewById(R.id.cardView);
            lvExpandBar = itemView.findViewById(R.id.lvExpandBar);
            txtCancelOrder = itemView.findViewById(R.id.txtCancelOrder);

        }
    }
}
