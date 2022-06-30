package me.nopox.image.config

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import me.nopox.image.config.Config.Companion.logger
import net.mamoe.yamlkt.Yaml
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess


private const val DEFAULT_FILE_NAME = "config.yml"
private const val DEFAULT_RESOURCE_NAME = "/config.default.yml"

/**
 * Loads the server's [configuration][Config] from the file indicated by the supplied [pathString].
 *
 * - If the supplied path points to a directory, then the config file is assumed to be named "`config.yml`" inside that
 * directory. If that's also a directory, then the program exits with an error.
 * - If the file doesn't exist, a template is created in its place and the program exits, allowing the user to configure
 * it before starting again.
 *     - If the program doesn't have permission to create the new file, it exits with an error.
 * - If the program doesn't have permission to read the file, it exits with an error.
 * - If the configuration is invalid, malformed, or some other I/O error occurs, the program exits with an error.
 */
fun loadConfig(pathString: String?): Config = try {
    var configFile = when {
        // Use the default file name if non was specified.
        pathString == null -> File(DEFAULT_FILE_NAME)
        // Replace leading tildes (~) with the user's home directory (like on Unix systems).
        pathString.startsWith("~/") -> File(System.getProperty("user.home"), pathString.substring(startIndex = 2))
        // Otherwise, use the supplied path as-is.
        else -> File(pathString)
    }

    // If the supplied file is actually a directory, look inside it for the config file.
    if (configFile.isDirectory) configFile = File(configFile, DEFAULT_FILE_NAME)

    // If the config file is missing, copy the default one from the app's resource files.
    if (!configFile.exists()) {
        logger.info("Config file is missing. Creating a new one at ${configFile.absolutePath}")
        createDefaultConfig(configFile)
        logger.info("A template configuration has been created at ${configFile.absolutePath}")
        exitProcess(status = 0)
    }

    // Ensure the "file" isn't actually a directory.
    if (configFile.isDirectory) exitWithError("Config file is a directory, not a file: ${configFile.absolutePath}")

    // Ensure we're actually able to read from the config file.
    if (!configFile.canRead()) exitWithError("No permission to read the config file at ${configFile.absolutePath}")

    // Parse, deserialize, and returns its YAML contents.
    Yaml.decodeFromString(configFile.readText())
} catch (cause: IOException) {
    exitWithError("Failed to load the app's configuration file", cause)
} catch (cause: SerializationException) {
    exitWithError("Invalid configuration file", cause)
}

/**
 * Copies a default [configuration][Config] to the desired [configFile].
 *
 * If the program doesn't have permission to write to the [configFile], the program exits with an error.
 */
private fun createDefaultConfig(configFile: File) {
    if (!configFile.parentFile.canWrite()) exitWithError("No permission to create a config file at ${configFile.absolutePath}")

    val defaultContents = Config::class.java.getResource(DEFAULT_RESOURCE_NAME).readText()
    configFile.parentFile.mkdirs()
    configFile.writeText(defaultContents)
}

/**
 * [Exits][exitProcess] the process with a non-zero exit code (indicating an error) and logs the supplied [message].
 */
private fun exitWithError(message: String?, cause: Throwable? = null): Nothing {
    logger.error(message, cause)
    exitProcess(status = 1)
}