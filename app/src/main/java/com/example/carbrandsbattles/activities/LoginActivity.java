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
import com.example.carbrandsbattles.entities.SignInInput;
import com.example.carbrandsbattles.services.UserInfoService;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userName, password;
    private TextView loginRegisterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.userNameLoginEditText);
        password = findViewById(R.id.passwordLoginEditText);
        loginRegisterTextView = findViewById(R.id.loginRegisterTextView);
        Button login = findViewById(R.id.loginUserLoginButton);
        loginRegisterTextView.setOnClickListener(view -> goRegistration());
        login.setOnClickListener(view -> login());
        UserInfoService.init(LoginActivity.this);
    }

    public void login(){
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi api = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        if(isValidInfo()){
            api.signIn(new SignInInput(userName.getText().toString(), password.getText().toString()))
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<LoginResponse> call,
                                               @NotNull Response<LoginResponse> response) {
                            if(response.isSuccessful()){
                                LoginResponse responseMessage = response.body();
                                UserInfoService.addProperty("access_token", responseMessage.getToken());
                                UserInfoService.addProperty("userId", String.valueOf(responseMessage.getUserId()));
                                UserInfoService.addProperty("username", responseMessage.getUsername());
                                UserInfoService.addProperty("name", responseMessage.getName());
                                goBattleList();
                            } else{
                                LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to login. Try again. Maybe username or password is incorrect",
                                        Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<LoginResponse> call,
                                              @NotNull Throwable t) {
                            LoginActivity.this.runOnUiThread(() -> Toast.makeText(
                                    LoginActivity.this, "Failed to login. Try again. Maybe username or password is incorrect",
                                    Toast.LENGTH_SHORT).show());
                        }
                    });
        }
    }

    private boolean isValidInfo(){
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

    private void goRegistration() {
        Intent intent = new Intent(this, RegistryActivity.class);
        startActivity(intent);
    }
}
