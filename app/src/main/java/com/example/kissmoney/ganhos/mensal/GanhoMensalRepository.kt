package com.example.kissmoney.ganhos.mensal

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class GanhoMensalRepository (private val ganhoMensalDao: GanhoMensalDao) {

    val allGanhosMensais: LiveData<List<GanhoMensal>> = ganhoMensalDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert (ganhoMensal: GanhoMensal): Long {
        return ganhoMensalDao.insert(ganhoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update (ganhoMensal: GanhoMensal) {
        ganhoMensalDao.update(ganhoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete (ganhoMensal: GanhoMensal) {
        ganhoMensalDao.delete(ganhoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllGanhosMensaisDoGanho(ganhoMensal: GanhoMensal) {
        ganhoMensalDao.deleteAllGanhosMensaisDoGanho(ganhoMensal.ganhoId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun setIsRecebido(id: Long, cheked: Boolean) {
        ganhoMensalDao.setIsRecebido(id, cheked)
    }
}