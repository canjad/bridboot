package com.bridboot.core;
//该线程任务负责跟指定的客户端进行Http交互

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            int d;
            while ((d=in.read())!=-1){
                System.out.print((char)d);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
