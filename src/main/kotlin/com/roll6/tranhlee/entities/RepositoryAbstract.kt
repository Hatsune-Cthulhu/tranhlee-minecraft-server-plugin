package com.roll6.tranhlee.entities

import javax.persistence.EntityManager

abstract class RepositoryAbstract <E: Any>(
    var entityManager: EntityManager
) {
    inline fun <reified E> findByID(id: Int): E {
        return this.entityManager.find(E::class.java, id)
    }

    inline fun <reified E> findAll(): List<E> =
        this.entityManager.createQuery("SELECT e FROM ${E::class.java.name} e")
            .resultList.filterIsInstance<E>()

    /**
     * @throws Exception
     */
    inline fun <reified E> persist(entity: E): E {
        this.entityManager.transaction.begin()
        this.entityManager.persist(entity)
        this.entityManager.transaction.commit()

        return entity
    }
}