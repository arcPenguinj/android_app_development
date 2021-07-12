package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RViewHolder extends RecyclerView.ViewHolder {

    public TextView from;
    public TextView to;
    public TextView date;
    public ImageView sticker;

    public RViewHolder(View itemView) {
        super(itemView);
        from = itemView.findViewById(R.id.item_from);
        to = itemView.findViewById(R.id.item_to);
        date = itemView.findViewById(R.id.item_date);
        sticker = itemView.findViewById(R.id.item_sticker);
    }
}
