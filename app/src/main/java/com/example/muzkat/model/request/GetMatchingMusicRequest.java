package com.example.muzkat.model.request;

public class GetMatchingMusicRequest {
    private Integer amount;
    private String login;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
