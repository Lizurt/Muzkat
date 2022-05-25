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
import android.widget.Toast;

import com.example.muzkat.model.request.CountInMetricRequest;
import com.example.muzkat.recycler.MusicAdapter;
import com.example.muzkat.model.entity.MusicEntity;
import com.example.muzkat.model.request.GetMatchingMusicRequest;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.AuthorApi;
import com.example.muzkat.retrofit.api.GenreApi;
import com.example.muzkat.retrofit.api.MetricApi;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicFragment extends Fragment {
    private RetrofitService retrofitService;
    private GenreApi genreApi;
    private AuthorApi authorApi;
    private UserApi userApi;
    private MusicApi musicApi;
    private MetricApi metricApi;

    private MainActivity mainActivity;

    private RecyclerView rvMusicList;
    private FloatingActionButton fabAddMusic;

    SharedPreferences sharedPreferences;

    public MusicFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            throw new ClassCastException("Couldn't get the main activity.");
        }
        this.retrofitService = new RetrofitService();
        this.genreApi = retrofitService.getRetrofit().create(GenreApi.class);
        this.authorApi = retrofitService.getRetrofit().create(AuthorApi.class);
        this.userApi = retrofitService.getRetrofit().create(UserApi.class);
        this.musicApi = retrofitService.getRetrofit().create(MusicApi.class);
        this.metricApi = retrofitService.getRetrofit().create(MetricApi.class);
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rvMusicList = mainActivity.findViewById(R.id.rvMusicList);
        rvMusicList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        sharedPreferences = mainActivity.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        fabAddMusic = mainActivity.findViewById(R.id.fabAddMusic);
        String login = sharedPreferences.getString(MainActivity.LOGIN_PREF, "");
        String password = sharedPreferences.getString(MainActivity.PASSWORD_PREF, "");
        if (login.isEmpty() || password.isEmpty()) {
            askServerToFillMusicListRandomly();
            fabAddMusic.setVisibility(View.GONE);
        } else {
            askServerToFillMusicListSmartly();
            fabAddMusic.setOnClickListener(v -> {
                Intent intentGoToAddNewMusic = new Intent(mainActivity, AddMusicActivity.class);
                intentGoToAddNewMusic.putExtra(CabinetFragment.EXTRA_LOGIN, login);
                startActivity(intentGoToAddNewMusic);
            });
        }
    }

    private void askServerToFillMusicListSmartly() {
        String login = sharedPreferences.getString(MainActivity.LOGIN_PREF, "");
        if (login.isEmpty()) {
            return;
        }
        GetMatchingMusicRequest getMatchingMusicRequest = new GetMatchingMusicRequest();
        getMatchingMusicRequest.setLogin(login);
        getMatchingMusicRequest.setAmount(8);
        musicApi.getMatching(getMatchingMusicRequest).enqueue(new Callback<List<MusicEntity>>() {
            @Override
            public void onResponse(@NotNull Call<List<MusicEntity>> call, @NotNull Response<List<MusicEntity>> response) {
                fillMusicList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<MusicEntity>> call, @NotNull Throwable t) {
                Toast.makeText(mainActivity, "Couldn't get any matching music from the server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void askServerToFillMusicListRandomly() {
        musicApi.getRandom(4).enqueue(new Callback<List<MusicEntity>>() {
            @Override
            public void onResponse(@NotNull Call<List<MusicEntity>> call, @NotNull Response<List<MusicEntity>> response) {
                fillMusicList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<MusicEntity>> call, @NotNull Throwable t) {
                Toast.makeText(
                        mainActivity,
                        "Couldn't get any music from the server.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void fillMusicList(List<MusicEntity> musicEntities) {
        rvMusicList.setAdapter(new MusicAdapter(musicEntities));
    }
}
