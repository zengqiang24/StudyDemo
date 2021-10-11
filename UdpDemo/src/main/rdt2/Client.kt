package main.rdt2

import main.base.*
import java.net.DatagramSocket
import java.net.SocketTimeoutException

//停等协议，发送端发送一条分组后，要等到这个分组接收到ack才继续发送下一条，否则原地等待，
fun main() {
    val socket = DatagramSocket()
    socket.soTimeout = 2000
    val data = ByteArray(1024)
    val inputStream = getFileInputStream(FILE_PATH)
    while (true) {
        val read = inputStream.read(data)
        if (read != -1) {
            rdtSend(socket, data)
            val cacheData = data
            synchronized(socket) {
                val data  = ByteArray(1024)
                try {
                    socket.receive(makePacket(data))
                } catch (socketTimeOut: SocketTimeoutException) {
                    rdtSend(socket, cacheData)
                }
                if(!isAck(data)){
                    rdtSend(socket, cacheData)
                }
            }
        } else {
            socket.close()
        }
    }
}

fun isAck(data: ByteArray):Boolean {
    return true
}

