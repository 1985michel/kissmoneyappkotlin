package com.example.kissmoney.ganhos.mensal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GanhoMensalViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: GanhoMensalRepository

    val allGanhosMensais: LiveData<List<GanhoMensal>>

    init {
        val ganhoMensalDao = KissmoneyDatabase.getDatabase(application, viewModelScope).ganhoMensalDao()
        repository = GanhoMensalRepository(ganhoMensalDao)
        allGanhosMensais = repository.allGanhosMensais
    }

    fun insert (ganhoMensal: GanhoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(ganhoMensal)
    }

    fun update(ganhoMensal: GanhoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(ganhoMensal)
    }

    fun delete(ganhoMensal: GanhoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(ganhoMensal)
    }

    fun deleteAllGanhosMensaisDoGanho(ganhoMensal: GanhoMensal) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllGanhosMensaisDoGanho(ganhoMensal)
    }

    fun setIsRecebido(id : Long, isCheked : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.setIsRecebido(id, isCheked)
    }
}