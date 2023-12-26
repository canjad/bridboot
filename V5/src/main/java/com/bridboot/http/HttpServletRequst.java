package com.bridboot.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象  每一个实例表示浏览奇发送过来一个HTTP请求
 */
public class HttpServletRequst {
    private Socket socket;
    //请求行的相关信息
   private String method;
    private String uri;
   private  String protocol;
   //消息头的相关信息
    private Map<String,String> headers=new HashMap<>();
    public HttpServletRequst(Socket socket) throws IOException {
        this.socket = socket;
        parseRequstLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();
    }

        //消息头相关信息 key: 消息头名字 value：消息头对应的值


    //解析请求行
     private void parseRequstLine() throws IOException {
         String line = readLine();
         System.out.println("请求行：" + line);

         //将请求行的内容按照空格进行拆分
         String[] data = line.split("\\s");
         method = data[0];
         uri = data[1];
         protocol = data[2];

         System.out.println("method:" + method);
         System.out.println("uri:" + uri);
         System.out.println("protocol:" + protocol);
     }
    //解析消息头
    private void parseHeaders() throws IOException {
            while (true){
                String line = readLine();

                if(line.isEmpty()){
                    break;
                }
                System.out.println("消息头:" + line);
                String[] data= line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);
        }



     private void parseContent(){}
    private String readLine() throws IOException {
        //对一个socket实例调用多次getinputStream
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
        String line=builder.toString().trim();
        return line;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /***
     * 根据给定的消息头的名字获取对应的值
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }
}
