package main.base

import java.io.File
import java.io.FileInputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
const val FILE_PATH = "src/resource/IMG_2180.JPG"

/**
 * 获得文件输入流
 */
fun getFileInputStream(filePath: String): FileInputStream {
    return FileInputStream(File(filePath))
}

/**
 * 接收端构建数据包，不需要目的端口和
 */
fun makePacket(data: ByteArray): DatagramPacket {
    return DatagramPacket(
        data,
        0,
        data.size
    )
}

/**
 * 构建发送端数据包，包括目的地址（ip+port）
 *
 */
fun makeSendPacket(data: ByteArray): DatagramPacket {
    //打包成数据报发送给接收端
    return DatagramPacket(
        data,
        1024,
        InetAddress.getLocalHost(),
        12000
    )
}

/**
 * 发送报文
 */
fun rdtSend(
    socket: DatagramSocket,
    bytes: ByteArray
) {
    //打包成数据报发送给接收端
    makeSendPacket(bytes)
        .run {
            socket.send(this)
        }
}
