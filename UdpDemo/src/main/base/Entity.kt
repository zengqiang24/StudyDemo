package main.base

import java.io.Serializable

data class Entity(var seq:Int, var ack:Int, var content: String): Serializable
