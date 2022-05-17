package com.roll6.tranhlee.auth.discord

import com.roll6.tranhlee.Main
import com.roll6.tranhlee.auth.discord.jda.DiscordBot
import com.roll6.tranhlee.auth.discord.data.DiscordAuthentication
import com.roll6.tranhlee.entities.player.Player
import com.roll6.tranhlee.entities.player.PlayerRepository
import com.roll6.tranhlee.entities.role.DiscordRoleRepository
import com.roll6.tranhlee.handlers.ChatHandler
import com.roll6.tranhlee.manager.RepositoryManager
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player as BukkitPlayer

class Authentication {
    private val discordBot: DiscordBot = DiscordBot(
        this,
        ResourceManager.getResource<RepositoryManager>(RepositoryManager::class)
            .getRepository(DiscordRoleRepository::class.java),
        ResourceManager.getResource<Main>(Main::class).config.getLong("discord.serverId"),
        ResourceManager.getResource<Main>(Main::class).config.getString("discord.botToken")!!,
    )

    private val discordAuthRequests: MutableMap<Long, DiscordAuthentication> = mutableMapOf()

    fun beginDiscordAuthentication(): Boolean {
        if (this.discordBot.isRunning()) {
            return true
        }

        return this.discordBot.start()
    }

    fun endDiscordAuthentication(): Boolean {
        if (!this.discordBot.isRunning()) {
            return true
        }

        return this.discordBot.stop()
    }

    fun addDiscordAuthentication(discordId: Long): DiscordAuthentication {
        return try {
            this.discordAuthRequests[discordId]!!
        } catch (exception: NullPointerException) {
            this.discordAuthRequests.put(discordId, DiscordAuthentication(discordId))!!
        }
    }

    fun linkDiscordAccount(bukkitPlayer: BukkitPlayer, key: String): Player {
        return try {
            val repositoryManager: RepositoryManager = ResourceManager.getResource(RepositoryManager::class)
            val player = repositoryManager.getRepository(PlayerRepository::class.java).findByBukkitPlayer(bukkitPlayer)
            val authentication = this.discordAuthRequests.values.firstOrNull { auth -> auth.key == key }!!

            player.discordId = authentication.discordId
            player.discordRoles = this.discordBot.getRoles(authentication.discordId).toMutableList()

            repositoryManager.getRepository(PlayerRepository::class.java).persist(player)
            bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    "Discord account successfully linked"
                )
            )

            player
        } catch (exception: NullPointerException) {
            bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    "Please double check you pasted your authentication key correctly"
                )
            )

            throw RuntimeException(exception)
        } catch (exception: Exception) {
            bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    "An error occurred while linking your discord account"
                )
            )

            throw RuntimeException(exception)
        }
    }

    fun beginTwitchAuthentication() {
        return
    }

    fun endTwitchAuthentication() {
        return
    }
}