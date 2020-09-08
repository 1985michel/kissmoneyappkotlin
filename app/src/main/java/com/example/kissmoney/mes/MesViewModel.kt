package com.example.kissmoney.mes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kissmoney.database.KissmoneyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MesViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: MesRepository

    val allMeses: LiveData<List<Mes>>

    init {
        val mesDao = KissmoneyDatabase.getDatabase(application, viewModelScope).mesDao()
        repository = MesRepository(mesDao)
        allMeses = repository.allMeses
    }

    fun insert(mes: Mes) = viewModelScope.launch(Dispatchers.IO) {
        var id = repository.insert(mes)
        mes.mesId = id
    }

    fun update(mes: Mes) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(mes)
    }

    fun delete(mes: Mes) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(mes)
    }

    fun getByName(mes: Mes, callback: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {

        var mesObitido  = repository.getPorNome(mes)

        if (mesObitido is Mes) {
            mes.mesId = mesObitido.mesId
        } else {
            var id =  repository.insert(mes)
            mes.mesId = id
        }

        callback()
    }

    fun getById(mes: Mes, callback: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {

        var mesObitido  = repository.getPorId(mes)

        if (mesObitido is Mes) {
            mes.nomeMes = mesObitido.nomeMes
        } else {
            var id =  repository.insert(mes)
            mes.mesId = id
        }
        callback()
    }

}