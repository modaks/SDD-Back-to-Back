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
    private ArrayList<Clothing> arrayClothingOutput;

    // Create array of clothing objects
    private ArrayList<Clothing> arrayClothing;

    // Create array of liked objects
    private ArrayList<Clothing> arrayLikedClothing;

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

        arrayClothingOutput = new ArrayList<Clothing>();

        // Create clothing objects and clothing arrays
        arrayClothing = new ArrayList<Clothing>();
        arrayClothing = getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/AkshayMata");

        // Set current bitmap image
        bitmap = getBitmapFromURL("http://loololi.com/wp-content/uploads/2013/09/Scarves-175-1.jpg");

        // Mock Objects
        /*arrayClothing.add(new Clothing("http://ecx.images-amazon.com/images/I/617gCojOJBL._UL1500_.jpg",
                "19.99", "shirt", "N/A", "A Wu Tang Shirt"));
        arrayClothing.add(new Clothing("https://s3.amazonaws.com/rapgenius/filepicker%2F5jTDmubSTnCREE8BIe5w_nike_shoes.jpg",
                "60.00", "shoes", "Nike", "Nike Shoes"));
        arrayClothing.add(new Clothing("https://cdnd.lystit.com/photos/2013/11/08/blk-dnm-black-black-skinny-jeans-8-product-5-14777777-664835857.jpeg",
                "55.60", "pants", "N/A", "A black pair of skinny jeans."));
        arrayClothing.add(new Clothing("http://www.polyvore.com/cgi/img-thing?.out=jpg&size=l&tid=69616610",
                "44.99", "pants", "N/A", "A blue pair of skinny jeans."));
        arrayClothing.add(new Clothing("http://www.equip2golf.com/wordpress/wp-content/themes/bigfeature/library/timthumb/timthumb.php?src=/wordpress/wp-content/uploads/2013/07/nike-flat-bill-tour-hat.jpg&w=608&zc=1&a=c",
                "25.00", "hat", "Nike", "A Nike hat"));*/

        //Initialize liked clothing array
        arrayLikedClothing = new ArrayList<Clothing>();
    }

    // Get and parse clothing objects from AWS server
    public ArrayList<Clothing> getClothing(final String urlAWS){

        arrayClothingOutput.clear();

        // Create new thread since we are accessing Network
        new Thread(){
            public void run() {

                //string will contain allJson text
                String link="";

                try{
                    //retreiving json info regarding all clothes
                    link = getHTML(urlAWS);
                }catch(Exception e){
                    Log.i(LOG_TAG,"Failed to retrieve Jsoin string from onCreate");
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
                        arrayClothingOutput.add(new Clothing(tokens_content[1], tokens_content[2], tokens_content[3], tokens_content[4], tokens_content[5], tokens_content[6], tokens_content[7], tokens_content[8], tokens_content[9], tokens_content[10], tokens_content[11]));
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

        return arrayClothingOutput;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // When clicking or swiping through clothes with x mark
    public void buttonOnClick(View v){
        iv = (ImageButton) findViewById((R.id.center_image));

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

        // Get bitmap from URL
        try{
            iv.setImageBitmap(getBitmapFromURL(arrayClothing.get(count).getUrlImage()));
        }catch(Exception e){
            Log.i(LOG_TAG,Integer.toString(count));
            Log.i(LOG_TAG, arrayClothing.get(count).getUrlImage());
        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.setText("Price - $");
        try{
            field.append(arrayClothing.get(count).getPrice());
        }catch(Exception e){

        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.setText("");
        try{
            field.append(arrayClothing.get(count).getName());
        }catch(Exception e){

        }
    }

    // When the user clicks the check mark, refresh image and store clothing into liked clothing array
    public void buttonOnClickCheck(View v){
        iv = (ImageButton) findViewById((R.id.center_image));

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

        // Get bitmap from URL
        bitmap.recycle();
        bitmap = getBitmapFromURL(arrayClothing.get(count).getUrlImage());
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.setText("Price - $");
        try{
            field.append(arrayClothing.get(count).getPrice());
        }catch(Exception e){

        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.setText("");
        try{
            field.append(arrayClothing.get(count).getName());
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
            bitmap = getBitmapFromURL(arrayClothing.get(location_of_clothes).getUrlImage());
            iv.setImageBitmap(bitmap);

            // Set Price
            TextView field = (TextView) findViewById(R.id.Price);
            field.append(arrayClothing.get(location_of_clothes).getPrice());

            // Set Brand
            field = (TextView) findViewById(R.id.Brand);
            field.append(arrayClothing.get(location_of_clothes).getBrand());

            // Get Type
            field = (TextView) findViewById(R.id.Type);
            field.append(arrayClothing.get(location_of_clothes).getType());

            // Get Description
            field = (TextView) findViewById(R.id.Description);
            field.append(arrayClothing.get(location_of_clothes).getDescription());

            // Get Name
            field = (TextView) findViewById(R.id.textView7);
            field.setText("");
            field.append(arrayClothing.get(location_of_clothes).getName());

        }else{
        // From Recently liked page
            bitmap.recycle();
            bitmap = getBitmapFromURL(arrayLikedClothing.get(location_of_clothes).getUrlImage());
            iv.setImageBitmap(bitmap);

            // Set Price
            TextView field = (TextView) findViewById(R.id.Price);
            field.append(arrayLikedClothing.get(location_of_clothes).getPrice());

            // Set Brand
            field = (TextView) findViewById(R.id.Brand);
            field.append(arrayLikedClothing.get(location_of_clothes).getBrand());

            // Get Type
            field = (TextView) findViewById(R.id.Type);
            field.append(arrayLikedClothing.get(location_of_clothes).getType());

            // Get Description
            field = (TextView) findViewById(R.id.Description);
            field.append(arrayLikedClothing.get(location_of_clothes).getDescription());

            // Get Name
            field = (TextView) findViewById(R.id.textView7);
            field.setText("");
            field.append(arrayClothing.get(location_of_clothes).getName());
        }
    }

    // Go to the swiping page
    public void buttonToSwiping(View v){
        fromSwiping = true;                     // set to true for if going to detailed page
        location_of_clothes = count;

        setContentView(R.layout.swiping_main);

        arrayClothing.clear();
        arrayClothing = getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getProducts/AkshayMata");

        // Set checkmark image
        iv = (ImageButton) findViewById(R.id.right_image);
        iv.setImageBitmap(getBitmapFromURL("http://www.clipartbest.com/cliparts/niE/LL8/niELL86iA.png"));

        // Set x image
        iv = (ImageButton) findViewById(R.id.left_image);
        iv.setImageBitmap(getBitmapFromURL("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/X_mark.svg/896px-X_mark.svg.png"));

        // Set Center Image
        iv = (ImageButton) findViewById(R.id.center_image);
        try{
            iv.setImageBitmap(getBitmapFromURL(arrayClothing.get(count).getUrlImage()));
        }catch(Exception e){

        }

        // Set Price
        TextView field = (TextView) findViewById(R.id.Price_main);
        field.clearComposingText();
        try {
            field.append(arrayClothing.get(count).getPrice());
        }catch(Exception e){

        }

        // Set Description
        field = (TextView) findViewById(R.id.Description_main);
        field.clearComposingText();
        try {
            field.append(arrayClothing.get(count).getName());
        }catch(Exception e){

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
            arrayLikedClothing.clear();
            arrayLikedClothing = getClothing("http://ec2-54-210-37-207.compute-1.amazonaws.com/getLikedProducts/AkshayMata");

            // Put all liked clothing into an array
            String[] myStringArray = new String[arrayLikedClothing.size()];
            for(int i = 0; i < arrayLikedClothing.size(); i++){
                myStringArray[i] = arrayLikedClothing.get(i).getName();
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

            String[] myStringArray={"Shirts", "Pants", "Shoes", "Socks", "Hats", "Undergarments", "Miscellaneous"};
            ArrayAdapter<String> myAdapter=new
                    ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_checked,
                    myStringArray);
            ListView myList=(ListView)
                    findViewById(R.id.listView2);
            myList.setAdapter(myAdapter);

            // Turn on all filters (set every checkmark to true)
            myList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            for(int i = 0; i < 7; i++){
                myList.setItemChecked(i, true);
            }

            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent,
                                        View v,
                                        int position,
                                        long id) {

                    CheckedTextView textView = (CheckedTextView) v;
                    textView.setChecked(!textView.isChecked());

                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
