package me.nopox.image.commands

import dev.minn.jda.ktx.messages.Embed
import me.nopox.image.image.repository.ImageRepository
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.Random

class GalleryCommand : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != "gallery") return

        val images = event.member?.let { ImageRepository.getImages(it.id) }

        if (images?.isEmpty() == true) {
            event.reply("You don't have any images yet!").setEphemeral(true).queue()
            return
        }

        val random = Random()

        val randomImage = images?.get(random.nextInt(images.size))

        val embed = Embed {
            title = "**Gallery**"
            description = "One item in your gallery:"
            if (randomImage != null) {
                image = randomImage.discordUrl
            }
        }

        event.replyEmbeds(embed).setEphemeral(true).queue()
    }
}