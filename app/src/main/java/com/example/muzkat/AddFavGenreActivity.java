package com.example.muzkat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private ProgressBar pbLoading;
    private Button bAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_genre);
        initComponents();
        YandexMetrica.reportEvent(MetricEventNames.STARTED_ADDING_PREFS);
    }

    private void initComponents() {
        etGenreName = findViewById(R.id.etGenre);
        bAdd = findViewById(R.id.bAddFavoriteGenre);
        pbLoading = findViewById(R.id.pbLoadingAddGenre);

        RetrofitService retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        bAdd.setOnClickListener(v -> askServerToAddFavoriteGenre());
    }

    private void askServerToAddFavoriteGenre() {
        String genreName = etGenreName.getText().toString();
        if (genreName.isEmpty()) {
            Toast.makeText(this, "You should fill all the fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        AddFavGenreRequest addFavGenreRequest = new AddFavGenreRequest();
        addFavGenreRequest.setGenreName(genreName);
        addFavGenreRequest.setLogin(getIntent().getStringExtra(CabinetFragment.EXTRA_LOGIN));
        onAddRequestSent();
        userApi.addFavGenre(addFavGenreRequest).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NotNull Call<Boolean> call, @NotNull Response<Boolean> response) {
                onAddRequestFinished();
                if (response.body() == null || !response.body()) {
                    Toast.makeText(AddFavGenreActivity.this,
                            "Failed to add the genre to favorites. " +
                                    "Perhaps there is no music with such genre.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                YandexMetrica.reportEvent(MetricEventNames.ADDED_PREFS);
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Boolean> call, @NotNull Throwable t) {
                onAddRequestFinished();
                findViewById(R.id.pbLoadingAddGenre).setVisibility(View.GONE);
                Toast.makeText(
                        AddFavGenreActivity.this,
                        "Failed to add the genre to favorites.",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }
        });
    }

    private void onAddRequestSent() {
        pbLoading.setVisibility(View.VISIBLE);
        bAdd.setVisibility(View.GONE);
    }

    private void onAddRequestFinished() {
        pbLoading.setVisibility(View.GONE);
        bAdd.setVisibility(View.VISIBLE);
    }
}