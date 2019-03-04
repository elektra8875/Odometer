package com.example.cindylou.starbuzz;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
import android.widget.Toast;

public class activity_top_level extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor favoritesCursor;
    //adding these as private variables so that onDestroy method has access to them

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        // create an OnItemClickListener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {

           public void onItemClick(AdapterView<?> listView,
                                    View v,
                                    int position,
                                    long id) {
            if (position == 0) {
                Intent intent = new Intent(activity_top_level.this, DrinkCategoryActivity.class);
                startActivity(intent);

                    }

                    }
                };
        //Add the listener to the list view
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
        //this code populates options list view and gets list view to respond to clicks


        //populates list_favorites ListView from cursor
        ListView listFavorites = findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            favoritesCursor = db.query("DRINK", new String[]{"_id", "NAME"}, "FAVORITE =1", null, null, null, null);
            //creates cursor that gets values of _id and NAME where FAVORITE = 1

            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(activity_top_level.this, android.R.layout.simple_expandable_list_item_1,
                    favoritesCursor, new String[]{"NAME"}, new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            //display a message if there is a problem with the database

        }

        //Navigate to DrinkActivity if a drink is clicked
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id)

            {

                Intent intent = new Intent(activity_top_level.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKNO, (int) id);
                startActivity(intent);



        }

    });
}

    @Override
    public void onRestart() {
        super.onRestart();
        try {
            StarbuzzDatabaseHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            Cursor newCursor = db.query("DRINK", new String[]{"_id", "NAME"}, "FAVORITE =1", null, null, null, null);
            ListView listFavorites = findViewById(R.id.list_favorites);
            CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
            adapter.changeCursor(newCursor);
            favoritesCursor = newCursor;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        }




    //close the cursor and database in the onDestroy() method
    @Override
    public void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();


    }
}

