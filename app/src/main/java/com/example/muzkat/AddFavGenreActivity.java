package com.example.muzkat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muzkat.model.request.AddFavAuthorRequest;
import com.example.muzkat.model.request.AddFavGenreRequest;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.UserApi;
import com.yandex.metrica.YandexMetrica;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFavGenreActivity extends AppCompatActivity {
    private UserApi userApi;
    private EditText etGenreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_genre);
        initComponents();
        YandexMetrica.reportEvent(MetricEventNames.STARTED_ADDING_PREFS);
    }

    private void initComponents() {
        etGenreName = findViewById(R.id.etGenre);
        RetrofitService retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);
        findViewById(R.id.bAddFavoriteGenre).setOnClickListener(v -> askServerToAddFavoriteGenre());
    }

    private void askServerToAddFavoriteGenre() {
        String genreName = etGenreName.getText().toString();
        if (genreName.isEmpty()) {
            Toast.makeText(this, "Nothing was added.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AddFavGenreRequest addFavGenreRequest = new AddFavGenreRequest();
        addFavGenreRequest.setGenreName(genreName);
        addFavGenreRequest.setLogin(getIntent().getStringExtra(CabinetFragment.EXTRA_LOGIN));
        findViewById(R.id.pbLoadingAddGenre).setVisibility(View.VISIBLE);
        userApi.addFavGenre(addFavGenreRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                findViewById(R.id.pbLoadingAddGenre).setVisibility(View.GONE);
                YandexMetrica.reportEvent(MetricEventNames.ADDED_PREFS);
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                findViewById(R.id.pbLoadingAddGenre).setVisibility(View.GONE);
                Toast.makeText(
                        AddFavGenreActivity.this,
                        "Failed to add a genre to favorites.",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }
        });
    }
}