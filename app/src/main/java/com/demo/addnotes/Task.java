package com.demo.addnotes;

public class Task {
    String Id;
    String Title;
    String Note;
    String Date;
    String Time;
    String CurrentTime;

    public Task(String id, String title, String note, String date, String time, String currentTime) {
        Id = id;
        Title = title;
        Note = note;
        Date = date;
        Time = time;
        CurrentTime = currentTime;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
