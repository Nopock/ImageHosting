package me.nopox.image.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordConfig(
    /**
     * The private token that the app will use to authenticate with Discord.
     */
    @SerialName("token") val token: String = "xxx.xxx.xxx"
)