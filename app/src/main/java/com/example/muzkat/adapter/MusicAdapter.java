package com.example.muzkat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;
import com.example.muzkat.model.entity.MusicEntity;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {
    private List<MusicEntity> musics;

    public MusicAdapter(List<MusicEntity> musics) {
        this.musics = musics;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.music_list_item,
                parent,
                false
        );
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        MusicEntity musicEntity = musics.get(position);
        holder.getTvMusicName().setText(musicEntity.getName());
        holder.getTvAuthorName().setText(musicEntity.getAuthor().getName());
        holder.getTvGenreName().setText(musicEntity.getGenre().getName());
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }
}
