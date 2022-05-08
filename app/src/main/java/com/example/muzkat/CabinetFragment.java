package com.example.muzkat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muzkat.model.entity.UserEntity;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.AuthorApi;
import com.example.muzkat.retrofit.api.GenreApi;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CabinetFragment extends Fragment {
    private RetrofitService retrofitService;
    private GenreApi genreApi;
    private AuthorApi authorApi;
    private UserApi userApi;
    private MusicApi musicApi;

    private MainActivity mainActivity;

    private SharedPreferences sharedPreferences;

    public CabinetFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            throw new ClassCastException("Couldn't get the main activity.");
        }
        this.retrofitService = new RetrofitService();
        this.genreApi = retrofitService.getRetrofit().create(GenreApi.class);
        this.authorApi = retrofitService.getRetrofit().create(AuthorApi.class);
        this.userApi = retrofitService.getRetrofit().create(UserApi.class);
        this.musicApi = retrofitService.getRetrofit().create(MusicApi.class);
        sharedPreferences = mainActivity.getSharedPreferences(
                MainActivity.PREFS_NAME,
                Context.MODE_PRIVATE
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initButtons();
        tryClientLogin();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_cabinet, container, false);
    }

    private void saveLoginData(String login, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.LOGIN_PREF, login);
        editor.putString(MainActivity.PASSWORD_PREF, password);
        editor.apply();
    }

    private void eraseLoginData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MainActivity.LOGIN_PREF, "");
        editor.putString(MainActivity.PASSWORD_PREF, "");
        editor.apply();
    }

    private void unsafelyClientLogin() {
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.VISIBLE);
    }

    private void tryClientLogin() {
        String login = sharedPreferences.getString(MainActivity.LOGIN_PREF, "");
        String password = sharedPreferences.getString(MainActivity.PASSWORD_PREF, "");

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            return;
        }

        unsafelyClientLogin();
    }

    private void clientLogout() {
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.GONE);
        eraseLoginData();
    }

    private void initButtons() {
        mainActivity.findViewById(R.id.bLogin).setOnClickListener(this::onLoginButtonClicked);
        mainActivity.findViewById(R.id.bLogon).setOnClickListener(this::onLogonButtonClicked);
        mainActivity.findViewById(R.id.bLogout).setOnClickListener(this::onLogoutButtonClicked);
    }

    private void onLogoutButtonClicked(View view) {
        clientLogout();
    }

    private void onLoginButtonClicked(View view) {
        EditText etLogin = mainActivity.findViewById(R.id.etLogin);
        String login = String.valueOf(etLogin.getText());
        if (login.isEmpty()) {
            Toast.makeText(mainActivity, "You should enter a login.", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText etPassword = mainActivity.findViewById(R.id.etPassword);
        String password = etPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(mainActivity, "You should enter a password.", Toast.LENGTH_SHORT).show();
            return;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setPassword(password);
        userApi.tryLogin(userEntity).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NotNull Call<Boolean> call, @NotNull Response<Boolean> response) {
                if (!response.body()) {
                    Toast.makeText(
                            mainActivity,
                            "Wrong login or password.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                saveLoginData(login, password);
                unsafelyClientLogin();
                Toast.makeText(mainActivity, "Logged in successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<Boolean> call, @NotNull Throwable t) {
                Toast.makeText(mainActivity, "Login failed.", Toast.LENGTH_SHORT).show();
                Logger.getLogger(mainActivity.getClass().getName()).log(
                        Level.SEVERE, "Login failed.", t
                );
            }
        });
    }

    private void onLogonButtonClicked(View view) {
        EditText etLogin = mainActivity.findViewById(R.id.etLogin);
        String login = etLogin.getText().toString();
        if (login.isEmpty()) {
            Toast.makeText(mainActivity, "You should enter a login.", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText etPassword = mainActivity.findViewById(R.id.etPassword);
        String password = etPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(mainActivity, "You should enter a password.", Toast.LENGTH_SHORT).show();
            return;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setPassword(password);
        userApi.tryLogon(userEntity).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NotNull Call<Boolean> call, @NotNull Response<Boolean> response) {
                if (!response.body()) {
                    Toast.makeText(
                            mainActivity,
                            "Failed to logon - the login is already occupied.",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                Toast.makeText(
                        mainActivity,
                        "Logged on successfully.",
                        Toast.LENGTH_SHORT
                ).show();
                saveLoginData(login, password);
                unsafelyClientLogin();
            }

            @Override
            public void onFailure(@NotNull Call<Boolean> call, @NotNull Throwable t) {
                Toast.makeText(mainActivity, "Logon failed.", Toast.LENGTH_SHORT).show();
                Logger.getLogger(mainActivity.getClass().getName()).log(
                        Level.SEVERE, "Logon failed.", t
                );
            }
        });
    }
}