package com.valjapan.todoapp;

public class ToDoData {
    public String title;
    public String context;
    public String firebaseKey;

    public ToDoData(String key, String title, String context) {
        this.firebaseKey = key;
        this.title = title;
        this.context = context;
    }

    public ToDoData() {
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}