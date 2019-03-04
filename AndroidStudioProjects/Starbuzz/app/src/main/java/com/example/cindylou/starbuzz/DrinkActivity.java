package com.example.cindylou.starbuzz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.CheckBox;
import android.content.ContentValues;
import android.os.AsyncTask;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKNO = "drinkNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Get the drink from the intent
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);


        //creates the cursor
        try {

            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"}, "_id = ?",
                    new String[]{Integer.toString(drinkNo)}, null, null, null);

            //move to the first record in the Cursor
            if (cursor.moveToFirst()) {
                //get the drink details of the cursor
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);


                //populate the drink name
                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                //populate the drink description
                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);


                //Populate the drink image
                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                //populate favorite checkbox
                CheckBox favorite = findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);


            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //update the database when the checkbox is clicked
    public void onFavoriteClicked(View view) {
        int drinkNo = (Integer) getIntent().getExtras().get("drinkNo");

        new UpdateDrinkTask().execute(drinkNo);

    }

        //Inner class to update the drink
    private class UpdateDrinkTask extends AsyncTask<Integer, Void, Boolean>    {
        private ContentValues drinkValues;

            protected void onPreExecute() {
                CheckBox favorite = findViewById(R.id.favorite);
                drinkValues = new ContentValues();
                drinkValues.put("FAVORITE", favorite.isChecked());

            }

            protected Boolean doInBackground(Integer... drinks) {
                int drinkNo = drinks[0];
                SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);

                try {
                    SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
                    db.update("DRINK", drinkValues, "_id = ?", new String[]{Integer.toString(drinkNo)});
                    db.close();
                    return true;
                } catch (SQLiteException e) {
                    return false;
                }

            }

            protected void onPostExecute(Boolean success) {
                if (!success) {
                    Toast toast = Toast.makeText(DrinkActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        }
    }

