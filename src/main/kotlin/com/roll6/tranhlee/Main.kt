package com.roll6.tranhlee

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.commands.Auth
import com.roll6.tranhlee.entities.player.PlayerRepository
import com.roll6.tranhlee.listeners.ChatListener
import com.roll6.tranhlee.listeners.PlayerJoinListener
import com.roll6.tranhlee.manager.RepositoryManager
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.hibernate.service.spi.ServiceException
import javax.persistence.Persistence
import com.roll6.tranhlee.commands.executors.DadJoke as DadJokeCommand

class Main : JavaPlugin() {
    override fun onEnable() {
        Thread.currentThread().contextClassLoader = Main::class.java.classLoader

        ResourceManager.setResource(Main::class, this)
        this.saveDefaultConfig()

        try {
            val repositoryManager = RepositoryManager(
                Persistence.createEntityManagerFactory(
                    "Minecraft",
                    RepositoryManager.formatDatabaseCredentials(
                        this.config.getString("database.url", "localhost")!!,
                        this.config.getInt("database.port", 3306),
                        this.config.getString("database.schema")!!,
                        this.config.getString("database.username")!!,
                        this.config.getString("database.password") ?: ""
                    )
                ).createEntityManager()
            )

            ResourceManager.setResource(RepositoryManager::class, repositoryManager)

            val authentication = Authentication()
            authentication.importDiscordRoles()

            ResourceManager.setResource(Authentication::class, authentication)

            Bukkit.getServer().pluginManager.registerEvents(
                PlayerJoinListener(
                    repositoryManager.getRepository(PlayerRepository::class.java),
                ),
                this
            )

            Bukkit.getServer().pluginManager.registerEvents(
                ChatListener(
                    repositoryManager.getRepository(PlayerRepository::class.java),
                ),
                this
            )

            this.getCommand("dadjoke")?.setExecutor(DadJokeCommand())

            val commandMapField = this.server::class.java.getDeclaredField("commandMap")
            commandMapField.trySetAccessible()

            val commandName = this.config.getString("auth.commandName") ?: "auth"
            (commandMapField.get(this.server) as CommandMap)
                .register(
                    commandName,
                    Auth(
                        commandName,
                        repositoryManager.getRepository(PlayerRepository::class.java)
                    )
                )

            val minutes = 30L
            this.server.scheduler.scheduleSyncRepeatingTask(
                this,
                { ResourceManager.getResource<Authentication>(Authentication::class).endDiscordAuthentication() },
                0,
                (60 * minutes) * 20
            )
        } catch (exception: ServiceException) {
            return
        }
    }

    override fun onDisable() {
        ResourceManager.getResource<Authentication>(Authentication::class).endDiscordAuthentication()
    }
}