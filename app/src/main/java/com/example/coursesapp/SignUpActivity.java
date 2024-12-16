package com.example.coursesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coursesapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    Appdatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=Appdatabase.getDatabase(this);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (binding.edUsername.getText().toString().isEmpty() || binding.edEmail.getText().toString().isEmpty() || binding.edPassword.getText().toString().isEmpty() || binding.edRepassword.getText().toString().isEmpty()){
                        Toast.makeText(SignUpActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                    }else if (!binding.edPassword.getText().toString().equals(binding.edRepassword.getText().toString())){
                        Toast.makeText(SignUpActivity.this, "Password Not The Same", Toast.LENGTH_SHORT).show();
                    }else {
                        db.userDao().insertUser(new User(binding.edUsername.getText().toString(),binding.edEmail.getText().toString(),binding.edPassword.getText().toString()));
                        Toast.makeText(SignUpActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            }
        });




    }
}