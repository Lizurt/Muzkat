package com.example.muzkat.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muzkat.R;

public class AuthorHolder extends RecyclerView.ViewHolder {
    private TextView tvAuthorName;

    public AuthorHolder(@NonNull View itemView) {
        super(itemView);

        this.tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
    }

    public TextView getTvAuthorName() {
        return tvAuthorName;
    }
}
