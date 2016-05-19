package com.ileja.calendar.cronofy.bean;

/**
 * Created by chentao on 16/5/16.
 */
public class AccessTokenBean {

    private String client_id;
    private String client_secret;
    private String grant_type;
    private String code;
    private String redirect_uri;


    public AccessTokenBean(){

    }

    public AccessTokenBean(String client_id, String client_secret, String code, String redirect_uri) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.grant_type = "authorization_code";
        this.code = code;
        this.redirect_uri = redirect_uri;
    }
}
