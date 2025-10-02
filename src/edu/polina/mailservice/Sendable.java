package edu.polina.mailservice;

public abstract class Sendable<T> {
    String from;
    String to;
    T content;

    public Sendable(String from, String to, T content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public T getContent() {
        return content;
    }
}