@file:Suppress("FunctionName")

package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import me.nopox.image.image.ImageEntry
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

/**
 * The command for displaying a collection of a user's uploaded images.
 */
@Command(name = "gallery", description = "Shows all your uploaded images!")
fun GalleryCommand(command: SlashCommandInteractionEvent) {
    val randomImage = ImageRepository.getImages(command.user.id).randomOrNull()
        ?: return command.reply("You don't have any images yet!").setEphemeral(true).queue()

    command.replyEmbeds(Embeds.Success.randomImage(randomImage))
        .setEphemeral(true)
        .queue()
}

/**
 * Functions for generating the command's responses, which we display as embedded messages.
 */
private val Embeds = object {
    val Success = object {
        fun randomImage(randomImage: ImageEntry) = Embed {
            title = "**Gallery**"
            description = "One item in your gallery:"
            image = "tacker.cc:8080/${randomImage.imageId}"
        }
    }
}