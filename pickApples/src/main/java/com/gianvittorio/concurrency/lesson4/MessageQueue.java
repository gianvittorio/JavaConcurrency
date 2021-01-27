package com.gianvittorio.concurrency.lesson4;

public interface MessageQueue {
    void send(String message);

    String receive();
}
