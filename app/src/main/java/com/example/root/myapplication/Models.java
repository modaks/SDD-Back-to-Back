package com.example.root.myapplication;

import android.util.Log;

import java.util.ArrayList;
/**
 * To store Data in regards to Users and Data
 */
public class Models {
    // Private variables
    private ArrayList<Clothing> arrayClothing;
    private ArrayList<Clothing> arrayLikedClothing;
    private String[] myStringArrayFilters={"Shirts", "Pants", "Shoes", "Casual", "Hats", "Men's", "Women's"};
    private boolean[] arrayFiltersOnOff={false,false,false,false,false,false,false};
    private static final String LOG_TAG = "debugger";

    // Constructor
    public Models(){
        arrayLikedClothing = new ArrayList<Clothing>();
        arrayClothing = new ArrayList<Clothing>();
    }

    // Return arrays
    public ArrayList<Clothing> getArrayClothes(){ return arrayClothing; }
    public ArrayList<Clothing> getArrayLikedClothes(){ return arrayLikedClothing; }
    public String[] getMyStringArrayFilters(){return myStringArrayFilters;}
    public boolean[] getArrayFiltersOnOff(){return arrayFiltersOnOff;}
    public void updateArrayFiltersOnOff(int index, boolean TF){
        arrayFiltersOnOff[index]=TF;
    }

    // Set arrays
    public void setArrayClothes(ArrayList<Clothing> clothes) {
        Log.i(LOG_TAG, "Updating Model, size of model:");
        if(arrayClothing.size() > 0){
            arrayClothing.clear();
        }
        arrayClothing = clothes;
        Log.i(LOG_TAG, Integer.toString(clothes.size()));
    }
    public void setArrayLikedClothes(ArrayList<Clothing> clothes){
        if(arrayLikedClothing.size() > 0) {
            arrayLikedClothing.clear();
        }
        arrayLikedClothing = clothes;
    }

    // Update arrays
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