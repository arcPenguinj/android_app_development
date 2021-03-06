package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = MessagingService.class.getSimpleName();

    private static final String CHANNEL_ID = "5520_BMY_CHANNEL_ID";
    private static final String CHANNEL_NAME = "5520_BMY_CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "5520_BMY_CHANNEL_DESCRIPTION";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(String newToken) {
        //super.onNewToken(newToken);

        Log.d(TAG, "Refreshed token: " + newToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(newToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]


        /* NOTICE!!!
         * Message types are inevitable in recent Android version.
         * remoteMessage.getData() Method will return null for 'topic-subscribed messages' from FCMActivity
         *
         * remoteMessage.getFrom() Method will recognize topic-subscribed messages
         * remoteMessage.getNotification() Method will show the raw-data of topic-subscribed messages
         */

        myClassifier(remoteMessage);

        Log.e("msgId", remoteMessage.getMessageId());
        Log.e("senderId", remoteMessage.getSenderId());

    }
    // [END receive_message]

    private void myClassifier(RemoteMessage remoteMessage) {

        String identificator = remoteMessage.getFrom();
        if (identificator != null) {
            if (remoteMessage.getData().size() > 0) {
                    RemoteMessage.Notification notification = remoteMessage.getNotification();
                    showNotification(notification);
                    Utils.postToastMessage(remoteMessage.getData().get("title"), getApplicationContext());
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessageNotification FCM message  received.
     */
    private void showNotification(RemoteMessage.Notification remoteMessageNotification) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        } else {
            builder = new NotificationCompat.Builder(this);
        }

        notification = builder
                .setLargeIcon(Utils.getBitmapFromVectorDrawable(this, Utils.ParseStickerType(remoteMessageNotification.getBody())))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(remoteMessageNotification.getTitle())
                .setContentText(remoteMessageNotification.getBody())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);

    }
}
