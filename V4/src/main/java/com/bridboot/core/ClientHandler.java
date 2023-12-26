package com.bridboot.core;
//该线程任务负责跟指定的客户端进行Http交互

import com.bridboot.http.HttpServletRequst;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;

    public Cl·ientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1.解析请求 将解析请求移动到Http 构造器当中
            HttpServletRequst requst = new HttpServletRequst(socket);
            String path=requst.getUri();
            System.out.println(path);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}



