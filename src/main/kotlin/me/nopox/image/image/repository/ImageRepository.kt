package me.nopox.image.image.repository

import me.nopox.image.image.ImageEntry
import me.nopox.image.mongo.MongoDetails
import org.litote.kmongo.eq

object ImageRepository {
    /**
     * Creates a new image entry in the database.
     */
    fun create(image: ImageEntry) {
        MongoDetails.images.insertOne(image)
    }

    /**
     * Gets all the images from a user in the database.
     */
    fun getImages(userId: String): List<ImageEntry> {
        return MongoDetails.images.find(ImageEntry::authorId eq userId).toList()
    }

    /**
     * Gets an image from the database.
     */
    fun getImage(id: String): ImageEntry? {
        return MongoDetails.images.find(ImageEntry::imageId eq id).first()
    }
}