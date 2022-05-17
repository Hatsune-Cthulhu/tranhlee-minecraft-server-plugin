package com.roll6.tranhlee.manager

import com.roll6.tranhlee.entities.RepositoryAbstract
import javax.persistence.EntityManager

class RepositoryManager (
    private val entityManager: EntityManager
) {
    private val repositories: MutableMap<String, RepositoryAbstract<*>> = mutableMapOf()

    companion object {
        fun formatDatabaseCredentials(
            url: String,
            port: Int,
            schema: String,
            username: String,
            password: String,
        ): Map<String, String> {
            return mapOf(
                Pair("javax.persistence.jdbc.url", "jdbc:mysql://${url}:${port}/${schema}"),
                Pair("javax.persistence.jdbc.user", username),
                Pair("javax.persistence.jdbc.password", password.trim()),
            )
        }
    }

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