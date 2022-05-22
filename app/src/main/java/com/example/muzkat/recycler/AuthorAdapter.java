package com.example.muzkat.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;
import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorHolder> {
    private List<AuthorEntity> authors;

    public AuthorAdapter(List<AuthorEntity> authors) {
        this.authors = authors;
    }

    @NonNull
    @Override
    public AuthorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.author_list_item,
                parent,
                false
        );
        return new AuthorHolder(view);
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
