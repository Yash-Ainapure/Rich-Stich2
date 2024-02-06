package com.example.rich_stich;

import java.io.Serializable;

public class GenderAndApparelSelection implements Serializable {
    String gender, apparel;
    String[] measurements;

    public GenderAndApparelSelection() {
    }

    public GenderAndApparelSelection(String gender, String apparel, String[] measurements) {
        this.gender = gender;
        this.apparel = apparel;
        this.measurements = measurements;
    }

    public String[] getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String[] measurements) {
        this.measurements = measurements;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getApparel() {
        return apparel;
    }

    public void setApparel(String apparel) {
        this.apparel = apparel;
    }
}
