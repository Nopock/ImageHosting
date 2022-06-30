package me.nopox.image.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MongoConfig(
    /**
     * The name of the database that the app should use in the MongoDB server.
     */
    @SerialName("database_name") val databaseName: String = "imageHosting",

    /**
     * The names of the collections that the app should use to store & retrieve data for its various features.
     */
    @SerialName("collection_names") val collectionNames: CollectionNames = CollectionNames(),

    /**
     * The URL that the app will use to connect to the database server.
     */
    @SerialName("connection_string") val connectionString: String = "mongodb://localhost:27017",
) {

    @Serializable
    data class CollectionNames(
        /**
         * The collection used to store metadata about uploaded images, such as their author & upload date.
         */
        @SerialName("images") val images: String = "images",
    )
}