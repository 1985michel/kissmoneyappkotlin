package com.example.kissmoney.compromissos.mensal

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CompromissoMensalRepository (private val compromissoMensalDao: CompromissoMensalDao) {

    val allCompromissosMensais: LiveData<List<CompromissoMensal>> = compromissoMensalDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert (compromissoMensal: CompromissoMensal) : Long {
        return compromissoMensalDao.insert(compromissoMensal)
    }

    fun insertMonitorado (compromissoMensal: CompromissoMensal) : Long {
        return compromissoMensalDao.insert(compromissoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update (compromissoMensal: CompromissoMensal) {
        compromissoMensalDao.update(compromissoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete (compromissoMensal: CompromissoMensal) {
        compromissoMensalDao.delete(compromissoMensal)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllCompromissosMensaisDoCompromisso (compromissoMensal: CompromissoMensal) {
        compromissoMensalDao.deleteAllCompromissosMensaisDoCompromisso(compromissoMensal.compromissoId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun setIsPago(id: Long, cheked: Boolean) {
        compromissoMensalDao.setIsPago(id, cheked)
    }
}