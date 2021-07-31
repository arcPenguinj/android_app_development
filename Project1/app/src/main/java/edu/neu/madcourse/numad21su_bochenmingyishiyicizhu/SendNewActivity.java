package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SendNewActivity extends AppCompatActivity {
    private ImageView smile;
    private ImageView angry;
    private ImageView like;
    private ImageView cry;
    private Spinner spinner;

    private Sticker selectedSticker;
    private User selectedUser;
    private List<User> otherUsers;
    private DatabaseReference mDatabase;
    private String myname;
    private List<Message> allMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new);
        smile = (ImageView)findViewById(R.id.stickerSmile);
        angry = (ImageView)findViewById(R.id.stickerAngry);
        like = (ImageView)findViewById(R.id.stickerLike);
        cry = (ImageView)findViewById(R.id.stickerCry);
        spinner = (Spinner)findViewById(R.id.spinner1);
        selectedSticker = null;
        allMessages = new ArrayList<>();

        myname = ((StickItApplication)getApplication()).getUsername();

        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        GenericTypeIndicator<List<User>> t = new GenericTypeIndicator<List<User>>() {};
                        List<User> users = snapshot.getValue(t);
                        if (users == null || users.size() == 1) {
                            Toast.makeText(getApplicationContext(),"No other users to send!",Toast.LENGTH_SHORT).show();
                        } else {
                            otherUsers = users.stream().filter(u -> u!=null && u.token!=null && !u.equals(new User(myname))).collect(Collectors.toList());

                            ArrayAdapter<User> arrayAdapter = new ArrayAdapter<User>(SendNewActivity.this, android.R.layout.simple_spinner_item, otherUsers);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(arrayAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedUser = (User)parent.getItemAtPosition(position);
                                    // Toast.makeText(parent.getContext(), "Selected: " + selectedUser, Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onNothingSelected(AdapterView <?> parent) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }
                }
        );

        mDatabase.child("messages").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        GenericTypeIndicator<List<Message>> t = new GenericTypeIndicator<List<Message>>() {};
                        List<Message> messages = snapshot.getValue(t);
                        if (messages == null) {
                            Toast.makeText(getApplicationContext(),"no messages",Toast.LENGTH_SHORT).show();
                        } else {
                            allMessages = messages;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void clickOnSticker(View view) {
        unselectAll();
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable highlight = getResources().getDrawable(R.drawable.highlight);
        switch (view.getId()) {
            case R.id.stickerSmile:
                selectedSticker = Sticker.Smile;
                smile.setBackground(highlight);
                break;
            case R.id.stickerLike:
                selectedSticker = Sticker.Like;
                like.setBackground(highlight);
                break;
            case R.id.stickerAngry:
                selectedSticker = Sticker.Angry;
                angry.setBackground(highlight);
                break;
            case R.id.stickerCry:
                selectedSticker = Sticker.Cry;
                cry.setBackground(highlight);
                break;
            default:
                selectedSticker = null;
        }
    }

    public void sendSticker(View view) {
        if (selectedSticker == null) {
            Toast.makeText(this, "Please select a sticker!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedUser == null) {
            Toast.makeText(this, "Please select a User!", Toast.LENGTH_SHORT).show();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessageToDevice();
            }
        }).start();
    }

    private void sendMessageToDevice() {

        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "New sticker received from " + myname);
            jNotification.put("body", selectedSticker);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
            jdata.put("title", "New sticker from " + myname);
            jdata.put("content", selectedSticker);

            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", selectedUser.token); // CLIENT_REGISTRATION_TOKEN);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String resp = Utils.fcmHttpConnection("key=" + StickItApplication.FIREBASE_SERVER_KEY, jPayload);
        // Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
        if (resp != null) {
            // update db for history
            User to = selectedUser;
            User from = new User(((StickItApplication)getApplication()).getUsername(), ((StickItApplication)getApplication()).getFirebase_client_token());
            Message m = new Message(new Date(), from, to, selectedSticker.toString());
            allMessages.add(m);
            mDatabase.child("messages").setValue(allMessages);
        }
    }

    private void unselectAll() {
        smile.setBackground(null);
        angry.setBackground(null);
        like.setBackground(null);
        cry.setBackground(null);
    }
}