package com.example.root.myapplication;

/**
 * Created by root on 11/1/15.
 */
public class Clothing {

    private String url;
    private String price;
    private String type;
    private String brand;
    private String description;

    public Clothing(String URL, String Price, String Type, String Brand, String Description){
        url = URL;
        price = Price;
        type = Type;
        brand = Brand;
        description = Description;
    }


    public String getPrice(){
        return price;
    }

    public String getType(){
        return type;
    }

    public String getBrand(){
        return brand;
    }

    public String getDescription(){
        return description;
    }

    public String getURL(){
        return url;
    }


}
