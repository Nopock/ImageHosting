package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import me.nopox.image.image.ImageEntry
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.concurrent.ForkJoinPool

class UploadCommand : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "upload") return

        val maxUploads = 5

        if (event.member?.let { ImageRepository.getImages(it.id).size }!! >= maxUploads) {
            val embed = Embed {
                title = "**Error**"
                description = "You have reached the maximum number of images you can upload ($maxUploads). Please delete one of your images before uploading a new one using the buttons in /gallery."
            }

            event.replyEmbeds(embed).setEphemeral(true).queue()
            return
        }

        val specialImage = event.getOption("image")?.asAttachment

        if (specialImage != null) {
            if (!specialImage.isImage) {
                val embed = Embed {
                    title = "**Error**"
                    description = "You must upload an image."
                }

                event.replyEmbeds(embed).setEphemeral(true).queue()
                return
            }
        }

        // PROXY URL has the image
        val imageId = Math.random().toString().substring(2, 9);

        if (specialImage != null) {
            ImageRepository.create(
                ImageEntry(
                    authorId = event.member!!.id,
                    imageId = imageId,
                    discordUrl = specialImage.proxyUrl
                )
            )
        }

        val embed = Embed {
            title = "**Success**"
            description = "Your image has been uploaded. You can access it via the following link: https://c623-73-158-63-248.ngrok.io/$imageId"

            this.image = "https://c623-73-158-63-248.ngrok.io/$imageId"
        }

        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}