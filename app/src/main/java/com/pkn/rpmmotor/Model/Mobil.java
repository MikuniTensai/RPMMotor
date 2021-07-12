package com.pkn.rpmmotor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mobil {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("no_plat")
    @Expose
    private String no_plat;
    @SerializedName("brand_mobil")
    @Expose
    private String brand_mobil;
    @SerializedName("jenis_mobil")
    @Expose
    private String jenis_mobil;

    public Mobil(){}

    public Mobil(int id, String no_plat, String brand_mobil, String jenis_mobil){
        this.id = id;
        this.no_plat = no_plat;
        this.brand_mobil = brand_mobil;
        this.jenis_mobil = jenis_mobil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNo_plat() {
        return no_plat;
    }

    public void setNo_plat(String no_plat) {
        this.no_plat = no_plat;
    }

    public String getBrand_mobil() {
        return brand_mobil;
    }

    public void setBrand_mobil(String brand_mobil) {
        this.brand_mobil = brand_mobil;
    }

    public String getJenis_mobil() {
        return jenis_mobil;
    }

    public void setJenis_mobil(String jenis_mobil) {
        this.jenis_mobil = jenis_mobil;
    }

}
