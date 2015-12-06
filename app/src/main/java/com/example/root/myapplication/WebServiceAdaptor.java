package com.example.root.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Web service adaptor for handling web service calls
 */
public class WebServiceAdaptor {
    // Private variables
    private ArrayList<Clothing> temp_array;
    private static final String LOG_TAG = "debugger";
    private Models model;
    private String clothesFilterURL;
    private String userString;
    private String userID;
    private String username;

    public WebServiceAdaptor(String AllClothes){

        model = new Models();
        model.setArrayClothes(getClothing(AllClothes));
        String formatted_URL =
                String.format
                        ("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/%s/%s",
                        username, userID);
        clothesFilterURL=new StringBuilder().append(formatted_URL).append("/").toString();
        model.setArrayLikedClothes(getClothing(formatted_URL));
    }

    public void updateWebServerAdaptor(String AllClothes){
        clothesFilterURL=new StringBuilder().append(AllClothes).append("/").toString();
        model = new Models();
        model.setArrayClothes(getClothing(AllClothes));
        String formatted_URL =
                String.format
                        ("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/%s/%s",
                                username, userID);
        model.setArrayLikedClothes(getClothing(formatted_URL));
    }

    // Get and update methods
    public ArrayList<Clothing> getArrayClothing(){return model.getArrayClothes();}
    public ArrayList<Clothing> getArrayLikedClothing(){return model.getArrayLikedClothes();}
    public String[] getMyStringArrayFilters(){return model.getMyStringArrayFilters();}
    public boolean getArrayFiltersOnOff(int index){return model.getArrayFiltersOnOff()[index];}
    public void updateArrayFiltersOnOff(int index, boolean TF){
        model.updateArrayFiltersOnOff(index, TF);
    }

    public void updateUserID(String ID){
        userID = ID;
    }

    public void updateUsername(String input){
        username=input;
    }

    // Update or remove clothing array when we press on the filters options
    public void updateClothingFilters(String filterOption){
        if(clothesFilterURL.charAt(clothesFilterURL.length() - 1)=='/') {
            clothesFilterURL =
                    new StringBuilder().append(clothesFilterURL).append(filterOption).toString();
        }else {
            clothesFilterURL =
                    new StringBuilder().append(clothesFilterURL)
                            .append(",").append(filterOption).toString();
        }
        Log.i(LOG_TAG, "Updating filters " + clothesFilterURL);
    }
    public void removeClothingFilters(String filterOption){
        clothesFilterURL=clothesFilterURL.replace(filterOption,"");
        clothesFilterURL=clothesFilterURL.replace("/,","/");
        clothesFilterURL=clothesFilterURL.replace(",,",",");
        Log.i(LOG_TAG, "Updating filters "+clothesFilterURL);
    }

