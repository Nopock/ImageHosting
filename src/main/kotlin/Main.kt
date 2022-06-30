package me.nopox.image

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import me.nopox.image.commands.CommandDispatcher
import me.nopox.image.commands.GalleryCommand
import me.nopox.image.commands.UploadCommand
import me.nopox.image.config.DiscordConfig
import me.nopox.image.config.WebConfig
import me.nopox.image.image.ImageEntry
import me.nopox.image.config.loadConfig
import me.nopox.image.config.mappedArguments
import me.nopox.image.image.repository.ImageRepository
import me.nopox.image.mongo.MongoDetails
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

fun main(args: Array<String>) {
    val argPairs = args.mappedArguments()
    val config = loadConfig(argPairs["config"] ?: "config.yml")

    MongoDetails.start(config.mongodb)
    setupDiscord(config.discord)
    createApi(config.web)
}

fun setupDiscord(config: DiscordConfig) {
    val jda = light(config.token, enableCoroutines = true) {
        intents += listOf(
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_PRESENCES)

        setActivity(Activity.watching("Your images"))
        setStatus(OnlineStatus.DO_NOT_DISTURB)
        setMemberCachePolicy(MemberCachePolicy.NONE)
    }

    val commands = CommandDispatcher(discord = jda)
    commands.registerSlash(UploadCommand, GalleryCommand)
    jda.addEventListener(commands)
}

fun createApi(config: WebConfig) {
    runBlocking {
        val client = HttpClient(CIO)

        embeddedServer(Netty, port = config.port.toInt()) {
            routing {
                get("/{imageId}") {
                    val imageId = call.parameters["imageId"]

                    val image = imageId?.let { it1 -> ImageRepository.getImage(it1) }

                    if (image == null) {
                        call.respondText("Image not found!", ContentType.Text.Plain)
                        return@get
                    }

                    call.respondBytes(image.image)
                }

                post("/image/") {
                    val image = call.receive<ImageEntry>()

                    ImageRepository.create(image)
                }
            }
        }.start(wait = true)
    }
}