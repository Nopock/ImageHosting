package me.nopox.image.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.nopox.image.image.ImageEntry
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

object MongoDetails {

    lateinit var client: MongoClient
    lateinit var database: MongoDatabase

    lateinit var images: MongoCollection<ImageEntry>

    fun start() {
        client = KMongo.createClient("mongodb://localhost:27017")
        database = client.getDatabase("imageHosting")

        images = database.getCollection()
    }
}