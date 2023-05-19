package com.example.retrofitdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.Retro;
import com.example.retrofitdemo.model.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_name, edt_email, edt_password;
    TextView login_text;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        login_text = findViewById(R.id.login_text);
        register_btn = findViewById(R.id.register_btn);


        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edt_name.getText().toString().isEmpty() && !edt_email.getText().toString().isEmpty() && ! edt_password.getText().toString().isEmpty()){
                    String name = edt_name.getText().toString();
                    String email = edt_email.getText().toString();
                    String password = edt_password.getText().toString();

                    ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Registering User...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Retro.getInstance().retroAPI.userRegister(name, email, password).enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if (response.body().getConnection() == 1){
                                if (response.body().getResult() == 1){
                                    progressDialog.dismiss();

                                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                                    edt_name.setText("");
                                    edt_email.setText("");
                                    edt_password.setText("");
                                } else if (response.body().getResult() == 2) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Connection Filed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (edt_name.getText().toString().isEmpty()){
                        edt_name.setError("Enter Name");
                    }
                    if (edt_email.getText().toString().isEmpty()){
                        edt_email.setError("Enter Email");
                    }
                    if (edt_password.getText().toString().isEmpty()){
                        edt_password.setError("Enter Password");
                    }
                }
            }
        });
    }
}