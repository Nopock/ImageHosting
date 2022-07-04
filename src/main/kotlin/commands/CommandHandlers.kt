package me.nopox.image.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

/*
 * We use KFunctions instead of the regular function syntax ((SlashCommandInteractionEvent) -> Unit) so that we can
 * reflectively get the functions' annotations when they're passed to the CommandDispatcher. Regular function types
 * don't support that yet.
 */

/**
 * A function that handles invocations of a specific [slash command][SlashCommandData].
 *
 * Implementations should take the form of...
 * ```kotlin
 * @Command(/* ... */)
 * fun MyCommand(command: SlashCommandInteractionEvent) { /* ... */ }
 * ```
 *
 * They may also be annotated with one or more [@CommandOption][CommandOption] annotations that describe the command's
 * [options][SlashCommandData.addOption] (aka arguments or parameters).
 */
typealias SlashCommandHandler = KFunction1<SlashCommandInteractionEvent, Unit>

/**
 * Same as [SlashCommandHandler], except for `suspend`ing functions.
 */
typealias AsyncSlashCommandHandler = KSuspendFunction1<SlashCommandInteractionEvent, Unit>