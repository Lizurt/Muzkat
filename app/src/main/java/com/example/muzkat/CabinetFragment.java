package com.example.muzkat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muzkat.entities.UserEntity;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.AuthorApi;
import com.example.muzkat.retrofit.api.GenreApi;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
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

    public CabinetFragment() {

    }

    public static CabinetFragment newInstance() {
        CabinetFragment fragment = new CabinetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_cabinet, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            throw new ClassCastException("Couldn't get the main activity.");
        }
        if (mainActivity.getCurrUser() != null) {
            onLogin(mainActivity.getCurrUser());
        }
        this.retrofitService = new RetrofitService();
        this.genreApi = retrofitService.getRetrofit().create(GenreApi.class);
        this.authorApi = retrofitService.getRetrofit().create(AuthorApi.class);
        this.userApi = retrofitService.getRetrofit().create(UserApi.class);
        this.musicApi = retrofitService.getRetrofit().create(MusicApi.class);
        initButtons(view);
    }

    public void onLogout() {
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.GONE);
        mainActivity.setCurrUser(null);
    }


    public void onLogin(UserEntity userEntity) {
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.VISIBLE);
        mainActivity.setCurrUser(null);
    }

    private void initButtons(View view) {
        view.findViewById(R.id.bLogin).setOnClickListener(this::onLoginButtonClicked);
        view.findViewById(R.id.bLogon).setOnClickListener(this::onLogonButtonClicked);
    }

    private void onLoginButtonClicked(View view) {
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
                mainActivity.setCurrUser(userEntity);
                onLogin(userEntity);
                Toast.makeText(mainActivity, "Logged in successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
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
                onLogin(userEntity);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(mainActivity, "Logon failed.", Toast.LENGTH_SHORT).show();
                Logger.getLogger(mainActivity.getClass().getName()).log(
                        Level.SEVERE, "Logon failed.", t
                );
            }
        });
    }
}