package com.example.root.myapplication;

/**
 * Created by root on 11/1/15.
 */
public class Clothing {
    private String maker;
    private String type;//Category
    private String description;
    private String urlShop;
    private String urlImage;
    private String onSale;
    private String price;
    private String partNumber;
    private String objectID;
    private String brand;
    private String name;


    public Clothing( String Maker, String Type, String Description, String UrlShop, String UrlImage, String OnSale, String Price, String PartNumber,String ObjectID, String Brand,String Name){
        maker=Maker;
        type = Type;
        description = Description;

        urlShop=UrlShop;
        urlImage=UrlImage;

        onSale=OnSale;
        price = Price;
        partNumber=PartNumber;
        objectID=ObjectID;
        brand = Brand;
        name=Name;
        this.parseFurther();

    }

    private void parseFurther(){
        //parse Description to 25 characters
        description = description.substring(0, Math.min(description.length(), 150));
        if(description.length() == 150){
            description = description + "...";
        }
        description = description.replace("\\n", " ");
        description = description.replace("\\r", "");

        //parse Name
        name = name.replace("'","");
        name = name.replace("\"","");

        //edit url image to only have single url not multiple
        if(urlImage.contains("', u'")){
            String[] urlSplit = urlImage.split("', u'");
            urlImage = urlSplit[1];
        }
        urlImage = urlImage.replace("'","");

        //parse type
        if(type.contains("\", u\"")){
            String[] typeSplit = type.split("\", u\"");
            type=typeSplit[1];
        }
        type = type.replace("\"","");
        type = type.replace("]","");
        type = type.replace(">","");
    }
    public String getMaker(){return maker;}

    public String getType(){return type;}

    public String getDescription(){return description;}

    public String getUrlShop(){return urlShop;}

    public String getUrlImage(){return urlImage;}

    public String getOnSale(){return onSale;}

    public String getPrice(){return price;}

    public String getPartNumber(){return partNumber;}

    public String getObjectID(){return objectID;}

    public String getBrand(){return brand;}

    public String getName(){return name;}
}