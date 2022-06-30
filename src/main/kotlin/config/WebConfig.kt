package me.nopox.image.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebConfig(
    /**
     * The TCP port that the web server will be accessible on.
     */
    @SerialName("port") val port: UShort = 8080u
)