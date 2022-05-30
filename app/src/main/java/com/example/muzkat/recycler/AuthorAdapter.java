package com.example.muzkat.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;
import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;
import com.example.muzkat.model.request.DeleteFavAuthorRequest;
import com.example.muzkat.model.request.DeleteFavGenreRequest;
import com.example.muzkat.retrofit.api.UserApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorHolder> {
    private List<AuthorEntity> authors;
    private UserApi userApi;
    private String login;

    public AuthorAdapter(List<AuthorEntity> authors, UserApi userApi, String login) {
        this.authors = authors;
        this.userApi = userApi;
        this.login = login;
    }

    @NonNull
    @Override
    public AuthorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.author_list_item,
                parent,
                false
        );
        AuthorHolder result = new AuthorHolder(view);
        view.findViewById(R.id.bRemoveAuthor).setOnClickListener(v -> {
            DeleteFavAuthorRequest deleteFavAuthorRequest = new DeleteFavAuthorRequest();
            deleteFavAuthorRequest.setLogin(login);
            deleteFavAuthorRequest.setAuthorName(result.getTvAuthorName().getText().toString());
            view.findViewById(R.id.bRemoveAuthor).setVisibility(View.GONE);
            view.findViewById(R.id.pbLoadingRemAuthor).setVisibility(View.VISIBLE);
            userApi.delFavAuthor(deleteFavAuthorRequest).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                    int pos = result.getAdapterPosition();
                    authors.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, authors.size());
                }

                @Override
                public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                    view.findViewById(R.id.bRemoveAuthor).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.pbLoadingRemAuthor).setVisibility(View.GONE);
                    Toast.makeText(
                            view.getContext(),
                            "Failed to remove the author from favorites.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        });
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorHolder holder, int position) {
        AuthorEntity authorEntity = authors.get(position);
        holder.getTvAuthorName().setText(authorEntity.getName());
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }
}
