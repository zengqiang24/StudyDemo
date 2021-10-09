package main.urdt;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(
                    12000,
                    InetAddress.getLocalHost());  //把套接字绑定到本地ip：端口12000上
            System.out.println("服务端已经启动");
            while (true) { // 轮询等待客户端的请求
                byte[] bytes = new byte[4048];
                DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);//创建分组
                serverSocket.receive(datagramPacket);//阻塞等待客户端的请求
                System.out.println("收到客户端(" + datagramPacket.getAddress().getHostAddress() + ":" + datagramPacket.getPort() + ")发来的请求..."
                        + new String(bytes, 0, bytes.length));
                byte[] data = datagramPacket.getData();
                String decodedData = new String(data);
                int i = 0;
                while (i < 10000) {
                    datagramPacket.setData(("xi" + (i++)).getBytes());
                    serverSocket.send(datagramPacket);// 发送响应报文给客户端
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
