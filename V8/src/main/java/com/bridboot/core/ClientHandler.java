package com.bridboot.core;


import com.bridboot.http.HttpServletRequst;
import com.bridboot.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * 该线程任务负责与指定的客户端进行HTTP交互
 * HTTP协议要求浏览器与服务端采取"一问一答"的模式。对此，这里的处理流程分为三步:
 * 1:解析请求
 * 2:处理请求
 * 3:发送响应
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //1 解析请求
            HttpServletRequst request = new HttpServletRequst(socket);
            HttpServletResponse response = new HttpServletResponse(socket);
            //获取请求的抽象路径


            //2 处理请求 将处理的请求移动到dispatch servlet方法中调用并且执行
           DispatcherServlet servlet = new DispatcherServlet();
           servlet.service(request,response);
            //发送响应
            response.response();


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //Http协议要求浏览器和服务端交互完毕过后要断开连接
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data=line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }



}




