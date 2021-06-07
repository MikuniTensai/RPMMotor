package com.pkn.rpmmotor.Remote;

public class APIUtils {
    private APIUtils(){};

    public static final String API_URL = "https://rpm-motor.web.id/";

    public static DataService getDataService(){
        return RetrofitClient.getClient(API_URL).create(DataService.class);
    }
}
