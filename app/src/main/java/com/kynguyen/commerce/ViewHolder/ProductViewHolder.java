package com.kynguyen.commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kynguyen.commerce.Intetfave.itemClickLitsner;

import com.kynguyen.commerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public TextView txvProduct_name, txvProduct_description, txvProduct_price;
  public ImageView imageView;
  public itemClickLitsner listenr;
  public ProductViewHolder(View itemView) {
    super(itemView);
    imageView = (ImageView) itemView.findViewById(R.id.show_product_image);
    txvProduct_name = (TextView) itemView.findViewById(R.id.show_product_name);
    txvProduct_description = (TextView) itemView.findViewById(R.id.show_product_description);
    txvProduct_price = (TextView) itemView.findViewById(R.id.show_product_price);
  }

  public void setItemClickListen(itemClickLitsner listenr){
    this.listenr= listenr;
  }



  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }
}
