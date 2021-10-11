package main.rdt1

import main.base.makePacket
import java.io.File
import java.io.FileOutputStream
import java.net.DatagramSocket
import java.net.InetAddress

fun main() {
    val serverSocket = DatagramSocket(12000, InetAddress.getLocalHost())
    var file = File("src/resource", "newFile.JPG")
    if (file.createNewFile()) {
        println("file created")
    } else {
        println("file already created")
    }
    println("server started...")
    var outputStream = FileOutputStream(file)
    while (true) {
        val data = ByteArray(1024)
        makePacket(data).run {
            serverSocket.receive(this)
            serverSocket.send(this)
            if (!file.exists()) {
                file.createNewFile()
                outputStream = FileOutputStream(file)
            }
            println("receive bytes = ${data.size}")
            outputStream.write(data)
            outputStream.flush()
        }
    }
}
