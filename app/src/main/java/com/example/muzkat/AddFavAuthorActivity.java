package com.example.muzkat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.muzkat.model.request.AddFavAuthorRequest;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;
import com.yandex.metrica.YandexMetrica;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFavAuthorActivity extends AppCompatActivity {
    private UserApi userApi;

    private EditText etAuthorName;
    private ProgressBar pbLoading;
    private Button bAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_author);
        initComponents();
        YandexMetrica.reportEvent(MetricEventNames.STARTED_ADDING_PREFS);
    }

    private void initComponents() {
        bAdd = findViewById(R.id.bAddFavoriteAuthor);
        pbLoading = findViewById(R.id.pbLoadingAddAuthor);
        etAuthorName = findViewById(R.id.etAuthor);
        RetrofitService retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);

        bAdd.setOnClickListener(v -> askServerToAddFavoriteAuthor());
    }

    private void askServerToAddFavoriteAuthor() {
        String authorName = etAuthorName.getText().toString();
        if (authorName.isEmpty()) {
            Toast.makeText(this, "You should fill all the fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        AddFavAuthorRequest addFavAuthorRequest = new AddFavAuthorRequest();
        addFavAuthorRequest.setAuthorName(authorName);
        addFavAuthorRequest.setLogin(getIntent().getStringExtra(CabinetFragment.EXTRA_LOGIN));
        onAddRequestSent();
        userApi.addFavAuthor(addFavAuthorRequest).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NotNull Call<Boolean> call, @NotNull Response<Boolean> response) {
                onAddRequestFinished();
                if (response.body() == null || !response.body()) {
                    Toast.makeText(AddFavAuthorActivity.this,
                            "Failed to add the author to favorites. " +
                                    "Perhaps there is no music with such author.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                YandexMetrica.reportEvent(MetricEventNames.ADDED_PREFS);
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Boolean> call, @NotNull Throwable t) {
                onAddRequestFinished();
                Toast.makeText(
                        AddFavAuthorActivity.this,
                        "Failed to add the author to favorites.",
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