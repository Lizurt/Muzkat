package com.example.muzkat.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;
import com.example.muzkat.model.entity.GenreEntity;
import com.example.muzkat.model.request.DeleteFavGenreRequest;
import com.example.muzkat.retrofit.api.UserApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreAdapter extends RecyclerView.Adapter<GenreHolder> {
    private List<GenreEntity> genres;
    private UserApi userApi;
    private String login;

    public GenreAdapter(List<GenreEntity> genres, UserApi userApi, String login) {
        this.genres = genres;
        this.userApi = userApi;
        this.login = login;
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.genre_list_item,
                parent,
                false
        );
        GenreHolder result = new GenreHolder(view);
        view.findViewById(R.id.bRemoveGenre).setOnClickListener(v -> {
            DeleteFavGenreRequest deleteFavGenreRequest = new DeleteFavGenreRequest();
            deleteFavGenreRequest.setLogin(login);
            deleteFavGenreRequest.setGenreName(result.getTvGenreName().getText().toString());
            view.findViewById(R.id.bRemoveGenre).setVisibility(View.GONE);
            view.findViewById(R.id.pbLoadingRemGenre).setVisibility(View.VISIBLE);
            userApi.delFavGenre(deleteFavGenreRequest).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                    int pos = result.getAdapterPosition();
                    genres.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, genres.size());
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    view.findViewById(R.id.bRemoveGenre).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.pbLoadingRemGenre).setVisibility(View.GONE);
                    Toast.makeText(
                            view.getContext(),
                            "Failed to remove the genre from favorites.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
        GenreEntity genreEntity = genres.get(position);
        holder.getTvGenreName().setText(genreEntity.getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}
