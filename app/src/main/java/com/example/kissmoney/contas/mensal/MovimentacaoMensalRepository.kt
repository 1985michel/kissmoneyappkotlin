package com.example.kissmoney.contas.mensal

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MovimentacaoMensalRepository (private val movimentacaoMensalDao: MovimentacaoMensalDao) {

    val allMovimentacoes: LiveData<List<MovimentacaoMensal>> = movimentacaoMensalDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert (movimentacaoMensal: MovimentacaoMensal) : Long {
        return movimentacaoMensalDao.insert(movimentacaoMensal)
    }

    fun insertMonitorado (movimentacaoMensal: MovimentacaoMensal) : Long {
        var id = movimentacaoMensalDao.insert(movimentacaoMensal)
        return id
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update (movimentacaoMensal: MovimentacaoMensal){
        movimentacaoMensalDao.update(movimentacaoMensal)
    }

    fun updateMonitorado (movimentacaoMensal: MovimentacaoMensal){
        movimentacaoMensalDao.update(movimentacaoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete (movimentacaoMensal: MovimentacaoMensal) {
        movimentacaoMensalDao.delete(movimentacaoMensal)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllMovimentacoesDaConta (movimentacaoMensal: MovimentacaoMensal){
        movimentacaoMensalDao.deleteAllMovimentacoesDaConta(movimentacaoMensal.contaId)
    }

}