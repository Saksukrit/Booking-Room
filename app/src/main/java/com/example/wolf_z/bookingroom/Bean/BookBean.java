package com.example.wolf_z.bookingroom.Bean;

import org.parceler.Parcel;

@Parcel
public class BookBean {
    private int bookingid;
    private String subject;
    private String meeting_type;
    private String date;
    private String starttime;
    private String endtime;
    private String detail;
    private int roomid;
    private int projid;

    public BookBean() {
    }

    public BookBean(int bookingid, String subject, String meeting_type, String date, String starttime, String endtime, String detail, int roomid, int projid) {
        this.bookingid = bookingid;
        this.subject = subject;
        this.meeting_type = meeting_type;
        this.date = date;
        this.starttime = starttime;
        this.endtime = endtime;
        this.detail = detail;
        this.roomid = roomid;
        this.projid = projid;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMeeting_type() {
        return meeting_type;
    }

    public void setMeeting_type(String meeting_type) {
        this.meeting_type = meeting_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getProjid() {
        return projid;
    }

    public void setProjid(int projid) {
        this.projid = projid;
    }

}
