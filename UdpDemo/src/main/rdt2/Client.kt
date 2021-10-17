package main.rdt2

import main.base.*
import java.net.DatagramSocket
import java.net.SocketTimeoutException
var seq = 0
var nextSeq = 0
//停等协议，发送端发送一条分组后，要等到这个分组接收到ack才继续发送下一条，否则原地等待，
fun main() {
    val socket = DatagramSocket()
    socket.soTimeout = 2000//2秒超时
    val data = ByteArray(1024)
    val inputStream = getFileInputStream(FILE_PATH)

    while (true) {
        val read = inputStream.read(data)
        if (read != -1) {
            rdtSend(socket, data)
            nextSeq ++
            val cacheData = data
            synchronized(socket) {
                val data  = ByteArray(1024)
                try {
                    socket.receive(makePacket(data))
                    val ack = isAck(data)
                    if(!ack){
                        rdtSend(socket, cacheData)
                    } else {
                        println("接收到确认报文ack = " )
                    }
                } catch (socketTimeOut: SocketTimeoutException) {
                    rdtSend(socket, cacheData)//如果接收端接收确认码ack超时，则代表状态码已经丢失，重发这个数据报文。
                }

            }
        } else {
            socket.close()
        }
    }
}

fun isAck(int: ByteArray):Boolean {
    return true
}

