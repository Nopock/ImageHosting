package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

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

        val image = event.getOption("image")?.asAttachment
        // PROXY URL has the image




    }
}