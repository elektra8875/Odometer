package com.example.cindylou.beeradvisor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;

public class FindBeerActivity extends Activity {
    private BeerExpert expert = new BeerExpert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_beer);
    }

    //Call when the button get clicked
    public void onClickFindBeer(View view) {

    //Get a reference to TextView
    TextView brands = (TextView) findViewById(R.id.brands);
    //Get a reference to the Spinner
    Spinner color = (Spinner) findViewById(R.id.color);
    String beerType = String.valueOf(color.getSelectedItem());


//Get recommendations from the BeerExpert Class
    List<String> brandsList = expert.getBrands(beerType);
    StringBuilder brandsFormatted = new StringBuilder();
    for (String brand: brandsList) {
        brandsFormatted.append(brand).append('\n');
    }
//Display the Beers
    brands.setText(brandsFormatted);


    }
}

