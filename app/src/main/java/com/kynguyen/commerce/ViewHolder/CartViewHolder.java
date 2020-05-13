package com.kynguyen.commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.commerce.Intetfave.itemClickLitsner;
import com.kynguyen.commerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public TextView txtProductName, txtProductPrice, txtProductQuantity;
  public itemClickLitsner listenr;

  public CartViewHolder(@NonNull View itemView) {
    super(itemView);
    txtProductName =  itemView.findViewById(R.id.cart_product_name);
    txtProductPrice = itemView.findViewById(R.id.cart_product_price);
    txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
  }


  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }

  public void setListenr(itemClickLitsner listenr) {
    this.listenr = listenr;
  }
}
