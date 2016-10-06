package com.example.wolf_z.bookingroom.Bean;

import org.parceler.Parcel;

@Parcel
public class ParticipantArray {
    private int bookingid;
    private String[] username;

    public ParticipantArray() {
    }

    public ParticipantArray(int bookingid, String[] username) {
        this.bookingid = bookingid;
        this.username = username;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public String[] getUsername() {
        return username;
    }

    public void setUsername(String[] username) {
        this.username = username;
    }

}
