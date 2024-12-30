package com.example.coursesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivityLoginBinding;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Appdatabase db;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = Appdatabase.getDatabase(this);
        preferences = getSharedPreferences("MyPrefe", MODE_PRIVATE);
        editor = preferences.edit();




        View coustem= LayoutInflater.from(LoginActivity.this).inflate(R.layout.log,null);
        AlertDialog.Builder bulder=new AlertDialog.Builder(this);
        bulder.setView(coustem);
        AlertDialog dialog=bulder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        if (db.categoryDao().getAllCategory().isEmpty()){
            db.categoryDao().insertCategory(new Category("Education"));
            db.categoryDao().insertCategory(new Category("Engineering"));
            db.categoryDao().insertCategory(new Category("Business"));
            db.categoryDao().insertCategory(new Category("Other"));
        }



        if (preferences.contains("savedName")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {




            binding.btnSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.edEmail.getText().toString().isEmpty() || binding.edPassword.getText().toString().isEmpty()) {
                        if (binding.edEmail.getText().toString().isEmpty()) {
                            binding.edEmail.setError("Enter Email");
                            binding.edEmail.requestFocus();
                        }
                        if (binding.edPassword.getText().toString().isEmpty()) {
                            binding.edPassword.setError("Enter Password");
                            binding.edPassword.requestFocus();
                        }
                        //Toast.makeText(LoginActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                    } else  if (binding.edPassword.getText().toString().equals("a")&&binding.edEmail.getText().toString().equals("a")) {
                        binding.edEmail.getText().clear();
                        binding.edPassword.getText().clear();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

                    } else {

                             User user = db.userDao().getUser(binding.edEmail.getText().toString(), binding.edPassword.getText().toString());
                        if (user == null) {
                            dialog.show();
                            coustem.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });

                        } else if (db.userDao().getUser(binding.edEmail.getText().toString(), binding.edPassword.getText().toString()) != null) {
                            if (binding.checkbox.isChecked()){
                                editor.putString("savedName",binding.edEmail.getText().toString());
                                editor.putString("savedPAssword",binding.edPassword.getText().toString());
                                editor.commit();

                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            long id=db.userDao().getUser(binding.edEmail.getText().toString(), binding.edPassword.getText().toString()).getId();
                            editor.putLong("savedid",user.getId());
                            editor.commit();

                            intent.putExtra("id", id);
                            startActivity(intent);
                            finish();
                            binding.edEmail.getText().clear();
                            binding.edPassword.getText().clear();
                        }
                    }
                }


            });

            binding.btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.edEmail.getText().clear();
                    binding.edPassword.getText().clear();
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });


        }
    }
}