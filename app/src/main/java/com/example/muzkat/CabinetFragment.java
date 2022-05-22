package com.example.muzkat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;
import com.example.muzkat.model.entity.UserEntity;
import com.example.muzkat.recycler.AuthorAdapter;
import com.example.muzkat.recycler.GenreAdapter;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.AuthorApi;
import com.example.muzkat.retrofit.api.GenreApi;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CabinetFragment extends Fragment {
    public final static String EXTRA_LOGIN = "LOGIN";

    private RetrofitService retrofitService;
    private GenreApi genreApi;
    private AuthorApi authorApi;
    private UserApi userApi;
    private MusicApi musicApi;

    private MainActivity mainActivity;

    private SharedPreferences sharedPreferences;

    private RecyclerView rvFavAuthors;
    private RecyclerView rvFavGenres;

    private String login;

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
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_cabinet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tryAutoLogin();
        initButtons();
        this.rvFavAuthors = view.findViewById(R.id.rvFavAuthors);
        this.rvFavAuthors.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.rvFavGenres = view.findViewById(R.id.rvFavGenres);
        this.rvFavGenres.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void initButtons() {
        mainActivity.findViewById(R.id.bLogin).setOnClickListener(this::onLoginButtonClicked);
        mainActivity.findViewById(R.id.bLogon).setOnClickListener(this::onLogonButtonClicked);

        mainActivity.findViewById(R.id.bLogout).setOnClickListener(this::onLogoutButtonClicked);
        mainActivity.findViewById(R.id.bAddFavoriteAuthor).setOnClickListener(
                this::onAddFavAuthorButtonClicked
        );
        mainActivity.findViewById(R.id.bAddFavoriteGenre).setOnClickListener(
                this::onAddFavGenreButtonClicked
        );
    }

    private void onAddFavGenreButtonClicked(View view) {
        Intent intent = new Intent(mainActivity, AddFavGenreActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        startActivity(intent);

    }

    private void onAddFavAuthorButtonClicked(View view) {
        Intent intent = new Intent(mainActivity, AddFavAuthorActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        startActivity(intent);
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

    private void clientOnLoggedIn() {
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.VISIBLE);
    }

    private void onLoggedIn(String login) {
        this.login = login;
        clientOnLoggedIn();
        askServerToFillFavoriteAuthors(login);
        askServerToFillFavoriteGenres(login);
    }

    private void onLoggedOut() {
        login = null;
        mainActivity.findViewById(R.id.layoutCabinetAnon).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.layoutCabinetDeanon).setVisibility(View.GONE);
        eraseLoginData();
    }

    private UserEntity tryGetSavedUser() {
        String login = sharedPreferences.getString(MainActivity.LOGIN_PREF, "");
        String password = sharedPreferences.getString(MainActivity.PASSWORD_PREF, "");

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setPassword(password);
        return userEntity;
    }

    private void tryAutoLogin() {
        UserEntity userEntity = tryGetSavedUser();
        if (userEntity == null) {
            return;
        }
        tryToServerLogin(userEntity);
    }

    private void askServerToFillFavoriteAuthors(String login) {
        userApi.getFavAuthors(login).enqueue(new Callback<Set<AuthorEntity>>() {
            @Override
            public void onResponse(
                    @NotNull Call<Set<AuthorEntity>> call,
                    @NotNull Response<Set<AuthorEntity>> response
            ) {
                fillFavAuthors(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Set<AuthorEntity>> call, @NotNull Throwable t) {
                Toast.makeText(mainActivity,
                        "Unable to get favorite authors.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void fillFavAuthors(Set<AuthorEntity> favAuthors) {
        rvFavAuthors.setAdapter(new AuthorAdapter(new ArrayList<>(favAuthors), userApi, login));
    }

    private void askServerToFillFavoriteGenres(String login) {
        userApi.getFavGenres(login).enqueue(new Callback<Set<GenreEntity>>() {
            @Override
            public void onResponse(
                    @NotNull Call<Set<GenreEntity>> call,
                    @NotNull Response<Set<GenreEntity>> response
            ) {
                fillFavGenres(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Set<GenreEntity>> call, @NotNull Throwable t) {
                Toast.makeText(mainActivity,
                        "Unable to get favorite genres.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void fillFavGenres(Set<GenreEntity> favGenres) {
        rvFavGenres.setAdapter(new GenreAdapter(new ArrayList<>(favGenres), userApi, login));
    }

    private void onLogoutButtonClicked(View view) {
        onLoggedOut();
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
        tryToServerLogin(userEntity);
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
        tryToServerLogon(userEntity);
    }

    private void tryToServerLogin(UserEntity userEntity) {
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
                saveLoginData(userEntity.getLogin(), userEntity.getPassword());
                onLoggedIn(userEntity.getLogin());
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

    private void tryToServerLogon(UserEntity userEntity) {
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
                saveLoginData(userEntity.getLogin(), userEntity.getPassword());
                onLoggedIn(userEntity.getLogin());
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