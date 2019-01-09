package com.example.mrg20.menuing_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String DEFAULT_CHANNEL_ID = "cat.eps.jgervas.messagingkk.DEFAULT";
    public static final String DEFAULT_CHANNEL_NAME = "DEFAULT CHANNEL";
    public static final int NOTIFICATION_ID = 1;
    private String token;

    @Override
    public void onNewToken(String s) {
        System.out.println("NEW_TOKEN:" + s);
        this.token = s;
        sendRegistrationTokenToServer(s);
        super.onNewToken(s);
    }

    private void sendRegistrationTokenToServer(String s) {
        UrlConnectorSubmitToken ucst = new UrlConnectorSubmitToken();
        ucst.execute();
    }

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            System.out.println("Message data Body: " + remoteMessage.getData().get("body"));
            sendNotification(remoteMessage.getData().get("body"));

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }
    // [END receive_message]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Menuing Activity")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // create android channel
            NotificationChannel androidChannel = new
                    NotificationChannel(DEFAULT_CHANNEL_ID,
                    DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(androidChannel);
        }

        notificationManager.notify(NOTIFICATION_ID , notificationBuilder.build());
    }

    private class UrlConnectorSubmitToken extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                //CREATE CLIENT IN DB
                URL url = new URL("http://47fe2b76.ngrok.io/api/resources/tokens/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonString = new JSONObject()
                        .put("token", token)
                        .toString();

                OutputStream os = conn.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                os.close();
                System.out.println("TOKEN ENVIAT");
                System.out.println("CONNECTION CODE: " + conn.getResponseCode());
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("Token could not be sended");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}