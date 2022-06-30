package me.nopox.image.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.nopox.image.config.MongoConfig
import me.nopox.image.image.ImageEntry
import org.litote.kmongo.KMongo

object MongoDetails {

    lateinit var client: MongoClient
    lateinit var database: MongoDatabase

    lateinit var images: MongoCollection<ImageEntry>

    fun start(config: MongoConfig) {
        client = KMongo.createClient(config.connectionString)
        database = client.getDatabase(config.databaseName)

        images = database.getCollection(config.collectionNames.images, ImageEntry::class.java)
    }
}