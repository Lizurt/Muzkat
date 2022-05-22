package com.example.muzkat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muzkat.model.request.AddFavAuthorRequest;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFavAuthorActivity extends AppCompatActivity {
    private EditText etAuthorName;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav_author);
        initComponents();
    }

    private void initComponents() {
        etAuthorName = findViewById(R.id.etAuthor);
        RetrofitService retrofitService = new RetrofitService();
        userApi = retrofitService.getRetrofit().create(UserApi.class);
        findViewById(R.id.bAddFavoriteAuthor).setOnClickListener(v -> askServerToAddFavoriteAuthor());
    }

    private void askServerToAddFavoriteAuthor() {
        String authorName = etAuthorName.getText().toString();
        if (authorName.isEmpty()) {
            Toast.makeText(this, "Nothing was added.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AddFavAuthorRequest addFavAuthorRequest = new AddFavAuthorRequest();
        addFavAuthorRequest.setAuthorName(authorName);
        addFavAuthorRequest.setLogin(getIntent().getStringExtra(CabinetFragment.EXTRA_LOGIN));
        findViewById(R.id.pbLoading).setVisibility(View.VISIBLE);
        userApi.addFavAuthor(addFavAuthorRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                findViewById(R.id.pbLoading).setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                findViewById(R.id.pbLoading).setVisibility(View.GONE);
                Toast.makeText(
                        AddFavAuthorActivity.this,
                        "Failed to add an author to favorites.",
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }
        });
    }
}