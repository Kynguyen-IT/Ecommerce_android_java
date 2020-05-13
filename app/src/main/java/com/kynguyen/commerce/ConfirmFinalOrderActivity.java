package com.kynguyen.commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.commerce.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
  private EditText edName, edPhone, edAddress, edCity;
  private Button btn_confirm;
  private String totalAmount="";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirm_final_order);
    mapping();
    totalAmount= getIntent().getStringExtra("Total Detail");
    Toast.makeText(this, "Total Detail = " + totalAmount +"$", Toast.LENGTH_SHORT).show();
    btn_confirm.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        check();
      }
    });
  }

  private void check() {
    if (TextUtils.isEmpty(edName.getText().toString())){
      Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
    } else  if (TextUtils.isEmpty(edPhone.getText().toString())){
      Toast.makeText(this, "Please provide your phone", Toast.LENGTH_SHORT).show();
    } else  if (TextUtils.isEmpty(edAddress.getText().toString())){
      Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
    } else  if (TextUtils.isEmpty(edCity.getText().toString())){
      Toast.makeText(this, "Please provide your City", Toast.LENGTH_SHORT).show();
    }else {
      confirmOrder();
    }
  }

  private void confirmOrder() {
    String saveCurrentTime, saveCurrentDate;

    Calendar calForDate = Calendar.getInstance();
    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
    saveCurrentDate = currentDate.format(calForDate.getTime());

    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
    saveCurrentTime = currentTime.format(calForDate.getTime());

    final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
        .child("Orders")
        .child(Prevalent.currentOnLineUsers.getPhone());

    final HashMap<String, Object> orderMap = new HashMap<>();
    orderMap.put("totalAmount", totalAmount);
    orderMap.put("name", edName.getText().toString());
    orderMap.put("phone", edPhone.getText().toString());
    orderMap.put("date", saveCurrentDate);
    orderMap.put("time", saveCurrentTime);
    orderMap.put("address", edAddress.getText().toString());
    orderMap.put("city", edCity.getText().toString());
    orderMap.put("state", "not shipped");

    orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()){
          FirebaseDatabase.getInstance().getReference().child("Cart List")
              .child("User View")
              .child(Prevalent.currentOnLineUsers.getPhone())
              .removeValue()
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been placed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                  }
                }
              });
        }
      }
    });
  }

  private void mapping() {
    edName = (EditText) findViewById(R.id.shipment_name);
    edPhone = (EditText) findViewById(R.id.shipment_phone);
    edAddress = (EditText) findViewById(R.id.shipment_address);
    edCity = (EditText) findViewById(R.id.shipment_city);
    btn_confirm = (Button) findViewById(R.id.confirm_final_order_btn);
  }

}
