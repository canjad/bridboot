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
            String path = request.getUri();
            System.out.println(path);

            //2 处理请求
            File baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            //定位类加载路径下的static目录
            File staticDir = new File(baseDir, "static");
            //fiLE 定位static目录下请求一个不存在或者定位的是一个目录这都属于失效的情况
            //定位static下的index.html
            File file = new File(staticDir, path);

            if(file.isFile()){
                //由于响应对象中状态码是200，OK因此 正确情况下不用再设置
                response.setContentFile(file);
            }else {
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir, "404.html");
                response.setContentFile(file);
            }
            //发送响应
            response.response();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
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




