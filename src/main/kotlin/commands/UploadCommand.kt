@file:Suppress("FunctionName")

package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import me.nopox.image.image.ImageEntry
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

/**
 * The maximum number of images that a user may have in our database at any given time.
 */
private const val MAX_UPLOADS = 5

/**
 * An HTTP client for downloading image files from discord's CDN.
 */
private val attachmentFetcher: HttpClient by lazy { HttpClient(CIO) }

/**
 * The command used to upload images to our backend.
 */
@Command("upload", "Uploads an image!")
@CommandOption("image", "The image to upload", OptionType.ATTACHMENT, isRequired = true)
suspend fun UploadCommand(command: SlashCommandInteractionEvent) {
    // Ensure the user hasn't surpassed their maximum amount of uploads.
    val totalAlreadyUploaded = ImageRepository.getImages(command.user.id).size
    if (totalAlreadyUploaded >= MAX_UPLOADS)
        return command.replyEmbeds(Embeds.Error.tooManyUploads()).setEphemeral(true).queue()

    // Ensure the user included an image file when they invoked the command.
    val attachment = command.getOption("image")?.asAttachment
    if (attachment == null || !attachment.isImage)
        return command.replyEmbeds(Embeds.Error.notAnImage()).setEphemeral(true).queue()

    // Fetch the image itself from discord's CDN.
    val response = attachmentFetcher.get(attachment.proxyUrl)
    // Generate a random ID that the image can be referenced by.
    // TODO: 7/4/22 Check for collisions so that we don't overwrite anything by coincidence.
    val imageId = Math.random().toString().substring(2, 9)

    ImageRepository.create(
        ImageEntry(
            authorId = command.user.id,
            imageId = imageId,
            image = response.readBytes()
        )
    )

    command.replyEmbeds(Embeds.Success.uploaded(imageId))
        .setEphemeral(true)
        .queue()
}

/**
 * Functions for generating the command's responses, which we display as embedded messages.
 */
private val Embeds = object {
    val Error = object {
        fun tooManyUploads() = Embed {
            title = "**Error**"
            description = "You have reached the maximum number of images you can upload ($MAX_UPLOADS). " +
                    "Please delete one of your images before uploading a new one using the buttons in /gallery."
        }

        fun notAnImage() = Embed {
            title = "**Error**"
            description = "You must upload an image."
        }
    }

    val Success = object {
        fun uploaded(imageId: String) = Embed {
            title = "**Success**"
            description = "Your image has been uploaded. You can access it via the following link: " +
                    "https://2328-73-158-63-248.ngrok.io/$imageId"
            image = "https://2328-73-158-63-248.ngrok.io/$imageId"
        }
    }
}