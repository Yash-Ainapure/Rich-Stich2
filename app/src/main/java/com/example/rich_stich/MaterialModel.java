package com.example.rich_stich;

public class MaterialModel {
    private String name,url;
    private int price;

    public MaterialModel() {
    }

    public MaterialModel(String url,int price,String name) {
        this.url = url;
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
