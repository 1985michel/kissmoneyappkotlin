package com.example.kissmoney.compromissos

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class CompromissoRepository (private val compromissoDao: CompromissoDao) {

    val allCompromissos : LiveData<List<Compromisso>> = compromissoDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(compromisso: Compromisso) : Long {
        return compromissoDao.insert(compromisso)
    }

    fun insertMonitorado(compromisso: Compromisso) : Long {
        return compromissoDao.insert(compromisso)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(compromisso: Compromisso) {
        compromissoDao.update(compromisso)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(compromisso: Compromisso) {
        compromissoDao.delete(compromisso)
    }

}