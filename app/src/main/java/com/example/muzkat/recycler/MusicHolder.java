package com.example.muzkat.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;

public class MusicHolder extends RecyclerView.ViewHolder {
    private TextView tvMusicName;
    private TextView tvAuthorName;
    private TextView tvGenreName;

    public MusicHolder(@NonNull View itemView) {
        super(itemView);
        this.tvMusicName = itemView.findViewById(R.id.tvMusicName);
        this.tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
        this.tvGenreName = itemView.findViewById(R.id.tvGenreName);
    }

    public TextView getTvMusicName() {
        return tvMusicName;
    }

    public TextView getTvAuthorName() {
        return tvAuthorName;
    }

    public TextView getTvGenreName() {
        return tvGenreName;
    }
}
