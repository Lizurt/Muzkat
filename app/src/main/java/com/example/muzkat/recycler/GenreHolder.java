package com.example.muzkat.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;

public class GenreHolder extends RecyclerView.ViewHolder {
    private TextView tvGenreName;

    public GenreHolder(@NonNull View itemView) {
        super(itemView);

        this.tvGenreName = itemView.findViewById(R.id.tvGenreName);
    }

    public TextView getTvGenreName() {
        return tvGenreName;
    }
}
