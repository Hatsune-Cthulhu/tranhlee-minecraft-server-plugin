package com.roll6.tranhlee.commands

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.entities.player.Player
import com.roll6.tranhlee.entities.player.PlayerRepository
import com.roll6.tranhlee.handlers.ChatHandler
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

class Auth(
    name: String,
    private val playerRepository: PlayerRepository
) : BukkitCommand(name) {

    init {
        this.usageMessage = "Please use /${name} help for instructions"
    }

    private lateinit var sender: Player
    private val authentication: Authentication = ResourceManager.getResource(Authentication::class)

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (sender !is org.bukkit.entity.Player || args.size <= 1) {
            return false
        }
        this.sender = playerRepository.findByBukkitPlayer(sender)

        var message = "An unknown error occurred while processing your request"
        try {
            when (args[0]) {
                "discord" -> {
                    if (!Authentication.isDiscordAccountLinked(this.sender)) {
                        return this.handleDiscord(args[1], args.getOrNull(2))
                    }
                    message = "You already have a discord account linked"
                }
                "twitch" -> {
                    if (!Authentication.isTwitchAccountLinked(this.sender)) {
                        return this.handleTwitch(args[1], args.getOrNull(2))
                    }
                    message = "You already have a twitch account linked"
                }
                "help" -> {
                    return this.handleHelp()
                }
            }
        } catch (_: RuntimeException) {}

        ChatHandler.sendMessage("${ChatColor.RED}Auth", message)
        return false
    }

    private fun handleDiscord(stage: String, key: String?): Boolean {
        when(stage) {
            "begin" -> {
                return this.authentication.beginDiscordAuthentication()
            }
            "link"  -> {
                return this.authentication.linkDiscordAccount(this.sender, key!!)
            }
            "keys"  -> {
                this.sender.bukkitPlayer.sendMessage(
                    ChatHandler.formatWhisper(
                        "${ChatColor.RED}Auth",
                        this.authentication.getDiscordAuthRequests().values.joinToString(separator = ", ") { it.key }
                    )
                )
                return true
            }
        }
        return false
    }

    private fun handleTwitch(stage: String, key: String?): Boolean {
        return false
    }

    private fun handleHelp(): Boolean {
        return false
    }
}