    // Get User from URL
    public String getUser(final String urlAWS){
        // Create return variable for this function
        // Create new thread since we are accessing Network
        new Thread(){
            public void run() {
                //string will contain allJson text
                String link="";
                try{
                    // Retreiving json info regarding all clothes
                    link = getHTML(urlAWS);
                }catch(Exception e){
                    Log.i(LOG_TAG, "Failed to retrieve Jsoin string from onCreate");
                }
                userString = link;
            }
        }.start();
        // Sleep for .4 seconds to allow thread to catch up
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e){
            Log.i(LOG_TAG,"Failed to sleep");
        }
        return userString;
    }
    public String[] parser(String input){
        String delims1 = "u'merchant': u'|\\', u'category': |\\, u'description': u\"" +
                "|\\\", u'url': u'|\\', u'image': \\[u'|\\'\\], u'on_sale': " +
                "|\\, u'price': |\\, u'part_number': u'|\\', u'_id': ObjectId\\('" +
                "|\\'\\), u'brand': u'|\\', u'name': u|\\, u'description': u'" +
                "|\\', u'url': u'";
        return input.split(delims1);

    }
    // Get Clothing Objects from URL
    public ArrayList<Clothing> getClothing(final String urlAWS){
        // Create return variable for this function
        temp_array = new ArrayList<Clothing>();
        if(temp_array.size() > 0){
            temp_array.clear();
        }
        // Create new thread since we are accessing Network
        new Thread(){
            public void run() {
                // String will contain allJson text
                String link="";
                try{
                    // Retreiving json info regarding all clothes
                    link = getHTML(urlAWS);
                }catch(Exception e){
                    Log.i(LOG_TAG, "Failed to retrieve Jsoin string from onCreate");
                }
                if(link.equals("Invalid user/token combination")){
                    // Throw error message here
                    Log.i(LOG_TAG,"Error: invalid combination");
                    Log.i(LOG_TAG, urlAWS);
                }else {
                    // Tokens tores individual Json objaects as strings
                    String[] tokens = link.split("\\}\\{");
                    // Tokens_content temporarily stores content of each individual token
                    String[] tokens_content = null;
                    // Delims will be used to parse tokens into individual content
                   /* String delims1 = "u'merchant': u'|\\', u'category': |\\, u'description': u\"" +
                            "|\\\", u'url': u'|\\', u'image': \\[u'|\\'\\], u'on_sale': " +
                            "|\\, u'price': |\\, u'part_number': u'|\\', u'_id': ObjectId\\('" +
                            "|\\'\\), u'brand': u'|\\', u'name': u|\\, u'description': u'" +
                            "|\\', u'url': u'";*/

                    for (int i = 0; i < tokens.length; i++) {
                        tokens_content = parser(tokens[i]);
                        Log.i(LOG_TAG, tokens[i]);
                        if (tokens_content.length == 12) {
                            temp_array.add(new Clothing(tokens_content[1], tokens_content[2],
                                    tokens_content[3], tokens_content[4], tokens_content[5],
                                    tokens_content[6], tokens_content[7], tokens_content[8],
                                    tokens_content[9], tokens_content[10], tokens_content[11]));
                        }
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

    // When user dislikes clothing
    public int dislike(int count){
        String formatted_URL = String.format
                ("http://ec2-54-210-37-207.compute-1.amazonaws.com/updateLikedProducts/%s/%s/",
                        username, userID);
        StringBuffer link = new StringBuffer(formatted_URL);

        try{
            link.append(model.getArrayClothes().get(count).getObjectID());
        }catch(Exception e){
            Log.i(LOG_TAG,"Failed to append object ID");
        }

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
            // Refresh
            count = 0;
        }
        return count;
    }

    // When user likes clothing
    public int like(int count){
        // Write to getLikedProducts page
        String formatted_URL = String.format
                ("http://ec2-54-210-37-207.compute-1.amazonaws.com/updateLikedProducts/%s/%s/",
                        username, userID);

        StringBuffer link = new StringBuffer(formatted_URL);
        try{
            link.append(model.getArrayClothes().get(count).getObjectID());
        }catch(Exception e){
            Log.i(LOG_TAG,"Failed to append object ID");
        }
        link.append("/1");
        String temp_link = link.toString();
        Log.i(LOG_TAG, temp_link);
        try{
            getHTML(temp_link);
        }catch(Exception e){
            Log.i(LOG_TAG,"Failed to retrieve Jsoin string from onCreate");
        }
        // Increment count
        if(count < model.getArrayClothes().size() - 1){
            count += 1;
        }else{
            // Refresh
            count = 0;
        }
        return count;
    }

    // Refresh clothing array
    public void clearClothing(){
        if(clothesFilterURL.charAt(clothesFilterURL.length() - 1)=='/'){
            String formatted_URL
                    = String.format("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/%s/%s",
                    username, userID);
            model.setArrayClothes(getClothing(formatted_URL));
        }else{
            model.setArrayClothes(getClothing(clothesFilterURL.substring(0, clothesFilterURL.length() - 1)));   // Make sure last comma is gone
        }
        Log.i(LOG_TAG,clothesFilterURL);
    }

    // Refresh liked clothing array
    public void clearLikedClothing(){
        String formatted_URL = String.format
                ("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/%s/%s", username, userID);
        model.setArrayLikedClothes(getClothing(formatted_URL));
    }
}
