package com.pkn.rpmmotor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("product")
    @Expose
    private String product;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("tax")
    @Expose
    private String tax;

    public Data() {
    }

    public Data(int id, String product, String qty, String price, String total, String status, String created_at, String tax) {
        this.id = id;
        this.product = product;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.status = status;
        this.created_at = created_at;
        this.tax = tax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
