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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private Button bUpdate;
    private Button bPrev;
    private Button bNext;

    private ProgressBar pbLoading;

    private SharedPreferences sharedPreferences;

    private int currPage = 0;
    public static final int AMT_PER_PAGE = 4;

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
        rvMusicList.setAdapter(null);
        rvMusicList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        pbLoading = mainActivity.findViewById(R.id.pbLoadingMusic);
        initButtons();

        sharedPreferences = mainActivity.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String login = sharedPreferences.getString(MainActivity.LOGIN_PREF, "");
        String password = sharedPreferences.getString(MainActivity.PASSWORD_PREF, "");
        if (login.isEmpty() || password.isEmpty()) {
            askServerToFillMusicListRandomly();
            fabAddMusic.setVisibility(View.GONE);
            bUpdate.setVisibility(View.VISIBLE);
            bPrev.setVisibility(View.GONE);
            bNext.setVisibility(View.GONE);
        } else {
            fabAddMusic.setVisibility(View.VISIBLE);
            bUpdate.setVisibility(View.GONE);
            bPrev.setVisibility(View.VISIBLE);
            bNext.setVisibility(View.VISIBLE);
            askServerToFillMusicListSmartly();
            fabAddMusic.setOnClickListener(v -> {
                Intent intentGoToAddNewMusic = new Intent(mainActivity, AddMusicActivity.class);
                intentGoToAddNewMusic.putExtra(CabinetFragment.EXTRA_LOGIN, login);
                startActivity(intentGoToAddNewMusic);
            });
        }
    }

    private void initButtons() {
        fabAddMusic = mainActivity.findViewById(R.id.fabAddMusic);
        bUpdate = mainActivity.findViewById(R.id.bUpdate);
        bUpdate.setOnClickListener(v -> {
            askServerToFillMusicListRandomly();
        });
        bPrev = mainActivity.findViewById(R.id.bPrev);
        bPrev.setOnClickListener(v -> {
            currPage = Math.max(0, currPage - 1);
            askServerToFillMusicListSmartly();
        });
        bNext = mainActivity.findViewById(R.id.bNext);
        bNext.setOnClickListener(v -> {
            currPage = Math.max(0, currPage + 1);
            askServerToFillMusicListSmartly();
        });
    }

    private void askServerToFillMusicListSmartly() {
        String login = sharedPreferences.getString(MainActivity.LOGIN_PREF, "");
        if (login.isEmpty()) {
            return;
        }
        GetMatchingMusicRequest getMatchingMusicRequest = new GetMatchingMusicRequest();
        getMatchingMusicRequest.setLogin(login);
        getMatchingMusicRequest.setAmount(AMT_PER_PAGE);
        getMatchingMusicRequest.setPage(currPage);
        onUpdateListStarted();
        musicApi.getMatching(getMatchingMusicRequest).enqueue(new Callback<List<MusicEntity>>() {
            @Override
            public void onResponse(@NotNull Call<List<MusicEntity>> call, @NotNull Response<List<MusicEntity>> response) {
                onUpdateListFinished();
                fillMusicList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<MusicEntity>> call, @NotNull Throwable t) {
                onUpdateListFinished();
                Toast.makeText(
                        mainActivity,
                        "Server is not responding. Try again later.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void askServerToFillMusicListRandomly() {
        onUpdateListStarted();
        musicApi.getRandom(AMT_PER_PAGE).enqueue(new Callback<List<MusicEntity>>() {
            @Override
            public void onResponse(@NotNull Call<List<MusicEntity>> call, @NotNull Response<List<MusicEntity>> response) {
                onUpdateListFinished();
                fillMusicList(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<MusicEntity>> call, @NotNull Throwable t) {
                onUpdateListFinished();
                Toast.makeText(
                        mainActivity,
                        "Server is not responding. Try again later.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void onUpdateListStarted() {
        pbLoading.setVisibility(View.VISIBLE);
        rvMusicList.setVisibility(View.GONE);
    }

    private void onUpdateListFinished() {
        pbLoading.setVisibility(View.GONE);
        rvMusicList.setVisibility(View.VISIBLE);
    }

    private void fillMusicList(List<MusicEntity> musicEntities) {
        rvMusicList.setAdapter(new MusicAdapter(musicEntities));
    }
}
