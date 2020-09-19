package com.example.kissmoney.compromissos.mensal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompromissoMensalViewModel  (application: Application) : AndroidViewModel(application) {

    private val repository: CompromissoMensalRepository

    val allCompromissosMensais: LiveData<List<CompromissoMensal>>

    init {
        val compromissoMensalDao = KissmoneyDatabase.getDatabase(application, viewModelScope).compromissoMensalDao()
        repository = CompromissoMensalRepository(compromissoMensalDao)
        allCompromissosMensais = repository.allCompromissosMensais
    }

    fun insert (compromissoMensal: CompromissoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(compromissoMensal)
    }

    fun insertMonitorado (compromissoMensal: CompromissoMensal, callback: () -> Unit) {
        compromissoMensal.compromissoMensalId = repository.insertMonitorado(compromissoMensal)
        callback()
    }

    fun update(compromissoMensal: CompromissoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(compromissoMensal)
    }

    fun delete(compromissoMensal: CompromissoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(compromissoMensal)
    }

    fun deleteAllCompromissosMensaisDoCompromisso(compromissoMensal: CompromissoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllCompromissosMensaisDoCompromisso(compromissoMensal)
    }

    fun setIsPago(id : Long, isCheked : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.setIsPago(id, isCheked)
    }
}