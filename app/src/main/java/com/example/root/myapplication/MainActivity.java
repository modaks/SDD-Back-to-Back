package com.example.root.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private int count;      // Store the location/index of the clothes array for the swiping page
    private ImageButton iv; // Initialize ImageButton for later use
    private Bitmap bitmap;  // Initialize bitmap for later use in making images
    private String userID;
    private String username;
    // Used for Log.I debugging statments printed to logTag
    private static final String LOG_TAG = "debugger";

    // When app goes to DetailedPage, checks if it is coming from RecentlyLikedPage or SwipingPage
    private boolean fromSwiping = true;

    // WebserviceAdaptor variable handles all webserver calls and acceses models class
    private WebServiceAdaptor webserver;

    // Store the index of the clothesLiked array for the detailed page
    private int location_of_clothes = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Go to activity main page
        count = 0;  // Initialize count to 0

        // Allows getting URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Set current bitmap image
        bitmap = getBitmapFromURL("http://loololi.com/wp-content/uploads/2013/09/Scarves-175-1.jpg");

        // Initialize the webserver
        String formatted_URL = String.format
                ("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/%s/%s", username, userID);
        webserver = new WebServiceAdaptor(formatted_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // Event Handler for X button
    public void buttonDislike(View v){
        iv = (ImageButton) findViewById((R.id.center_image));   // get ID of center image

        // Update indexes
        location_of_clothes = webserver.dislike(count);
        count = location_of_clothes;
        // Get bitmap from URL
        try{
            iv.setImageBitmap(getBitmapFromURL(webserver.getArrayClothing().get(count).getUrlImage()));
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get bitmap image");
        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.setText("Price - $");
        try{
            field.append(webserver.getArrayClothing().get(count).getPrice());
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get price");
        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.setText("");
        try{
            field.append(webserver.getArrayClothing().get(count).getName());
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get description");
        }
    }

    // Event handler for check button
    public void buttonLike(View v){
        iv = (ImageButton) findViewById((R.id.center_image));   // get ID of center image

        // Update indexes
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
            Log.i(LOG_TAG, "Error: Cannot get price");
        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.setText("");
        try{
            field.append(webserver.getArrayClothing().get(count).getName());
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get description");
        }
    }

    // Go to the detailed page
    public void buttonToDetailed(View v){
        setContentView(R.layout.detail_main);
        iv = (ImageButton) findViewById(R.id.imageButton4);

        // If from swiping page
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


            // Get Hyperlink
            String temp_url;
            temp_url = new StringBuilder().append("<a href=\"").append(webserver.getArrayClothing().get(location_of_clothes).getUrlShop()).append("\">").append(webserver.getArrayClothing().get(location_of_clothes).getName()).append("</a>").toString();

            // Get Name
            field = (TextView) findViewById(R.id.title);
            field.setText(Html.fromHtml(temp_url));
            field.setMovementMethod(LinkMovementMethod.getInstance());

        }else{
        // If from recently liked page
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

            // Get Hyperlink
            String temp_url;
            temp_url = new StringBuilder().append("<a href=\"").append(webserver.getArrayLikedClothing().get(location_of_clothes).getUrlShop()).append("\">").append(webserver.getArrayLikedClothing().get(location_of_clothes).getName()).append("</a>").toString();

            // Get Name
            field = (TextView) findViewById(R.id.title);
            field.setText(Html.fromHtml(temp_url));
            field.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    // Go to the swiping page
    public void buttonToSwiping(View v){
        fromSwiping = true;                     // Set to true for if going to detailed page
        location_of_clothes = count;            // Update index
        setContentView(R.layout.swiping_main);  // Go to swiping main

        webserver.clearClothing();              // Refresh clothing array
        // Set checkmark image
        iv = (ImageButton) findViewById(R.id.right_image);
        iv.setImageBitmap(getBitmapFromURL("http://www.clipartbest.com/cliparts/niE/LL8/niELL86iA.png"));

        // Set x image
        iv = (ImageButton) findViewById(R.id.left_image);
        iv.setImageBitmap(getBitmapFromURL
                ("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/X_mark.svg/896px-X_mark.svg.png"));

        // Set center Image
        iv = (ImageButton) findViewById(R.id.center_image);
        try{
            iv.setImageBitmap(getBitmapFromURL(webserver.getArrayClothing().get(count).getUrlImage()));
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get bitmap for center image");
        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.clearComposingText();
        try {
            field.append(webserver.getArrayClothing().get(count).getPrice());
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get price");
        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.clearComposingText();
        try {
            field.append(webserver.getArrayClothing().get(count).getName());
        }catch(Exception e){
            Log.i(LOG_TAG, "Error: Cannot get description");
        }
    }

    // New class to connect to login button
    public void buttonFromLogin(View v){
        // Get username string
        EditText usernameButton = (EditText)this.findViewById(R.id.usernameID);
        username = usernameButton.getText().toString();

        // Get password string
        EditText passwordButton = (EditText)this.findViewById(R.id.passwordID);
        String password = passwordButton.getText().toString();
        // Get URL string and call getUser method
        String URL = String.format
                ("http://ec2-54-210-37-207.compute-1.amazonaws.com/authenticate/%s/%s", username, password);
        userID = webserver.getUser(URL);
        webserver.updateUsername(username);
        webserver.updateUserID(userID);
        if(!userID.equals("Invalid User/Password Combination") && !userID.equals("User Not Registered")){
            Log.i(LOG_TAG, userID);

            String formatted_URL = String.format
                    ("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/%s/%s", username, userID);
            webserver.updateWebServerAdaptor(formatted_URL);

            buttonToSwiping(v);
        }else{
            Log.i(LOG_TAG, "Error, invalid username or password");
            TextView errorText = (TextView)this.findViewById(R.id.errorID);
            userID = String.format("Error: %s", userID);
            errorText.setText(userID);
        }
    }

    // Get Bitmap image from URL
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
            setContentView(R.layout.clothes_liked_main);    // Go to clothes liked page
            webserver.clearLikedClothing();                 // Refresh clothes array

            // Put all liked clothing into an array
            String[]myStringArray = new String[webserver.getArrayLikedClothing().size()];
            for(int i = 0; i < webserver.getArrayLikedClothing().size(); i++){
                myStringArray[i] = webserver.getArrayLikedClothing().get(i).getName();
            }

            // Create new adapter
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
            // Create new adapter
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
            // Set OnClickListener
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
                    // Filters options
                    if(webserver.getArrayFiltersOnOff(position)){
                        Log.i(LOG_TAG,"isChecked");
                        if(position == 0){
                            webserver.updateClothingFilters("Shirts");
                        }else if(position == 1){
                            webserver.updateClothingFilters("Pants");
                        }else if(position == 2){
                            webserver.updateClothingFilters("Shoes");
                        }else if(position == 3){
                            webserver.updateClothingFilters("Casual");
                        }else if(position == 4){
                            webserver.updateClothingFilters("Hats");
                        }else if(position == 5){
                            webserver.updateClothingFilters("Men");
                        }else if(position == 6){
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
                        }else if(position == 3){
                            webserver.removeClothingFilters("Casual");
                        }else if(position == 4){
                            webserver.removeClothingFilters("Hats");
                        }else if(position == 5){
                            webserver.removeClothingFilters("Men");
                        }else if(position == 6){
                            webserver.removeClothingFilters("Women");
                        }
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
