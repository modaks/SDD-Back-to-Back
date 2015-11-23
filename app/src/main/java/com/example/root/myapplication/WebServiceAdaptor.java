package com.example.root.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by modaks on 11/21/15.
 */
public class WebServiceAdaptor {


    private ArrayList<Clothing> temp_array;
    private static final String LOG_TAG = "debugger";
    private Models model;
    private String clothesFilterURL;


    public WebServiceAdaptor(String AllClothes/*String LikedClothes*/){

        clothesFilterURL=new StringBuilder().append(AllClothes).append("/").toString();
       // clothesLikedURL="http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/AkshayMata";
        // Create Model class
        model = new Models();
        model.setArrayClothes(getClothing(AllClothes));
        model.setArrayLikedClothes(getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/AkshayMata"));
    }

    public ArrayList<Clothing> getArrayClothing(){return model.getArrayClothes();}
    public ArrayList<Clothing> getArrayLikedClothing(){return model.getArrayLikedClothes();}
    public String[] getMyStringArrayFilters(){return model.getMyStringArrayFilters();}
    public boolean getArrayFiltersOnOff(int index){return model.getArrayFiltersOnOff()[index];}
    public void updateArrayFiltersOnOff(int index, boolean TF){
        model.updateArrayFiltersOnOff(index,TF);
    }

    // Update clothing array when we press on the filters
    public void updateClothingFilters(String filterOption){
        clothesFilterURL=new StringBuilder().append(clothesFilterURL).append(filterOption).append(",").toString();
        //model.setArrayClothes(getClothing(filterOption));
        Log.i(LOG_TAG, "Updating filters" + clothesFilterURL);
    }
    public void removeClothingFilters(String filterOption){
        clothesFilterURL=clothesFilterURL.replace(filterOption+",","");
        //model.setArrayClothes(getClothing(filterOption));
        Log.i(LOG_TAG, "Updating filters"+clothesFilterURL);
    }

    public ArrayList<Clothing> getClothing(final String urlAWS){
        //create return variable for this function
        temp_array = new ArrayList<Clothing>();
        temp_array.clear();

        // Create new thread since we are accessing Network
        new Thread(){
            public void run() {

                //string will contain allJson text
                String link="";

                try{
                    //retreiving json info regarding all clothes
                    link = getHTML(urlAWS);
                }catch(Exception e){
                    Log.i(LOG_TAG, "Failed to retrieve Jsoin string from onCreate");
                }

                //Tokens tores individual Json objaects as strings
                String[] tokens = link.split("\\}\\{");
                //Tokens_content temporarily stores content of each individual token
                String[] tokens_content=null;
                //Delims will be used to parse tokens into individual content
                String delims1 = "u'merchant': u'|\\', u'category': |\\, u'description': u\"|\\\", u'url': u'|\\', u'image': \\[u'|\\'\\], u'on_sale': |\\, u'price': |\\, u'part_number': u'|\\', u'_id': ObjectId\\('|\\'\\), u'brand': u'|\\', u'name': u|\\, u'description': u'|\\', u'url': u'";

                for (int i=0; i<tokens.length;i++){
                    tokens_content = tokens[i].split(delims1);
                    if(tokens_content.length==12){
                        temp_array.add(new Clothing(tokens_content[1], tokens_content[2], tokens_content[3], tokens_content[4], tokens_content[5], tokens_content[6], tokens_content[7], tokens_content[8], tokens_content[9], tokens_content[10], tokens_content[11]));
                    }
                }
            }
        }.start();

        // Sleep for .4 seconds to allow thread to catch up
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e){
            Log.i(LOG_TAG,"Failed to sleep");
        }

        return temp_array;
    }

    // Get text from HTML file
    public static String getHTML(String urlToRead) throws Exception {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            Log.i(LOG_TAG, "printed from TRY and returned result end");
            return result.toString();
        }catch (Exception e){
            e.printStackTrace();
            Log.i(LOG_TAG, "printed from catch and returned NULL");
            return null;
        }
    }

    public int dislike(int count){
        StringBuffer link = new StringBuffer("http://ec2-54-210-37-207.compute-1.amazonaws.com/updateLikedProducts/AkshayMata/");

        try{
            link.append(model.getArrayClothes().get(count).getObjectID());
        }catch(Exception e){}

        link.append("/0");
        String temp_link = link.toString();
        try{
            getHTML(temp_link);
        }catch(Exception e){
            Log.i(LOG_TAG,"Failed to retrieve Jsoin string from onCreate");
        }

        // Increment count
        if(count != model.getArrayClothes().size() - 1){
            count += 1;
        }else{
            count = 0;
        }
        return count;
    }

    public int like(int count){
        // Write to getLikedProducts page
        StringBuffer link = new StringBuffer("http://ec2-54-210-37-207.compute-1.amazonaws.com/updateLikedProducts/AkshayMata/");
        try{
            link.append(model.getArrayClothes().get(count).getObjectID());
        }catch(Exception e){

        }
        link.append("/1");
        String temp_link = link.toString();
        try{
            getHTML(temp_link);
        }catch(Exception e){
            Log.i(LOG_TAG,"Failed to retrieve Jsoin string from onCreate");
        }

        // Increment count
        if(count < model.getArrayClothes().size() - 1){
            count += 1;
        }else{
            // Ideally refresh
            count = 0;
        }
        return count;
    }

    public void clearClothing(){
        if(clothesFilterURL.charAt(clothesFilterURL.length() - 1)=='/')
            model.setArrayClothes(getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/AkshayMata"));
        else
            model.setArrayClothes(getClothing(clothesFilterURL));
        Log.i(LOG_TAG,clothesFilterURL);
    }

    public void clearLikedClothing(){
        model.setArrayLikedClothes(getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/AkshayMata"));
    }
}
