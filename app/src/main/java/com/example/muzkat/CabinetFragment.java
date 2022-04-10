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
        if (!mainActivity.getCurrLogin().isEmpty()) {
            onLogin(mainActivity.getCurrLogin());
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
        mainActivity.setCurrLogin("");
    }


    public void onLogin(String login) {
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.VISIBLE);
        mainActivity.setCurrLogin(login);
    }

    private void initButtons(View view) {
        view.findViewById(R.id.bLogin).setOnClickListener(this::onLoginButtonClicked);
        view.findViewById(R.id.bLogon).setOnClickListener(this::onLogonButtonClicked);
    }

    private void onLoginButtonClicked(View view) {
        List<UserEntity> users;
        try {
            users = userApi.getAllUsers().execute().body();
            if (users == null || users.size() == 0) {
                return;
            }
            for (UserEntity userEntity : users) {
                if (userEntity.getLogin().equals(mainActivity.getCurrLogin())) {
                    onLogin(userEntity.getLogin());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onLogonButtonClicked(View view) {
        userApi.getAllUsers().enqueue(new Callback<List<UserEntity>>() {
            @Override
            public void onResponse(Call<List<UserEntity>> call, Response<List<UserEntity>> response) {
                List<UserEntity> users = response.body();
                if (users != null) {
                    for (UserEntity userEntity : users) {
                        if (userEntity.getLogin().equals(mainActivity.getCurrLogin())) {
                            Toast.makeText(mainActivity, "The login is already occupied.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                UserEntity newUser = new UserEntity();
                EditText etLogin = view.getRootView().findViewById(R.id.etLogin);
                newUser.setLogin(String.valueOf(etLogin.getText()));
                EditText etPassword = view.getRootView().findViewById(R.id.etPassword);
                newUser.setPassword(String.valueOf(etPassword.getText()));
                // welcome to the callback hell
                userApi.saveUser(newUser).enqueue(new Callback<UserEntity>() {
                    @Override
                    public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                        Toast.makeText(mainActivity, "Logon success.", Toast.LENGTH_SHORT).show();
                        onLogin(newUser.getLogin());
                    }

                    @Override
                    public void onFailure(Call<UserEntity> call, Throwable t) {
                        Toast.makeText(mainActivity, "Logon failed.", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(mainActivity.getClass().getName()).log(
                                Level.SEVERE, "Logon failed.", t
                        );
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserEntity>> call, Throwable t) {
                Toast.makeText(mainActivity, "Logon failed.", Toast.LENGTH_SHORT).show();
                Logger.getLogger(mainActivity.getClass().getName()).log(
                        Level.SEVERE, "Logon failed.", t
                );
            }
        });
    }
}