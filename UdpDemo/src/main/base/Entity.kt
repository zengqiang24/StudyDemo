package main.base

data class Entity(var seq: Int, var ack: Int, var content: String)

fun Entity.encode(): ByteArray {
    var byteArray = ByteArray(2 + 1022)
    var seqByte = seq.toByte()
    var ackByte = ack.toByte()
    var headArray = byteArrayOf(seqByte, ackByte)
    var contentArray = content.toByteArray()

    System.arraycopy(headArray, 0, byteArray, 0, 2)
    System.arraycopy(contentArray, 0, byteArray, 2, contentArray.size)

    return byteArray
}

fun Entity.decode(byteArray: ByteArray): Entity {
    val seqByte = byteArray[0]
    seq = seqByte.toInt()
    val ackByte = byteArray[1]
    ack = ackByte.toInt()
    val contentByteArray = byteArray.slice(2 until byteArray.size).toByteArray()
    content = String(contentByteArray)
    return this
}

//Test encode() and decode()
fun main() {
    val entity = Entity(1, 5, "hello")
    val buf = entity.encode()
    println("encode = ${entity.encode()}")

    val entityDecoded = entity.decode(buf)
    println("entityDecoded = $entityDecoded")
}