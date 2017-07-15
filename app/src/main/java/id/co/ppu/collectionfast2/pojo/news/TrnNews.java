package id.co.ppu.collectionfast2.pojo.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Eric on 12-Jul-17.
 */

public class TrnNews extends RealmObject implements Serializable{

    @PrimaryKey
    @SerializedName("uid")
    private String uid;

    @SerializedName("title")
    private String title;

    @SerializedName("sender")
    private String sender;

    @SerializedName("to")
    private String to;

    @SerializedName("message")
    private String message;

    @SerializedName("excerpt")
    private String excerpt;

    // 0: common text, 1: timestamp, 2: web/html
    @SerializedName("messageType")
    private String messageType;

    // 0 or null : unopen, 1: server receive, 2: target client receive, 3: read and opened
    @SerializedName("messageStatus")
    private String messageStatus;

    @SerializedName("createdTimestamp")
    private Date createdTimestamp;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public String toString() {
        return "TrnNews{" +
                "uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", sender='" + sender + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", messageType='" + messageType + '\'' +
                ", messageStatus='" + messageStatus + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }
}
