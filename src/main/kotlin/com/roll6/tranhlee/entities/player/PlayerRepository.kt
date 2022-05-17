package com.roll6.tranhlee.entities.player

import com.roll6.tranhlee.entities.RepositoryAbstract
import javax.persistence.EntityManager
import org.bukkit.entity.Player as BukkitPlayer

class PlayerRepository(entityManager: EntityManager) : RepositoryAbstract<Player>(entityManager) {
    fun findByBukkitPlayer(bukkitPlayer: BukkitPlayer, flushCache: Boolean = false): Player {
        if (flushCache) this.entityManager.clear()

        return (entityManager.createQuery("SELECT p FROM Player p WHERE p.uuid = :UUID")
            .setParameter("UUID", bukkitPlayer.uniqueId.toString())
            .singleResult as Player).apply {
            this.bukkitPlayer = bukkitPlayer
            this.initialisePermissions()
        }
    }
}