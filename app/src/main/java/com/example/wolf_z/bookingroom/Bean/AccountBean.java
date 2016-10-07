package com.example.wolf_z.bookingroom.Bean;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by Wolf-Z on 12/9/2559.
 */

@Parcel
public class AccountBean implements Parcelable {
    private int id;
    private String displayname;
    private String username;
    private String password;
    private String department;

    public AccountBean() {
    }

    public AccountBean(int id, String displayname, String username, String password, String department) {
        this.id = id;
        this.displayname = displayname;
        this.username = username;
        this.password = password;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.displayname);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.department);
    }

    protected AccountBean(android.os.Parcel in) {
        this.id = in.readInt();
        this.displayname = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.department = in.readString();
    }

    public static final Parcelable.Creator<AccountBean> CREATOR = new Parcelable.Creator<AccountBean>() {
        @Override
        public AccountBean createFromParcel(android.os.Parcel source) {
            return new AccountBean(source);
        }

        @Override
        public AccountBean[] newArray(int size) {
            return new AccountBean[size];
        }
    };
}
