package com.pkn.rpmmotor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Spearpart {
    @SerializedName("id_spearpart")
    @Expose
    private int id_spearpart;
    @SerializedName("spearpart")
    @Expose
    private String spearpart;
    @SerializedName("harga_s")
    @Expose
    private String harga_s;

    public Spearpart(){}

    public Spearpart(int id_spearpart, String spearpart, String harga_s){
        this.id_spearpart = id_spearpart;
        this.spearpart = spearpart;
        this.harga_s = harga_s;
    }

    public int getId_spearpart() {
        return id_spearpart;
    }

    public void setId_spearpart(int id_spearpart) {
        this.id_spearpart = id_spearpart;
    }

    public String getSpearpart() {
        return spearpart;
    }

    public void setSpearpart(String spearpart) {
        this.spearpart = spearpart;
    }

    public String getHarga_s() {
        return harga_s;
    }

    public void setHarga_s(String harga_s) {
        this.harga_s = harga_s;
    }
}
