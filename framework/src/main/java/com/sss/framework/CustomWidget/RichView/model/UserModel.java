package com.sss.framework.CustomWidget.RichView.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 用户model
 * Created by shuyu on 2016/11/10.
 */

public class UserModel implements Serializable, Parcelable {

    public UserModel() {

    }

    public UserModel(String user_name, String user_id) {
        this.uid = user_id;
        this.username = user_name;
    }

    /**
     * 名字不能带@和空格
     */
    private String uid;

    private String username;

    public String getUser_name() {
        return username;
    }

    public void setUser_name(String user_name) {
        this.
                username = user_name;
    }

    public String getUser_id() {
        return uid;
    }

    public void setUser_id(String user_id) {
        this.uid = user_id;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "user_name='" + username + '\'' +
                ", user_id='" + uid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.username);
    }

    protected UserModel(Parcel in) {
        this.uid = in.readString();
        this.username = in.readString();
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
