package com.bridboot.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
* 响应对象 每一个实例
*
* */
public class HttpServletResponse {
    private Socket socket;
    private int statusCode=200; //状态代码

    private  String statusReason="OK"; //状态描述
    //响应头的相关信息
    //响应正文相关信息
    private File contentFile;
    public HttpServletResponse(Socket socket){
        this.socket=socket;
    }
    //该方法用于将当前的响应对象以标准的HTTP响应格式发送
    public void  response() throws IOException {
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
        sendStatusLine();
        //3.2发送响应头
        sendHeaders();
        //3.3发送响应正文(index.html页面内容)
        sendContent();
    }
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+statusReason);
    }
    private void sendHeaders() throws IOException {
        println("Content-Type: text/html");
        println("Content-Length: "+contentFile.length());


        //单独发送回车+换行，表示响应头发送完毕
        println("");
    }
    private void sendContent() throws IOException {
        FileInputStream fis = new FileInputStream(contentFile);
        OutputStream out = socket.getOutputStream();
        byte[] buf = new byte[1024*10];//10kb
        int d;//记录每次实际读取的数据量
        while( (d = fis.read(buf)) !=-1){
            out.write(buf,0,d);
        }
    }
    /*
    * */
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data=line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }
}
