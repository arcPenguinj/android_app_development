package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.numad21su_bochenmingyishiyicizhu.R;


public class RealtimeDatabaseActivity extends AppCompatActivity {

    private static final String TAG = RealtimeDatabaseActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private TextView user1;
    private TextView sent_user1;
    private TextView user2;
    private TextView sent_user2;
    private Button sentButton;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_history);

        user1 = (TextView) findViewById(R.id.user1);
        user2 = (TextView) findViewById(R.id.user2);
        sent_user1 = (TextView) findViewById(R.id.sent1);
        sent_user2 = (TextView) findViewById(R.id.sent2);
        sentButton = (Button) findViewById(R.id.button4);

        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Update the score in realtime
        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        showScore(dataSnapshot);
                        Log.e(TAG, "onChildAdded: dataSnapshot = " + dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        showScore(dataSnapshot);
                        Log.v(TAG, "onChildChanged: " + dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled:" + databaseError);
                        Toast.makeText(getApplicationContext()
                                , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // Add 5 points Button
    public void addFivePoints(View view) {
        if (((StickItApplication) getApplication()).getUsername().equals("user1")) {
        RealtimeDatabaseActivity.this.onAddScore(mDatabase, "user1");
        } else {
            RealtimeDatabaseActivity.this.onAddScore(mDatabase, "user2");
        }
    }


    // Reset USERS Button
    public void resetUsers(View view) {

        User user;
        user = new User("user1", "0");
        Task t1 = mDatabase.child("users").child(user.username).setValue(user);

        user = new User("user2", "0");
        Task t2 = mDatabase.child("users").child(user.username).setValue(user);

        if(!t1.isSuccessful() && !t2.isSuccessful()){
            Toast.makeText(getApplicationContext(),"Unable to reset players!",Toast.LENGTH_SHORT).show();
        }
        else if(!t1.isSuccessful() && t2.isSuccessful()){
            Toast.makeText(getApplicationContext(),"Unable to reset player1!",Toast.LENGTH_SHORT).show();
        }
        else if(t1.isSuccessful() && t2.isSuccessful()){
            Toast.makeText(getApplicationContext(),"Unable to reset player2!",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Called on score_user1 add
     *
     * @param postRef
     * @param user
     */
    private void onAddScore(DatabaseReference postRef, String user) {
        postRef
                .child("users")
                .child(user)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        User user = mutableData.getValue(User.class);
                        if (user == null) {
                            return Transaction.success(mutableData);
                        }

                        user.sentNumber = String.valueOf(Integer.parseInt(user.sentNumber) + 1);

                        mutableData.setValue(user);
                        int i =0 ;

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                        Toast.makeText(getApplicationContext()
                                , "DBError: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showScore(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);

        if (dataSnapshot.getKey().equalsIgnoreCase("user1")) {
            sent_user1.setText(String.valueOf(user.sentNumber));
            user1.setText(user.username);
        } else {
            sent_user2.setText(String.valueOf(user.sentNumber));
            user2.setText(user.username);
        }
    }


}
