package com.kynguyen.commerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.commerce.Model.AdminOrders;
import com.kynguyen.commerce.R;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
  public TextView txtusername, txtphone, txtaddress, txtpriceTotal, txtdatetime;
  public Button btn_show_product;
  public AdminOrdersViewHolder(@NonNull View itemView) {
    super(itemView);
    txtusername = itemView.findViewById(R.id.order_username);
    txtphone = itemView.findViewById(R.id.order_phone);
    txtaddress = itemView.findViewById(R.id.order_address);
    txtpriceTotal = itemView.findViewById(R.id.order_total_price);
    txtdatetime = itemView.findViewById(R.id.order_date_time);
    btn_show_product = itemView.findViewById(R.id.Show_all_products);
  }

  @Override
  public void onClick(View v) {

  }
}