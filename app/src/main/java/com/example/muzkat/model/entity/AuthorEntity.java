package com.example.muzkat.model.entity;


import java.util.Set;

public class AuthorEntity {
    private int id;
    private String name;
    private Set<UserEntity> favoredUsers;
    private Set<MusicEntity> musicsByAuthor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserEntity> getFavoredUsers() {
        return favoredUsers;
    }

    public void setFavoredUsers(Set<UserEntity> favoredUsers) {
        this.favoredUsers = favoredUsers;
    }

    public Set<MusicEntity> getMusicsByAuthor() {
        return musicsByAuthor;
    }

    public void setMusicsByAuthor(Set<MusicEntity> musicsByAuthor) {
        this.musicsByAuthor = musicsByAuthor;
    }
}
