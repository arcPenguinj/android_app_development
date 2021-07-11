package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import java.util.Date;

public class Message {
    public Date date;
    public String from; // sender's client id
    public String to; // receiver's client id

    public Message() {

    }

    public Message(Date date, String from, String to) {
        this.date = date;
        this.from = from;
        this.to = to;
    }
}
