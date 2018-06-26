package com.robert.aidldemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageModel implements Parcelable {

    private String from;
    private String to;
    private String content;

    @Override
    public String toString() {
        return "MessageModel{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public MessageModel() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.content);
    }

    //Parcelable接口并没有要求实现这个方法，但是在aidl生成的Java文件中需要，如果设置为out或者inout的时候
    public void readFromParcel(Parcel dest) {
        this.from = dest.readString();
        this.to = dest.readString();
        this.content = dest.readString();
    }

    protected MessageModel(Parcel in) {
        this.from = in.readString();
        this.to = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<MessageModel> CREATOR = new Parcelable.Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public static MessageModel create(String from, String to, String content) {
        MessageModel model = new MessageModel();
        model.setContent(content);
        model.setFrom(from);
        model.setTo(to);
        return model;
    }
}
