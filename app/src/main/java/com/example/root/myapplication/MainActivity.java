package com.example.root.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.root.myapplication.R.drawable.pants2;
import static com.example.root.myapplication.R.drawable.pants;
import static com.example.root.myapplication.R.drawable.x;
import static com.example.root.myapplication.R.layout.swiping_main;


public class MainActivity extends AppCompatActivity {

    ImageButton b1;
    private int count = 0;
    private ImageButton iv;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allows getting URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //View rootView = getMenuInflater().inflate(swiping_main);
        //b1 = (ImageButton) findViewById((R.id.center_image));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonOnClick(View v){
        iv = (ImageButton) findViewById((R.id.center_image));

        if(count % 2 == 0){
            bitmap = getBitmapFromURL("http://cdn.fluidretail.net/customers/c1500/P51050/P51050_pdp/zoom_variation_251_view_A_2192x2200.jpg");
            iv.setImageBitmap(bitmap);

        }else{
            bitmap = getBitmapFromURL("http://content.backcountry.com/images/items/medium/MAR/MAR2242/DESKH.jpg");
            iv.setImageBitmap(bitmap);
        }
        count += 1;
        //b1.setBackground(getDrawable(x));
        //setContentView(R.layout.detail_main);
        //b1.setBackground(R.drawable.x);
    }

    public void buttonToDetailed(View v){

        setContentView(R.layout.detail_main);

        iv = (ImageButton) findViewById(R.id.imageButton4);
        bitmap = getBitmapFromURL("http://loololi.com/wp-content/uploads/2013/09/Scarves-175-1.jpg");
        iv.setImageBitmap(bitmap);

    }

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

/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.swiping_main, container, false);

        //Buscar el textview en la vista
        ImageButton button = (ImageButton) rootView.findViewById(R.id.center_image);
        button.setBackground(getDrawable(pants));

        return rootView;
    }
    */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_main) {
            setContentView(R.layout.activity_main);
            return true;
        }else if(id == R.id.menu_clothes){
            setContentView(swiping_main);

            // Set center image
            iv = (ImageButton) findViewById(R.id.center_image);
            bitmap = getBitmapFromURL("http://loololi.com/wp-content/uploads/2013/09/Scarves-175-1.jpg");
            iv.setImageBitmap(bitmap);

            // Set check image
            iv = (ImageButton) findViewById(R.id.right_image);
            bitmap = getBitmapFromURL("http://www.clipartbest.com/cliparts/niE/LL8/niELL86iA.png");
            iv.setImageBitmap(bitmap);

            // Set x image
            iv = (ImageButton) findViewById(R.id.left_image);
            bitmap = getBitmapFromURL("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/X_mark.svg/896px-X_mark.svg.png");
            iv.setImageBitmap(bitmap);

            return true;
        }else if(id == R.id.menu_liked){
            setContentView(R.layout.clothes_liked_main);

            String[] myStringArray={"Most recently liked clothes","Pants from Walmart","Shirt"};
            ArrayAdapter<String> myAdapter=new
                    ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    myStringArray);
            ListView myList=(ListView)
                    findViewById(R.id.listView);
            myList.setAdapter(myAdapter);

            return true;
        }else if(id == R.id.menu_filters){
            setContentView(R.layout.filters_main);
            return true;
        }else if(id == R.id.menu_search){
            setContentView(R.layout.search_main);
            return true;
        }else if(id == R.id.detail_main){
            setContentView(R.layout.detail_main);

            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
