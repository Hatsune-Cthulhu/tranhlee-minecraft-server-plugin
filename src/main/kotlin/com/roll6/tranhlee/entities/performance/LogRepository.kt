package com.roll6.tranhlee.entities.performance

import com.roll6.tranhlee.entities.RepositoryAbstract
import javax.persistence.EntityManager

class LogRepository(entityManager: EntityManager) : RepositoryAbstract<Log>(entityManager)
