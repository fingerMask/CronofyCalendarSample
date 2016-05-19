package com.ileja.calendar.cronofy.bean;

/**
 * Created by chentao on 16/5/16.
 */
public class RefreshTokenBean {

    private String client_id;
    private String client_secret;
    private String grant_type;
    private String refresh_token;


    public RefreshTokenBean(){

    }

    public RefreshTokenBean(String client_id, String client_secret, String refreshToken) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = "refresh_token";
        this.refresh_token = refreshToken;
    }
}
