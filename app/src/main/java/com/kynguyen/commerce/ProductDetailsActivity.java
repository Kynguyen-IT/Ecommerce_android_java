package com.kynguyen.commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.commerce.Model.Products;
import com.kynguyen.commerce.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
  private FloatingActionButton addToCartBtn;
  private ImageView productImage;
  private ElegantNumberButton numberButton;
  private TextView productPrice, productDescription, productName;
  private String productID = "", state = "Normal";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_details);
    productID = getIntent().getStringExtra("pid");
    mapping();
    getProductDetail(productID);

    addToCartBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (state.equals("order Shipped") || state.equals("order Placed")){
          Toast.makeText(ProductDetailsActivity.this, "You can add purchase more products, once your order is shipped or confirmed", Toast.LENGTH_SHORT).show();
        }else {
          addingToCartList();
        }
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    checkOrderState();
  }

  private void addingToCartList() {
    String saveCurrentTime, saveCurrentDate;

    Calendar calForDate = Calendar.getInstance();
    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
    saveCurrentDate = currentDate.format(calForDate.getTime());

    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
    saveCurrentTime = currentTime.format(calForDate.getTime());

    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
    final HashMap<String, Object> cartMap = new HashMap<>();
      cartMap.put("pid", productID);
      cartMap.put("name", productName.getText().toString());
      cartMap.put("price", productPrice.getText().toString());
      cartMap.put("date", saveCurrentDate);
      cartMap.put("time", saveCurrentTime);
      cartMap.put("quantity", numberButton.getNumber());
      cartMap.put("discount", "");

      cartListRef.child("User View").child(Prevalent.currentOnLineUsers.getPhone())
          .child("Products").child(productID)
          .updateChildren(cartMap)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                cartListRef.child("Admin View").child(Prevalent.currentOnLineUsers.getPhone())
                    .child("Products").child(productID)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                          Toast.makeText(ProductDetailsActivity.this, "Added To Cart List", Toast.LENGTH_SHORT).show();

                          Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                          startActivity(intent);
                        }
                      }
                    });
              }
            }
          });
  }

  private void getProductDetail(String productID) {
    DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
    productRef.child(productID).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          Products products = dataSnapshot.getValue(Products.class);
          productName.setText(products.getname());
          productPrice.setText(products.getPrice());
          productDescription.setText(products.getDescription());
          Picasso.get().load(products.getImages()).into(productImage);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void mapping() {
    addToCartBtn = (FloatingActionButton) findViewById(R.id.add_product_to_cart_btn);
    productImage = (ImageView) findViewById(R.id.product_image_detail);
    numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
    productName= (TextView) findViewById(R.id.product_name_detail);
    productDescription= (TextView) findViewById(R.id.product_description_detail);
    productPrice = (TextView) findViewById(R.id.product_price_detail);
  }

  private void checkOrderState(){
    DatabaseReference orderRef;
    orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnLineUsers.getPhone());

    orderRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          String shippingState = dataSnapshot.child("state").getValue().toString();
          String usename = dataSnapshot.child("name").getValue().toString();
          String msg ="Congratulations, your final order has been Shipped successfully. Soon you will received you order at your door step.";

          if (shippingState.equals("shipped")){
            state ="order Shipped";
          }else if (shippingState.equals("not shipped")){
            state ="order Placed";
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
}
