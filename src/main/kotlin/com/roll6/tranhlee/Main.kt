package com.roll6.tranhlee

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.entities.player.PlayerRepository
import com.roll6.tranhlee.listeners.ChatListener
import com.roll6.tranhlee.listeners.PlayerJoinListener
import com.roll6.tranhlee.manager.RepositoryManager
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import org.hibernate.service.spi.ServiceException
import javax.persistence.Persistence
import com.roll6.tranhlee.commands.Auth as AuthCommand
import com.roll6.tranhlee.commands.DadJoke as DadJokeCommand

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
            ResourceManager.setResource(Authentication::class, Authentication())

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
            this.getCommand("auth")?.setExecutor(
                AuthCommand(
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