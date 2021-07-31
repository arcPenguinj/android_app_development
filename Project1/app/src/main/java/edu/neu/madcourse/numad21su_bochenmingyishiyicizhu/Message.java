package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import java.util.Date;

public class Message {
    public Date date;
    public User from;
    public User to;
    public String sticker;

    public Message() {
    }

    public Message(Date date, User from, User to, String sticker) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.sticker = sticker;
    }
}
