package com.example.muzkat.entities;

import java.util.Set;

public class UserEntity {
    private int id;
    private String login;
    private String password;
    private Set<AuthorEntity> favoriteAuthors;
    private Set<GenreEntity> favoriteGenres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<AuthorEntity> getFavoriteAuthors() {
        return favoriteAuthors;
    }

    public void setFavoriteAuthors(Set<AuthorEntity> favoriteAuthors) {
        this.favoriteAuthors = favoriteAuthors;
    }

    public Set<GenreEntity> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(Set<GenreEntity> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }
}
