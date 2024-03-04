package com.cinntra.rgelectric.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SoldItemResponse implements Serializable
{

    @SerializedName("ItemName")
    @Expose
    private String ItemName;
    @SerializedName("ItemCode")
    @Expose
    private String ItemCode;

    @SerializedName("UnitPirce")
    @Expose
    private String UnitPirce;
    @SerializedName("LastSoldDate")
    @Expose
    private String LastSoldDate;
    @SerializedName("TotalQty")
    @Expose
    private String TotalQty;

    @SerializedName("TotalPrice")
    @Expose
    private String TotalPrice;

    @SerializedName("ItemOrderList")
    @Expose
    private ArrayList<SoldItem> ItemOrderList = null;


    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getUnitPirce() {
        return UnitPirce;
    }

    public void setUnitPirce(String unitPirce) {
        UnitPirce = unitPirce;
    }

    public String getLastSoldDate() {
        return LastSoldDate;
    }

    public void setLastSoldDate(String lastSoldDate) {
        LastSoldDate = lastSoldDate;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public ArrayList<SoldItem> getItemOrderList() {
        return ItemOrderList;
    }

    public void setItemOrderList(ArrayList<SoldItem> itemOrderList) {
        ItemOrderList = itemOrderList;
    }
}
