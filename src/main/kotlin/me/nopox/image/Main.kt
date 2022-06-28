package me.nopox.image

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import me.nopox.image.commands.GalleryCommand
import me.nopox.image.commands.UploadCommand
import me.nopox.image.mongo.MongoDetails
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

fun main() {
    MongoDetails.start()

    setupDiscord()
}

fun setupDiscord() {
    val token = "***REMOVED***"

    val jda = light(token, enableCoroutines = true) {
        intents += listOf(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_PRESENCES)

        setActivity(Activity.watching("Your images"))
        setStatus(OnlineStatus.DO_NOT_DISTURB)
        setMemberCachePolicy(MemberCachePolicy.NONE)

        addEventListeners(
            UploadCommand(),
            GalleryCommand()
        )
    }

    jda.upsertCommand("upload", "Uploads an image!")
        .addOption(OptionType.ATTACHMENT, "image", "The image to upload", true)
        .queue()

    jda.upsertCommand("gallery", "Shows all your uploaded images!").queue()
}