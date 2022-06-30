package me.nopox.image.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A root-level container for all the app's configuration.
 */
@Serializable
data class Config(
    /**
     * Configuration related to the backend web server hosted by the app.
     */
    @SerialName("web") val web: WebConfig = WebConfig(),

    /**
     * Configuration related to the app's connection to its database, which is MongoDB.
     */
    @SerialName("mongodb") val mongodb: MongoConfig = MongoConfig(),

    /**
     * Configuration related to the app's discord bot & integration.
     */
    @SerialName("discord") val discord: DiscordConfig = DiscordConfig(),
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger("image.config")
    }
}