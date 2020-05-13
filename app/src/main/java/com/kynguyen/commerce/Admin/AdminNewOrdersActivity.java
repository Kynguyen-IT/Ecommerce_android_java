package com.kynguyen.commerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.commerce.Model.AdminOrders;
import com.kynguyen.commerce.R;
import com.kynguyen.commerce.ViewHolder.AdminOrdersViewHolder;

public class AdminNewOrdersActivity extends AppCompatActivity {
  private RecyclerView orderList;
  private DatabaseReference orderRef;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_new_orders);

    orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

    orderList = findViewById(R.id.order_list);
    orderList.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  protected void onStart() {
    super.onStart();

    FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
        .setQuery(orderRef,AdminOrders.class)
        .build();

    FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
      @Override
      protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
        holder.txtusername.setText("Name: " + model.getName());
        holder.txtaddress.setText("Shipping Address: " + model.getAddress()+ " " + model.getCity());
        holder.txtphone.setText("Phone: " + model.getPhone());
        holder.txtpriceTotal.setText("Total Amount: " + model.getTotalAmount());
        holder.txtdatetime.setText("Order At: " + model.getTime() + " " + model.getDate());

        holder.btn_show_product.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String uID = getRef(position).getKey();

            Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductActivity.class);
            intent.putExtra("uid" , uID);
            startActivity(intent);
          }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            CharSequence options[] = new CharSequence[]{
              "Yes",
              "No"
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
            builder.setTitle("Have you shipped this order products ?");

            builder.setItems(options, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (which == 0 ){
                  String uID = getRef(position).getKey();
                  removerOrder(uID);
                }else {
                  dialog.cancel();
                }

              }
            });
            builder.show();
          }
        });
      }

      @NonNull
      @Override
      public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent, false);
        return new AdminOrdersViewHolder(view);
      }
    };
    orderList.setAdapter(adapter);
    adapter.startListening();
  }

  private void removerOrder(String uID) {
    orderRef.child(uID).removeValue();
  }

}
