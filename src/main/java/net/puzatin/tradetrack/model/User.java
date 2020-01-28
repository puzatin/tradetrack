package net.puzatin.tradetrack.model;

public class User {


    private Tracker tracker;

    private String username;

    private String password;

    public Tracker getTrack() {
        return tracker;
    }

    public void setTrack(Tracker track) {
        this.tracker = track;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
