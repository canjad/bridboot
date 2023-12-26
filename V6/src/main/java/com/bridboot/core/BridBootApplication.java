package com.bridboot.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BridBootApplication {
    private ServerSocket  serverSocket;
    public BridBootApplication(){

        try {
            System.out.println("正在启动服务器...");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务器启动完毕！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void start(){
        try {
            while (true) {
                System.out.println("等待客户端连接...");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端连接了");
                //启动一个线程处理该客户端交互
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) {
        BridBootApplication application = new BridBootApplication();
        application.start();
    }
}
