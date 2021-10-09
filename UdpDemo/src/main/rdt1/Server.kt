package main.rdt1

import java.io.File
import java.io.FileOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun main() {
    val serverSocket = DatagramSocket(12000, InetAddress.getLocalHost())
    var file = File("src/resource","newFile.JPG")
    if(file.createNewFile()){
        println("file created")
    } else {
        println("file already created")
    }
    println("server started...")
    var outputStream = FileOutputStream(file)
    while (true) {
        val byteArray = ByteArray(1024)
        DatagramPacket(
            byteArray,
            0,
            byteArray.size
        ).run {
            serverSocket.receive(this)
            if(!file.exists()){
                file.createNewFile()
                outputStream = FileOutputStream(file)
            }
            println("receive bytes = ${byteArray.size}")
            outputStream.write(byteArray)
            outputStream.flush()
        }

    }
}