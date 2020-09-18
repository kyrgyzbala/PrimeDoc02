package kg.kyrgyzcoder.primedoc02.ui.chat.model;

import com.google.firebase.Timestamp;

public class Chat {

    private String clientId;
    private String adminId;
    private String adminPhone;
    private String lastMessage;
    private String lastMessageSenderId;
    private Boolean chatStarted;
    private Timestamp lastMessageTime;

    public Chat(String clientId, String adminId, String adminPhone, String lastMessage, String lastMessageSenderId, Boolean chatStarted) {
        this.clientId = clientId;
        this.adminId = adminId;
        this.adminPhone = adminPhone;
        this.lastMessage = lastMessage;
        this.lastMessageSenderId = lastMessageSenderId;
        this.chatStarted = chatStarted;
    }

    public Chat() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public Boolean getChatStarted() {
        return chatStarted;
    }

    public void setChatStarted(Boolean chatStarted) {
        this.chatStarted = chatStarted;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "clientId='" + clientId + '\'' +
                ", adminId='" + adminId + '\'' +
                ", adminPhone='" + adminPhone + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageSenderId='" + lastMessageSenderId + '\'' +
                ", chatStarted=" + chatStarted +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }
}
