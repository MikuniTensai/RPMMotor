package com.pkn.rpmmotor.Remote;

import com.pkn.rpmmotor.Model.Data;
import com.pkn.rpmmotor.Model.Jasa;
import com.pkn.rpmmotor.Model.Mobil;
import com.pkn.rpmmotor.Model.Spearpart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DataService {

    @POST("restapi/")
    Call<Data> addData(@Body Data data);

    @PUT("restapi/")
    Call<Data> updateData(@Body Data data);

    @HTTP(method = "DELETE", path = "restapi/", hasBody = true)
    Call<Data> deleteData(@Body Data data);

    @POST("jasa/")
    Call<Jasa> addDataJasa(@Body Jasa jasa);

    @POST("mobil/")
    Call<Mobil> addDataMobil(@Body Mobil mobil);

    @POST("spearpart/")
    Call<Spearpart> addDataSpearpart(@Body Spearpart spearpart);
}
