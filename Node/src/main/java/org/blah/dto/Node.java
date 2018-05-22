package org.blah.dto;

import jdk.net.SocketFlow;
import org.joda.time.DateTime;

public class Node {

    private String address;

    private DateTime lastMessage;

    private boolean responsive;

    public Node(String address, DateTime lastMessage, boolean responsive) {
        this.address = address;
        this.lastMessage = lastMessage;
        this.responsive = responsive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DateTime getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(DateTime lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isResponsive() {
        return responsive;
    }

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    @Override
    public String toString() {
        return "Node{" +
                "address='" + address + '\'' +
                ", lastMessage=" + lastMessage +
                ", responsive=" + responsive +
                '}';
    }
}
