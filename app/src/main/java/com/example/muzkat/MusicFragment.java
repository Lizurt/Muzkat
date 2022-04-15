package com.example.muzkat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.muzkat.adapter.MusicAdapter;
import com.example.muzkat.entities.MusicEntity;
import com.example.muzkat.retrofit.RetrofitService;
import com.example.muzkat.retrofit.api.AuthorApi;
import com.example.muzkat.retrofit.api.GenreApi;
import com.example.muzkat.retrofit.api.MusicApi;
import com.example.muzkat.retrofit.api.UserApi;

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

    private MainActivity mainActivity;

    private RecyclerView rvMusicList;

    public MusicFragment() {

    }

    public static MusicFragment newInstance(String param1, String param2) {
        return new MusicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            throw new ClassCastException("Couldn't get the main activity.");
        }
        this.retrofitService = new RetrofitService();
        this.genreApi = retrofitService.getRetrofit().create(GenreApi.class);
        this.authorApi = retrofitService.getRetrofit().create(AuthorApi.class);
        this.userApi = retrofitService.getRetrofit().create(UserApi.class);
        this.musicApi = retrofitService.getRetrofit().create(MusicApi.class);
        this.mainActivity = mainActivity;

        this.rvMusicList = view.findViewById(R.id.rvMusicList);
        rvMusicList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        askServerToFillMusicList();
    }

    private void askServerToFillMusicList() {
        musicApi.getAllMusic().enqueue(new Callback<List<MusicEntity>>() {
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
