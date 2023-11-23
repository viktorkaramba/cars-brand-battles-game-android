package com.example.carbrandsbattles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.entities.LoginResponse;
import com.example.carbrandsbattles.entities.User;
import com.example.carbrandsbattles.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistryActivity extends AppCompatActivity {

    private EditText name, userName, password;
    private TextView loginRegisterTextView;
    private Button registry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        UserInfoService.init(RegistryActivity.this);
        name = findViewById(R.id.nameUserRegistryEditText);
        userName = findViewById(R.id.userNameUserRegistryEditText);
        password = findViewById(R.id.passwordUserRegistryEditText);
        registry = findViewById(R.id.registryLoginButton);
        loginRegisterTextView = findViewById(R.id.loginRegisterTextView);
        loginRegisterTextView.setOnClickListener(view -> goLogin());
        registry.setOnClickListener(view -> registry());
    }

    public void registry(){
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        if(isValidInfo()){
            User user = new User(0, name.getText().toString(), userName.getText().toString(), password.getText().toString());
            carBrandsBattlesApi.signUp(user)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if(response.isSuccessful()){
                                LoginResponse responseMessage = response.body();
                                UserInfoService.addProperty("access_token", responseMessage.getToken());
                                UserInfoService.addProperty("userId", String.valueOf(responseMessage.getUserId()));
                                UserInfoService.addProperty("username", responseMessage.getUsername());
                                UserInfoService.addProperty("name", responseMessage.getName());
                                goBattleList();
                            } else{
                                RegistryActivity.this.runOnUiThread(() -> Toast.makeText(RegistryActivity.this, "Failed to register. Try again later. Maybe username is already exist",
                                        Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            RegistryActivity.this.runOnUiThread(() -> Toast.makeText(RegistryActivity.this, "Failed to register. Try again later",
                                    Toast.LENGTH_SHORT).show());
                        }
                    });
        }
    }

    private boolean isValidInfo(){
        if(name.getText().toString().length() < 4 || name.getText().toString().length() > 16){
            name.setError(getResources().getString(R.string.name_to_small));
            return false;
        }
        if(name.getText().toString().length() == 0){
            name.setError(getResources().getString(R.string.name_empty));
            return false;
        }
        if(userName.getText().toString().length() < 4 || userName.getText().toString().length() > 16){
            userName.setError(getResources().getString(R.string.user_name_to_small));
            return false;
        }
        if(userName.getText().toString().length() == 0){
            userName.setError(getResources().getString(R.string.username_empty));
            return false;
        }
        if(password.getText().toString().length() < 8 || userName.getText().toString().length() > 20){
            password.setError(getResources().getString(R.string.user_password_to_small));
            return false;
        }
        if(password.getText().toString().length() == 0){
            password.setError(getResources().getString(R.string.user_password_empty));
            return false;
        }
        return true;
    }

    private void goBattleList() {
        Intent intent = new Intent(this, BattlesListActivity.class);
        startActivity(intent);
    }

    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}