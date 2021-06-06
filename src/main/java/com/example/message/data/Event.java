package com.example.message.data;

import java.util.Date;

public class Event {
    private String eventName;
    private Date startTime;
    private Date endTime;

    Event() {
        eventName = "";
        startTime = new Date();
        endTime = new Date();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
