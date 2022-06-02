package com.example.muzkat.recycler;

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

    /**
     * Automatically being called when a view holder is being created
     * @param parent
     * @param viewType
     * @return
     */
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

    /**
     * Automatically being called when a view holder is being binded
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        MusicEntity musicEntity = musics.get(position);
        holder.getTvMusicName().setText(musicEntity.getName());
        holder.getTvAuthorName().setText(musicEntity.getAuthor().getName());
        holder.getTvGenreName().setText(musicEntity.getGenre().getName());
    }

    /**
     * Gets amount of list items
     * @return
     */
    @Override
    public int getItemCount() {
        return musics.size();
    }
}
