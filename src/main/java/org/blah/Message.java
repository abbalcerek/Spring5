package org.blah;

import lombok.Data;

/**
 * Created by ubuntu on 5/8/18.
 */
@Data
public class Message {

    private String senderAddress;

    private String payload;

    public Message() {
    }

    public Message(String senderAddress, String payload) {
        this.senderAddress = senderAddress;
        this.payload = payload;
    }

//    public String getSenderAddress() {
//        return senderAddress;
//    }
//
//    public void setSenderAddress(String senderAddress) {
//        this.senderAddress = senderAddress;
//    }
//
//    public String getPayload() {
//        return payload;
//    }
//
//    public void setPayload(String payload) {
//        this.payload = payload;
//    }

    @Override
    public String toString() {
        return "Message{" +
                "senderAddress='" + senderAddress + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
