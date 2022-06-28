package me.nopox.image.image.repository

import me.nopox.image.image.ImageEntry
import me.nopox.image.mongo.MongoDetails
import org.litote.kmongo.eq

object ImageRepository {
    fun create(image: ImageEntry) {
        MongoDetails.images.insertOne(image)
    }

    fun getImages(userId: String): List<ImageEntry> {
        return MongoDetails.images.find(ImageEntry::authorId eq userId).toList()
    }

    fun getImage(id: String): ImageEntry? {
        return MongoDetails.images.find(ImageEntry::imageId eq id).first()
    }
}