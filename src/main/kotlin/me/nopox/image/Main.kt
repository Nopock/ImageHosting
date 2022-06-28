package me.nopox.image

import ltd.matrixstudios.duplex.DuplexMongoManager

fun main() {
    println("[Main] Hello, world!")

    DuplexMongoManager.start("mongodb://localhost:27107", "image")
}