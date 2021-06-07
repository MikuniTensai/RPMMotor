package com.pkn.rpmmotor.Remote;

import com.pkn.rpmmotor.Model.Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataService {

    @POST("restapi/")
    Call<Data> addData(@Body Data data);

    @PUT("restapi/")
    Call<Data> updateData(@Body Data data);

    @HTTP(method = "DELETE", path = "restapi/", hasBody = true)
    Call<Data> deleteData(@Body Data data);
}
