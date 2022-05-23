package com.roll6.tranhlee.entities.role

import com.roll6.tranhlee.entities.RepositoryAbstract
import javax.persistence.EntityManager

class DiscordRoleRepository(entityManager: EntityManager) : RepositoryAbstract<DiscordRole>(entityManager) {
    fun findByName(name: String): DiscordRole = this.entityManager
        .createQuery("SELECT dr FROM DiscordRole dr WHERE dr.name = :NAME")
        .setParameter("NAME", name).singleResult as DiscordRole
}