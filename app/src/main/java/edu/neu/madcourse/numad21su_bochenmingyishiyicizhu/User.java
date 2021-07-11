package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import java.util.Objects;

public class User {

    public String username;
    public String token;
    public String sentNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.sentNumber = "0";
    }

    public User(String username) {
        this.username = username;
        this.token = "";
        this.sentNumber = "0";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // we don't allow different users share same token or username
        // so compare one of them is enough
        return  username.equalsIgnoreCase(user.username) || token.equals(user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    @Override
    public String toString() {
        return username;
    }
}
