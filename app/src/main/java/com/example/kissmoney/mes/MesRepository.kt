package com.example.kissmoney.mes

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MesRepository (private val mesDao: MesDao) {

    val allMeses: LiveData<List<Mes>> = mesDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mes: Mes) : Long {
        return mesDao.insert(mes)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPorNome(mes: Mes) : Mes {
        var mesNoRepository = mesDao.getPorNome(mes.nomeMes)
        return mesNoRepository
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getPorId(mes: Mes) : Mes {
        var mesNoRepository = mesDao.getPorId(mes.mesId)
        return mesNoRepository
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(mes: Mes) {
        mesDao.update(mes)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(mes: Mes) {
        mesDao.delete(mes)
    }
}