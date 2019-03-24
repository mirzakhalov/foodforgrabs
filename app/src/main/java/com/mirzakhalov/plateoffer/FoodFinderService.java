package com.mirzakhalov.plateoffer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.android.gms.location.FusedLocationProviderClient;

public class FoodFinderService extends FirebaseMessagingService {
    private static final String TAG = "FOOD_FOR_GRABS_FIREBASE";
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            final Location place = new Location("");
            double latitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
            double longitude = Double.parseDouble(remoteMessage.getData().get("latitude"));
            place.setLatitude(latitude);
            place.setLongitude(longitude);
            sendNotification("THE PLACE IS CLOSE");
            try{
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    float distanceInMeters = place.distanceTo(location);
                                    if(distanceInMeters < 1609.34){
                                        sendNotification("THE PLACE IS CLOSE");
                                    }
                                }
                            }
                        });
            }
            catch(SecurityException e){

            }


            Log.d("Lat and Lon", latitude + " " + longitude);

        }

    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer(final String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        String userUID = sharedPreferences.getString("uid", "");
        if(!userUID.equals("")){
            FirebaseDatabase.getInstance().getReference().child("Users/" + userUID + "/profile/token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Firebase Token", "Token refreshed to " + token);
                }
            });
        }

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, LandingPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Hello")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
