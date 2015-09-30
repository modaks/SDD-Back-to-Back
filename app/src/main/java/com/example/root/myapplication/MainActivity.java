package com.example.root.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
            setContentView(R.layout.swiping_main);
            return true;
        }else if(id == R.id.menu_liked){
            setContentView(R.layout.clothes_liked_main);
            return true;
        }else if(id == R.id.menu_filters){
            setContentView(R.layout.filters_main);
            return true;
        }else if(id == R.id.menu_search){
            setContentView(R.layout.search_main);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
