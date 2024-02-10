package com.example.rich_stich;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.rich_stich.Customer;

import java.util.HashMap;
import java.util.Map;

public class OrderInfo implements Parcelable {
    private String gender, material, apparel, quantity, total, clothAmount, addons;
    private Map<String, String> measurements;
    private Customer customer;

    public OrderInfo() {
        measurements = new HashMap<>();
    }

    protected OrderInfo(Parcel in) {
        gender = in.readString();
        material = in.readString();
        apparel = in.readString();
        measurements = in.readHashMap(String.class.getClassLoader());
        quantity = in.readString();
        total = in.readString();
        clothAmount = in.readString();
        addons = in.readString();
        customer = in.readParcelable(Customer.class.getClassLoader());
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel in) {
            return new OrderInfo(in);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getApparel() {
        return apparel;
    }

    public void setApparel(String apparel) {
        this.apparel = apparel;
    }

    public Map<String, String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, String> measurements) {
        this.measurements = measurements;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getClothAmount() {
        return clothAmount;
    }

    public void setClothAmount(String clothAmount) {
        this.clothAmount = clothAmount;
    }

    public String getAddons() {
        return addons;
    }

    public void setAddons(String addons) {
        this.addons = addons;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gender);
        dest.writeString(material);
        dest.writeString(apparel);
        dest.writeMap(measurements);
        dest.writeString(quantity);
        dest.writeString(total);
        dest.writeString(clothAmount);
        dest.writeString(addons);
        dest.writeParcelable(customer, flags);
    }
}
