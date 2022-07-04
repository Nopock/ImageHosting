package me.nopox.image.commands

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend

/**
 * A discord [EventListener] that routes commands received by the bot to their appropriate
 * [handlers][SlashCommandHandler].
 *
 * Handlers for [slash commands][SlashCommandData] are registered via the [registerSlash] method.
 */
class CommandDispatcher(private val discord: JDA) : ListenerAdapter() {

    private val slashHandlers = mutableMapOf<String, SlashCommandHandler>()

    override fun onSlashCommandInteraction(command: SlashCommandInteractionEvent) {
        val handler = slashHandlers[command.name.lowercase()]
        when (handler?.isSuspend) {
            // If this dispatcher doesn't know any command with that name, ignore it. We don't return an error here
            // because it's possible that a different dispatcher might be able to handle it.
            null -> return

            // If it's a suspending function, wrap it in a runBlocking{} block and use callSuspend() instead of call().
            true -> runBlocking { handler.callSuspend(command) }

            // If it's a regular function, we can just invoke it normally.
            false -> handler(command)
        }
    }

    /**
     * Adds a new [slash command][SlashCommandData] for the dispatcher to handle.
     *
     * @param[command] The function that will respond to invocations of the command. It must be annotated with
     * [@Command][Command], and may also have one or more [@CommandOption][CommandOption] annotations describing the
     * command's [options][SlashCommandData.addOption] (aka arguments or parameters).
     *
     * @throws[IllegalArgumentException] if the [command] is not annotated with [@Command][Command].
     * @throws[IllegalStateException] if the [command]'s name is the same as one already handled by the dispatcher.
     */
    fun registerSlash(command: SlashCommandHandler) {
        // Get the function's @Command annotation (which is required).
        val metadataAnnotation = command.annotations
            .filterIsInstance<Command>()
            .singleOrNull()
            ?: throw IllegalArgumentException("Handler ${command.name.ifBlank { "<constructor>" }} needs to have a @${Command::class.qualifiedName} annotation")

        // Get the function's @CommandOption annotations (if any).
        // Required parameters are sorted to be first, as required by JDA.
        val optionsAnnotations = command.annotations
            .filterIsInstance<CommandOption>()
            .sortedByDescending { it.isRequired }

        val metadata = Commands.slash(metadataAnnotation.name, metadataAnnotation.description).apply {
            for (option in optionsAnnotations) addOption(
                option.type,
                option.name,
                option.description,
                option.isRequired
            )
        }
        val name = metadata.name.lowercase()
        if (slashHandlers[name] != null)
            throw IllegalStateException("A command named \"$name\" is already registered!")

        slashHandlers[name] = command
        discord.upsertCommand(metadata).queue()
    }

    /**
     * A variant of the [registerSlash] method that accepts [suspending][KFunction.isSuspend] functions as handlers.
     *
     * Other than that, this method's contract is identical to that of [registerSlash].
     */
    @Suppress("UNCHECKED_CAST")
    fun registerAsyncSlash(command: AsyncSlashCommandHandler) =
        registerSlash(command as SlashCommandHandler)
}