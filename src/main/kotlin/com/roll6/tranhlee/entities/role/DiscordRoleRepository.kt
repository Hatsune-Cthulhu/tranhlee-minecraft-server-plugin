package com.roll6.tranhlee.entities.role

import com.roll6.tranhlee.entities.RepositoryAbstract
import javax.persistence.EntityManager

class DiscordRoleRepository(entityManager: EntityManager) : RepositoryAbstract<DiscordRole>(entityManager)