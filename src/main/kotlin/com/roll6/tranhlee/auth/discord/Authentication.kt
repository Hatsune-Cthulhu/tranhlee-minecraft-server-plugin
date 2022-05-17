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

    fun linkDiscordAccount(player: Player, key: String): Boolean {
        var message = "An error occurred while linking your discord account"
        return try {
            val repositoryManager: RepositoryManager = ResourceManager.getResource(RepositoryManager::class)
            val authentication = this.discordAuthRequests.values.firstOrNull { auth -> auth.key == key }!!

            player.discordId = authentication.discordId
            player.discordRoles = this.discordBot.getRoles(authentication.discordId).toMutableList()

            repositoryManager.getRepository(PlayerRepository::class.java).persist(player)
            message = "Discord account successfully linked"

            false
        } catch (exception: NullPointerException) {
            message = "Please double check you pasted your authentication key correctly"

            false
        } catch (exception: Exception) {
            throw RuntimeException(exception)
        } finally {
            player.bukkitPlayer.sendMessage(
                ChatHandler.formatWhisper(
                    "${ChatColor.RED}Auth",
                    message
                )
            )
        }
    }

    fun beginTwitchAuthentication() {
        return
    }

    fun endTwitchAuthentication() {
        return
    }
}