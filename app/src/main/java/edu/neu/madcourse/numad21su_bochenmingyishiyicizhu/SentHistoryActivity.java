package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SentHistoryActivity extends AppCompatActivity {
    private TextView totalReceive;
    private TextView totalSent;
    private RecyclerView sentView;
    private RecyclerView receiveView;
    private RecyclerView.LayoutManager rLayoutManger1;
    private RecyclerView.LayoutManager rLayoutManger2;
    private RViewAdapter rviewAdapterReceive;
    private RViewAdapter rviewAdapterSent;

    private List<Message> allHistory;
    private List<Message> sentHistory;
    private List<Message> receiveHistory;

    private String myname;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_history);

        myname = ((StickItApplication)getApplication()).getUsername();
        totalReceive = findViewById(R.id.totalReceiveId);
        totalSent = findViewById(R.id.totalSentId);
        sentView = findViewById(R.id.sentViewId);
        receiveView = findViewById(R.id.receiveViewId);

        allHistory = new ArrayList<>();
        sentHistory = new ArrayList<>();
        receiveHistory = new ArrayList<>();

        // for testing -
        // sentHistory.add(new Message(new Date(), new User("abc"), new User("def"), Sticker.Angry.toString()));
        // sentHistory.add(new Message(new Date(), new User("abc"), new User("def"), Sticker.Smile.toString()));
        // receiveHistory.add(new Message(new Date(), new User("zxc"), new User("abc"), Sticker.Like.toString()));


        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("messages").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        GenericTypeIndicator<List<Message>> t = new GenericTypeIndicator<List<Message>>() {};
                        List<Message> messages = snapshot.getValue(t);
                        if (messages == null) {
                            Toast.makeText(getApplicationContext(),"no messages",Toast.LENGTH_SHORT).show();
                        } else {
                            allHistory = messages;
                            reset();
                            for (Message m: allHistory) {
                                if (m == null || m.from == null || m.to == null) continue;
                                if (m.from.username.equalsIgnoreCase(myname)) {
                                    sentHistory.add(m);
                                } else if (m.to.username.equalsIgnoreCase(myname)) {
                                    receiveHistory.add(m);
                                }
                            }
                            updateView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void reset() {
        this.receiveHistory.clear();
        this.sentHistory.clear();
    }

    private void updateView() {
        totalReceive.setText("Total received: " + receiveHistory.size());
        totalSent.setText("Total sent: " + sentHistory.size());

        rLayoutManger1 = new LinearLayoutManager(this);
        rLayoutManger2 = new LinearLayoutManager(this);

        sentView.setHasFixedSize(true);
        receiveView.setHasFixedSize(true);

        rviewAdapterReceive = new RViewAdapter(receiveHistory);
        rviewAdapterSent= new RViewAdapter(sentHistory);

        receiveView.setAdapter(rviewAdapterReceive);
        sentView.setAdapter(rviewAdapterSent);
        receiveView.setLayoutManager(rLayoutManger1);
        sentView.setLayoutManager(rLayoutManger2);
    }
}