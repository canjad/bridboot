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


            //3 发送响应
            /*
                V5的第一个测试目标:将static目录下的index.html页面发送给浏览器

                相对路径中非常常用的一个:类加载路径
                是我们项目中任何一个类(该类的class文件)所在包的顶级包的上一层目录
                就是类加载路径。
                如果使用File去定位该目录，格式是固定的
                File dir = new File(
                    当前类名.class.getClassLoader().getResource(".").toURI()
                );

                以在当前类ClientHandler中使用为例:
                ClientHandler所在的包:com.birdboot.core。该包的顶层包是com，
                此时类加载路径就是指com所在的目录。
                在当前项目中代码编译后，src/main/java中所有的java类都会编译后
                放到该项目的target/classes目录中。而src/main/resources下的静态
                资源也会放到target/classes目录中。
                而com目录就是放到了target/classes中，因此该目录就是类加载路径。
                从该目录能看出，只要定位到他，就可以顺着该目录找到所有java类以及
                所有的静态资源
             */
            //定位当前项目的类加载路径
            File baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            //定位类加载路径下的static目录
            File staticDir = new File(baseDir, "static");
            //定位static下的index.html
           File file = new File(staticDir, path);

            /*
                一个响应的大致格式：
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......(index.html页面内容)
             */


            //3.1发送状态行

           println("HTTP/1.1 200 OK");

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




