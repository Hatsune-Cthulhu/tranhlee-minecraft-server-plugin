package com.roll6.tranhlee.commands

import com.roll6.tranhlee.auth.discord.Authentication
import com.roll6.tranhlee.entities.player.PlayerRepository
import com.roll6.tranhlee.handlers.ChatHandler
import com.roll6.tranhlee.manager.ResourceManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Auth(
    private val playerRepository: PlayerRepository
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player || args.size <= 1) {
            return false
        }

        val authentication: Authentication = ResourceManager.getResource(Authentication::class)

        return try {
            when (args[1]) {
                ("begin") -> {
                    when (args[0]) {
                        "discord" -> {
                            if (null === playerRepository.findByBukkitPlayer(sender)?.discordId) {
                                if (authentication.beginDiscordAuthentication()) {
                                    sender.sendMessage(
                                        ChatHandler.formatWhisper(
                                            "${ChatColor.RED}Auth",
                                            "Discord authentication bot started"
                                        )
                                    )
                                }
                            }
                        }
                        "twitch"  -> {
                            if (null === playerRepository.findByBukkitPlayer(sender)?.twitchAccount) {
                                authentication.beginTwitchAuthentication()
                            }
                        }
                    }
                }
                ("link")  ->
                    when (args[0]) {
                        "discord" -> {
                            authentication.linkDiscordAccount(sender, args[2])
                        }
                        "twitch"  -> {
                            authentication.beginTwitchAuthentication()
                        }
                    }
            }

            true
        } catch (e: NullPointerException) {
            false
        }
    }
}