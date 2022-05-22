package com.example.muzkat.model.request;

public class DeleteFavAuthorRequest {
    private String login;
    private String authorName;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
