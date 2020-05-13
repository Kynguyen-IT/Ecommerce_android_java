package com.kynguyen.commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.kynguyen.commerce.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
  private CircleImageView profileImage;
  private EditText fullnameET,phoneET, addressET;
  private TextView changeImage,close, save;

  private Uri imageUri;
  private String myUrl= "";
  private StorageTask uploadTask;
  private StorageReference storageProfilePrictureRef;
  private String checker = "";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

    mapping();
    userinfoDisplay(profileImage, fullnameET, phoneET, addressET);

    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (checker.equals("clicked")){
          userInfoSave();
        }else {
          updateOnlyUserInfo();
        }
      }
    });

    changeImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checker = "clicked";
        CropImage.activity(imageUri)
            .setAspectRatio(1,1)
            .start(SettingActivity.this);
      }
    });
  }

  private void updateOnlyUserInfo() {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

    HashMap<String, Object>  userMap = new HashMap<>();
    userMap.put("name", fullnameET.getText().toString());
    userMap.put("address", addressET.getText().toString());
    userMap.put("phoneOrder", phoneET.getText().toString());
    ref.child(Prevalent.currentOnLineUsers.getPhone()).updateChildren(userMap);

    startActivity(new Intent(SettingActivity.this, SettingActivity.class));
    Toast.makeText(SettingActivity.this, "Profile info update successfully", Toast.LENGTH_SHORT).show();
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
    {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      imageUri = result.getUri();

      profileImage.setImageURI(imageUri);
    }
    else
    {
      Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

      startActivity(new Intent(SettingActivity.this, SettingActivity.class));
      finish();
    }
  }

  private void userInfoSave() {
    if (TextUtils.isEmpty(fullnameET.getText().toString())){
      Toast.makeText(this, "Name is madatory", Toast.LENGTH_SHORT).show();
    } else if (TextUtils.isEmpty(phoneET.getText().toString())){
      Toast.makeText(this, "Phone is madatory", Toast.LENGTH_SHORT).show();
    }else if (TextUtils.isEmpty(addressET.getText().toString())){
      Toast.makeText(this, "Address is madatory", Toast.LENGTH_SHORT).show();
    } else if (checker.equals("clicked")){
      uploadImage();
    }
  }

  private void uploadImage() {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Update Profile");
    progressDialog.setMessage("Please wait, while we are updating your account information");
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();

    if (imageUri != null){
      final StorageReference fileRef = storageProfilePrictureRef
          .child(Prevalent.currentOnLineUsers.getPhone() + ".jpg");
      uploadTask = fileRef.putFile(imageUri);

      uploadTask.continueWithTask(new Continuation() {
        @Override
        public Object then(@NonNull Task task) throws Exception {
          if (!task.isSuccessful()){
            throw task.getException();
          }
          return fileRef.getDownloadUrl();
        }
      }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
          if (task.isSuccessful()){
            Uri downloadUrl = task.getResult();
            myUrl = downloadUrl.toString();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            HashMap<String, Object>  userMap = new HashMap<>();
            userMap.put("name", fullnameET.getText().toString());
            userMap.put("address", addressET.getText().toString());
            userMap.put("image", myUrl);
            userMap.put("phoneOrder", phoneET.getText().toString());
            progressDialog.dismiss();

            ref.child(Prevalent.currentOnLineUsers.getPhone()).updateChildren(userMap);
            startActivity(new Intent(SettingActivity.this, HomeActivity.class));
            Toast.makeText(SettingActivity.this, "Profile info update successfully", Toast.LENGTH_SHORT).show();
            finish();
          } else {
            progressDialog.dismiss();
            Toast.makeText(SettingActivity.this, "Error", Toast.LENGTH_SHORT).show();
          }
        }
      });
    } else {
      Toast.makeText(this, "image is not selected", Toast.LENGTH_SHORT).show();
    }
  }

  private void userinfoDisplay(final CircleImageView profileImage, final EditText fullnameET, final EditText phoneET, final EditText addressET) {
    DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnLineUsers.getPhone());

    userRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
          if (dataSnapshot.child("image").exists()){
            String image = dataSnapshot.child("image").getValue().toString();
            String name = dataSnapshot.child("name").getValue().toString();
            String address = dataSnapshot.child("address").getValue().toString();
            String phone = dataSnapshot.child("phone").getValue().toString();

            Picasso.get().load(image).into(profileImage);
            fullnameET.setText(name);
            phoneET.setText(phone);
            addressET.setText(address);
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private void mapping() {
    profileImage = (CircleImageView) findViewById(R.id.settings_profile_image);
    fullnameET = (EditText) findViewById(R.id.settings_full_name);
    phoneET = (EditText) findViewById(R.id.settings_phone_number);
    addressET = (EditText) findViewById(R.id.settings_address);
    changeImage = (TextView) findViewById(R.id.profile_image_change_btn);
    close = (TextView) findViewById(R.id.close_settings_btn);
    save = (TextView) findViewById(R.id.update_account_settings_btn);
  }

}
