package com.example.kissmoney.contas.mensal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class MovimentacaoMensalViewModel  (application: Application) : AndroidViewModel(application) {

    private val repository: MovimentacaoMensalRepository

    val allMovimentacoes: LiveData<List<MovimentacaoMensal>>

    init {
        val movimentacaoMensalDao = KissmoneyDatabase.getDatabase(application, viewModelScope).movimentacaoMensalDao()
        repository = MovimentacaoMensalRepository(movimentacaoMensalDao)
        allMovimentacoes = repository.allMovimentacoes
    }

    fun insert(movimentacaoMensal: MovimentacaoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(movimentacaoMensal)
    }

    fun insertMonitorado(movimentacaoMensal: MovimentacaoMensal, callback: () -> Unit) {
        var id = repository.insertMonitorado(movimentacaoMensal)
        movimentacaoMensal.movimentacaoMensalId = id
        callback()
    }

    fun update(movimentacaoMensal: MovimentacaoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(movimentacaoMensal)
    }

    fun delete(movimentacaoMensal: MovimentacaoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(movimentacaoMensal)
    }

    fun deleteAllMovimentacoesDaConta(movimentacaoMensal: MovimentacaoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllMovimentacoesDaConta(movimentacaoMensal)
    }
}