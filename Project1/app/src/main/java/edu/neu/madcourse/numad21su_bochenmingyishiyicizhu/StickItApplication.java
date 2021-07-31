package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import android.app.Application;

public class StickItApplication extends Application {

    public static final String FIREBASE_SERVER_KEY = "AAAAmhVYbWA:APA91bEYlh1r0r4KlP6-dDJ2tJK1mwgHnzmnLM87wPfpAgWCwRiKK1At2Em6UhtQrrVpe8ck0pyV7G1CTHgvH3Ag6YmYfPY4_RFGMxOmo3vgJi1eYlK5HbMwSy41Xc1kwDHQedMn98d9";

    private String firebase_client_token = "";
    private String username = "";

    public String getFirebase_client_token() {
        return firebase_client_token;
    }

    public void setFirebase_client_token(String key) {
        this.firebase_client_token = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
