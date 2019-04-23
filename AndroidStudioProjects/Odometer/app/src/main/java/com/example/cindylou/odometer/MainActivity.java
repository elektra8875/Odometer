package com.example.cindylou.odometer;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    //main activity connects to ServiceConnection (used to create connection with service) via an intent
    //ServiceConnection connects to OdometerService via intent

    private OdometerService odometer;
    private boolean bound = false;
    private final int PERMISSION_REQUEST_CODE = 698;
    private final int NOTIFICATION_ID = 423;
    //public int stop_button;



    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            OdometerService.OdometerBinder odometerBinder =
                    (OdometerService.OdometerBinder) binder;
            odometer = odometerBinder.getOdometer(); //odometer service
            bound = true;
        // bind to, receiving an object through which it can communicate with the service.
        }//send requests, receive responses and perform interprocess communication

        //the Binder connects the OdometerService back to the ServiceConnection and again back to MainActivity
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
        //this is called when connection with the service has been disconnected; componentName = classname
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDistance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, OdometerService.class);
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                } else {
                    //Create a notification builder
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.ic_menu_compass)
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.permission_denied))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setVibrate(new long[]{1000, 1000})
                            .setAutoCancel(true);
                    //Create an action
                    Intent actionIntent = new Intent(this, MainActivity.class);
                    PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0,
                            actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(actionPendingIntent);
                    //Issue the notification
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this,
                OdometerService.PERMISSION_STRING)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{OdometerService.PERMISSION_STRING},
                    PERMISSION_REQUEST_CODE);
        } else {
            Intent service = new Intent(this, OdometerService.class);
            bindService(service, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(); //unbinds service when the onStop method is used
    }

    private void unbindService() {
        if (bound) {
            unbindService(connection); //if stopped unbind service
            bound = false;
        }
    }

    private void displayDistance() {
        final TextView distanceView = (TextView) findViewById(R.id.distance);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0.0; //double means a double precision number
                if (bound && odometer != null) {
                    distance = odometer.getDistance();
                }
                String distanceStr = String.format(Locale.getDefault(),
                        "%1$,.2f miles", distance);
                distanceView.setText(distanceStr);
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void stopService(View view) {
        unbindService();//method is located under the OnStop method
        Toast.makeText(this, "service stop", Toast.LENGTH_LONG).show();
    }

    public void onClickReset(View view) {
       //TextView stop_button = findViewById(R.id.stop_button);
        TextView distanceView = findViewById(R.id.distance);
        distanceView.setText("0");


       //this is where I added the code to reset the odometer app.
    }  //I was thinking about calling the id of distance since it is under the class TextView that shows the mileage
       //however I am having a hard time assigning the variable distance to zero when you press the reset button

    // public void stopService(View view) {
  //  stopService(new Intent(getBaseContext(), OdometerService.class));
  //      Toast.makeText(this, "service stop", Toast.LENGTH_LONG).show();
 //   }
}



    //sets number of seconds on timer
  //  private void runTimer() {
     //   final TextView timeView = findViewById(R.id.time_view);
    //    final Handler handler = new Handler(); //creating handler
    //    handler.post(new Runnable () {
         //   @Override
     //       public void run() {
           //     int hours = seconds/3600;
               // int minutes = (seconds%3600/60);
           //     int secs = seconds%60;
           //     String time = String.format("%d:%02d:%02d", hours, minutes, secs); //formats seconds into hrs, mins, seconds
          //      timeView.setText(time); //set text view text
             //   if (running) {
             //   seconds++; //if running is true, increment seconds variable

           //     }
               // handler.postDelayed(this, 1000); //post code again with delay of 1 sec
        //    }
    //    });

  ////  }


//}


