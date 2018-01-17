package com.anosi.asset.model.jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "messageInfo")
public class MessageInfo extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 2676027100413861771L;

    private Account from;

    private Account to;

    private Date sendTime;

    private Date readTime;

    private String content;

    private String title;

    private String url;

    private MessageType messageType = MessageType.DEFAULT;

    private MessageStatus messageStatus = MessageStatus.UNREAD;

    //信息的种类，内部枚举类
    public static enum MessageType {

        DEFAULT("默认"), INFO("普通信息"), PRIMARY("私人信息"), SUCCESS("成功"), WARNING("警告"), DANGER("危险");

        private String type;

        private MessageType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    //信息状态
    public static enum MessageStatus {
        READ, UNREAD
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(nullable = false)
    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(nullable = false)
    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    @Transient
    public boolean isRead() {
        return Objects.equals(messageStatus, MessageStatus.READ);
    }

}
