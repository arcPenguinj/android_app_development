package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RViewAdapter extends RecyclerView.Adapter<RViewHolder> {

    private final List<Message> messages;

    //Constructor
    public RViewAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @NotNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RViewHolder holder, int position) {
        Message m = messages.get(position);
        holder.from.setText("From: " + m.from.username);
        holder.to.setText("To: " + m.to.username);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d hh:mm:ss");
        holder.date.setText(dateFormat.format(m.date));
        holder.sticker.setImageResource(Utils.ParseStickerType(m.sticker));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
