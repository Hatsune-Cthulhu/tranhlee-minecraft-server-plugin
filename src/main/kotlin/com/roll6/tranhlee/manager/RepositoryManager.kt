package com.roll6.tranhlee.manager

import com.roll6.tranhlee.entities.RepositoryAbstract
import javax.persistence.EntityManager

class RepositoryManager (
    private val entityManager: EntityManager
) {
    private val repositories: MutableMap<String, RepositoryAbstract<*>> = mutableMapOf()

    fun <R: RepositoryAbstract<*>> getRepository(repositoryName: Class<R>): R
    {
        if (repositoryName.name in this.repositories.keys) {
            return this.repositories.get(repositoryName.name) as R
        }

        val repository: RepositoryAbstract<*> =
            repositoryName.getConstructor(EntityManager::class.java).newInstance(this.entityManager)

        repository.entityManager = this.entityManager

        this.repositories.put(repositoryName.name, repository)

        return this.repositories.get(repositoryName.name) as R
    }
}