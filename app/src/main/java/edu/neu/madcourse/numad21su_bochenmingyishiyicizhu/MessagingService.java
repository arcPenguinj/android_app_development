package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = MessagingService.class.getSimpleName();

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
            if (identificator.contains("topic")) {
                if (remoteMessage.getNotification() != null) {
                    RemoteMessage.Notification notification = remoteMessage.getNotification();
                    // showNotification(remoteMessage.getNotification());
                    Utils.postToastMessage(notification.getTitle(), getApplicationContext());
                }
            } else {
                if (remoteMessage.getData().size() > 0) {
                    RemoteMessage.Notification notification = remoteMessage.getNotification();
                    // showNotification(notification);
                    Utils.postToastMessage(remoteMessage.getData().get("title"), getApplicationContext());
                }
            }
        }
    }
}
