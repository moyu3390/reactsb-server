package com.example.demo.tcp;

interface INettyTcpAdapter {
    boolean run();
    boolean stop();
    boolean send(String message);
}
