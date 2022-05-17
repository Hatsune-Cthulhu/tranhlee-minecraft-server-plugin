package com.roll6.tranhlee.listeners

import com.roll6.tranhlee.entities.player.Player
import com.roll6.tranhlee.entities.player.PlayerRepository
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import javax.persistence.NoResultException

class PlayerJoinListener(
    private val playerRepository: PlayerRepository
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        try {
            this.playerRepository.findByBukkitPlayer(event.player, true)
        } catch (e: NoResultException) {
            this.playerRepository.persist(Player(event.player))
        }
    }
}