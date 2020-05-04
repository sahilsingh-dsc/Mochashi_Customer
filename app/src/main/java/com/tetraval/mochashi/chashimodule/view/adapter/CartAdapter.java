package com.tetraval.mochashi.chashimodule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tetraval.mochashi.R;
import com.tetraval.mochashi.chashimodule.model.ChashiCart;
import com.tetraval.mochashi.chashimodule.model.ChashiOrder;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<ChashiOrder> chashiCartList;

    public CartAdapter(Context context, List<ChashiOrder> chashiCartList) {
        this.context = context;
        this.chashiCartList = chashiCartList;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        ChashiOrder chashiCart = chashiCartList.get(position);
        Glide.with(context).load(chashiCart.getOrder_product_image()).into(holder.ivProductImage);
        holder.tvProductName.setText(chashiCart.getOrder_product());
        holder.tvProductQty.setText("Quantity :"+chashiCart.getOrder_quantity()+"Kg");
        holder.tvProductRate.setText("Rate :₹"+chashiCart.getOrder_rate()+"/Kg");
        holder.tvProductAmount.setText("Sub total :"+chashiCart.getOrder_chashi_amount());
        holder.btnRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                CollectionReference customerColRef = db.collection("customer_profiles");
                DocumentReference documentReference = customerColRef.document(firebaseAuth.getCurrentUser().getUid());
                documentReference.collection("mo_cart").document(chashiCart.getOrder_id()).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context, "Product removed from cart!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return chashiCartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName, tvProductQty, tvProductRate, tvProductAmount;
        MaterialButton btnRemoveProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
            tvProductRate = itemView.findViewById(R.id.tvProductRate);
            tvProductAmount = itemView.findViewById(R.id.tvProductAmount);
            btnRemoveProduct = itemView.findViewById(R.id.btnRemoveProduct);

        }
    }
}
