package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import me.nopox.image.image.ImageEntry
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import java.net.URL

/**
 * The command used to upload images to our backend.
 */
object UploadCommand : SlashCommandHandler {

    private const val MAX_UPLOADS = 5

    override val metadata: SlashCommandData
        get() = Commands.slash("upload", "Uploads an image!")
            .addOption(OptionType.ATTACHMENT, "image", "The image to upload", true)

    override suspend fun handle(command: SlashCommandInteractionEvent) {
        if (ImageRepository.getImages(command.user.id).size >= MAX_UPLOADS) {
            command.replyEmbeds(Messages.tooManyUploads()).setEphemeral(true).queue()
            return
        }

        val attachment = command.getOption("image")?.asAttachment

        if (attachment == null || !attachment.isImage) {
            command.replyEmbeds(Messages.notAnImage()).setEphemeral(true).queue()
            return
        }

        val client = HttpClient(CIO)

        val imageId = Math.random().toString().substring(2, 9)

        val response = client.get(attachment.proxyUrl)

        ImageRepository.create(
            ImageEntry(
                authorId = command.member!!.id,
                imageId = imageId,
                image = response.readBytes()
            )
        )

        command.replyEmbeds(Messages.successfulUpload(imageId))
            .setEphemeral(true)
            .queue()
    }

    private object Messages {
        fun tooManyUploads() = Embed {
            title = "**Error**"
            description = "You have reached the maximum number of images you can upload ($MAX_UPLOADS). " +
                    "Please delete one of your images before uploading a new one using the buttons in /gallery."
        }

        fun notAnImage() = Embed {
            title = "**Error**"
            description = "You must upload an image."
        }

        fun successfulUpload(imageId: String) = Embed {
            title = "**Success**"
            description = "Your image has been uploaded. You can access it via the following link: " +
                    "https://2328-73-158-63-248.ngrok.io/$imageId"
            image = "https://2328-73-158-63-248.ngrok.io/$imageId"
        }
    }
}