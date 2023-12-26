package com.bridboot.core;


import com.bridboot.http.HttpServletRequst;

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
            int statusCode;
            String  statusReason;
            if(file.isFile()){
                statusCode=200;
                statusReason="OK";
            }else {
                statusCode=404;
                statusReason="NotFound";
                file = new File(staticDir, "404.html");
            }

            //3 发送响应

            //定位当前项目的类加载路径

            /*
                一个响应的大致格式：
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......(index.html页面内容)
             */


            //3.1发送状态行

            println("HTTP/1.1"+" "+statusCode+statusReason);

            //3.2发送响应头
            println("Content-Type: text/html");
            println("Content-Length: "+file.length());


            //单独发送回车+换行，表示响应头发送完毕
            println("");

            //3.3发送响应正文(index.html页面内容)
            FileInputStream fis = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();
            byte[] buf = new byte[1024*10];//10kb
            int d;//记录每次实际读取的数据量
            while( (d = fis.read(buf)) !=-1){
                out.write(buf,0,d);
            }

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

    public static void main(String[] args) throws URISyntaxException {
        File baseDir = new File(
                ClientHandler.class.getClassLoader()
                        .getResource(".").toURI()
        );
        //定位类加载路径下的static目录
        File staticDir = new File(baseDir, "static");
        //定位static下的index.html
        File file = new File(staticDir, "index.html");
        System.out.println("页面是否存在:" + file.exists());
    }

}




