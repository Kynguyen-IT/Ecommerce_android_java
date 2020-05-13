package com.kynguyen.commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText ipName, ipphone, ipPassword;
    private Button registerButton;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mapping();
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }


        });
    }

    public void mapping(){
        ipName = (EditText) findViewById(R.id.register_username_input);
        ipphone = (EditText) findViewById(R.id.register_phone_number_input);
        ipPassword = (EditText) findViewById(R.id.register_password_input);

        registerButton = (Button) findViewById(R.id.register_btn);
    }

    public void CreateAccount() {
        String name = ipName.getText().toString().trim();
        String phone = ipphone.getText().toString().trim();
        String password = ipPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please write your name...",Toast.LENGTH_SHORT ).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your phone...",Toast.LENGTH_SHORT ).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_SHORT ).show();
        }else {
             loadingBar.setTitle("Create Account");
             loadingBar.setMessage("Please wait, while we are checking the credentials.");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             ValidatephoneNumber(name, phone, password);
        }

    }

    private void ValidatephoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    Rootref.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(RegisterActivity.this,"this" + phone , Toast.LENGTH_SHORT ).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again using another phone number.", Toast.LENGTH_SHORT ).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
