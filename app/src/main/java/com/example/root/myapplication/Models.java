package com.example.root.myapplication;

import java.util.ArrayList;
/**
 * To store Data in regards to Users and Data
 */
public class Models {
    // Private variables
    private ArrayList<Clothing> arrayClothing;
    private ArrayList<Clothing> arrayLikedClothing;

    // Store index for location of current clothes
    private int index = 0;

    public Models(){
        arrayLikedClothing = new ArrayList<Clothing>();
        arrayClothing = new ArrayList<Clothing>();
    }

    // Return arrays
    public ArrayList<Clothing> getArrayClothes(){ return arrayClothing; }
    public ArrayList<Clothing> getArrayLikedClothes(){ return arrayLikedClothing; }

    // Set arrays
    public void setArrayClothes(ArrayList<Clothing> clothes) {
        arrayClothing.clear();
        arrayClothing = clothes;
    }
    public void setArrayLikedClothes(ArrayList<Clothing> clothes){
        arrayLikedClothing.clear();
        arrayLikedClothing = clothes;
    }

    public void addClothes(Clothing cloth){ arrayClothing.add(cloth); }
    public void addRecentlyLiked(Clothing clothes){ arrayLikedClothing.add(clothes); }


    public void addClothes(String Maker, String Type, String Description, String UrlShop, String UrlImage, String OnSale, String Price, String PartNumber,String ObjectID, String Brand,String Name){
        Clothing cloth = new Clothing(Maker, Type, Description, UrlShop, UrlImage, OnSale, Price, PartNumber, ObjectID, Brand, Name);
        arrayClothing.add(cloth);
    }
    public void addRecentlyLiked(String Maker, String Type, String Description, String UrlShop, String UrlImage, String OnSale, String Price, String PartNumber,String ObjectID, String Brand,String Name){
        Clothing clothes = new Clothing(Maker, Type, Description, UrlShop, UrlImage, OnSale, Price, PartNumber, ObjectID, Brand, Name);
        arrayLikedClothing.add(clothes);
    }

}