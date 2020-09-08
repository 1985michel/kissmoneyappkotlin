package com.example.kissmoney.meta

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MetaRepository (private val metaDao: MetaDao) {

    val allMetas : LiveData<List<Meta>> = metaDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(meta: Meta) : Long {
        return metaDao.insert(meta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(meta: Meta){
        metaDao.update(meta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(meta: Meta){
        metaDao.delete(meta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getById(id: Long) : Meta {
        return metaDao.getById(id)
    }
}