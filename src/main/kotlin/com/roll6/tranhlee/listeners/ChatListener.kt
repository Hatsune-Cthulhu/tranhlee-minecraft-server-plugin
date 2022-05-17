package com.roll6.tranhlee.listeners

import com.roll6.tranhlee.entities.player.PlayerRepository
import com.roll6.tranhlee.handlers.ChatHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener (
    private val playerRepository: PlayerRepository
) : Listener {

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = this.playerRepository.findByBukkitPlayer(event.player)

        event.format = ChatHandler.formatMessage(player, event.message)
    }
}