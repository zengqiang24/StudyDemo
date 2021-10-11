package main.urdt;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws SocketException {
        DatagramSocket clientSocket = new DatagramSocket();
        String hi = "hi";
        byte[] bytes = new byte[4048];
        byte[] bytes1 = hi.getBytes();
        System.arraycopy(bytes1,0, bytes,0, bytes1.length);
        try {
            DatagramPacket packet = new DatagramPacket(bytes,  //创建分组，并设置目的地址
                    0,
                    bytes.length,
                    Inet4Address.getLocalHost(),
                    12000);
            clientSocket.send(packet);
            System.out.println("发送报文分组至" + packet.getAddress().getHostAddress() + ":" + packet.getPort());
            System.out.println("*******************************************************");

            while(true) {
                clientSocket.receive(packet);
                byte[] data = packet.getData();

                System.out.println("接收到来自：" + packet.getAddress().getHostAddress() + ":"+ packet.getPort() + "的数据报文分组"+
                        "; 数据长度 = " + data.length +
                        "; 数据内容 = " + new String(data,"utf-8"));
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientSocket.close();
        }
    }
}
