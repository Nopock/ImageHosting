package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GalleryCommand : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "gallery") return

        val embed = Embed {
            title = "**Gallery**"
            description = "One item in your gallery:"
            image {
                url = ImageRepository.getRandomImage()
            }
        }

        event.replyEmbeds(embed).setEphemeral(true).queue
        return
    }
}