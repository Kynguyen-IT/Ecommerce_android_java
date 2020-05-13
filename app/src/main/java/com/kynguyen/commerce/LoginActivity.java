package com.kynguyen.commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kynguyen.commerce.Admin.AdminCategoryActivity;
import com.kynguyen.commerce.Model.Users;
import com.kynguyen.commerce.Prevalent.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
  private EditText ipphone, ippassword;
  private Button buttonlogin;
  private ProgressDialog loadingBar;
  private String parentDbName = "Users";
  private CheckBox cbRememberMe;
  private TextView Adminlink, NotAdminLink, forget_password;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mapping();
    Paper.init(this);
    buttonlogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loginUser();
      }
    });

    Adminlink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        buttonlogin.setText("Login Admin");
        Adminlink.setVisibility(v.INVISIBLE);
        NotAdminLink.setVisibility(v.VISIBLE);
        parentDbName = "Admins";
      }
    });

    NotAdminLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        buttonlogin.setText("Login");
        Adminlink.setVisibility(v.VISIBLE);
        NotAdminLink.setVisibility(v.INVISIBLE);
        parentDbName = "Users";
      }
    });

    forget_password.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        
      }
    });
  }

  public void mapping(){
    ipphone = (EditText) findViewById(R.id.login_phone_number_input);
    ippassword = (EditText) findViewById(R.id.login_password_input);
    buttonlogin = (Button) findViewById(R.id.login_btn);
    loadingBar = new ProgressDialog(this);
    cbRememberMe = (CheckBox) findViewById(R.id.remember_me_checkbox);
    Adminlink = (TextView) findViewById(R.id.admin_panel_link);
    NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
    forget_password = (TextView) findViewById(R.id.forget_password_link);
  }

  public void loginUser(){
    String phone = ipphone.getText().toString().trim();
    String password = ippassword.getText().toString().trim();

    if (TextUtils.isEmpty(phone)){
      Toast.makeText(this,"Please write your Phone...",Toast.LENGTH_SHORT).show();
    } else if (TextUtils.isEmpty(password)){
      Toast.makeText(this,"Please write your Password...",Toast.LENGTH_SHORT).show();
    } else {
      loadingBar.setTitle("Login");
      loadingBar.setMessage("Please wait, while we are checking the credentials.");
      loadingBar.setCanceledOnTouchOutside(false);
      loadingBar.show();

      AllowAccessAccount(phone, password);
    }
  }

  private void AllowAccessAccount(final String phone, final String password) {
    if (cbRememberMe.isChecked()){
      Paper.book().write(Prevalent.Userphonekey, phone);
      Paper.book().write(Prevalent.Userpasswordkey, password);
    }
    final DatabaseReference RootRef;
    RootRef = FirebaseDatabase.getInstance().getReference();

    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.child(parentDbName).child(phone).exists()){
          Users userdata = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
          if (userdata.getPhone().equals(phone)){
            if (userdata.getPassword().equals(password)){
              if (parentDbName.equals("Admins")){
                Toast.makeText(LoginActivity.this,"Welcome Admin, you are logged in successfully...",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
              }else if (parentDbName.equals("Users")){
                Toast.makeText(LoginActivity.this,"logged in successfully...",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                Prevalent.currentOnLineUsers = userdata;
                startActivity(intent);
              }
            }else {
              loadingBar.dismiss();
              Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
            }
          }
        }else {
          Toast.makeText(LoginActivity.this,"Account with this " + phone + " number do not exits",Toast.LENGTH_SHORT).show();
          loadingBar.dismiss();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

}
