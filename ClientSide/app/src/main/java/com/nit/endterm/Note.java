package com.nit.endterm;
public class Note {
    private String note_id;
    private String title;
    private String description;
    private String eventTime;
    private int isDone;

    public Note(String note_id, String title, String description, String eventTime, int isDone) {
        this.note_id = note_id;
        this.title = title;
        this.description = description;
        this.eventTime = eventTime;
        this.isDone = isDone;
    }

    public Note(String title, String description, String eventTime, int isDone) {
        this.title = title;
        this.description = description;
        this.eventTime = eventTime;
        this.isDone = isDone;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}