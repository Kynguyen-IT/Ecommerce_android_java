package com.kynguyen.commerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kynguyen.commerce.HomeActivity;
import com.kynguyen.commerce.MainActivity;
import com.kynguyen.commerce.R;

public class AdminCategoryActivity extends AppCompatActivity {
  private ImageView tshirts, sports, femaleDresses, sweathers;
  private ImageView glasses, hats, wallets, shose;
  private ImageView headphone, laptops, watches, mobilePhones;
  private Button btn_check_oders, btn_logout, btn_maintain;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_category);

    mapping();

    tshirts.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "tShirts");
        startActivity(intent);
      }
    });

    sports.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Sports");
        startActivity(intent);
      }
    });

    femaleDresses.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Female Dresses");
        startActivity(intent);
      }
    });

    sweathers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Sweathers");
        startActivity(intent);
      }
    });

    glasses.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Glasses");
        startActivity(intent);
      }
    });

    hats.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Hats Caps");
        startActivity(intent);
      }
    });

    wallets.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Wallets Bags purses");
        startActivity(intent);
      }
    });

    shose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Shoes");
        startActivity(intent);
      }
    });

    headphone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "HeadPhone HandFree");
        startActivity(intent);
      }
    });

    laptops.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Laptops");
        startActivity(intent);
      }
    });

    watches.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "Watches");
        startActivity(intent);
      }
    });

    mobilePhones.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        intent.putExtra("Category", "MobilePhone");
        startActivity(intent);
      }
    });

    btn_logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
      }
    });

    btn_check_oders.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
        startActivity(intent);
        finish();
      }
    });

    btn_maintain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this , HomeActivity.class);
        intent.putExtra("Admin", "Admin");
        startActivity(intent);
      }
    });


  }

  private void mapping() {
    tshirts = (ImageView) findViewById(R.id.t_shirts);
    sports = (ImageView) findViewById(R.id.t_sports);
    femaleDresses = (ImageView) findViewById(R.id.female_dresses);
    sweathers = (ImageView) findViewById(R.id.sweather);
    glasses = (ImageView) findViewById(R.id.glasses);
    hats = (ImageView) findViewById(R.id.hats);
    wallets = (ImageView) findViewById(R.id.purses_bags_wallets);
    shose = (ImageView) findViewById(R.id.shoess);
    headphone = (ImageView) findViewById(R.id.headphoness);
    laptops = (ImageView) findViewById(R.id.laptops);
    watches = (ImageView) findViewById(R.id.watches);
    mobilePhones = (ImageView) findViewById(R.id.mobilesphones);
    btn_maintain = (Button) findViewById(R.id.maintain_btn);
    btn_check_oders = (Button) findViewById(R.id.check_orders_btn);
    btn_logout = (Button) findViewById(R.id.admin_logout);
  }


}
