package com.my.dingtalkdelete.dingtalk.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class DentryInfo implements Parcelable {
    public String spaceId;
    public String name;
    public String path;
    public String creatorEmail;
    public String serverId;
    public String loadMoreId;
    public String title;
    public String ownerId;

    public DentryInfo() {

    }

    private DentryInfo(Parcel in) {
        spaceId = in.readString();
        name = in.readString();
        path = in.readString();
        creatorEmail = in.readString();
        serverId = in.readString();
        loadMoreId = in.readString();
        title = in.readString();
        ownerId = in.readString();
    }

    public static final Creator<DentryInfo> CREATOR = new Creator<DentryInfo>() {
        @Override
        public DentryInfo createFromParcel(Parcel in) {
            return new DentryInfo(in);
        }

        @Override
        public DentryInfo[] newArray(int size) {
            return new DentryInfo[size];
        }
    };

    @Override
    public String toString() {
        return "DentryInfo{" +
                "spaceId='" + spaceId + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", creatorEmail='" + creatorEmail + '\'' +
                ", serverId='" + serverId + '\'' +
                ", loadMoreId='" + loadMoreId + '\'' +
                ", title='" + title + '\'' +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(spaceId);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(creatorEmail);
        dest.writeString(serverId);
        dest.writeString(loadMoreId);
        dest.writeString(title);
        dest.writeString(ownerId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DentryInfo) {
            DentryInfo info = (DentryInfo)obj;
            if (this.hashCode() == info.hashCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        String strHash = path + serverId;
        return strHash.hashCode();
    }
}
