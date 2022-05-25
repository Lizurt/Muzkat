package com.example.muzkat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muzkat.model.request.AddMusicRequest;
import com.example.muzkat.model.request.CountInMetricRequest;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.MetricApi;
import com.example.muzkat.retrofit.api.MusicApi;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMusicActivity extends AppCompatActivity {
    private EditText etMusic;
    private EditText etAuthor;
    private EditText etGenre;
    private MusicApi musicApi;
    private MetricApi metricApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        initComponents();
    }

    private void initComponents() {
        etMusic = findViewById(R.id.etMusic);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        RetrofitService retrofitService = new RetrofitService();
        musicApi = retrofitService.getRetrofit().create(MusicApi.class);
        metricApi = retrofitService.getRetrofit().create(MetricApi.class);
        findViewById(R.id.bAddMusic).setOnClickListener(v -> askServerToAddMusic());
    }

    private void askServerToAddMusic() {
        String musicName = String.valueOf(etMusic.getText());
        String authorName = String.valueOf(etAuthor.getText());
        String genreName = String.valueOf(etGenre.getText());

        if (musicName.isEmpty() || authorName.isEmpty() || genreName.isEmpty()) {
            Toast.makeText(
                    this,
                    "You should fill all the fields.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        CountInMetricRequest countInMetricRequest = new CountInMetricRequest();
        countInMetricRequest.setLogin(getIntent().getStringExtra(CabinetFragment.EXTRA_LOGIN));
        countInMetricRequest.setMetricName(Metrics.ADDED_MUSIC);
        metricApi.countUser(countInMetricRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {

            }
        });

        AddMusicRequest addMusicRequest = new AddMusicRequest();
        addMusicRequest.setMusicName(musicName);
        addMusicRequest.setAuthorName(authorName);
        addMusicRequest.setGenreName(genreName);
        musicApi.saveMusic(addMusicRequest).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NotNull Call<Boolean> call, @NotNull Response<Boolean> response) {
                if (response.body() == null) {
                    Toast.makeText(AddMusicActivity.this,
                            "Unknown error during saving music.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String feedback;
                if (response.body()) {
                    feedback = "Successfully added the music to a music list.";
                } else {
                    feedback = "Failed to add the music to the music list.";
                }
                Toast.makeText(
                        AddMusicActivity.this,
                        feedback,
                        Toast.LENGTH_SHORT
                ).show();
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Boolean> call, @NotNull Throwable t) {
                Toast.makeText(
                        AddMusicActivity.this,
                        "Failed to add the music to the music list - the server isn't responding. ",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}