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
            char pre='a'; //上次读取的字符
            char cur='a'; //本次读取的字符
            StringBuilder builder = new StringBuilder();//保存读取后的所有字符
            while ((d=in.read())!=-1){
                System.out.print((char)d);
                cur =(char)d;
                if (pre==13 && cur==10){
                    break;
                }
                builder.append(cur); //将本次读取的字符拼接
                pre= cur; //将读取的内容保存到上次读取的字符中
            }
            String line = builder.toString().trim();
            System.out.println("请求行："+line);
            String method;
            String uri;
            String protocol;
            //将请求行的内容按照空格进行拆分
            String [] data=line.split("\\s");
            method= data[0];
            uri= data[1];
            protocol= data[2];

            System.out.println("method"+method);
            System.out.println("uri"+uri);
            System.out.println("protocol"+protocol);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
