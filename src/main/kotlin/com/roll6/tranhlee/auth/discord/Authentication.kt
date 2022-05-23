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

class Authentication {
    private val discordBot: DiscordBot = DiscordBot(
        this,
        ResourceManager.getResource<RepositoryManager>(RepositoryManager::class)
            .getRepository(DiscordRoleRepository::class.java),
        ResourceManager.getResource<Main>(Main::class).config.getLong("discord.serverId"),
        ResourceManager.getResource<Main>(Main::class).config.getString("discord.botToken")!!,
        ResourceManager.getResource<Main>(Main::class).config.getString("discord.channelName"),
        ResourceManager.getResource<Main>(Main::class).config.getString("auth.commandName") ?: "auth",
    )

    private val discordAuthRequests: MutableMap<Long, DiscordAuthentication> = mutableMapOf()

    companion object {
        fun isDiscordAccountLinked(player: Player): Boolean {
            return null != player.discordId
        }

        fun isTwitchAccountLinked(player: Player): Boolean {
            return null != player.twitchAccount
        }
    }

    fun beginDiscordAuthentication(): Boolean {
        if (this.discordBot.isRunning()) {
            return true
        }

        return this.discordBot.start()
    }

    fun getDiscordAuthRequests(): Map<Long, DiscordAuthentication> {
        return this.discordAuthRequests
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
            val discordAuthentication = DiscordAuthentication(discordId)
            this.discordAuthRequests[discordId] = discordAuthentication

            discordAuthentication
        }
    }

    fun linkDiscordAccount(player: Player, key: String): Boolean {
        return try {
            val repositoryManager: RepositoryManager = ResourceManager.getResource(RepositoryManager::class)
            val authentication = this.discordAuthRequests.values.firstOrNull { auth -> auth.key == key }!!

            player.discordId = authentication.discordId
            player.discordRoles = this.discordBot.getRoles(authentication.discordId).toMutableList()

            repositoryManager.getRepository(PlayerRepository::class.java).persist(player)

            player.bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    "Discord account successfully linked"
                )
            )
            false
        } catch (exception: NullPointerException) {
            player.bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    "Please double check you pasted your authentication key correctly"
                )
            )
            false
        } catch (exception: Exception) {
            player.bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    "An error occurred while linking your discord account"
                )
            )
            throw RuntimeException(exception)
        }
    }

    fun importDiscordRoles(): Boolean {
        this.beginDiscordAuthentication()

        return this.discordBot.getRoles().isNotEmpty()
    }

    fun beginTwitchAuthentication() {
        return
    }

    fun endTwitchAuthentication() {
        return
    }
}