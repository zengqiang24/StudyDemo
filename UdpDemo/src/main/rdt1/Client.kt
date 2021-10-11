package main.rdt1

import main.base.makePacket
import main.base.makeSendPacket
import main.base.rdtSend
import java.io.File
import java.io.FileInputStream
import java.net.DatagramSocket

/**
 * 假设底层信道是可靠的，接收端不会丢失任何数据。
 * 这是第一个最简单的可靠传输实现机制v1.0
 */
fun main() {
    val file = File("src/resource/IMG_2180.JPG")
    val inputStream = FileInputStream(file)
    val socket = DatagramSocket()

    println("file scan completed, file length = ${file.length()}")
    println("client start...")
    while (true) {
        val bytes = ByteArray(1024)
        val read = inputStream.read(bytes)
        if (read != -1) {
            rdtSend(socket, bytes)
        } else {
            println("file sent finished.")
            socket.close()
            println("client closed.")
            break
        }
    }
}

