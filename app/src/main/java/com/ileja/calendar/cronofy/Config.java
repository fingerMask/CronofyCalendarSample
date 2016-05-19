package com.ileja.calendar.cronofy;

/**
 * Created by chentao on 16/5/16.
 */
public class Config {


    /**
     * 请求的URL
     */
    public static class URL {

        public static final String Request_App_Url = "https://app.cronofy.com/oauth/authorize";
        public static final String Request_Api_Url = "https://api.cronofy.com";
    }

    /**
     * 请求账户相关的信息
     */
    public static class Account{
        public static final String Client_ID = "HHwnZRWhmRtH1B4i1nsgLG-zqckzKTDy";
        public static final String Client_Secret = "IqmG-hKWDrHK7Mp-jlO4XCoEeqNvKadDPfAsbSyI93QNLVwoQW5HI4-2LJ7p-XMKwSEDYbMmuF9ZNSrAQUo6iA";
        public static final String Redirect_Url = "http://www.carrobot.com";
        public static final String Scope = "list_calendars read_events create_event delete_event";
    }

    public static class Const{
        public static final String Request_Url = "request_url";
        public static final String Auth_Code = "auth_code";
        public static final int Request_Login_Code = 0x20;
    }
}
