package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import me.nopox.image.image.ImageEntry
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

/**
 * The command for displaying a collection of a user's uploaded images.
 */
object GalleryCommand : SlashCommandHandler {

    override val metadata: SlashCommandData
        get() = Commands.slash("gallery", "Shows all your uploaded images!")

    override fun handle(command: SlashCommandInteractionEvent) {
        val randomImage = ImageRepository.getImages(command.user.id).randomOrNull()
        if (randomImage == null) {
            command.reply("You don't have any images yet!").setEphemeral(true).queue()
            return
        }

        command.replyEmbeds(Messages.Success.randomImage(randomImage))
            .setEphemeral(true)
            .queue()
    }

    private object Messages {
        object Success {
            fun randomImage(randomImage: ImageEntry) = Embed {
                title = "**Gallery**"
                description = "One item in your gallery:"
                image = randomImage.discordUrl
            }
        }
    }
}