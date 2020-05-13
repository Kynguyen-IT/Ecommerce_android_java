package com.kynguyen.commerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.commerce.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMainProductsActivity extends AppCompatActivity {
  private Button change_btn, delete_btn;
  private EditText name, price, description;
  private ImageView image;
  private String productID = "";
  private DatabaseReference productRef;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_main_products);

    mapping();

    productID = getIntent().getStringExtra("pid");
    productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
    displaySpecificProductInfo();

    change_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        applyChanges();
      }
    });

    delete_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        delete_product();
      }
    });
  }

  private void delete_product() {
    productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()){
          Toast.makeText(AdminMainProductsActivity.this, "Deleted product successfully", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(AdminMainProductsActivity.this, AdminCategoryActivity.class);
          startActivity(intent);
        }
      }
    });
  }

  private void applyChanges() {
    String pName = name.getText().toString();
    String pPrice = price.getText().toString();
    String pDescription = description.getText().toString();

    if (pName.equals("")){
      Toast.makeText(this, "write down Product Name", Toast.LENGTH_SHORT).show();
    } else if (pPrice.equals("")){
      Toast.makeText(this, "write down Product Price", Toast.LENGTH_SHORT).show();
    } else if (pDescription.equals("")){
      Toast.makeText(this, "write down Product Description", Toast.LENGTH_SHORT).show();
    } else {
      HashMap<String,Object> productMap = new HashMap<>();
      productMap.put("pid", productID);
      productMap.put("name", pName);
      productMap.put("price", pPrice);
      productMap.put("description",pDescription);

      productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          if (task.isSuccessful()){
            Toast.makeText(AdminMainProductsActivity.this, "Applied successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminMainProductsActivity.this, AdminCategoryActivity.class);
            startActivity(intent);
            finish();
          }

        }
      });
    }

  }

  private void displaySpecificProductInfo() {
    productRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          String pName = dataSnapshot.child("name").getValue().toString();
          String pPrice = dataSnapshot.child("price").getValue().toString();
          String pDescription = dataSnapshot.child("description").getValue().toString();
          String pImage = dataSnapshot.child("images").getValue().toString();

          name.setText(pName);
          price.setText(pPrice);
          description.setText(pDescription);
          Picasso.get().load(pImage).into(image);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void mapping() {
    change_btn = (Button) findViewById(R.id.apply_change_btn);
    delete_btn = (Button) findViewById(R.id.delete_product_btn);
    name = (EditText) findViewById(R.id.product_name_maintain);
    price = (EditText) findViewById(R.id.product_price_maintain);
    description = (EditText) findViewById(R.id.product_description_maintain);
    image = (ImageView) findViewById(R.id.product_image_maintain);
  }
}
