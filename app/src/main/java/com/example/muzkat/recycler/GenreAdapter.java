package com.example.muzkat.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;
import com.example.muzkat.model.entity.GenreEntity;
import com.example.muzkat.model.entity.MusicEntity;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreHolder> {
    private List<GenreEntity> genres;

    public GenreAdapter(List<GenreEntity> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.genre_list_item,
                parent,
                false
        );
        return new GenreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreHolder holder, int position) {
        GenreEntity genreEntity = genres.get(position);
        holder.getTvAuthorName().setText(genreEntity.getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}
