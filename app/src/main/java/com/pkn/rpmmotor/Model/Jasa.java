package com.pkn.rpmmotor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Jasa {
    @SerializedName("id_jasa")
    @Expose
    private int id_jasa;
    @SerializedName("jasa")
    @Expose
    private String jasa;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    public Jasa(){}

    public Jasa(int id_jasa, String jasa, String harga, String tanggal){
        this.id_jasa = id_jasa;
        this.jasa = jasa;
        this.harga = harga;
        this.tanggal = tanggal;
    }

    public int getId_jasa() {
        return id_jasa;
    }

    public void setId_jasa(int id_jasa) {
        this.id_jasa = id_jasa;
    }

    public String getJasa() {
        return jasa;
    }

    public void setJasa(String jasa) {
        this.jasa = jasa;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
