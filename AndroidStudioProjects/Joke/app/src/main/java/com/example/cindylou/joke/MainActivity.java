package com.example.cindylou.joke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {
        //this will run when the button gets clicked
        Intent intent = new Intent(this, DelayedMessageService.class); //creates intent
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE, getResources().getString(R.string.button_response)); //add text to the intent
        startService(intent);
        Log.v("tag:marty", "msg: timing!!");
        //start service

    }




}
