package me.nopox.image

import dev.minn.jda.ktx.jdabuilder.intents
import dev.minn.jda.ktx.jdabuilder.light
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import me.nopox.image.commands.CommandDispatcher
import me.nopox.image.commands.GalleryCommand
import me.nopox.image.commands.UploadCommand
import me.nopox.image.image.repository.ImageRepository
import me.nopox.image.mongo.MongoDetails
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

fun main() {
    MongoDetails.start()

    setupDiscord()

    createApi()
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
    }

    val commands = CommandDispatcher(discord = jda)
    commands.registerSlash(UploadCommand, GalleryCommand)
    jda.addEventListener(commands)
}

fun createApi() {
    runBlocking {
        val client = HttpClient(CIO)

        embeddedServer(Netty, port = 8080) {
            routing {
                get("/{imageId}") {
                    val imageId = call.parameters["imageId"]

                    val image = imageId?.let { it1 -> ImageRepository.getImage(it1) }

                    if (image == null) {
                        call.respondText("Image not found!", ContentType.Text.Plain)
                        return@get
                    }

                    val response = client.get(image.discordUrl)

                    call.respondBytes(response.readBytes())
                }
            }
        }.start(wait = true)
    }
}