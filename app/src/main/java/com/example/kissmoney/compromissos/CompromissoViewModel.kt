package com.example.kissmoney.compromissos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompromissoViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: CompromissoRepository

    val allCompromissos: LiveData<List<Compromisso>>

    init {
        val compromissoDao = KissmoneyDatabase.getDatabase(application, viewModelScope).compromissoDao()
        repository = CompromissoRepository(compromissoDao)
        allCompromissos = repository.allCompromissos
    }

    fun insert(compromisso: Compromisso) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(compromisso)
    }

    fun update(compromisso: Compromisso) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(compromisso)
    }


    fun delete(compromisso: Compromisso) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(compromisso)
    }
}