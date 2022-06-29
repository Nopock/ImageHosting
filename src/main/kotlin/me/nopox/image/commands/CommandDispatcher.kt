package me.nopox.image.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

/**
 * A discord [EventListener] that routes commands received by the bot to their appropriate
 * [handlers][CommandHandler].
 *
 * Handlers for [slash commands][SlashCommandData] are registered via the [registerSlash] method.
 */
class CommandDispatcher(private val discord: JDA) : ListenerAdapter() {

    private val slashHandlers = mutableMapOf<String, SlashCommandHandler>()

    /**
     * Adds a new command for the dispatcher to handle.
     *
     * @param[commands] The handlers that will respond to invocations of each command.
     *
     * @throws[IllegalStateException] if any of the [commands] have a name already handled by the dispatcher.
     * @throws[IllegalStateException] if any of the [commands] handle the same command.
     */
    fun registerSlash(vararg commands: SlashCommandHandler) {
        for (command in commands) {
            val name = command.metadata.name.lowercase()
            if (slashHandlers[name] != null)
                throw IllegalStateException("A command named \"$name\" is already registered!")

            slashHandlers[name] = command
            discord.upsertCommand(command.metadata).queue()
        }
    }

    override fun onSlashCommandInteraction(command: SlashCommandInteractionEvent) {
        // If we don't find a command with that name, don't do anything; another dispatcher might be handling it.
        slashHandlers[command.name.lowercase()]?.handle(command)
    }
}