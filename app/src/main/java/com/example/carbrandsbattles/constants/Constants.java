package com.example.carbrandsbattles.constants;

import com.example.carbrandsbattles.services.UserInfoService;

public class Constants {
    public static final String HTTP_PROTOCOL = "http";
    public static final String HTTP_HOST = HTTP_PROTOCOL + "://192.168.31.32:8000/";
    public static final String TOKEN_EXPIRED = "token is expired by";
    public static String getAuthHeader(){
        return "Bearer " + UserInfoService.getProperty("access_token");
    }
}
