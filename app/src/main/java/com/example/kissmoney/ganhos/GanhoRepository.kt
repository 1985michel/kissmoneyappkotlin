package com.example.kissmoney.ganhos

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class GanhoRepository(private val ganhoDao: GanhoDao) {

    val allGanhos: LiveData<List<Ganho>> = ganhoDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(ganho: Ganho): Long {
        return ganhoDao.insert(ganho)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(ganho: Ganho) {
        ganhoDao.update(ganho)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(ganho: Ganho) {
        ganhoDao.delete(ganho)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getById(id: Long): Ganho {
        return ganhoDao.getById(id)
    }
}