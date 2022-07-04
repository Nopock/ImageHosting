package me.nopox.image.commands

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

/**
 * Indicates that a function is a command, and sets the command's [name] & [description].
 *
 * @param[name] The name used to invoke the command (case-insensitive). This does *not* include the slash `/` prefix.
 * @param[description] A short description of what the command does.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val name: String,
    val description: String
)

/**
 * Adds an [option][SlashCommandData.addOption] (aka an argument or parameter) to a function also annotated with
 * [@Command][Command].
 *
 * This annotation is repeatable to allow for commands that have several options.
 *
 * @param[name] The option's identifier (case-insensitive).
 * @param[description] A short description of how the option affects the command's output.
 * @param[type] The type of input that the option accepts, such as a string or attached file.
 * @param[isRequired] Whether the option must be included when invoking the command.
 * @param[isAutoCompleted] Whether the option's value can be [auto-completed][CommandAutoCompleteInteractionEvent] by a
 * user's discord client. If `true`, the [type] itself must also [support auto-completion][OptionType.supportsChoices].
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class CommandOption(
    val name: String,
    val description: String,
    val type: OptionType,
    val isRequired: Boolean = false,
    val isAutoCompleted: Boolean = false,
)