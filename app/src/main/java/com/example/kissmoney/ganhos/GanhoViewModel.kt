package com.example.kissmoney.ganhos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GanhoViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: GanhoRepository

    val allGanhos: LiveData<List<Ganho>>

    init {
        val ganhoDao = KissmoneyDatabase.getDatabase(application, viewModelScope).ganhoDao()
        repository = GanhoRepository(ganhoDao)
        allGanhos = repository.allGanhos
    }

    fun insert(ganho: Ganho) = viewModelScope.launch(Dispatchers.IO) {
        var id = repository.insert(ganho)
        ganho.ganhoId = id
    }

    fun insertMonitorado(ganho: Ganho, callback: () -> Unit)  {
        var id = repository.insertMonitorado(ganho)
        ganho.ganhoId = id
        callback()
    }

    fun update(ganho: Ganho) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(ganho)
    }

    fun delete(ganho: Ganho) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(ganho)
    }

    fun getById(ganho: Ganho) = viewModelScope.launch(Dispatchers.IO) {
        var ganhoEncontrado = repository.getById(ganho.ganhoId)
        if (ganhoEncontrado is Ganho) {
            ganho.nomeGanho = ganhoEncontrado.nomeGanho
            ganho.isRendaPassiva = ganhoEncontrado.isRendaPassiva
            ganho.isRecorrente = ganhoEncontrado.isRecorrente
        }
    }
}