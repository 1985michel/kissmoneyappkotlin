package com.example.kissmoney.contas

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ContaRepository (private  val contaDao: ContaDao) {

    val allContas: LiveData<List<Conta>> = contaDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(conta: Conta) : Long {
        return contaDao.insert(conta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(conta: Conta){
        contaDao.update(conta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(conta: Conta){
        contaDao.delete(conta)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getById(id: Long) : Conta{
        return contaDao.getById(id)
    }

}
