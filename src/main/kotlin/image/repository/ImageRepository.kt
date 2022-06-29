package me.nopox.image.image.repository

import me.nopox.image.image.ImageEntry
import me.nopox.image.mongo.MongoDetails
import org.litote.kmongo.eq

object ImageRepository {
    /**
     * Creates a new image entry in the database.
     */
    fun create(image: ImageEntry) {
        val time = System.currentTimeMillis()
        MongoDetails.images.insertOne(image)
        println("[ImageRepository] create: ${System.currentTimeMillis() - time}ms")
    }

    /**
     * Gets all the images from a user in the database.
     */
    fun getImages(userId: String): List<ImageEntry> {
        val time = System.currentTimeMillis()

        val images = MongoDetails.images.find(ImageEntry::authorId eq userId).toList()

        println("[ImageRepository] getImages: ${System.currentTimeMillis() - time}")

        return images
    }

    /**
     * Gets an image from the database.
     */
    fun getImage(id: String): ImageEntry? {
        val time = System.currentTimeMillis()

        val image = MongoDetails.images.find(ImageEntry::imageId eq id).first()

        println("[ImageRepository] getImage: ${System.currentTimeMillis() - time}ms")

        return image
    }
}