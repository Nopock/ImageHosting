package me.nopox.image.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

/**
 * A functional interface for classes capable of handling an individual command.
 *
 * The primary function is [handle], which accepts an instance of the [CommandInteraction] event received from JDA.
 *
 * @param[D] The interface used to represent the command's [metadata].
 * @param[E] The interface used to represent [invocations][handle] of the command.
 */
interface CommandHandler<out D : CommandData, in E : CommandInteraction> {

    /**
     * Information about the command being handled.
     *
     * This includes its [name][CommandData.getName], and possibly its [description][SlashCommandData.getDescription],
     * and [arguments][SlashCommandData.getOptions] as well.
     */
    val metadata: D

    /**
     * Processes invocations of this handler's [command].
     *
     * This usually involves replying to the user in some way.
     */
    fun handle(command: E)
}

/**
 * A [CommandHandler] specifically for [slash commands][SlashCommandData].
 */
typealias SlashCommandHandler = CommandHandler<SlashCommandData, SlashCommandInteractionEvent>