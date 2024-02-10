package com.example.rich_stich;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer  implements Parcelable {
    private String customerId;
    private String name;
    private String mobile;
    private String address;

    @Override
    public String toString() {
        return name; // Display customer name in the Spinner
    }
    // Default constructor required for Firebase
    public Customer() {
    }

    public Customer(String customerId, String name, String mobile, String address) {
        this.customerId = customerId;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    protected Customer(Parcel in) {
        customerId = in.readString();
        name = in.readString();
        mobile = in.readString();
        address = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerId);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
