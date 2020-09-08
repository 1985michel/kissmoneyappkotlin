package com.example.kissmoney.meta

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MetaViewModel (application: Application) : AndroidViewModel(application)  {

    private val repository: MetaRepository

    val allMetas: LiveData<List<Meta>>

    init {
        val metaDao = KissmoneyDatabase.getDatabase(application, viewModelScope).metaDao()
        repository = MetaRepository(metaDao)
        allMetas = repository.allMetas
    }

    fun insert(meta: Meta) = viewModelScope.launch(Dispatchers.IO) {
        var id = repository.insert(meta)
        meta.metaId = id
    }

    fun update(meta: Meta) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(meta)
    }

    fun delete(meta: Meta) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(meta)
    }

    fun getById(meta: Meta) = viewModelScope.launch(Dispatchers.IO) {
        var metaEncontrada = repository.getById(meta.metaId)
        if (metaEncontrada is Meta) {
            meta.nomeMeta = metaEncontrada.nomeMeta
            meta.dataLimite = metaEncontrada.dataLimite
            meta.isAlcancada = metaEncontrada.isAlcancada
            meta.valor = metaEncontrada.valor
            meta.observacao = metaEncontrada.observacao
        }
    }
}