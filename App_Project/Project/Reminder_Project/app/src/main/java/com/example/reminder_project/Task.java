package com.example.reminder_project;

public class Task {

    String name;
    String note;
    String date;
    String time;
    String repeat;
    String priority;

    public Task() {
    }

    public Task(String name, String note, String date, String time, String repeat, String priority) {
        this.name = name;
        this.note = note;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
