package com.example.root.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    ImageButton b1;
    private int count;  // Store the location of clothes for the swiping page
    private ImageButton iv;
    private Bitmap bitmap;

    //used for LOg.I debugging statments printed to logTag
    private static final String LOG_TAG = "debugger";

    // Checks if coming from recently liked page or swiping page
    private boolean fromSwiping = true;

    // Created only for a function
    //private ArrayList<Clothing> arrayClothingOutput;

    // Create array of clothing objects
   // private ArrayList<Clothing> arrayClothing;

    // Create array of liked objects
    //private ArrayList<Clothing> arrayLikedClothing;

    //WebserviceAdaptor variable handles all webserver
    private WebServiceAdaptor webserver;

    // Store the location of the clothes for the detailed page
    private int location_of_clothes = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;

        // Allows getting URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set current bitmap image
        bitmap = getBitmapFromURL("http://loololi.com/wp-content/uploads/2013/09/Scarves-175-1.jpg");

        //initialize the webserver
        webserver=new WebServiceAdaptor("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/AkshayMata");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // When clicking or swiping through clothes with x mark
    public void buttonOnClick(View v){
        iv = (ImageButton) findViewById((R.id.center_image));
        /*
        // Write to getLikedProducts page
        StringBuffer link = new StringBuffer("http://ec2-54-210-37-207.compute-1.amazonaws.com/updateLikedProducts/AkshayMata/");

        try{
            link.append(arrayClothing.get(count).getObjectID());
        }catch(Exception e){}

        link.append("/0");
        String temp_link = link.toString();
        try{
            getHTML(temp_link);
        }catch(Exception e){
            Log.i(LOG_TAG,"Failed to retrieve Jsoin string from onCreate");
        }

        // Increment count
        if(count != arrayClothing.size() - 1){
            count += 1;
        }else{
            count = 0;
        }
        location_of_clothes = count;
        */

        location_of_clothes=webserver.dislike(count);
        count=location_of_clothes;
        // Get bitmap from URL
        try{
            iv.setImageBitmap(getBitmapFromURL(webserver.getArrayClothing().get(count).getUrlImage()));
        }catch(Exception e){
            Log.i(LOG_TAG,Integer.toString(count));
            Log.i(LOG_TAG, webserver.getArrayClothing().get(count).getUrlImage());
        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.setText("Price - $");
        try{
            field.append(webserver.getArrayClothing().get(count).getPrice());
        }catch(Exception e){

        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.setText("");
        try{
            field.append(webserver.getArrayClothing().get(count).getName());
        }catch(Exception e){

        }
    }

    // When the user clicks the check mark, refresh image and store clothing into liked clothing array
    public void buttonOnClickCheck(View v){
        iv = (ImageButton) findViewById((R.id.center_image));
        /*
        // Write to getLikedProducts page
        StringBuffer link = new StringBuffer("http://ec2-54-210-37-207.compute-1.amazonaws.com/updateLikedProducts/AkshayMata/");
        try{
            link.append(arrayClothing.get(count).getObjectID());
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
        if(count < arrayClothing.size() - 1){
            count += 1;
        }else{
            // Ideally refresh
            count = 0;
        }
        location_of_clothes = count;
        */

        location_of_clothes = webserver.like(count);
        count = location_of_clothes;
        // Get bitmap from URL
        bitmap.recycle();
        bitmap = getBitmapFromURL(webserver.getArrayClothing().get(count).getUrlImage());
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.setText("Price - $");
        try{
            field.append(webserver.getArrayClothing().get(count).getPrice());
        }catch(Exception e){

        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.setText("");
        try{
            field.append(webserver.getArrayClothing().get(count).getName());
        }catch(Exception e){

        }
    }

    // Go to the detailed page and load the current bitmap image
    public void buttonToDetailed(View v){
        setContentView(R.layout.detail_main);
        iv = (ImageButton) findViewById(R.id.imageButton4);

        //If from swiping page
        if(fromSwiping){
            bitmap.recycle();
            bitmap = getBitmapFromURL(webserver.getArrayClothing().get(location_of_clothes).getUrlImage());
            iv.setImageBitmap(bitmap);

            // Set Price
            TextView field = (TextView) findViewById(R.id.Price);
            field.append(webserver.getArrayClothing().get(location_of_clothes).getPrice());

            // Set Brand
            field = (TextView) findViewById(R.id.Brand);
            field.append(webserver.getArrayClothing().get(location_of_clothes).getBrand());

            // Get Type
            field = (TextView) findViewById(R.id.Type);
            field.append(webserver.getArrayClothing().get(location_of_clothes).getType());

            // Get Description
            field = (TextView) findViewById(R.id.Description);
            field.append(webserver.getArrayClothing().get(location_of_clothes).getDescription());

            // Get Name
            field = (TextView) findViewById(R.id.textView7);
            field.setText("");
            field.append(webserver.getArrayClothing().get(location_of_clothes).getName());

        }else{
        // From Recently liked page
            bitmap.recycle();
            bitmap = getBitmapFromURL(webserver.getArrayLikedClothing().get(location_of_clothes).getUrlImage());
            iv.setImageBitmap(bitmap);

            // Set Price
            TextView field = (TextView) findViewById(R.id.Price);
            field.append(webserver.getArrayLikedClothing().get(location_of_clothes).getPrice());

            // Set Brand
            field = (TextView) findViewById(R.id.Brand);
            field.append(webserver.getArrayLikedClothing().get(location_of_clothes).getBrand());

            // Get Type
            field = (TextView) findViewById(R.id.Type);
            field.append(webserver.getArrayLikedClothing().get(location_of_clothes).getType());

            // Get Description
            field = (TextView) findViewById(R.id.Description);
            field.append(webserver.getArrayLikedClothing().get(location_of_clothes).getDescription());

            // Get Name
            field = (TextView) findViewById(R.id.textView7);
            field.setText("");
            field.append(webserver.getArrayLikedClothing().get(location_of_clothes).getName());
        }
    }

    // Go to the swiping page
    public void buttonToSwiping(View v){
        fromSwiping = true;                     // set to true for if going to detailed page
        location_of_clothes = count;

        setContentView(R.layout.swiping_main);
        /*
        arrayClothing.clear();
        arrayClothing = getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/AkshayMata");
        */

        webserver.clearClothing();
        // Set checkmark image
        iv = (ImageButton) findViewById(R.id.right_image);
        iv.setImageBitmap(getBitmapFromURL("http://www.clipartbest.com/cliparts/niE/LL8/niELL86iA.png"));

        // Set x image
        iv = (ImageButton) findViewById(R.id.left_image);
        iv.setImageBitmap(getBitmapFromURL("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/X_mark.svg/896px-X_mark.svg.png"));

        // Set Center Image
        iv = (ImageButton) findViewById(R.id.center_image);
        try{
            iv.setImageBitmap(getBitmapFromURL(webserver.getArrayClothing().get(count).getUrlImage()));
        }catch(Exception e){

        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.clearComposingText();
        try {
            field.append(webserver.getArrayClothing().get(count).getPrice());
        }catch(Exception e){

        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.clearComposingText();
        try {
            field.append(webserver.getArrayClothing().get(count).getName());
        }catch(Exception e){

        }
    }

    // New class to connect to login button
    public void buttonFromLogin(View v){
        EditText usernameButton = (EditText)this.findViewById(R.id.usernameID);
        String username = usernameButton.getText().toString();

        EditText passwordButton = (EditText)this.findViewById(R.id.passwordID);
        String password = passwordButton.getText().toString();

        String URL = String.format("http://ec2-54-210-37-207.compute-1.amazonaws.com/authenticate/%s/%s", username, password);
        String userID = webserver.getUser(URL);
        if(!userID.equals("Invalid User/Password Combination") && !userID.equals("User Not Registered")){
            Log.i(LOG_TAG, userID);
            buttonToSwiping(v);
        }else{
            Log.i(LOG_TAG, "Error, invalid username or password");
            TextView errorText = (TextView)this.findViewById(R.id.errorID);
            userID = String.format("Error: %s", userID);
            errorText.setText(userID);
        }
    }

    // Get bitmap image from URL
    public Bitmap getBitmapFromURL(String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            // Go to log in page
            setContentView(R.layout.activity_main);
            return true;

        }else if(id == R.id.menu_liked){
            // Go to clothes liked page
            setContentView(R.layout.clothes_liked_main);

            // Pull Liked Clothing from AWS
            /*
            arrayLikedClothing.clear();
            arrayLikedClothing = getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/AkshayMata");
            */
            webserver.clearLikedClothing();

            // Put all liked clothing into an array
            String[]myStringArray = new String[webserver.getArrayLikedClothing().size()];
            for(int i = 0; i < webserver.getArrayLikedClothing().size(); i++){
                myStringArray[i] = webserver.getArrayLikedClothing().get(i).getName();
            }

            ArrayAdapter<String> myAdapter=new
                    ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    myStringArray);
            ListView myList=(ListView)
                    findViewById(R.id.listView);
            myList.setAdapter(myAdapter);

            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent,
                                        View v,
                                        int position,
                                        long id) {

                    fromSwiping = false;
                    location_of_clothes = position;
                    buttonToDetailed(v);
                }
            });


            return true;
        }else if(id == R.id.menu_filters){
            // Filters Page
            setContentView(R.layout.filters_main);
            Log.i(LOG_TAG, "Entered Filters");
            //String[] myStringArray={"Shirts", "Pants", "Shoes", "Socks", "Hats", "Men's", "Women's"};
            ArrayAdapter<String> myAdapter=new
                    ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_checked,
                    webserver.getMyStringArrayFilters());
            ListView myList=(ListView)
                    findViewById(R.id.listView2);
            myList.setAdapter(myAdapter);

            // Turn on all filters (set every checkmark to true)
            myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            for(int i = 0; i < 7; i++){
                if(webserver.getArrayFiltersOnOff(i)==true)
                    myList.setItemChecked(i, true);
                else
                    myList.setItemChecked(i, false);
            }

            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView parent,
                                        View v,
                                        int position,
                                        long id) {
                    Log.i(LOG_TAG, "Entered Filters OnClickItem");
                    CheckedTextView textView = (CheckedTextView) v;
                    textView.setChecked(textView.isChecked());
                    Log.i(LOG_TAG, "Entered Filters OnClickItem2" + position);
                    webserver.updateArrayFiltersOnOff(position, !webserver.getArrayFiltersOnOff(position));
                    // Implement filters option here

                    if(/*textView.isChecked()*/webserver.getArrayFiltersOnOff(position)){

                        Log.i(LOG_TAG,"isChecked");
                        if(position == 0){

                            webserver.updateClothingFilters("Shirts");
                        }else if(position == 1){
                            webserver.updateClothingFilters("Pants");
                        }else if(position == 2){
                            webserver.updateClothingFilters("Shoes");
                        }
                        else if(position == 3){
                            webserver.updateClothingFilters("Casual");
                        }
                        else if(position == 4){
                            webserver.updateClothingFilters("Hats");
                        }
                        else if(position == 5){
                            webserver.updateClothingFilters("Men");
                        }
                        else if(position == 6){
                            webserver.updateClothingFilters("Women");
                        }
                    }else{
                        Log.i(LOG_TAG,"isNotChecked");

                        if(position == 0){

                            webserver.removeClothingFilters("Shirts");
                        }else if(position == 1){
                            webserver.removeClothingFilters("Pants");
                        }else if(position == 2){
                            webserver.removeClothingFilters("Shoes");
                        }
                        else if(position == 3){
                            webserver.removeClothingFilters("Casual");
                        }
                        else if(position == 4){
                            webserver.removeClothingFilters("Hats");
                        }
                        else if(position == 5){
                            webserver.removeClothingFilters("Men");
                        }
                        else if(position == 6){
                            webserver.removeClothingFilters("Women");
                        }
                    }
                    // Refresh page
                    //setContentView(R.layout.filters_main);



                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
