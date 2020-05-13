package com.kynguyen.commerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kynguyen.commerce.Model.Products;
import com.kynguyen.commerce.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
  private String CategoryName, name, description, price, SaveCurrentDate, SaveCurrentTime;
  private Button AddProductsButton;
  private ImageView ipImage;
  private EditText ipName, ipDesciption, ipPrice;
  private static final int GalleryPick = 1;
  private Uri ImagesUri;
  private String productRandomKey, downloadImagesURL;
  private StorageReference ProductImageRef;
  private DatabaseReference productsRef;
  private ProgressDialog loadingBar;
  private Products products;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_add_new_product);

    CategoryName = getIntent().getExtras().get("Category").toString();
    Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();

    ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
    productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
    mapping();


    AddProductsButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ValidateProductData();
      }
    });

    ipImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        OpenGallery();
      }
    });

  }

  private void ValidateProductData() {
    description = ipDesciption.getText().toString();
    name = ipName.getText().toString();
    price = ipPrice.getText().toString();

    if (ImagesUri == null){
      Toast.makeText(this, "Product images is mandatory", Toast.LENGTH_SHORT).show();
    } else if (TextUtils.isEmpty(description)){
      Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
      ipDesciption.setError("Description is Required");

    }else if (TextUtils.isEmpty(name)){
      Toast.makeText(this, "Please write product Name...", Toast.LENGTH_SHORT).show();
      ipName.setError("Name is Required");

    }else if (TextUtils.isEmpty(price)){
      Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
      ipPrice.setError("Price is Required");
    }else {
      StoreProductInfomation();
    }

  }

  private void StoreProductInfomation() {

    loadingBar.setTitle("Add New Product");
    loadingBar.setMessage("Dear Admin, Please wait while we are adding the credentials.");
    loadingBar.setCanceledOnTouchOutside(false);
    loadingBar.show();

    Calendar calendar = Calendar.getInstance();
    String date = "yyyy-MM-dd";
    String time = "HH:mm:ss a";

    SimpleDateFormat currentDate = new SimpleDateFormat(date);
    SaveCurrentDate = currentDate.format(calendar.getTime());

    SimpleDateFormat currentTime = new SimpleDateFormat(time);
    SaveCurrentTime = currentTime.format(calendar.getTime());

    productRandomKey = SaveCurrentDate + SaveCurrentTime;

    final StorageReference filePath =  ProductImageRef.child(ImagesUri.getLastPathSegment() + productRandomKey + "jpg");

    final UploadTask uploadTask = filePath.putFile(ImagesUri);

    uploadTask.addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        String message = e.toString();
        Toast.makeText(AdminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
      }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(AdminAddNewProductActivity.this, "Product Images uploaded successfully...", Toast.LENGTH_SHORT).show();
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
          @Override
          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
            if (!task.isSuccessful()){
              throw task.getException();
            }
            downloadImagesURL = filePath.getDownloadUrl().toString();
            return filePath.getDownloadUrl();
          }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
          @Override
          public void onComplete(@NonNull Task<Uri> task) {
            if(task.isSuccessful()){
              downloadImagesURL = task.getResult().toString();
              Toast.makeText(AdminAddNewProductActivity.this, "got the Product image URL Successfully..", Toast.LENGTH_SHORT).show();
              SaveProductInfotoDatabesa();
            }
          }
        });
      }
    });
  }

  private void SaveProductInfotoDatabesa() {
    HashMap<String, Object> productMap = new HashMap<>();
    productMap.put("pid", productRandomKey);
    productMap.put("date", SaveCurrentDate);
    productMap.put("time", SaveCurrentTime);
    productMap.put("description", description);
    productMap.put("name", name);
    productMap.put("price", price);
    productMap.put("category", CategoryName);
    productMap.put("images",downloadImagesURL);

    productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
       if (task.isSuccessful()){
         Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
         startActivity(intent);
         loadingBar.dismiss();
         Toast.makeText(AdminAddNewProductActivity.this, "Product is added Successfully..", Toast.LENGTH_SHORT).show();
       } else {
         loadingBar.dismiss();
         String message = task.getException().toString();
         Toast.makeText(AdminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
       }
      }
    });
  }

  private void OpenGallery() {
    Intent galleryIntent = new Intent();
    galleryIntent.setType("image/*");
    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(galleryIntent, GalleryPick);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null){
      ImagesUri = data.getData();
      ipImage.setImageURI(ImagesUri);
      
    }
  }

  private void mapping() {
    AddProductsButton = (Button) findViewById(R.id.add_new_product);
    ipName = (EditText) findViewById(R.id.product_name);
    ipDesciption = (EditText) findViewById(R.id.product_description);
    ipPrice = (EditText) findViewById(R.id.product_price);
    ipImage = (ImageView) findViewById(R.id.Select_product_images);
    loadingBar = new ProgressDialog(this);
  }


}
